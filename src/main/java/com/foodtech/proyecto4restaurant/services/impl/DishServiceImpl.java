package com.foodtech.proyecto4restaurant.services.impl;

import com.foodtech.proyecto4restaurant.dtos.*;
import com.foodtech.proyecto4restaurant.models.Allergen;
import com.foodtech.proyecto4restaurant.models.AmountOfIngredient;
import com.foodtech.proyecto4restaurant.models.Dish;
import com.foodtech.proyecto4restaurant.models.Ingredient;
import com.foodtech.proyecto4restaurant.repositories.DishRepository;
import com.foodtech.proyecto4restaurant.services.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class DishServiceImpl implements DishService {

    @Autowired
    DishRepository dishRepository;

    @Override
    public String addDish(CreateDish createDish) {
        Dish dish = new Dish();
        dish.setName(createDish.getName());
        dish.setDescription(createDish.getDescription());
        dish.setDinners(createDish.getDinners());
        dish.setAmountsOfIngredients(createDish.getIngredients());
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
            dishDetails.setBuyPrice(calculateBuyPrice(dish));
            dishDetails.setSellPrice(calculateBuyPrice(dish).add(new BigDecimal("15")));
            dishDetails.setIngredients(mapIngredients(dish.getAmountsOfIngredients()));
            dishDetails.setAllergens(mapAllergens(dish.getAmountsOfIngredients()));
            return dishDetails;
        }
        return null;
    }

    private BigDecimal calculateBuyPrice(Dish dish) {
        BigDecimal buyPrice = BigDecimal.ZERO;
        for (AmountOfIngredient amountOfIngredient : dish.getAmountsOfIngredients()) {
            BigDecimal ingredientPrice = amountOfIngredient.getIngredient().getPurchasePrice();
            BigDecimal ingredientQuantity = amountOfIngredient.getValue();
            BigDecimal totalPriceForIngredient = ingredientPrice.multiply(ingredientQuantity);
            buyPrice = buyPrice.add(totalPriceForIngredient);
        }
        return buyPrice;
    }

    private List<DishDetails_ingredients_inner> mapIngredients(List<AmountOfIngredient> amountsOfIngredients) {
        List<DishDetails_ingredients_inner> ingredientsList = new ArrayList<>();
        for (AmountOfIngredient amountOfIngredient : amountsOfIngredients) {
            DishDetails_ingredients_inner ingredientInner = new DishDetails_ingredients_inner();
            ingredientInner.setId(amountOfIngredient.getIngredient().getId());
            ingredientInner.setName(amountOfIngredient.getIngredient().getName());
            ingredientsList.add(ingredientInner);
        }
        return ingredientsList;
    }

    private List<DishDetails_allergens_inner> mapAllergens(List<AmountOfIngredient> amountsOfIngredients) {
        List<DishDetails_allergens_inner> allergensList = new ArrayList<>();
        for (AmountOfIngredient amountOfIngredient : amountsOfIngredients) {
            for (Allergen allergen : amountOfIngredient.getIngredient().getAllergens()) {
                DishDetails_allergens_inner allergenInner = new DishDetails_allergens_inner();
                allergenInner.setId(allergen.getId());
                allergenInner.setName(allergen.getName());
                if (!allergensList.contains(allergenInner)) {
                    allergensList.add(allergenInner);
                }
            }
        }
        return allergensList;
    }

    @Override
    public DishSearchResult searchDish(Integer recordsPerpage, Integer page, String filter) {
        // Definir el número de página a partir de la página solicitada
        int pageNumber = (page != null && page >= 1) ? page - 1 : 0;

        // Definir el número de registros por página
        int pageSize = (recordsPerpage != null && recordsPerpage >= 1) ? recordsPerpage : 10;

        // Realizar la búsqueda de platos con paginación y filtrado
        Page<Dish> dishPage;
        if (filter != null && !filter.isEmpty()) {
            dishPage = (Page<Dish>) dishRepository.findByNameContainingIgnoreCase(filter, PageRequest.of(pageNumber, pageSize));
        } else {
            dishPage = dishRepository.findAll(PageRequest.of(pageNumber, pageSize));
        }

        // Construir el objeto DishSearchResult
        DishSearchResult searchResult = new DishSearchResult();
        searchResult.setTotalRecordsFound((int) dishPage.getTotalElements());
        searchResult.setTotalPages(dishPage.getTotalPages());
        searchResult.setRecordsPerPage(pageSize);

        return searchResult;
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
                        dish.setAmountsOfIngredients(updateDish.getIngredients());
                    }
                    dishRepository.save(dish);
                    return "El plato se ha actualizado correctamente";
                })
                .orElse("No se encontró ningún plato con el ID proporcionado");
    }
}
