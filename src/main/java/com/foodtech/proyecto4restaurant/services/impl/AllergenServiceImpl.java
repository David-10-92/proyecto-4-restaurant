package com.foodtech.proyecto4restaurant.services.impl;

import com.foodtech.proyecto4restaurant.dtos.AllergenDetails_allOf_presentIn;
import com.foodtech.proyecto4restaurant.models.Allergen;
import com.foodtech.proyecto4restaurant.dtos.AllergenDetails;
import com.foodtech.proyecto4restaurant.dtos.CreateAllergen;
import com.foodtech.proyecto4restaurant.dtos.UpdateAllergen;
import com.foodtech.proyecto4restaurant.models.Dish;
import com.foodtech.proyecto4restaurant.repositories.AllergenRepository;
import com.foodtech.proyecto4restaurant.repositories.DishRepository;
import com.foodtech.proyecto4restaurant.services.AllergenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Sort;

import java.util.*;
import java.util.stream.Collectors;

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
    public AllergenDetails getAllergen(Integer id) {
        Optional<Allergen> optionalAllergen = allergenRepository.findById(id);
        if (optionalAllergen.isPresent()) {
            Allergen allergen = optionalAllergen.get();
            AllergenDetails allergenDetails = new AllergenDetails();
            allergenDetails.setId(allergen.getId());
            allergenDetails.setName(allergen.getName());
            //No se como sacar la lista por que es una lista con un objeto de otro tipo y no se si con los
            //mapeos que tengo esta comunicado con tantas clases estoy un poco perdido jaja
        }
            return null;
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
