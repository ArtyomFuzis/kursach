package net.apigateway;

import jakarta.servlet.*;
import lombok.RequiredArgsConstructor;
import net.apigateway.exceptions.NotAllowedException;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

//@Component
//@Order(2)
//@RequiredArgsConstructor
//public class AuthFilter implements Filter {
//
//    private final AuthService authService;
//
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
//        if (!authService.canAccess(request, response)){
//            throw new NotAllowedException("Access to requested resource is not allowed.");
//        }
//        chain.doFilter(request, response);
//    }
//}
