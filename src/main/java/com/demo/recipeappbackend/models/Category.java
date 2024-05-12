package com.demo.recipeappbackend.models;


import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Category {
    MAIN_DISH("main dish"),
    BREAKFAST("breakfast"),
    DESSERT("dessert"),
    DRINKS("drinks"),
    SNACK("snack"),
    ALL("");

    private final String name;
}
