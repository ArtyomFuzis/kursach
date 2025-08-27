package net.apigateway;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.GenericContainer;

@SpringBootTest
class ApiGatewayApplicationTests {

    static {
        GenericContainer<?> redis = new GenericContainer<>("redis:alpine").withExposedPorts(6379);
        redis.start();
        System.setProperty("spring.data.redis.host", redis.getHost());
        System.setProperty("spring.data.redis.port", redis.getMappedPort(6379).toString());
    }
    @Test
    void contextLoads() {
    }


}
