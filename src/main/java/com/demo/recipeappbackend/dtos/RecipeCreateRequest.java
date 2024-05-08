package com.demo.recipeappbackend.dtos;


import com.demo.recipeappbackend.models.Category;
import lombok.Data;

@Data
public class RecipeCreateRequest {
    private String title;
    private int cookingTime;
    private String imageURL;
    private String ingredients;
    private String steps;
    private int calories;
    private int protein;
    private int fat;
    private int carb;
    private String tags;
    private boolean isPublished;
    private Category category;
}
