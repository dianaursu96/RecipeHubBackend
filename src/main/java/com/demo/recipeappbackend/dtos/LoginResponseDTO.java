package com.demo.recipeappbackend.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LoginResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private String token;
    private String message;
    private List<Integer> favourites;

    public LoginResponseDTO(String firstName, String lastName, String email, String token, Long id, String role, String message, List<Integer> favourites) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.token = token;
        this.id = id;
        this.role = role;
        this.message = message;
        this.favourites = favourites;
    }
}