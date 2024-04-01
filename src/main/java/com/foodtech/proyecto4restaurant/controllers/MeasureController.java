package com.foodtech.proyecto4restaurant.controllers;

import com.foodtech.proyecto4restaurant.dtos.*;
import com.foodtech.proyecto4restaurant.services.MeasureService;
import com.foodtech.proyecto4restaurant.services.errors.ServiceError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.function.Supplier;

@RestController
@RequestMapping("/restaurant")
public class MeasureController {

    private <T> ResponseEntity handleRequest(Supplier<T> supplier){
        try{
            return ResponseEntity.ok(supplier.get());
        }catch(ServiceError e){
            return new ResponseEntity(e.getMessage(), HttpStatusCode.valueOf(e.getErrorCode().getHttpErrorCode()));
        }
    }

    @Autowired
    MeasureService measureService;
    @PostMapping("/measures")
    public ResponseEntity addMeasure(@RequestBody CreateMeasure createMeasure) {
        return handleRequest( ()-> measureService.addMeasure(createMeasure));
    }

    @DeleteMapping("/measures/{id}")
    public ResponseEntity<AllergenDetails> deleteIngredient(@PathVariable Integer id) {
        return handleRequest( ()-> measureService.deleteMeasure(id));
    }

    @GetMapping("/measures/{id}")
    public ResponseEntity<AllergenDetails> getMeasure(@PathVariable Integer id) {
        return handleRequest( ()-> measureService.getMeasure(id));
    }

    @GetMapping("/measures")
    public ResponseEntity<AllergenDetails> listMeasures(String filter) {
        return handleRequest( ()-> measureService.listMeasures(filter));
    }

    @PutMapping("/measures/{id}")
    public ResponseEntity<AllergenDetails> updateIngredient(@PathVariable Integer id,@RequestBody UpdateMeasure updateMeasure) {
        return handleRequest( ()-> measureService.updateMeasure(id,updateMeasure));
    }
}
