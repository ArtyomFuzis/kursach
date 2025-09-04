package net.apigateway.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.project.TokenLib;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtValidationService {
    @Value("${token.signing.key}")
    private String jwtSigningKey;
    private final String claimsRoleKey = "roles";
    public SecretKey getSigningKey(){
        return Keys
                .hmacShaKeyFor(
                        Decoders.BASE64.decode(jwtSigningKey)
                );
    }
    public String generateToken(String username, List<GrantedAuthority> roles, Date expirationDate){
        return Jwts.builder()
                .subject(username)
                .claim("roles", roles.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .issuedAt(new Date())
                .expiration(expirationDate) // 1 min
                .signWith(getSigningKey())
                .compact();
    }
    private Claims extractAllClaims(String token){
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    private <T> T extractClaim(String token, Function<Claims, T> claimResolver){
        return claimResolver.apply(extractAllClaims(token));
    }
    private Date extractExprirationDate(String token){
        return extractClaim(token, Claims::getExpiration);
    }
    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }
    public List<GrantedAuthority> extractRoles(String token){
        return extractClaim(token, getRole());
    }
    private Function<Claims, List<GrantedAuthority>> getRole(){
        return
                (claims) -> (List<GrantedAuthority>) claims.get(claimsRoleKey, ArrayList.class)
                        .stream().map(role -> new SimpleGrantedAuthority(
                            role.toString()
                        )).collect(Collectors.toList());

//                (claims) -> List.of(
//                        new SimpleGrantedAuthority(
//                                String.format(roleTemplate, claims.get(claimsRoleKey, ArrayList.class))
//                        )
//                );
    }

}
