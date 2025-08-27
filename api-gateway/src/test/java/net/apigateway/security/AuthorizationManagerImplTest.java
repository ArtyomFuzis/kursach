package net.apigateway.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.AssertTrue;
import net.apigateway.AuthService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.server.authorization.AuthorizationContext;

import java.net.http.HttpRequest;
import java.util.List;
import java.util.function.Supplier;

@SpringBootTest
public class AuthorizationManagerImplTest {
    private AuthService authService;
    private AuthorizationManagerImpl authorizationManager;
    @Mock
    private Authentication authentication;
    @Mock
    private RequestAuthorizationContext context;
    @Mock
    private Supplier<Authentication> authenticationSupplier;
    @Mock
    private HttpServletRequest request;
    @Autowired
    public AuthorizationManagerImplTest(AuthService authService){
        this.authService = authService;
        this.authorizationManager = new AuthorizationManagerImpl(authService);
    }
    @Test
    public void shouldReturnTrue(){
        Mockito.when(authentication.getAuthorities()).thenReturn(List.of());
        Mockito.when(authenticationSupplier.get()).thenReturn(authentication);
        Mockito.when(request.getRequestURI()).thenReturn("/api/public/cock");
        Mockito.when(context.getRequest()).thenReturn(request);
        var result = authorizationManager.check(authenticationSupplier, context).isGranted();
        Assertions.assertTrue(result);
    }
    @Test
    public void shouldReturnFalse(){
        Mockito.when(authentication.getAuthorities()).thenReturn(List.of());
        Mockito.when(authenticationSupplier.get()).thenReturn(authentication);
        Mockito.when(request.getRequestURI()).thenReturn("/api/private/balls");
        Mockito.when(context.getRequest()).thenReturn(request);
        var result = authorizationManager.check(authenticationSupplier, context).isGranted();
        Assertions.assertFalse(result);
    }
}
