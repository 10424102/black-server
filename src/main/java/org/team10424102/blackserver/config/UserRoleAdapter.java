package org.team10424102.blackserver.config;

import org.springframework.security.core.GrantedAuthority;
import org.team10424102.blackserver.models.UserRole;

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
