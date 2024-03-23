package com.foodtech.proyecto4restaurant.services;

import com.foodtech.proyecto4restaurant.models.allergen.dtos.AllergenDetails;
import com.foodtech.proyecto4restaurant.models.allergen.dtos.CreateAllergen;
import com.foodtech.proyecto4restaurant.models.allergen.dtos.UpdateAllergen;

import java.util.List;

public interface AllergenService{
    String addAllergen(CreateAllergen createAllergen);
    AllergenDetails getAllergen(Integer id);
    List listAllergens(String filter);
    String updateAllergen(Integer id, UpdateAllergen updateAllergen);
}
