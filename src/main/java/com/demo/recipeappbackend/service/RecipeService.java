package com.demo.recipeappbackend.service;

import com.demo.recipeappbackend.dtos.RecipeCreateRequest;
import com.demo.recipeappbackend.dtos.RecipeUpdateRequest;
import com.demo.recipeappbackend.errors.RecipeAlreadyFavouredException;
import com.demo.recipeappbackend.errors.ResourceNotFoundException;
import com.demo.recipeappbackend.errors.UnauthorizedAccessException;
import com.demo.recipeappbackend.models.Category;
import com.demo.recipeappbackend.models.Recipe;
import com.demo.recipeappbackend.models.User;
import com.demo.recipeappbackend.models.UsersToFavourites;
import com.demo.recipeappbackend.repositories.RecipeRepository;
import com.demo.recipeappbackend.repositories.UsersToFavouritesRepository;
import com.demo.recipeappbackend.security.UserDetailsImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final UsersToFavouritesRepository usersToFavouritesRepository;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository, UsersToFavouritesRepository usersToFavouritesRepository) {
        this.recipeRepository = recipeRepository;
        this.usersToFavouritesRepository = usersToFavouritesRepository;
    }
    public User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImplementation userDetails = (UserDetailsImplementation) authentication.getPrincipal();
        return userDetails.getUser();
    }
    public List<Recipe> getAllRecipes() {
        return recipeRepository.findPublishedRecipes();
    }
    public Recipe getRecipeById (Integer recipeId) {
        Optional<Recipe> existingRecipeOptional = recipeRepository.findById(recipeId);
        Recipe existingRecipe;

        if (existingRecipeOptional.isPresent()) {
            existingRecipe = existingRecipeOptional.get();
        } else {
            throw new ResourceNotFoundException("Recipe not found");
        }
        return existingRecipe;
    }


    public List<Recipe> searchRecipesByTitleContainingAndCategory(String searchString, String categoryName) {
        if (StringUtils.isEmpty(categoryName)) {
            return recipeRepository.findByTitleContaining(searchString);
        } else {
            Category category;
            try {
                category = Category.valueOf(categoryName.toUpperCase());
            } catch (IllegalArgumentException ex) {
                return Collections.emptyList();
            }
            return recipeRepository.findByTitleContainingAndCategory(searchString, category);
        }
    }

    public List<Recipe> getFavouriteRecipes() {
        User user = getLoggedInUser();
        List<UsersToFavourites> favourites = usersToFavouritesRepository.findByUser(user);

        return favourites.stream()
                .map(UsersToFavourites::getRecipe)
                .collect(Collectors.toList());
    }
    @Transactional
    public List<Integer> addToFavourites(Integer recipeId) {
        User user = getLoggedInUser();
        Optional<Recipe> existingRecipeOptional = recipeRepository.findById(recipeId);
        Recipe existingRecipe;

        if (existingRecipeOptional.isPresent()) {
            existingRecipe = existingRecipeOptional.get();
        } else {
            throw new ResourceNotFoundException("Recipe not found");
        }

        if ( usersToFavouritesRepository.existsByUserAndRecipe(user, existingRecipe)) {
            throw new RecipeAlreadyFavouredException("Recipe already added to favourites");
        }

        UsersToFavourites favourite = new UsersToFavourites();
        favourite.setUser(user);
        favourite.setRecipe(existingRecipe);
        usersToFavouritesRepository.save(favourite);
        List<UsersToFavourites> favouriteEntries = usersToFavouritesRepository.findByUser(user);
        return favouriteEntries.stream()
                .map(UsersToFavourites::getRecipe)
                .map(Recipe::getId)
                .collect(Collectors.toList());
    }
    @Transactional
    public List<Integer> deleteFromFavourites (Integer recipeId) {
        User user = getLoggedInUser();

        Optional<Recipe> existingRecipeOptional = recipeRepository.findById(recipeId);
        Recipe existingRecipe;

        if (existingRecipeOptional.isPresent()) {
            existingRecipe = existingRecipeOptional.get();
        } else {
            throw new ResourceNotFoundException("Recipe not found");
        }
        Optional<UsersToFavourites> favouriteOptional = usersToFavouritesRepository.findByUserAndRecipe(user, existingRecipe);
        UsersToFavourites favourite;
        if (favouriteOptional.isPresent()) {
            favourite = favouriteOptional.get();
        } else {
            throw new ResourceNotFoundException("Recipe is not in your favourites");
        }
        usersToFavouritesRepository.delete(favourite);
        List<UsersToFavourites> favouriteEntries = usersToFavouritesRepository.findByUser(user);
        return favouriteEntries.stream()
                .map(UsersToFavourites::getRecipe)
                .map(Recipe::getId)
                .collect(Collectors.toList());
    }

    public List<Recipe> getAllRecipesForLoggedInChef() {
        User chef = getLoggedInUser();
        return recipeRepository.findByChefId(chef.getId().intValue());
    }

    public Recipe getRecipeByIdForLoggedInChef(Integer recipeId) {
        User loggedInChef = getLoggedInUser();
        Optional<Recipe> existingRecipeOptional = recipeRepository.findById(recipeId);
        Recipe existingRecipe;

        if (existingRecipeOptional.isPresent()) {
            existingRecipe = existingRecipeOptional.get();
        } else {
            throw new ResourceNotFoundException("Recipe not found");
        }

        if (!Objects.equals(existingRecipe.getChef().getId(), loggedInChef.getId())) {
            throw new UnauthorizedAccessException("You are not authorized to view this recipe");
        }
        return existingRecipe;
    }

    @Transactional
    public Recipe createRecipe(RecipeCreateRequest recipeRequest) {
        User chef = getLoggedInUser();
        Recipe newRecipe = new Recipe();
        newRecipe.setTitle(recipeRequest.getTitle());
        newRecipe.setCookingTime(recipeRequest.getCookingTime());
        newRecipe.setImageURL(recipeRequest.getImageURL());
        newRecipe.setIngredients(recipeRequest.getIngredients());
        newRecipe.setSteps(recipeRequest.getSteps());
        newRecipe.setCalories(recipeRequest.getCalories());
        newRecipe.setProtein(recipeRequest.getProtein());
        newRecipe.setFat(recipeRequest.getFat());
        newRecipe.setCarb(recipeRequest.getCarb());
        newRecipe.setTags(recipeRequest.getTags());
        newRecipe.setPublished(recipeRequest.isPublished());
        newRecipe.setCategory(recipeRequest.getCategory());
        newRecipe.setCreatedAt(LocalDateTime.now());
        newRecipe.setUpdatedAt(LocalDateTime.now());
        newRecipe.setChef(chef);
        recipeRepository.save(newRecipe);
        return newRecipe;
    }

    @Transactional
    public Recipe updateRecipeById(Integer recipeId, RecipeUpdateRequest recipeUpdateRequest) {
        User loggedInChef = getLoggedInUser();

        Optional<Recipe> existingRecipeOptional = recipeRepository.findById(recipeId);
        Recipe existingRecipe;

        if (existingRecipeOptional.isPresent()) {
            existingRecipe = existingRecipeOptional.get();
        } else {
            throw new ResourceNotFoundException("Recipe not found");
        }

        if (!Objects.equals(existingRecipe.getChef().getId(), loggedInChef.getId())) {
            throw new UnauthorizedAccessException("You are not authorized to update this recipe");
        }

        if (recipeUpdateRequest.getTitle() != null) {
            existingRecipe.setTitle(recipeUpdateRequest.getTitle());
        }

        if (recipeUpdateRequest.getCookingTime() != null) {
            existingRecipe.setCookingTime(recipeUpdateRequest.getCookingTime());
        }

        if (recipeUpdateRequest.getImageURL() != null) {
            existingRecipe.setImageURL(recipeUpdateRequest.getImageURL());
        }

        if (recipeUpdateRequest.getIngredients() != null) {
            existingRecipe.setIngredients(recipeUpdateRequest.getIngredients());
        }

        if (recipeUpdateRequest.getSteps() != null) {
            existingRecipe.setSteps(recipeUpdateRequest.getSteps());
        }

        if (recipeUpdateRequest.getCalories() != null) {
            existingRecipe.setCalories(recipeUpdateRequest.getCalories());
        }

        if (recipeUpdateRequest.getProtein() != null) {
            existingRecipe.setProtein(recipeUpdateRequest.getProtein());
        }

        if (recipeUpdateRequest.getFat() != null) {
            existingRecipe.setFat(recipeUpdateRequest.getFat());
        }

        if (recipeUpdateRequest.getCarb() != null) {
            existingRecipe.setCarb(recipeUpdateRequest.getCarb());
        }

        if (recipeUpdateRequest.getTags() != null) {
            existingRecipe.setTags(recipeUpdateRequest.getTags());
        }

        if (recipeUpdateRequest.getIsPublished() != null) {
            existingRecipe.setPublished(recipeUpdateRequest.getIsPublished());
        }

        if (recipeUpdateRequest.getCategory() != null) {
            existingRecipe.setCategory(recipeUpdateRequest.getCategory());
        }

        existingRecipe.setUpdatedAt(LocalDateTime.now());

        return recipeRepository.save(existingRecipe);
    }
    @Transactional
    public Recipe updateIsPublishedRecipeById(Integer recipeId) {
        User loggedInChef = getLoggedInUser();

        Optional<Recipe> existingRecipeOptional = recipeRepository.findById(recipeId);
        Recipe existingRecipe;

        if (existingRecipeOptional.isPresent()) {
            existingRecipe = existingRecipeOptional.get();
        } else {
            throw new ResourceNotFoundException("Recipe not found");
        }

        if (!Objects.equals(existingRecipe.getChef().getId(), loggedInChef.getId())) {
            throw new UnauthorizedAccessException("You are not authorized to publish this recipe");
        }
        boolean isPublished = existingRecipe.getIsPublished();
        existingRecipe.setPublished(!isPublished);
        return recipeRepository.save(existingRecipe);
    }

    public void deleteRecipeById(Integer recipeId) {
        User loggedInChef = getLoggedInUser();

        Optional<Recipe> existingRecipeOptional = recipeRepository.findById(recipeId);
        Recipe existingRecipe;

        if (existingRecipeOptional.isPresent()) {
            existingRecipe = existingRecipeOptional.get();
        } else {
            throw new ResourceNotFoundException("Recipe not found");
        }

        if (!Objects.equals(existingRecipe.getChef().getId(), loggedInChef.getId())) {
            throw new UnauthorizedAccessException("You are not authorized to delete this recipe");
        }
        recipeRepository.deleteById((recipeId));
    }
}
