package com.foodtech.proyecto4restaurant.controllers;

import com.foodtech.proyecto4restaurant.dtos.*;
import com.foodtech.proyecto4restaurant.services.DishService;
import com.foodtech.proyecto4restaurant.services.errors.ServiceError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.function.Supplier;

@RestController
@RequestMapping("/restaurant")
public class DishController {
    private <T> ResponseEntity handleRequest(Supplier<T> supplier){
        try{
            return ResponseEntity.ok(supplier.get());
        }catch(ServiceError e){
            return new ResponseEntity(e.getMessage(), HttpStatusCode.valueOf(e.getErrorCode().getHttpErrorCode()));
        }
    }

    @Autowired
    DishService dishService;
    @PostMapping("/dishes")
    public ResponseEntity addDish(@RequestBody CreateDish createDish) {
        return handleRequest( ()-> dishService.addDish(createDish));
    }

    @DeleteMapping("/dishes/{id}")
    public ResponseEntity<AllergenDetails> deleteDish(@PathVariable Integer id) {
        return handleRequest( ()-> dishService.deleteDish(id));
    }

    @GetMapping("/dishes/{id}")
    public ResponseEntity<AllergenDetails> getDish(@PathVariable Integer id) {
        return handleRequest( ()-> dishService.getDish(id));
    }

    @GetMapping("/dishes")
    public ResponseEntity<AllergenDetails> searchDish(Integer recordsPerpage, Integer page, String filter) {
        return handleRequest( ()-> dishService.searchDish(recordsPerpage,page,filter));
    }

    @PutMapping("/dishes/{id}")
    public ResponseEntity<AllergenDetails> updateDish(@PathVariable Integer id,@RequestBody UpdateDish updateDish) {
        return handleRequest( ()-> dishService.updateDish(id,updateDish));
    }
}
