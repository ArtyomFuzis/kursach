package net.apigateway.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.auth.TokenLib;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.webauthn.authentication.WebAuthnAuthentication;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
//    private final JwtValidationService jwtValidationService;
//    private final TokenLib jwtValidationService;
    private final String AUTH_HEADER_NAME = "Authorization";
    private final String TOKEN_PREFIX = "Bearer ";
    private final int TOKEN_START_INDEX = TOKEN_PREFIX.length();
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(AUTH_HEADER_NAME);
        boolean tokenExists = authorizationHeader != null && authorizationHeader.startsWith(TOKEN_PREFIX);

        if (!tokenExists){
            filterChain.doFilter(request, response);
            return;
        }


        String token = authorizationHeader.substring(TOKEN_START_INDEX);
//        String username = jwtValidationService.extractUsername(token);
//        List<GrantedAuthority> roles = jwtValidationService.extractRoles(token);
        String username = TokenLib.extractUsername(token);
        List<GrantedAuthority> roles = TokenLib.extractRoles(token);
        Authentication auth = new UsernamePasswordAuthenticationToken(username, null, roles);

        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}
