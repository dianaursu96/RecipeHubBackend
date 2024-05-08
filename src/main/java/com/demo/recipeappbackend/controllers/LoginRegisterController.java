package com.demo.recipeappbackend.controllers;

import com.demo.recipeappbackend.dtos.LoginRequestDTO;
import com.demo.recipeappbackend.dtos.RegisterRequestDTO;
import com.demo.recipeappbackend.service.LoginRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

//@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping
public class LoginRegisterController {

    @Autowired
    private LoginRegisterService loginRegisterServiceService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        return loginRegisterServiceService.login(loginRequest);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequestDTO registerRequestDTO) {
        return loginRegisterServiceService.register(registerRequestDTO);
    }

    @PostMapping("/changeEmail")
    public ResponseEntity<?> changeEmail(@RequestParam String email) {
        return loginRegisterServiceService.changeEmail(email);
    }

    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@RequestParam String password) {
        return loginRegisterServiceService.changePassword(password);
    }
}