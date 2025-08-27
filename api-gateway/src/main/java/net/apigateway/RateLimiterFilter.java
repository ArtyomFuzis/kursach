package net.apigateway;

import jakarta.servlet.*;
import lombok.*;
import net.apigateway.exceptions.TooManyRequestsException;
import net.apigateway.redis.RateLimiterService;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(1)
@RequiredArgsConstructor
public class RateLimiterFilter implements Filter {

    private final RateLimiterService rateLimiterService;

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        if (!rateLimiterService.hasAvailableRequest(request, response)){
            throw new TooManyRequestsException("Too many requests, please try again later.");
        }
        chain.doFilter(request, response);
    }

}
