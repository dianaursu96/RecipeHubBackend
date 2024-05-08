package com.demo.recipeappbackend.errors;

public class EmailUsedException extends RuntimeException {
    public EmailUsedException(String message) {
        super(message);
    }
}