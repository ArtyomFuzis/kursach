package org.commons.auth;

import java.util.EnumSet;
import java.util.List;

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
