package com.foodtech.proyecto4restaurant.services.impl;

import com.foodtech.proyecto4restaurant.models.allergen.Allergen;
import com.foodtech.proyecto4restaurant.models.allergen.dtos.AllergenDetails;
import com.foodtech.proyecto4restaurant.models.allergen.dtos.CreateAllergen;
import com.foodtech.proyecto4restaurant.models.allergen.dtos.UpdateAllergen;
import com.foodtech.proyecto4restaurant.repositories.AllergenRepository;
import com.foodtech.proyecto4restaurant.services.AllergenService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class AllergenServiceImpl implements AllergenService{

    @Autowired
    AllergenRepository allergenRepository;
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
        if(optionalAllergen.isPresent()){
            Allergen allergen = optionalAllergen.get();
            AllergenDetails allergenDetails = new AllergenDetails();
            allergenDetails.setId(allergen.getId());
            allergenDetails.setName(allergen.getName());
            //No se sacar la lista de List<AllergenDetails_allOf_presentIn> presentIn;

        }else{
            //devuelve un serviceError con el mensaje de que no se ha econtrado
        }
        return null;
    }

    @Override
    public List<Allergen> listAllergens(String filter) {
        List<Allergen> allAllergenList = allergenRepository.findAll();
        List<Allergen> filterAllergens = new ArrayList<>();
        if(filter != null && !filter.isEmpty()){
            for(Allergen allergen : allAllergenList){
                if(allergen.getName().toLowerCase().contains(filter.toLowerCase())){
                    filterAllergens.add(allergen);
                }
            }
        }
        filterAllergens.sort(Comparator.comparing(Allergen::getName));
        return filterAllergens;
    }

    @Override
    public String updateAllergen(Integer id, UpdateAllergen updateAllergen) {
        Optional<Allergen> optionalAllergen = allergenRepository.findById(id);
        if(optionalAllergen.isPresent()){
            Allergen allergen = optionalAllergen.get();
            if(updateAllergen.getName() != null){
                allergen.setName(updateAllergen.getName());
            }
            allergenRepository.save(allergen);
            return "El alérgeno se ha actualizado correctamente";
        }else{
            return "No se encontró ningún alérgeno con el ID proporcionado";
        }
    }
}
