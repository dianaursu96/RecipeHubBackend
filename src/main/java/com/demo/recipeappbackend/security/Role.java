package com.demo.recipeappbackend.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
    READER("READER"),
    CHEF("CHEF"),
    ADMIN("ADMIN");

    private final String role;
}
