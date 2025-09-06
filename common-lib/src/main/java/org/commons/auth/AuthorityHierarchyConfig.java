package org.commons.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class AuthorityHierarchyConfig {
    @Bean
    public GrantedAuthoritiesMapper grantedAuthoritiesMapper() {
        return authorities -> {
            Set<GrantedAuthority> mapped = new HashSet<>();
            for(GrantedAuthority authority : authorities){
                mapped.add(authority);
                String role = authority.getAuthority();
                switch (role){
                    case "ROLE_ADMIN" -> {
                        mapped.add(new SimpleGrantedAuthority("ROLE_USER"));
                        mapped.add(new SimpleGrantedAuthority("ROLE_ANONYMOUS"));
                    }
                    case "ROLE_USER" ->
                        mapped.add(new SimpleGrantedAuthority("ROLE_ANONYMOUS"));
                }
            }
            return mapped;
        };
    }
}
