package net.apigateway;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.apigateway.security.Roles;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final RedisTemplate<String, Map<String, List<String>>> redisTemplate;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    private final String URL_KEY = "access_urls";
    @Getter
    private volatile Map<String, List<SimpleGrantedAuthority>> urlsAndRoles;
    @Getter
    private List<String> urlPatterns;
    @PostConstruct
    public void init(){
        var tempMap = new HashMap<String, List<String>>();
        tempMap.put("/api/public/**", List.of());
        tempMap.put("/api/private/**", List.of(Roles.ROLE_USER.getAuthority(),Roles.ROLE_ADMIN.getAuthority()));
        tempMap.put("/auth/register", List.of());
        tempMap.put("/auth/login", List.of());
        redisTemplate.opsForValue().set(URL_KEY, tempMap);
        var fetchedUrls = redisTemplate.opsForValue().get(URL_KEY);
        var convertedUrls = convertToAuthority(fetchedUrls);
        urlsAndRoles = convertedUrls;
        urlPatterns = convertedUrls.keySet().stream().sorted((keyOne, keyTwo) -> keyTwo.length() - keyOne.length()).collect(Collectors.toList());
    }
    @Scheduled(fixedDelay = 60_000)
    public void fetchUrlsAndRoles(){
        Map<String, List<String>> fetchedUrls = redisTemplate.opsForValue().get(URL_KEY);
        var convertedUrls = convertToAuthority(fetchedUrls);
        urlsAndRoles = convertedUrls;
        urlPatterns = convertedUrls.keySet().stream().sorted((keyOne, keyTwo) -> keyTwo.length() - keyOne.length()).collect(Collectors.toList());
    }
    public boolean canAccess(ServletRequest request, ServletResponse response){
        HttpServletRequest httpRequset = (HttpServletRequest) request;
        String requestUrl = httpRequset.getRequestURL().toString();
        var hasMatches = urlPatterns.stream().filter(pattern -> pathMatcher.match(pattern, requestUrl)).limit(1).toList();
        if (!hasMatches.isEmpty()){
            List<SimpleGrantedAuthority> neededRoles = urlsAndRoles.get(hasMatches.get(0));
            if (neededRoles.isEmpty()){
                return true;
            }

            return false;
        }
        return false;
    }
    public Map<String, List<SimpleGrantedAuthority>> convertToAuthority(Map<String, List<String>> urls){
        Map<String, List<SimpleGrantedAuthority>> convertedUrls = new HashMap<>();
        for (var key: urls.keySet()){
            var roles = urls.get(key);
            List<SimpleGrantedAuthority> roleList = new ArrayList<>();
            for (var role: roles){
                roleList.add(new SimpleGrantedAuthority(role));
            }
            convertedUrls.put(key, roleList);
        }
        return convertedUrls;
    }
}
