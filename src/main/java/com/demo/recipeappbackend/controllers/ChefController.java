package com.demo.recipeappbackend.controllers;

import com.demo.recipeappbackend.dtos.RecipeCreateRequest;
import com.demo.recipeappbackend.dtos.RecipeUpdateRequest;
import com.demo.recipeappbackend.models.Recipe;
import com.demo.recipeappbackend.models.User;
import com.demo.recipeappbackend.security.UserDetailsImplementation;
import com.demo.recipeappbackend.service.RecipeService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/chef")
@PreAuthorize("hasAnyAuthority('CHEF')")
public class ChefController {
    @Autowired
    private RecipeService recipeService;

    @Transactional
    @GetMapping("/recipes/all")
    public ResponseEntity<List<Recipe>> getAllRecipesForLoggedInChef() {
        List<Recipe> recipes = recipeService.getAllRecipesForLoggedInChef();
        return ResponseEntity.ok(recipes);
    }

    @Transactional
    @GetMapping("/recipes/{id}")
    public ResponseEntity<Recipe> getRecipeByIdForLoggedInChef(@PathVariable Integer id) {
        Recipe recipe = recipeService.getRecipeByIdForLoggedInChef(id);
        return ResponseEntity.ok(recipe);
    }

    @Transactional
    @PostMapping("/recipes/create")
    public ResponseEntity<Recipe> createRecipe(@Valid @RequestBody RecipeCreateRequest recipeRequest) {
        Recipe createdRecipe = recipeService.createRecipe(recipeRequest);
        return ResponseEntity.ok(createdRecipe);
    }

    @Transactional
    @PutMapping("/recipes/update/{recipeId}")
    public ResponseEntity<Recipe> updateRecipeById(@PathVariable Integer recipeId, @Valid @RequestBody RecipeUpdateRequest recipeUpdateRequest) {
        Recipe updatedRecipe = recipeService.updateRecipeById(recipeId, recipeUpdateRequest);
        return ResponseEntity.ok(updatedRecipe);
    }

    @Transactional
    @PutMapping("/recipes/publish/{recipeId}")
    public ResponseEntity<Recipe> updateIsPublishedRecipeById(@PathVariable int recipeId) {
        Recipe updatedRecipe = recipeService.updateIsPublishedRecipeById(recipeId);
        return ResponseEntity.ok(updatedRecipe);
    }

    @Transactional
    @DeleteMapping("/recipes/delete/{recipeId}")
    public ResponseEntity<String> deleteRecipeById(@PathVariable Integer recipeId) {
        recipeService.deleteRecipeById(recipeId);
        return ResponseEntity.ok("Recipe deleted successfully");
    }

}
