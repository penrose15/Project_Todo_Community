package com.example.to_do_list.domain.role;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Role {

    USER("user"),
    ADMIN("admin");

    private final String role;

    Role(String role) {
        this.role = role;
    }

    public Role convert(String role) {
        return Arrays.stream(Role.values())
                .filter(r -> r.getRole() == role)
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("role 이상함"));
    }
}
