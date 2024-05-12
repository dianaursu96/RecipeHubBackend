package com.demo.recipeappbackend.controllers;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("hasAnyAuthority('READER', 'ADMIN', 'CHEF')")
public class UserController {
    //updateProfile firstName, lastName, email, password





    private boolean isValidEmail(String email) {
        // Basic email validation logic
        return email != null && email.contains("@") && email.contains(".");
    }

    private boolean isValidPassword(String password) {
        if (password == null) {
            return false;
        }
        if (password.length() < 8) {
            return false;
        }
        boolean hasUpperCase = password.chars().anyMatch(Character::isUpperCase);
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);
        return hasUpperCase && hasDigit;
    }
}
