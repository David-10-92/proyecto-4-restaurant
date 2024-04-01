package com.foodtech.proyecto4restaurant.services.impl;

import com.foodtech.proyecto4restaurant.dtos.AllergenDetails_allOf_presentIn;
import com.foodtech.proyecto4restaurant.dtos.CreateIngredient;
import com.foodtech.proyecto4restaurant.dtos.IngredientDetails;
import com.foodtech.proyecto4restaurant.dtos.UpdateIngredient;
import com.foodtech.proyecto4restaurant.models.*;
import com.foodtech.proyecto4restaurant.repositories.AllergenRepository;
import com.foodtech.proyecto4restaurant.repositories.IngredientRepository;
import com.foodtech.proyecto4restaurant.repositories.MeasureRepository;
import com.foodtech.proyecto4restaurant.services.AllergenService;
import com.foodtech.proyecto4restaurant.services.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class IngredientServiceImpl implements IngredientService {

    @Autowired
    IngredientRepository ingredientRepository;
    @Autowired
    MeasureRepository measureRepository;
    @Autowired
    AllergenRepository allergenRepository;
    @Override
    public String addIngredient(CreateIngredient createIngredient) {
        Measure measure = getMeasure(createIngredient.getMeasureId());

        // Obtener los IDs de alérgenos de la lista de alérgenos de createIngredient
        List<Integer> allergenIds = createIngredient.getAllergens().stream()
                .map(Allergen::getId)
                .collect(Collectors.toList());

        // Obtener la lista de objetos Allergen correspondientes a los IDs
        List<Allergen> allergens = getAllergens(allergenIds);

        Ingredient ingredient = new Ingredient();
        ingredient.setName(createIngredient.getName());
        ingredient.setMeasureId(measure);
        ingredient.setPurchasePrice(createIngredient.getPurchasePrice());
        ingredient.setSellPrice(createIngredient.getSellPrice());
        ingredient.setAllergens(allergens);

        ingredientRepository.save(ingredient);
        return ingredient.getName();
    }
    private Measure getMeasure(Integer measureId) {
        return measureRepository.findById(measureId)
                .orElseThrow(() -> new RuntimeException("La medida especificada no existe."));
    }

    private List<Allergen> getAllergens(List<Integer> id) {
        List<Allergen> allergens = allergenRepository.findAllById(id);
        if (allergens.size() != id.size()) {
            throw new RuntimeException("Al menos uno de los IDs especificados no existe.");
        }
        return allergens;
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
            //probando get id
            ingredientDetails.setMeasureId(ingredient.getMeasureId().getId());
            ingredientDetails.setPurchasePrice(ingredient.getPurchasePrice());
            ingredientDetails.setSellPrice(ingredient.getSellPrice());
            ingredientDetails.setAllergens(ingredient.getAllergens());
            ingredientDetails.setUsedIn(getPlatesUsingIngredient(ingredient));
            return ingredientDetails;
        } else {
            return null;
        }
    }

    private List<AllergenDetails_allOf_presentIn> getPlatesUsingIngredient(Ingredient ingredient) {
        List<AllergenDetails_allOf_presentIn> usedInList = new ArrayList<>();
        for (AmountOfIngredient amountOfIngredient : ingredient.getAmountsInDishes()) {
            AllergenDetails_allOf_presentIn presentIn = new AllergenDetails_allOf_presentIn();
            presentIn.setId(amountOfIngredient.getDish().getId());
            presentIn.setName(amountOfIngredient.getDish().getName());
            usedInList.add(presentIn);
        }
        return usedInList;
    }

    @Override
    public List<Ingredient> listIngredients(String filter) {
        List<Ingredient> allIngredients = ingredientRepository.findAll();

        if (filter != null && !filter.isEmpty()) {
            String filterLower = filter.toLowerCase();
            return allIngredients.stream()
                    .filter(ingredient -> {
                        // Filtrar por nombre del ingrediente
                        boolean nameMatch = ingredient.getName().toLowerCase().contains(filterLower);

                        // Filtrar por nombre del alérgeno
                        boolean allergenMatch = ingredient.getAllergens().stream()
                                .anyMatch(allergen -> allergen.getName().toLowerCase().contains(filterLower));

                        // Retornar verdadero si alguna de las condiciones coincide
                        return nameMatch && allergenMatch;
                    })
                    .collect(Collectors.toList());
        } else {
            // Devolver la lista completa de ingredientes ordenada
            return allIngredients.stream()
                    .sorted(Comparator.comparing(Ingredient::getName))
                    .collect(Collectors.toList());
        }
    }

    @Override
    public String updateIngredient(Integer id, UpdateIngredient updateIngredient) {
        Measure measure = measureRepository.findById(updateIngredient.getMeasureId())
                .orElseThrow(() -> new RuntimeException("La medida especificada no existe."));
        return ingredientRepository.findById(id)
                .map(ingredient -> {
                    if (updateIngredient.getName() != null) {
                        ingredient.setName(updateIngredient.getName());
                    }
                    if (updateIngredient.getMeasureId() != null) {
                        ingredient.setMeasureId(measure);
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
