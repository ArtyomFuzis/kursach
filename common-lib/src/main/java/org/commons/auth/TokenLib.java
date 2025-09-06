package org.commons.auth;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.crypto.SecretKey;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TokenLib {
    @Value("${token.signing.key}")
    private static String jwtSigningKey;
    private final static String claimsRoleKey = "roles";
    private TokenLib() {}

    public static SecretKey getSigningKey(){
        return Keys
                .hmacShaKeyFor(
                        Decoders.BASE64.decode(jwtSigningKey)
                );
    }

    public static String generateToken(String username, List<GrantedAuthority> roles, Date expirationDate){
        return Jwts.builder()
                .subject(username)
                .claim(claimsRoleKey, roles.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .issuedAt(new Date())
                .expiration(expirationDate) // 1 min
                .signWith(getSigningKey())
                .compact();
    }

    private static Claims extractAllClaims(String token){
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private static <T> T extractClaim(String token, Function<Claims, T> claimResolver){
        return claimResolver.apply(extractAllClaims(token));
    }

    private static Date extractExprirationDate(String token){
        return extractClaim(token, Claims::getExpiration);
    }
    public static String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }
    public static List<GrantedAuthority> extractRoles(String token){
        return extractClaim(token, getRole());
    }
    private static Function<Claims, List<GrantedAuthority>> getRole(){
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