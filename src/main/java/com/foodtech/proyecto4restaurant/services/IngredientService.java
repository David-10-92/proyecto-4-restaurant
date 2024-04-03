package com.foodtech.proyecto4restaurant.services;

import com.foodtech.proyecto4restaurant.dtos.CreateIngredient;
import com.foodtech.proyecto4restaurant.dtos.IngredientDetails;
import com.foodtech.proyecto4restaurant.dtos.UpdateIngredient;
import com.foodtech.proyecto4restaurant.models.Ingredient;

import java.util.List;

public interface IngredientService {
    String addIngredient(CreateIngredient createIngredient);
    String deleteIngredient(Integer id);
    IngredientDetails getIngredient(Integer id);
    List<IngredientDetails> listIngredients(String filter);
    String updateIngredient(Integer id, UpdateIngredient updateIngredient);
}
