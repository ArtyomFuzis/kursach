package net.apigateway.security;

import org.springframework.security.core.GrantedAuthority;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public enum Roles {

    ROLE_USER,
    ROLE_ADMIN,
    ROLE_ANONYMOUS;

    public String getAuthority() {
        return this.name();
    }
    public static List<Roles> getRoles(){
        return EnumSet.allOf(Roles.class).stream().toList();
    }
}
