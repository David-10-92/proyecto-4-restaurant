package com.foodtech.proyecto4restaurant.repositories;

import com.foodtech.proyecto4restaurant.models.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository extends JpaRepository<Ingredient,Integer> {
}
