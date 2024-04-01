package com.foodtech.proyecto4restaurant.services;

import com.foodtech.proyecto4restaurant.dtos.AllergenDetails;
import com.foodtech.proyecto4restaurant.dtos.CreateAllergen;
import com.foodtech.proyecto4restaurant.dtos.UpdateAllergen;
import com.foodtech.proyecto4restaurant.models.Allergen;

import java.util.List;

public interface AllergenService{
    String addAllergen(CreateAllergen createAllergen);
    String deleteAllergen(Integer id);
    AllergenDetails getAllergen(Integer id);
    List<Allergen> listAllergens(String filter);
    String updateAllergen(Integer id, UpdateAllergen updateAllergen);
}
