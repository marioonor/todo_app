package com.todoapp.logintodoapp.login.loginservice;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.todoapp.logintodoapp.login.loginentity.Users;

public class UserService implements UserDetails {

    @Autowired
    private Users users;

    public UserService(Users users) {
        this.users = users;
    }

    @Override
    public String getUsername() {
        return users.getEmail();
    }

    @Override
    public String getPassword() {
        return users.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getFirstName() {
        return users.getFirstName();
    }
}
