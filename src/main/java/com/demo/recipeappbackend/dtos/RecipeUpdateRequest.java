package com.demo.recipeappbackend.dtos;


import com.demo.recipeappbackend.models.Category;
import lombok.Data;

@Data
public class RecipeUpdateRequest {
    private String title;
    private Integer cookingTime;
    private String imageURL;
    private String ingredients;
    private String steps;
    private Integer calories;
    private Integer protein;
    private Integer fat;
    private Integer carb;
    private String tags;
    private Boolean isPublished;
    private Category category;
}
