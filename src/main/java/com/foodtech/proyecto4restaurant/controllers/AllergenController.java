package com.foodtech.proyecto4restaurant.controllers;

import com.foodtech.proyecto4restaurant.dtos.AllergenDetails;
import com.foodtech.proyecto4restaurant.dtos.CreateAllergen;
import com.foodtech.proyecto4restaurant.dtos.UpdateAllergen;
import com.foodtech.proyecto4restaurant.models.Allergen;
import com.foodtech.proyecto4restaurant.services.AllergenService;
import com.foodtech.proyecto4restaurant.services.errors.ServiceError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.function.Supplier;

@RestController
@RequestMapping("/restaurant/allergens")
public class AllergenController {
    private <T> ResponseEntity handleRequest(Supplier<T> supplier){
        try{
            return ResponseEntity.ok(supplier.get());
        }catch(ServiceError e){
            return new ResponseEntity(e.getMessage(), HttpStatusCode.valueOf(e.getErrorCode().getHttpErrorCode()));
        }
    }

    @Autowired
    private AllergenService allergenService;

    // Agrega un nuevo alérgeno
    @PostMapping
    public ResponseEntity<String> addAllergen(@RequestBody CreateAllergen createAllergen) {
        return handleRequest( ()-> allergenService.addAllergen(createAllergen));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAllergen(@PathVariable Integer id) {
        return handleRequest( ()-> allergenService.deleteAllergen(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AllergenDetails> getAllergen(@PathVariable Integer id) {
        return handleRequest( ()-> allergenService.getAllergen(id));
    }

    @GetMapping
    public ResponseEntity<List<AllergenDetails>> listAllergens(String filter) {
        return handleRequest( ()-> allergenService.listAllergens(filter));
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateAllergen(@PathVariable Integer id,@RequestBody UpdateAllergen updateAllergen) {
        return handleRequest( ()-> allergenService.updateAllergen(id,updateAllergen));
    }

}
