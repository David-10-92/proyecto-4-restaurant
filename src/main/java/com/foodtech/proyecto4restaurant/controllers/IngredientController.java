package com.foodtech.proyecto4restaurant.controllers;

import com.foodtech.proyecto4restaurant.dtos.*;
import com.foodtech.proyecto4restaurant.services.IngredientService;
import com.foodtech.proyecto4restaurant.services.errors.ServiceError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.function.Supplier;

@RestController
@RequestMapping("/restaurant/ingredients")
public class IngredientController {

    private <T> ResponseEntity handleRequest(Supplier<T> supplier){
        try{
            return ResponseEntity.ok(supplier.get());
        }catch(ServiceError e){
            return new ResponseEntity(e.getMessage(), HttpStatusCode.valueOf(e.getErrorCode().getHttpErrorCode()));
        }
    }

    @Autowired
    IngredientService ingredientService;

    @PostMapping
    public ResponseEntity<String> addIngredient(@RequestBody CreateIngredient createIngredient) {
        return handleRequest( ()-> ingredientService.addIngredient(createIngredient));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteIngredient(@PathVariable Integer id) {
        return handleRequest( ()-> ingredientService.deleteIngredient(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<IngredientDetails> getIngredient(@PathVariable Integer id) {
        return handleRequest( ()-> ingredientService.getIngredient(id));
    }

    @GetMapping
    public ResponseEntity<List<IngredientDetails>> listIngredients(String filter) {
        return handleRequest( ()-> ingredientService.listIngredients(filter));
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateIngredient(@PathVariable Integer id,@RequestBody UpdateIngredient updateIngredient) {
        return handleRequest( ()-> ingredientService.updateIngredient(id,updateIngredient));
    }
}
