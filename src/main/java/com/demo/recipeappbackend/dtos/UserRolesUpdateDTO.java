package com.demo.recipeappbackend.dtos;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRolesUpdateDTO {
    private Long userId;
    private String newRole;
}
