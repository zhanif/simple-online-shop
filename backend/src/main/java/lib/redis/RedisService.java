package lib.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RedisService {
    private final RedisTemplate<String, Object> template;

    public void save(String key, Object data, Duration ttl) {
        template.opsForValue().set(key, data, ttl);
        log.info("Successfully stored data with key: {} (TTL: {})", key, ttl);
    }

    public Object getData(String key) {
        return template.opsForValue().get(key);
    }

    public void delete(String key) {
        boolean deleted = template.delete(key);

        if (deleted) {
            log.info("Successfully removed data with key: {}", key);
        }
        else {
            log.info("Failed to remove data with key: {}", key);
        }
    }
}
