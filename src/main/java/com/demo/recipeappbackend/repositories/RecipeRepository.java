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

//    @Query("SELECT r FROM Recipe r WHERE LOWER(r.title) LIKE LOWER(CONCAT('%', :searchString, '%')) AND (:category IS NULL OR r.category = :category)")
//    List<Recipe> findByTitleContainingAndCategory(@Param("searchString") String searchString, Category category);
    @Query("SELECT r FROM Recipe r WHERE LOWER(r.title) LIKE LOWER(CONCAT('%', :searchString, '%'))")
    List<Recipe> findByTitleContaining(@Param("searchString") String searchString);

    @Query("SELECT r FROM Recipe r WHERE LOWER(r.title) LIKE LOWER(CONCAT('%', :searchString, '%')) AND (:category IS NULL OR r.category = :category)")
    List<Recipe> findByTitleContainingAndCategory(@Param("searchString") String searchString, Category category);
    List<Recipe> findByCategory(Category category);


    /*

 Create:
   save(T entity): Persists the entity in the database. If the entity has an ID, it updates it; otherwise, it creates a new record.
Read:
   findById(ID id): Returns an Optional<T> for the given ID.
   findAll(): Returns all records in the repository.
   findAllById(Iterable<ID> ids): Returns all entities with the given IDs.
Update:
   save(T entity): Also acts as an update if the entity has an ID.
Delete:
   delete(T entity): Deletes a specific entity.
   deleteById(ID id): Deletes an entity by its ID.
   deleteAll(): Deletes all entities in the repository.
    */
}
