package net.apigateway.redis;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.Duration;

public class RateLimiterServiceTest {
    private static RateLimiterService rateLimiterService;
    private static StringRedisTemplate redisTemplate;
    private static ServletRequest request;
    private static ServletResponse response;
    @BeforeAll
    public static void init(){
        redisTemplate = new StringRedisTemplate();
        var connectionFactory = new LettuceConnectionFactory();
        connectionFactory.setPort(6379);
        connectionFactory.setHostName("localhost");
        connectionFactory.start();
        redisTemplate.setConnectionFactory(connectionFactory);
        rateLimiterService = new RateLimiterService(redisTemplate);
        redisTemplate.afterPropertiesSet();
        request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getRemoteAddr()).thenReturn("1.1.1.1");

    }
    @BeforeEach
    public void clear(){
        redisTemplate.opsForValue().set("rate_limit:1.1.1.1", String.valueOf(0));
    }
    @Test
    public void shouldReturnTrue(){
        boolean hasAvailable = rateLimiterService.hasAvailableRequest(request, response);
        Assertions.assertTrue(hasAvailable);
    }
    @Test
    public void shouldReturnFalse(){
        redisTemplate.opsForValue().set("rate_limit:1.1.1.1", String.valueOf(10));
        redisTemplate.expire("rate_limit:1.1.1.1", Duration.ofSeconds(5));
        boolean hasAvailable = rateLimiterService.hasAvailableRequest(request, response);
        Assertions.assertFalse(hasAvailable);
    }
    @Test
    public void shouldReturnFalseBeforeTimeWindowExpireAndTrueAfter() throws InterruptedException{
        redisTemplate.opsForValue().set("rate_limit:1.1.1.1", String.valueOf(10));
        redisTemplate.expire("rate_limit:1.1.1.1", Duration.ofSeconds(5));
        boolean hasAvailable = rateLimiterService.hasAvailableRequest(request, response);
        Assertions.assertFalse(hasAvailable);
        Thread.sleep(6000);
        hasAvailable = rateLimiterService.hasAvailableRequest(request, response);
        Assertions.assertTrue(hasAvailable);
    }
}
