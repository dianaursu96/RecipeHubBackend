package com.demo.recipeappbackend.dtos;


import com.demo.recipeappbackend.models.Category;
import lombok.Data;

@Data
public class RecipeUpdateRequest {
    private String title;  // Nullable, optional update
    private Integer cookingTime;  // Use Integer for optional update
    private String imageURL;
    private String ingredients;
    private String steps;
    private Integer calories;  // Nullable
    private Integer protein;  // Nullable
    private Integer fat;  // Nullable
    private Integer carb;  // Nullable
    private String tags;
    private Boolean isPublished;  // Use Boolean for optional update
    private Category category;  // Nullable
}
