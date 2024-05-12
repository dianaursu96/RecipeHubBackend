package com.demo.recipeappbackend.controllers;


import com.demo.recipeappbackend.dtos.ProfileUpdateRequestDTO;
import com.demo.recipeappbackend.dtos.RecipeUpdateRequest;
import com.demo.recipeappbackend.models.Recipe;
import com.demo.recipeappbackend.models.User;
import com.demo.recipeappbackend.service.RecipeService;
import com.demo.recipeappbackend.service.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("hasAnyAuthority('READER', 'ADMIN', 'CHEF')")
public class UserController {
    //updateProfile firstName, lastName, email, password
    @Autowired
    private UserService userService;

    @Transactional
    @PutMapping("/update")
    public ResponseEntity<User> updateLoggedInUser(@RequestBody ProfileUpdateRequestDTO profileUpdateRequest) {
        User updatedUser = userService.updateLoggedInUser(profileUpdateRequest);
        return ResponseEntity.ok(updatedUser);
    }


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
