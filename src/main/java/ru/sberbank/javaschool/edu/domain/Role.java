package ru.sberbank.javaschool.edu.domain;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    TEACHER,
    STUDENT;

    @Override
    public String getAuthority() {
        return name();
    }
}
