package com.demo.recipeappbackend.controllers;

import com.demo.recipeappbackend.models.Recipe;
import com.demo.recipeappbackend.models.Category;
import com.demo.recipeappbackend.service.RecipeService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(path = "/reader")
@PreAuthorize("hasAnyAuthority('READER')")
public class ReaderController {

    @Autowired
    private final RecipeService recipeService;

    public ReaderController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @Transactional
    @GetMapping("/recipes/all")
    public ResponseEntity<List<Recipe>> getAllRecipes() {
        List<Recipe> recipes = recipeService.getAllRecipes();
        return ResponseEntity.ok(recipes);
    }
    @Transactional
    @GetMapping("/recipes/favourites")
    public ResponseEntity<List<Recipe>> getFavouriteRecipes() {
        List<Recipe> favouriteRecipes = recipeService.getFavouriteRecipes();
        return ResponseEntity.ok(favouriteRecipes);
    }

    @Transactional
    @GetMapping("/search")
    public ResponseEntity<List<Recipe>> searchRecipes(@RequestParam("query") String searchString) {
        List<Recipe> recipes = recipeService.searchRecipes(searchString);
        return ResponseEntity.ok(recipes);
    }

    @Transactional
    @GetMapping("/category")
    public ResponseEntity<List<Recipe>> searchByCategory(@RequestParam("category") String categoryName) {
        Category category;
        try {
            category = Category.valueOf(categoryName.toUpperCase());
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(null);
        }

        List<Recipe> recipes = recipeService.getRecipesByCategory(category);
        return ResponseEntity.ok(recipes);
    }

    @Transactional
    @PostMapping("/favourites/add")
    public ResponseEntity<String> addToFavourites(@RequestParam("recipeId") Integer recipeId) {
        recipeService.addToFavourites(recipeId);
        return ResponseEntity.ok("Recipe added to favourites successfully");
    }

    @Transactional
    @DeleteMapping("/favourites/delete")
    public ResponseEntity<String> deleteFromFavourites(@RequestParam("recipeId") Integer recipeId) {
        recipeService.deleteFromFavourites(recipeId);
        return ResponseEntity.ok("Recipe deleted from favourites successfully");
    }
}

