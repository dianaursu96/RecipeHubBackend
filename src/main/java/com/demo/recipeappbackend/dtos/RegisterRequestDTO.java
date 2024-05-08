package com.demo.recipeappbackend.dtos;

import com.demo.recipeappbackend.models.Recipe;
import com.demo.recipeappbackend.models.UsersToFavourites;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public class RegisterRequestDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private String password;
    public RegisterRequestDTO() {
        this.role = "READER"; // Default value for role
    }

    // Constructor with parameters
    public RegisterRequestDTO(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = "READER"; // Default value for role
    }
}
