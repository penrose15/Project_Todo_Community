package com.example.to_do_list.domain.role;

import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

@Getter
public enum Role {

    USER("USER"),
    ADMIN("ADMIN"),
    GUEST("GUEST");

    private final String role;

    Role(String role) {
        this.role = role;
    }

    public static Role of(String role) {
        return Arrays.stream(Role.values())
                .filter(r -> Objects.equals(r.getRole(), role))
                .findAny()
                .orElse(GUEST);
    }
}
