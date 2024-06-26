package com.foodtech.proyecto4restaurant.controllers;

import com.foodtech.proyecto4restaurant.dtos.*;
import com.foodtech.proyecto4restaurant.services.DishService;
import com.foodtech.proyecto4restaurant.services.errors.ServiceError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.function.Supplier;

@RestController
@RequestMapping("/restaurant/dishes")
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
    @PostMapping
    public ResponseEntity<String> addDish(@RequestBody CreateDish createDish) {
        return handleRequest( ()-> dishService.addDish(createDish));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDish(@PathVariable Integer id) {
        return handleRequest( ()-> dishService.deleteDish(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DishDetails> getDish(@PathVariable Integer id) {
        return handleRequest( ()-> dishService.getDish(id));
    }

    @GetMapping
    public ResponseEntity<DishSearchResult> searchDish(Integer recordsPerpage, Integer page, String filter) {
        return handleRequest( ()-> dishService.searchDish(recordsPerpage,page,filter));
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateDish(@PathVariable Integer id,@RequestBody UpdateDish updateDish) {
        return handleRequest( ()-> dishService.updateDish(id,updateDish));
    }
}
