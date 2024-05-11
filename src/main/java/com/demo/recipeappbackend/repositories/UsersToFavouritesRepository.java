package com.demo.recipeappbackend.repositories;

import com.demo.recipeappbackend.models.Recipe;
import com.demo.recipeappbackend.models.User;
import com.demo.recipeappbackend.models.UsersToFavourites;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UsersToFavouritesRepository extends JpaRepository<UsersToFavourites, Long> {
    boolean existsByUserAndRecipe(User user, Recipe recipe);

    Optional<UsersToFavourites> findByUserAndRecipe(User user, Recipe recipe);

    List<UsersToFavourites> findByUser(User user);
}
