package net.apigateway;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.auth.TokenLib;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

@SpringBootTest
@ComponentScan
public class AuthServiceTest {
    private AuthService authService;
    @Mock
    private HttpServletRequest request;
    private HttpServletResponse response;
    @Autowired
    public AuthServiceTest(AuthService authService){
        this.authService = authService;
    }
    @Test
    public void shouldReturnTrue(){
        Mockito.when(request.getRequestURL()).thenReturn(new StringBuffer().append("/api/public/cock"));
        var canAccess = authService.canAccess(request, response);
        Assertions.assertTrue(canAccess);
    }
    @Test
    public void shouldReturnFalse(){
        Mockito.when(request.getRequestURL()).thenReturn(new StringBuffer().append("/api/private/balls"));
        var canAccess = authService.canAccess(request, response);
        Assertions.assertFalse(canAccess);
    }
}
