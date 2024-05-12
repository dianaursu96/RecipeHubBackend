package com.demo.recipeappbackend.dtos;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class ProfileUpdateRequestDTO {
    private String firstName;
    private String lastName;
    private String email;
}
