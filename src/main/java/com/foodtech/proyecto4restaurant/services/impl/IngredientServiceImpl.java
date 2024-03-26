package com.foodtech.proyecto4restaurant.services.impl;

import com.foodtech.proyecto4restaurant.dtos.CreateIngredient;
import com.foodtech.proyecto4restaurant.dtos.IngredientDetails;
import com.foodtech.proyecto4restaurant.dtos.UpdateIngredient;
import com.foodtech.proyecto4restaurant.models.Ingredient;
import com.foodtech.proyecto4restaurant.repositories.IngredientRepository;
import com.foodtech.proyecto4restaurant.services.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class IngredientServiceImpl implements IngredientService {

    @Autowired
    IngredientRepository ingredientRepository;
    @Override
    public String addIngredient(CreateIngredient createIngredient) {
        Ingredient ingredient = new Ingredient();
        ingredient.setName(createIngredient.getName());
        ingredient.setMeasureId(createIngredient.getMeasureId());
        ingredient.setPurchasePrice(createIngredient.getPurchasePrice());
        ingredient.setSellPrice(createIngredient.getSellPrice());
        ingredient.setAllergens(createIngredient.getAllergens());
        ingredientRepository.save(ingredient);
        return ingredient.getName();
    }

    @Override
    public String deleteIngredient(Integer id) {
        Optional<Ingredient> optionalIngredient = ingredientRepository.findById(id);
        return optionalIngredient.map(ingredient -> {
            ingredientRepository.deleteById(id);
            return ("El ingrediente se ha eliminado correctamente");
        }).orElse("No se encontró ningún ingrediente con el ID proporcionado");
    }

    @Override
    public IngredientDetails getIngredient(Integer id) {
        Optional<Ingredient> optionalIngredient = ingredientRepository.findById(id);
        if (optionalIngredient.isPresent()) {
            Ingredient ingredient = optionalIngredient.get();
            IngredientDetails ingredientDetails = new IngredientDetails();
            ingredientDetails.setName(ingredient.getName());
            ingredientDetails.setMeasureId(ingredient.getMeasureId());
            ingredientDetails.setPurchasePrice(ingredient.getPurchasePrice());
            ingredientDetails.setSellPrice(ingredient.getSellPrice());
            ingredientDetails.setAllergens(ingredient.getAllergens());
            //No se sacar la lista de platos por que pienso que es de una
            // forma en la que esta los modelos de forma distinta mapeados
            // no esta terminado queda por devolver el ultimo atributo
            return ingredientDetails;
        } else {
            return null;
        }
    }

    @Override
    public List<Ingredient> listIngredients(String filter) {
        List<Ingredient> allIngredients = ingredientRepository.findAll();

        if (filter != null && !filter.isEmpty()) {
            return allIngredients.stream()
                    .filter(ingredient -> ingredient.getName().toLowerCase().contains(filter.toLowerCase()))
                    .collect(Collectors.toList());
        } else {
            return allIngredients;
        }
    }

    @Override
    public String updateIngredient(Integer id, UpdateIngredient updateIngredient) {
        return ingredientRepository.findById(id)
                .map(ingredient -> {
                    if (updateIngredient.getName() != null) {
                        ingredient.setName(updateIngredient.getName());
                    }
                    if (updateIngredient.getMeasureId() != null) {
                        ingredient.setMeasureId(updateIngredient.getMeasureId());
                    }
                    if (updateIngredient.getPurchasePrice() != null) {
                        ingredient.setPurchasePrice(updateIngredient.getPurchasePrice());
                    }
                    if (updateIngredient.getSellPrice() != null) {
                        ingredient.setSellPrice(updateIngredient.getSellPrice());
                    }
                    if (updateIngredient.getAllergens() != null) {
                        ingredient.setAllergens(updateIngredient.getAllergens());
                    }
                    ingredientRepository.save(ingredient);
                    return "El ingrediente se ha actualizado correctamente";
                }).orElse("No se encontró ningún ingrediente con el ID proporcionado");
    }
}
