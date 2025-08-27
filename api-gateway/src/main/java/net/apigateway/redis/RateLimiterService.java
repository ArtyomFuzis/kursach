package net.apigateway.redis;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RateLimiterService {
    private static final int REQUEST_LIMIT = 10;
    private static final long LIMIT_TIMEOUT_SECONDS = 5;
    private final StringRedisTemplate redisTemplate;
    private static final String KEY_TEMPLATE = "rate_limit:%s";
    public boolean hasAvailableRequest(ServletRequest request, ServletResponse response){
            String key = String.format(KEY_TEMPLATE, getRequestClientId(request)).intern();
            Long availableRequests = redisTemplate.opsForValue().increment(key);
            if (availableRequests == null) {
                System.err.println("A redis error has occurred: failed to query available requests");       //might remove later
            }
            if (availableRequests == 1){
                redisTemplate.expire(key, Duration.ofSeconds(LIMIT_TIMEOUT_SECONDS));
            }
            return availableRequests <= REQUEST_LIMIT;
    }
    public String getRequestClientId(ServletRequest request){
        return request.getRemoteAddr();
    }
}
