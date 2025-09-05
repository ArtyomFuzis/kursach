package net.apigateway.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.auth.TokenLib;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@SpringBootTest

public class JwtAuthenticationFilterTest {

    private final JwtAuthenticationFilter filter;

    private final JwtValidationService service;
    @Autowired
    public JwtAuthenticationFilterTest(JwtValidationService service, JwtAuthenticationFilter filter){
        this.service = service;
        this.filter = filter;
    }
    @Mock
    private HttpServletRequest request;
    @Mock
    private FilterChain chain;

    @BeforeAll
    public static void init(){

    }
    @Test
    public void shouldReturnValid() throws ServletException, IOException {
        var role = new SimpleGrantedAuthority("ROLE_USER");
        TokenLib.generateToken("test", Collections.singletonList(role), new Date(System.currentTimeMillis() + 60_000));
        String testToken = TokenLib.generateToken("test", List.of(new SimpleGrantedAuthority("ROLE_USER")), new Date(System.currentTimeMillis() + 60_000));
        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer " + testToken);
        filter.doFilterInternal(request, null, chain);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Assertions.assertFalse(authentication.getAuthorities().isEmpty());
    }
    @Test
    public void shouldReturnInvalid(){

    }
}
