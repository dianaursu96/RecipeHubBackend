package com.demo.recipeappbackend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users_to_favourites") // id user_id, recipe_id
public class UsersToFavourites {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne  // Many recipes to one user (part of many-to-many)
    @JoinColumn(name = "recipe_id", referencedColumnName = "id")
    private Recipe recipe;


}
