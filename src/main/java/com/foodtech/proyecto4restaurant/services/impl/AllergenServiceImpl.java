package com.foodtech.proyecto4restaurant.services.impl;

import com.foodtech.proyecto4restaurant.dtos.AllergenDetails_allOf_presentIn;
import com.foodtech.proyecto4restaurant.models.Allergen;
import com.foodtech.proyecto4restaurant.dtos.AllergenDetails;
import com.foodtech.proyecto4restaurant.dtos.CreateAllergen;
import com.foodtech.proyecto4restaurant.dtos.UpdateAllergen;
import com.foodtech.proyecto4restaurant.models.AmountOfIngredient;
import com.foodtech.proyecto4restaurant.models.Dish;
import com.foodtech.proyecto4restaurant.repositories.AllergenRepository;
import com.foodtech.proyecto4restaurant.repositories.DishRepository;
import com.foodtech.proyecto4restaurant.services.AllergenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
@Service
public class AllergenServiceImpl implements AllergenService{

    @Autowired
    AllergenRepository allergenRepository;
    @Autowired
    DishRepository dishRepository;
    @Override
    public String addAllergen(CreateAllergen createAllergen) {
        Allergen allergen = new Allergen();
        allergen.setName(createAllergen.getName());
        allergenRepository.save(allergen);
        return allergen.getName();
    }

    @Override
    public String deleteAllergen(Integer id) {
        return allergenRepository.findById(id).map(allergen -> {
            allergenRepository.deleteById(id);
            return ("El alergeno se ha eliminado correctamente");
        }).orElse("No se encontró ningún allergeno con el ID proporcionado");
    }

    @Override
    public AllergenDetails getAllergen(Integer id) {
        Optional<Allergen> optionalAllergen = allergenRepository.findById(id);
        if (optionalAllergen.isPresent()) {
            Allergen allergen = optionalAllergen.get();
            AllergenDetails allergenDetails = new AllergenDetails();
            allergenDetails.setId(allergen.getId());
            allergenDetails.setName(allergen.getName());
            allergenDetails.setPresentIn(getPlatesWithAllergen(allergen));
            return allergenDetails;
        }
        return null;
    }

    private List<AllergenDetails_allOf_presentIn> getPlatesWithAllergen(Allergen allergen) {
        List<AllergenDetails_allOf_presentIn> presentInList = new ArrayList<>();
        List<Dish> allDishes = dishRepository.findAll();
        for (Dish dish : allDishes) {
            for (AmountOfIngredient amountOfIngredient : dish.getAmountsOfIngredients()) {
                if (amountOfIngredient.getIngredient().getAllergens().contains(allergen)) {
                    AllergenDetails_allOf_presentIn presentIn = new AllergenDetails_allOf_presentIn();
                    presentIn.setId(dish.getId());
                    presentIn.setName(dish.getName());
                    presentInList.add(presentIn);
                    break; // No es necesario seguir verificando los ingredientes una vez que se encuentra el alérgeno en un plato
                }
            }
        }
        return presentInList;
    }

    @Override
    public List<Allergen> listAllergens(String filter) {
        List<Allergen> filterAllergens;
        if (filter != null && !filter.isEmpty()) {
            filterAllergens = allergenRepository.findAll().stream()
                    .filter(allergen -> allergen.getName().toLowerCase().contains(filter.toLowerCase()))
                    .sorted(Comparator.comparing(Allergen::getName))
                    .collect(Collectors.toList());
        } else {
            filterAllergens = allergenRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
        }
        return filterAllergens;
    }

    @Override
    public String updateAllergen(Integer id, UpdateAllergen updateAllergen) {
        return allergenRepository.findById(id)
                .map(allergen -> {
                    if (updateAllergen.getName() != null) {
                        allergen.setName(updateAllergen.getName());
                    }
                    allergenRepository.save(allergen);
                    return "El alérgeno se ha actualizado correctamente";
                })
                .orElse("No se encontró ningún alérgeno con el ID proporcionado");
    }
}
