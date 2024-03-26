package com.foodtech.proyecto4restaurant.services.impl;

import com.foodtech.proyecto4restaurant.dtos.*;
import com.foodtech.proyecto4restaurant.models.Dish;
import com.foodtech.proyecto4restaurant.repositories.DishRepository;
import com.foodtech.proyecto4restaurant.services.DishService;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DishServiceImpl implements DishService {

    @Autowired
    DishRepository dishRepository;

    @Override
    public String addDish(CreateDish createDish) {
        Dish dish = new Dish();
        dish.setName(createDish.getName());
        dish.setDescription(createDish.getDescription());
        dish.setDinners(createDish.getDinners());
        dish.setIngredients(createDish.getIngredients());
        dishRepository.save(dish);
        return dish.getName();
    }

    @Override
    public String deleteDish(Integer id) {
        Optional<Dish> optionalDish = dishRepository.findById(id);
        return optionalDish.map(dish -> {
            dishRepository.deleteById(id);
            return "El plato se ha eliminado correctamente";
        }).orElse("No se encontró ningún plato con el ID proporcionado");
    }

    @Override
    public DishDetails getDish(Integer id) {
        Optional<Dish> optionalDish = dishRepository.findById(id);
        if (optionalDish.isPresent()) {
            Dish dish = optionalDish.get();
            DishDetails dishDetails = new DishDetails();
            dishDetails.setId(dish.getId());
            dishDetails.setName(dish.getName());
            dishDetails.setDinners(dish.getDinners());
            //no se sacar los alergenos que tiene por que en las listas se mezclan clases jeeje
            //estoy un poco perdido en la estructura del proyecto
        }
        return null;
    }

    @Override
    public DishSearchResult searchDish(Integer recordsPerpage, Integer page, String filter) {
        return null;
    }

    @Override
    public String updateDish(Integer id, UpdateDish updateDish) {
        return dishRepository.findById(id)
                .map(dish -> {
                    if (updateDish.getName() != null) {
                        dish.setName(updateDish.getName());
                    }
                    if (updateDish.getDescription() != null) {
                        dish.setDescription(updateDish.getDescription());
                    }
                    if (updateDish.getDinners() != null) {
                        dish.setDinners(updateDish.getDinners());
                    }
                    if (updateDish.getIngredients() != null) {
                        dish.setIngredients(updateDish.getIngredients());
                    }
                    dishRepository.save(dish);
                    return "El plato se ha actualizado correctamente";
                })
                .orElse("No se encontró ningún plato con el ID proporcionado");
    }
}
