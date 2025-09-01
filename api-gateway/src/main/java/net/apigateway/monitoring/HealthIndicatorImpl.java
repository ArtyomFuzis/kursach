package net.apigateway.monitoring;

import org.hibernate.resource.beans.container.internal.NoSuchBeanException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.WebApplicationContextUtils;

import java.util.List;
import java.util.Map;

@Component
public class HealthIndicatorImpl implements HealthIndicator {
    private final ApplicationContext applicationContext;
    @Autowired
    public HealthIndicatorImpl(ApplicationContext applicationContext){
        this.applicationContext = applicationContext;
    }
    @Override
    public Health health(){
        int errorCode = performHealthCheck();
        if (errorCode != 0){
            return Health.down().withDetail("Error code", errorCode).build();
        }
        return Health.up().build();
    }
    public int performHealthCheck(){
        if (applicationContext == null){
            return HealthErrors.CONTEXT_UNINITIALIZED.getCode();
        }
        try{
            applicationContext.getBean("urlInfoRedisTemplate");
        } catch (NoSuchBeanException | BeansException e) {
            return HealthErrors.REDIS_UNAVAILABLE.getCode();
        }
        return HealthErrors.OK.getCode();
    }
}
