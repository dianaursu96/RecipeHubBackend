package com.demo.recipeappbackend.controllers;


import com.demo.recipeappbackend.dtos.UserRolesUpdateDTO;
import com.demo.recipeappbackend.models.Recipe;
import com.demo.recipeappbackend.models.User;
import com.demo.recipeappbackend.service.RecipeService;
import com.demo.recipeappbackend.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/admin")
@PreAuthorize("hasAnyAuthority('ADMIN')")
public class AdminController {


    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @Transactional
    @GetMapping("/users/all")
    public ResponseEntity<List<User>> getAllUsersExceptLoggedInAdmin() {
        List<User> users = userService.getAllUsersExceptLoggedInAdmin();
        return ResponseEntity.ok(users);
    }
    @Transactional
    @PutMapping("/users/update/roles")
    public ResponseEntity<List<User>> updateRoles(@RequestBody List<UserRolesUpdateDTO> usersAndRoles) {
        List<User> updatedRoles = userService.updateRoles(usersAndRoles);
        return ResponseEntity.ok(updatedRoles);
    }




}
