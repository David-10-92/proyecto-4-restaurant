package com.foodtech.proyecto4restaurant.services.impl;

import com.foodtech.proyecto4restaurant.dtos.*;
import com.foodtech.proyecto4restaurant.models.Allergen;
import com.foodtech.proyecto4restaurant.models.AmountOfIngredient;
import com.foodtech.proyecto4restaurant.models.Dish;
import com.foodtech.proyecto4restaurant.models.Ingredient;
import com.foodtech.proyecto4restaurant.repositories.DishRepository;
import com.foodtech.proyecto4restaurant.repositories.IngredientRepository;
import com.foodtech.proyecto4restaurant.services.DishService;
import com.foodtech.proyecto4restaurant.services.errors.ErrorCode;
import com.foodtech.proyecto4restaurant.services.errors.ServiceError;
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
    @Autowired
    IngredientRepository ingredientRepository;

    @Override
    public String addDish(CreateDish createDish) {
        if (createDish == null || createDish.getName() == null || createDish.getName().isEmpty() ||
                createDish.getDescription() == null || createDish.getDescription().isEmpty() ||
                createDish.getDinners() == null || createDish.getIngredients() == null || createDish.getIngredients().isEmpty()) {
            throw new ServiceError(ErrorCode.INVALID_INPUT, "Los campos del plato son inválidos o están vacíos");
        }
        /*Dish dish = createDishFromDto(createDish);
        List<AmountOfIngredient> amountsOfIngredients = createAmountsOfIngredients(createDish.getIngredients(), dish);
        dish.setAmountsOfIngredients(amountsOfIngredients);
        dishRepository.save(dish);
        return dish.getName();*/
        // Crear un objeto Dish a partir de los datos proporcionados
        Dish dish = createDishFromDto(createDish);

        // Crear una lista de AmountOfIngredient a partir de los ingredientes proporcionados
        List<AmountOfIngredient> amountsOfIngredients = createAmountsOfIngredients(createDish.getIngredients(), dish);

        // Establecer la lista de AmountOfIngredient en el objeto Dish
        dish.setAmountsOfIngredients(amountsOfIngredients);

        // Guardar el objeto Dish en la base de datos
        dishRepository.save(dish);

        // Retornar el nombre del plato creado
        return dish.getName();
    }

    // Método para crear un objeto Dish a partir de un DTO CreateDish
    private Dish createDishFromDto(CreateDish createDish) {
        Dish dish = new Dish();
        dish.setName(createDish.getName());
        dish.setDescription(createDish.getDescription());
        dish.setDinners(createDish.getDinners());
        return dish;
    }

    // Método para crear una lista de objetos AmountOfIngredient a partir de los DTOs de ingredientes
    private List<AmountOfIngredient> createAmountsOfIngredients(List<AmountOfIngredientDto> ingredientDtos, Dish dish) {
        List<AmountOfIngredient> amountsOfIngredients = new ArrayList<>();
        for (AmountOfIngredientDto ingredientDto : ingredientDtos) {
            AmountOfIngredient amountOfIngredient = createAmountOfIngredientFromDto(ingredientDto, dish);
            amountsOfIngredients.add(amountOfIngredient);
        }
        return amountsOfIngredients;
    }

    // Método para crear un objeto AmountOfIngredient a partir de un DTO de ingrediente
    private AmountOfIngredient createAmountOfIngredientFromDto(AmountOfIngredientDto ingredientDto, Dish dish) {
        Integer ingredientId = ingredientDto.getIngredientId();
        BigDecimal value = ingredientDto.getValue();

        // Obtener el ingrediente de la base de datos según su ID
        Ingredient ingredient = getIngredientById(ingredientId);

        // Crear el objeto AmountOfIngredient
        AmountOfIngredient amountOfIngredient = new AmountOfIngredient();
        amountOfIngredient.setIngredient(ingredient);
        amountOfIngredient.setValue(value);
        amountOfIngredient.setDish(dish);
        return amountOfIngredient;
    }

    // Método para obtener un ingrediente de la base de datos según su ID
    private Ingredient getIngredientById(Integer ingredientId) {
        return ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new ServiceError(ErrorCode.INVALID_INPUT,"El ingrediente con ID " + ingredientId + " no existe."));
    }

    @Override
    public String deleteDish(Integer id) {
        if (id == null || id <= 0) {
            throw new ServiceError(ErrorCode.INVALID_INPUT, "El ID del plato es inválido");
        }
        Optional<Dish> optionalDish = dishRepository.findById(id);
        return optionalDish.map(dish -> {
            dishRepository.deleteById(id);
            return "El plato se ha eliminado correctamente";
        }).orElseThrow(() -> new ServiceError(ErrorCode.RESOURCE_NOT_FOUND, "No se encontró ningún plato con el ID proporcionado"));
    }

    @Override
    public DishDetails getDish(Integer id) {
        if (id == null || id <= 0) {
            throw new ServiceError(ErrorCode.INVALID_INPUT, "El ID del plato es inválido");
        }
        Optional<Dish> optionalDish = dishRepository.findById(id);
        if (optionalDish.isPresent()) {
            Dish dish = optionalDish.get();
            DishDetails dishDetails = new DishDetails();
            dishDetails.setId(dish.getId());
            dishDetails.setName(dish.getName());
            dishDetails.setDinners(dish.getDinners());

            BigDecimal buyPrice = calculateBuyPrice(dish); // Calcula el precio de compra
            BigDecimal sellPrice = calculateSellPrice(dish); // Calcula el precio de venta

            dishDetails.setBuyPrice(buyPrice);
            dishDetails.setSellPrice(sellPrice);
            dishDetails.setIngredients(mapIngredients(dish.getAmountsOfIngredients()));
            dishDetails.setAllergens(mapAllergens(dish.getAmountsOfIngredients()));
            return dishDetails;
        }else{
            throw new ServiceError(ErrorCode.RESOURCE_NOT_FOUND,"No se encontró ningún plato con el ID proporcionado");
        }

    }

    // Método para calcular el precio total de compra del plato
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

    // Método para calcular el precio total de venta del plato
    private BigDecimal calculateSellPrice(Dish dish) {
        BigDecimal sellPrice = BigDecimal.ZERO;
        for (AmountOfIngredient amountOfIngredient : dish.getAmountsOfIngredients()) {
            BigDecimal ingredientPrice = amountOfIngredient.getIngredient().getSellPrice();
            BigDecimal ingredientQuantity = amountOfIngredient.getValue();
            BigDecimal totalPriceForIngredient = ingredientPrice.multiply(ingredientQuantity);
            sellPrice = sellPrice.add(totalPriceForIngredient);
        }
        return sellPrice;
    }

    // Método para mapear los ingredientes del plato a la clase interna DishDetails_ingredients_inner
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

    // Método para mapear los alérgenos del plato a la clase interna DishDetails_allergens_inner
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
        int pageNumber = (page != null && page >= 1) ? page - 1 : 0;
        int pageSize = (recordsPerpage != null && recordsPerpage >= 1) ? recordsPerpage : 10;

        Page<Dish> dishPage;
        if (filter != null && !filter.isEmpty()) {
            dishPage = (Page<Dish>) dishRepository.findByNameContainingIgnoreCase(filter, PageRequest.of(pageNumber, pageSize));
        } else {
            dishPage = dishRepository.findAll(PageRequest.of(pageNumber, pageSize));
        }

        // Transformar los registros de Dish a DishSearchResultItem y asignar el precio de venta
        List<DishSearchResultItem> searchResultItems = dishPage.getContent().stream()
                .map(this::mapDishToSearchResultItem)
                .collect(Collectors.toList());

        DishSearchResult searchResult = new DishSearchResult();
        searchResult.setTotalRecordsFound((int) dishPage.getTotalElements());
        searchResult.setTotalPages(dishPage.getTotalPages());
        searchResult.setRecordsPerPage(pageSize);
        searchResult.setRecords(searchResultItems); // Asignar los registros encontrados al resultado de búsqueda

        return searchResult;
    }

    private DishSearchResultItem mapDishToSearchResultItem(Dish dish) {
        DishSearchResultItem item = new DishSearchResultItem();
        item.setId(dish.getId());
        item.setName(dish.getName());

        // Aquí asigna el precio del plato a item, dependiendo de cómo obtengas ese valor en tu sistema
        BigDecimal price = calculateSellPrice(dish); // Suponiendo que tienes un método para calcular el precio de venta
        item.setPrice(price);

        return item;
    }

    @Override
    public String updateDish(Integer id, UpdateDish updateDish) {
        if (id == null || id <= 0) {
            throw new ServiceError(ErrorCode.INVALID_INPUT, "El ID del plato es inválido");
        }
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
                .orElseThrow(() -> new ServiceError(ErrorCode.RESOURCE_NOT_FOUND, "No se encontró ningún plato con el ID proporcionado"));
    }
}
