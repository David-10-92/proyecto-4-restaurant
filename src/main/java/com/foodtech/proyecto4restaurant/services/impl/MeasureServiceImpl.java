package com.foodtech.proyecto4restaurant.services.impl;

import com.foodtech.proyecto4restaurant.dtos.CreateMeasure;
import com.foodtech.proyecto4restaurant.dtos.MeasureDetails;
import com.foodtech.proyecto4restaurant.dtos.MeasureDetails_allOf_ingredients;
import com.foodtech.proyecto4restaurant.dtos.UpdateMeasure;
import com.foodtech.proyecto4restaurant.models.Ingredient;
import com.foodtech.proyecto4restaurant.models.Measure;
import com.foodtech.proyecto4restaurant.repositories.AllergenRepository;
import com.foodtech.proyecto4restaurant.repositories.MeasureRepository;
import com.foodtech.proyecto4restaurant.services.MeasureService;
import com.foodtech.proyecto4restaurant.services.errors.ErrorCode;
import com.foodtech.proyecto4restaurant.services.errors.ServiceError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class MeasureServiceImpl implements MeasureService {

    @Autowired
    MeasureRepository measureRepository;

    @Override
    public String addMeasure(CreateMeasure createMeasure) {
        if (createMeasure.getName() == null || createMeasure.getName().isEmpty()) {
            throw new ServiceError(ErrorCode.INVALID_INPUT, "El nombre de la medida no puede estar vacío");
        }
        Measure measure = new Measure();
        measure.setName(createMeasure.getName());
        measureRepository.save(measure);
        return measure.getName();
    }

    @Override
    public String deleteMeasure(Integer id) {
        if (id == null || id <= 0) {
            throw new ServiceError(ErrorCode.INVALID_INPUT, "El ID de la medida es inválido");
        }
        return measureRepository.findById(id)
                .map(measure -> {
                    measureRepository.deleteById(id);
                    return ("La medida ha sido eliminada correctamente");
                })
                .orElseThrow(() -> new ServiceError(ErrorCode.RESOURCE_NOT_FOUND, "No se encontró ninguna medida con el ID proporcionado"));
    }

    @Override
    public MeasureDetails getMeasure(Integer id) {
        if (id == null || id <= 0) {
            throw new ServiceError(ErrorCode.INVALID_INPUT, "El ID de la medida es inválida");
        }
        Optional<Measure> optionalMeasure = measureRepository.findById(id);

        if (optionalMeasure.isPresent()) {
            Measure measure = optionalMeasure.get();
            MeasureDetails measureDetails = new MeasureDetails();

            // Establecer los detalles de la medida
            measureDetails.setId(measure.getId());
            measureDetails.setName(measure.getName());

            // Obtener la lista de ingredientes que usan esta medida
            List<Ingredient> ingredients = measure.getIngredients();
            List<MeasureDetails_allOf_ingredients> ingredientsDetails = ingredients.stream()
                    .map(ingredient -> {
                        MeasureDetails_allOf_ingredients ingredientDetails = new MeasureDetails_allOf_ingredients();
                        ingredientDetails.setId(ingredient.getId());
                        ingredientDetails.setName(ingredient.getName());
                        return ingredientDetails;
                    })
                    .collect(Collectors.toList());

            // Establecer la lista de ingredientes en los detalles de la medida
            measureDetails.setIngredients(ingredientsDetails);

            return measureDetails;
        } else {
            throw  new ServiceError(ErrorCode.RESOURCE_NOT_FOUND, "No se encontró ninguna medida con el ID proporcionado");
        }
    }

    @Override
    public List<MeasureDetails> listMeasures(String filter) {
        List<Measure> allMeasures = measureRepository.findAll();
        List<MeasureDetails> measureDetailsList = new ArrayList<>();

        // Filtrar y ordenar las medidas si el filtro no es nulo ni vacío
        if (filter != null && !filter.isEmpty()) {
            allMeasures.stream()
                    .filter(measure -> measure.getName().toLowerCase().contains(filter.toLowerCase()))
                    .sorted((m1, m2) -> m1.getName().compareToIgnoreCase(m2.getName()))
                    .forEach(measure -> {
                        MeasureDetails measureDetails = new MeasureDetails();
                        measureDetails.setId(measure.getId());
                        measureDetails.setName(measure.getName());

                        List<MeasureDetails_allOf_ingredients> ingredientList = new ArrayList<>();
                        measure.getIngredients().forEach(ingredient -> {
                            MeasureDetails_allOf_ingredients ingredientDetails = new MeasureDetails_allOf_ingredients();
                            ingredientDetails.setId(ingredient.getId());
                            ingredientDetails.setName(ingredient.getName());
                            // Configurar otros atributos si es necesario
                            ingredientList.add(ingredientDetails);
                        });
                        measureDetails.setIngredients(ingredientList);

                        measureDetailsList.add(measureDetails);
                    });
        } else {
            // Si no hay filtro, simplemente agregar todas las medidas a la lista de detalles
            allMeasures.forEach(measure -> {
                MeasureDetails measureDetails = new MeasureDetails();
                measureDetails.setId(measure.getId());
                measureDetails.setName(measure.getName());

                List<MeasureDetails_allOf_ingredients> ingredientList = new ArrayList<>();
                measure.getIngredients().forEach(ingredient -> {
                    MeasureDetails_allOf_ingredients ingredientDetails = new MeasureDetails_allOf_ingredients();
                    ingredientDetails.setId(ingredient.getId());
                    ingredientDetails.setName(ingredient.getName());
                    // Configurar otros atributos si es necesario
                    ingredientList.add(ingredientDetails);
                });
                measureDetails.setIngredients(ingredientList);

                measureDetailsList.add(measureDetails);
            });
        }

        return measureDetailsList;
    }

    @Override
    public String updateMeasure(Integer id, UpdateMeasure updateMeasure) {
        if (id == null || id <= 0) {
            throw new ServiceError(ErrorCode.INVALID_INPUT, "El ID de la medida es inválido");
        }
        return measureRepository.findById(id)
                .map(measure -> {
                    if (updateMeasure.getName() != null) {
                        measure.setName(updateMeasure.getName());
                    }
                    measureRepository.save(measure);
                    return "La medida se ha actualizado correctamente";
                })
                .orElseThrow(() -> new ServiceError(ErrorCode.RESOURCE_NOT_FOUND, "No se encontró ninguna medida con el ID proporcionado"));
    }
}
