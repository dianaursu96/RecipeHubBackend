package com.demo.recipeappbackend.errors;

public class UnauthorizedAccessException extends RuntimeException {
    public UnauthorizedAccessException (String message) {
        super(message);
    }
}
