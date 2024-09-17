package lib.minio.configuration;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {
    @Value("${spring.application.minio.url}")
    private String url;

    @Value("${spring.application.minio.accessKey}")
    private String accessKey;

    @Value("${spring.application.minio.secretKey}")
    private String secretKey;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
            .endpoint(this.url)
            .credentials(this.accessKey, this.secretKey)
            .build();
    }
}
