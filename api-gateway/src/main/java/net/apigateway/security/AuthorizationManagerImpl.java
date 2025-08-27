package net.apigateway.security;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import net.apigateway.AuthService;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class AuthorizationManagerImpl implements AuthorizationManager<RequestAuthorizationContext> {
    private final AuthService authService;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authenticationSupplier, RequestAuthorizationContext context) {
        Authentication auth = authenticationSupplier.get();
        HttpServletRequest request = context.getRequest();
        String requestUrl = request.getRequestURI();

        Map<String, List<SimpleGrantedAuthority>> urlsAndRoles = authService.getUrlsAndRoles();
        List<String> urlPatterns = authService.getUrlPatterns();

        List<String> hasPathMatches = urlPatterns.stream().filter(pattern -> pathMatcher.match(pattern, requestUrl)).limit(1).toList();
        if (!hasPathMatches.isEmpty()){
            List<SimpleGrantedAuthority> neededRoles = urlsAndRoles.get(hasPathMatches.get(0));
            if (neededRoles.isEmpty()){
                return new AuthorizationDecision(true);
            } else {
                if (auth.getAuthorities().stream()
                        .anyMatch(role -> neededRoles.contains(role))){
                    return new AuthorizationDecision(true);
                }
                return new AuthorizationDecision(false);
            }
        }
        return new AuthorizationDecision(false);
    }
}
