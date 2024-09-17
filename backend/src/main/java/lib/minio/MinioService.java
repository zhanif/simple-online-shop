package lib.minio;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.minio.*;
import lib.minio.exception.MinioServiceDownloadException;

import lib.minio.exception.MinioServiceException;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import io.minio.http.Method;
import io.minio.messages.Item;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MinioService {
    private final MinioClient minio;
    private static final Long DEFAULT_EXPIRY = TimeUnit.HOURS.toSeconds(1);
    private static String bMsg(String bucket) {
        return "bucket " + bucket;
    }
    private static String bfMsg(String bucket, String filename) {
        return bMsg(bucket) + " of file " + filename;
    }

    private String getLink(String bucket, String filename, Long expiry) {
        try {
            return minio.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucket)
                            .object(filename)
                            .expiry(Math.toIntExact(expiry), TimeUnit.SECONDS)
                            .build());
        } catch (InvalidKeyException | ErrorResponseException | InsufficientDataException | InternalException
                 | InvalidResponseException | NoSuchAlgorithmException | XmlParserException | ServerException
                 | IllegalArgumentException | IOException e) {
            log.error(bfMsg(bucket, filename) + ": " + e.getMessage());
            throw new MinioServiceDownloadException(bfMsg(bucket, filename) + ": " + e.getMessage());
        }
    }

    @Data
    public static class ListItem {
        private String objectName;
        private Long size;
        private boolean dir;
        private String versionId;

        @JsonIgnore
        private Item item;

        public ListItem(Item item) {
            this.objectName = item.objectName();
            this.size = item.size();
            this.dir = item.isDir();
            this.versionId = item.versionId();
            this.item = item;
        }
    }

    public List<Object> getList(String bucket) {
        List<Result<Item>> results = new ArrayList<>();
        minio.listObjects(
                        ListObjectsArgs.builder()
                                .bucket(bucket)
                                .recursive(true)
                                .build())
                .forEach(results::add);
        return results.stream().map(t -> {
            try {
                return new ListItem(t.get());
            } catch (InvalidKeyException | ErrorResponseException | IllegalArgumentException | InsufficientDataException
                     | InternalException | InvalidResponseException | NoSuchAlgorithmException | ServerException
                     | XmlParserException | IOException e) {
                log.error(bMsg(bucket) + ": " + e.getMessage());
                return null;
            }
        }).collect(Collectors.toList());
    }

    public void view(HttpServletResponse response, String bucket, String filename, Long expiry) {
        try {
            response.sendRedirect(this.getLink(bucket, filename, expiry));
        } catch (IOException e) {
            log.error(bfMsg(bucket, filename) + ": " + e.getMessage());
            throw new MinioServiceDownloadException(bfMsg(bucket, filename) + ": " + e.getMessage());
        }
    }

    public void view(HttpServletResponse response, String bucket, String filename) {
        this.view(response, bucket, filename, DEFAULT_EXPIRY);
    }

    @Data
    @Builder
    public static class UploadOption {
        private String filename;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class MinioResponseHandler {
        private String fileName;
        private ObjectWriteResponse response;
    }

    public ObjectWriteResponse upload(MultipartFile file, String bucket, Function<MultipartFile, UploadOption> modifier) {
        UploadOption opt = modifier.apply(file);
        try {
            return minio.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(opt.filename)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build());
        } catch (InvalidKeyException | ErrorResponseException | InsufficientDataException | InternalException
                 | InvalidResponseException | NoSuchAlgorithmException | ServerException | XmlParserException
                 | IllegalArgumentException | IOException e) {
            log.error(bfMsg(bucket, opt.getFilename()) + ": " + e.getMessage());
            throw new MinioServiceDownloadException(bfMsg(bucket, opt.getFilename()) + ": " + e.getMessage());
        }
    }

    public MinioResponseHandler upload(MultipartFile file, String bucket) {
        String fileName = System.currentTimeMillis() + "_-_" + file.getName().replace(" ", "_");
        ObjectWriteResponse object = this.upload(file, bucket,
            o -> UploadOption.builder().filename(fileName).build()
        );

        return new MinioResponseHandler(fileName, object);
    }

    // ---

    public ObjectWriteResponse upload(InputStream file, String filename, String bucket) {
        try {
            return minio.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(filename)
                            .stream(file, file.available(), -1)
                            .build());
        } catch (InvalidKeyException | ErrorResponseException | InsufficientDataException | InternalException
                 | InvalidResponseException | NoSuchAlgorithmException | ServerException | XmlParserException
                 | IllegalArgumentException | IOException e) {
            log.error(bfMsg(bucket, filename) + ": " + e.getMessage());
            throw new MinioServiceDownloadException(bfMsg(bucket, filename) + ": " + e.getMessage());
        }
    }

    public InputStream read(String filename, String bucket) throws InvalidKeyException, ErrorResponseException,
            InsufficientDataException, InternalException, InvalidResponseException, NoSuchAlgorithmException, ServerException,
            XmlParserException, IllegalArgumentException, IOException {
        return minio.getObject(GetObjectArgs.builder()
                .bucket(bucket)
                .object(filename)
                .build());
    }

    public void remove(String bucket, String fileName) {
        try {
            minio.removeObject(RemoveObjectArgs.builder().bucket(bucket).object(fileName).build());
        }
        catch (Exception e) {
            log.error(bfMsg(bucket, fileName) + ": " + e.getMessage());
            throw new MinioServiceException(bfMsg(bucket, fileName) + ": " + e.getMessage());
        }
    }

    public String getImageUrl(String bucket, String fileName, Duration duration) {
        try {
            return minio.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                    .bucket(bucket)
                    .object(fileName)
                    .method(Method.GET)
                    .expiry((int)duration.getSeconds(), TimeUnit.SECONDS)
                    .build()
            );
        }
        catch (Exception e) {
            log.error(bfMsg(bucket, fileName) + ": " + e.getMessage());
            throw new MinioServiceException(bfMsg(bucket, fileName) + ": " + e.getMessage());
        }
    }
}
