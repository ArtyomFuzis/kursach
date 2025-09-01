package net.apigateway.monitoring;

import lombok.Getter;

@Getter
public enum HealthErrors {
    REDIS_UNAVAILABLE(2),
    CONTEXT_UNINITIALIZED(1),
    OK(0);
    private final int code;
    HealthErrors(int code){ this.code = code;}
}
