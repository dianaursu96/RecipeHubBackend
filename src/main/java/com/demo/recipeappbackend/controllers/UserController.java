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

    @Autowired
    private UserService userService;

    @Transactional
    @PutMapping("/update")
    public ResponseEntity<User> updateLoggedInUser(@RequestBody ProfileUpdateRequestDTO profileUpdateRequest) {
        User updatedUser = userService.updateLoggedInUser(profileUpdateRequest);
        return ResponseEntity.ok(updatedUser);
    }


}
