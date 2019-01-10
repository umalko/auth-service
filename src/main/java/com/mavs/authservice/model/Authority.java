package com.mavs.authservice.model;

import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;

public enum Authority implements GrantedAuthority, Serializable {

    USER,
    ADMIN;

    @Override
    public String getAuthority() {
        return this.name();
    }
}
