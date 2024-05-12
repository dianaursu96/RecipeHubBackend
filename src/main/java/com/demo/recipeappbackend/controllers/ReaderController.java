package com.demo.recipeappbackend.controllers;

import com.demo.recipeappbackend.models.Recipe;
import com.demo.recipeappbackend.models.Category;
import com.demo.recipeappbackend.models.UsersToFavourites;
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
    @GetMapping("/recipes/{id}")
    public ResponseEntity<Recipe> getRecipeById(@PathVariable Integer id) {
        Recipe recipe = recipeService.getRecipeById(id);
        return ResponseEntity.ok(recipe);
    }
    @Transactional
    @GetMapping("/recipes/favourites")
    public ResponseEntity<List<Recipe>> getFavouriteRecipes() {
        List<Recipe> favouriteRecipes = recipeService.getFavouriteRecipes();
        return ResponseEntity.ok(favouriteRecipes);
    }

    @Transactional
    @GetMapping("/search")
    public ResponseEntity<List<Recipe>> searchRecipes(@RequestParam("query") String searchString,  @RequestParam("category") String categoryName) {
        List<Recipe> recipes = recipeService.searchRecipesByTitleContainingAndCategory(searchString, categoryName);
        return ResponseEntity.ok(recipes);
    }

//    @Transactional
//    @GetMapping("/category")
//    public ResponseEntity<List<Recipe>> searchByCategory(@RequestParam("category") String categoryName) {
//        Category category;
//        try {
//            category = Category.valueOf(categoryName.toUpperCase());
//        } catch (IllegalArgumentException ex) {
//            return ResponseEntity.badRequest().body(null);
//        }
//
//        List<Recipe> recipes = recipeService.getRecipesByCategory(category);
//        return ResponseEntity.ok(recipes);
//    }

    @Transactional
    @PostMapping("/favourites/add")
    public ResponseEntity<List<Integer>> addToFavourites(@RequestParam("recipeId") Integer recipeId) {
        List<Integer> favouriteList = recipeService.addToFavourites(recipeId);
        return ResponseEntity.ok(favouriteList);
    }

    @Transactional
    @DeleteMapping("/favourites/delete")
    public ResponseEntity<List<Integer>> deleteFromFavourites(@RequestParam("recipeId") Integer recipeId) {
        List<Integer> favouriteList  = recipeService.deleteFromFavourites(recipeId);
        return ResponseEntity.ok(favouriteList);
    }
}

