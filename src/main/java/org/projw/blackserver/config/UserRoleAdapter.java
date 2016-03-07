package org.projw.blackserver.config;

import org.springframework.security.core.GrantedAuthority;
import org.projw.blackserver.models.UserRole;

public class UserRoleAdapter implements GrantedAuthority {
    private UserRole role;

    public UserRoleAdapter(UserRole role) {
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return role.getName();
    }
}
