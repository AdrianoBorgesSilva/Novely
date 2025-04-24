package com.novely.novely.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.novely.novely.domain.User;

public class CustomUserDetails implements UserDetails{
    
    private final User user;

    public CustomUserDetails(User user){
        this.user = user;
    }

    public String getId() {
        return user.getId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList(); 
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public String getPassword() {
        return user.getPassword(); 
    }
}
