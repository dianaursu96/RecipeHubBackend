package com.demo.recipeappbackend.errors;

public class RecipeAlreadyFavouredException extends RuntimeException {
    public RecipeAlreadyFavouredException(String message) {
        super(message);
    }
}
