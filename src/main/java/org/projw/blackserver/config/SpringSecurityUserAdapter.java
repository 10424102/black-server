package org.projw.blackserver.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.projw.blackserver.models.User;

import java.util.Collection;
import java.util.stream.Collectors;

public class SpringSecurityUserAdapter implements Authentication {
    private User user;

    public SpringSecurityUserAdapter(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles().stream().map(UserRoleAdapter::new).collect(Collectors.toSet());
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return user;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean b) throws IllegalArgumentException {
    }

    @Override
    public String getName() {
        return user.getUsername();
    }
}
