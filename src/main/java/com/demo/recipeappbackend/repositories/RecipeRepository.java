package com.demo.recipeappbackend.repositories;

import com.demo.recipeappbackend.models.Category;
import com.demo.recipeappbackend.models.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Integer> {
    List<Recipe> findByTitle(String title);
    //Optional<Recipe> findById(int id);

    @Query("SELECT r FROM Recipe r WHERE r.chef.id = :chefId")
    List<Recipe> findByChefId(@Param("chefId") int chefId);

    @Query("SELECT r FROM Recipe r WHERE r.isPublished = true")
    List<Recipe> findPublishedRecipes();  // Finds published recipes

    @Query("SELECT r FROM Recipe r WHERE LOWER(r.title) LIKE LOWER(CONCAT('%', :searchString, '%')) AND r.isPublished = true")
    List<Recipe> findByTitleContaining(@Param("searchString") String searchString);

    @Query("SELECT r FROM Recipe r WHERE LOWER(r.title) LIKE LOWER(CONCAT('%', :searchString, '%')) AND (:category IS NULL OR r.category = :category) AND r.isPublished = true")
    List<Recipe> findByTitleContainingAndCategory(@Param("searchString") String searchString, Category category);
    List<Recipe> findByCategory(Category category);



}
