package com.foodtech.proyecto4restaurant.services.impl;

import com.foodtech.proyecto4restaurant.dtos.*;
import com.foodtech.proyecto4restaurant.models.*;
import com.foodtech.proyecto4restaurant.repositories.AllergenRepository;
import com.foodtech.proyecto4restaurant.repositories.IngredientRepository;
import com.foodtech.proyecto4restaurant.repositories.MeasureRepository;
import com.foodtech.proyecto4restaurant.services.IngredientService;
import com.foodtech.proyecto4restaurant.services.errors.ErrorCode;
import com.foodtech.proyecto4restaurant.services.errors.ServiceError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class IngredientServiceImpl implements IngredientService {

    @Autowired
    IngredientRepository ingredientRepository;
    @Autowired
    MeasureRepository measureRepository;
    @Autowired
    AllergenRepository allergenRepository;
    @Override
    public String addIngredient(CreateIngredient createIngredient) {
        if (createIngredient == null ||
                createIngredient.getName() == null || createIngredient.getName().isEmpty() ||
                createIngredient.getMeasureId() == null ||
                createIngredient.getPurchasePrice() == null || createIngredient.getPurchasePrice().compareTo(BigDecimal.ZERO) <= 0 ||
                createIngredient.getSellPrice() == null || createIngredient.getSellPrice().compareTo(BigDecimal.ZERO) <= 0) {

            throw new ServiceError(ErrorCode.INVALID_INPUT, "Los campos del ingrediente son inválidos o están vacíos");
        }

        Measure measure = getMeasure(createIngredient.getMeasureId());

        // Obtener los IDs de los alérgenos únicos
        Set<Integer> allergenIds = createIngredient.getAllergens().stream()
                .map(Allergen::getId)
                .collect(Collectors.toSet());

        // Obtener los objetos Allergen correspondientes a los IDs
        List<Allergen> allergens = getAllergens(allergenIds);

        // Crear el objeto Ingredient y establecer sus propiedades
        Ingredient ingredient = new Ingredient();
        ingredient.setName(createIngredient.getName());
        ingredient.setMeasureId(measure);
        ingredient.setPurchasePrice(createIngredient.getPurchasePrice());
        ingredient.setSellPrice(createIngredient.getSellPrice());
        ingredient.setAllergens(allergens);

        // Guardar el ingrediente en la base de datos
        ingredientRepository.save(ingredient);

        return ingredient.getName();
        /*if (createIngredient == null ||
                createIngredient.getName() == null || createIngredient.getName().isEmpty() ||
                createIngredient.getMeasureId() == null ||
                createIngredient.getPurchasePrice() == null || createIngredient.getPurchasePrice().compareTo(BigDecimal.ZERO) <= 0 ||
                createIngredient.getSellPrice() == null || createIngredient.getSellPrice().compareTo(BigDecimal.ZERO) <= 0 ||
                createIngredient.getAllergens() == null || createIngredient.getAllergens().isEmpty()) {

            throw new ServiceError(ErrorCode.INVALID_INPUT, "Los campos del ingrediente son inválidos o están vacíos");
        }
        Measure measure = getMeasure(createIngredient.getMeasureId());

        // Obtener los IDs de alérgenos de la lista de alérgenos de createIngredient
        List<Integer> allergenIds = createIngredient.getAllergens().stream()
                .map(Allergen::getId)
                .collect(Collectors.toList());

        // Obtener la lista de objetos Allergen correspondientes a los IDs
        List<Allergen> allergens = getAllergens(allergenIds);

        Ingredient ingredient = new Ingredient();
        ingredient.setName(createIngredient.getName());
        ingredient.setMeasureId(measure);
        ingredient.setPurchasePrice(createIngredient.getPurchasePrice());
        ingredient.setSellPrice(createIngredient.getSellPrice());
        ingredient.setAllergens(allergens);

        ingredientRepository.save(ingredient);
        return ingredient.getName();*/
    }

    private Measure getMeasure(Integer measureId) {
        return measureRepository.findById(measureId)
                .orElseThrow(() -> new ServiceError(ErrorCode.RESOURCE_NOT_FOUND, "La medida especificada no existe"));
    }

    private List<Allergen> getAllergens(Set<Integer> allergenIds) {
        List<Allergen> foundAllergens = allergenRepository.findAllById(allergenIds);

        if (foundAllergens.size() != allergenIds.size()) {
            throw new ServiceError(ErrorCode.RESOURCE_NOT_FOUND, "Al menos uno de los IDs de alérgenos especificados no existe.");
        }

        return foundAllergens;
    }

    @Override
    public String deleteIngredient(Integer id) {
        if (id == null || id <= 0) {
            throw new ServiceError(ErrorCode.INVALID_INPUT, "El ID del ingrediente es inválido");
        }
        Optional<Ingredient> optionalIngredient = ingredientRepository.findById(id);
        return optionalIngredient.map(ingredient -> {
            ingredientRepository.deleteById(id);
            return ("El ingrediente se ha eliminado correctamente");
        }).orElseThrow(()-> new ServiceError(ErrorCode.RESOURCE_NOT_FOUND,"No se encontró ningún ingrediente con el ID proporcionado"));
    }

    @Override
    public IngredientDetails getIngredient(Integer id) {
        if (id == null || id <= 0) {
            throw new ServiceError(ErrorCode.INVALID_INPUT, "El ID del ingrediente es inválido");
        }
        Optional<Ingredient> optionalIngredient = ingredientRepository.findById(id);
        if (optionalIngredient.isPresent()) {
            Ingredient ingredient = optionalIngredient.get();
            IngredientDetails ingredientDetails = new IngredientDetails();
            ingredientDetails.setName(ingredient.getName());
            ingredientDetails.setMeasureId(ingredient.getMeasureId().getId());
            ingredientDetails.setPurchasePrice(ingredient.getPurchasePrice());
            ingredientDetails.setSellPrice(ingredient.getSellPrice());

            // Mapear cada Allergen a AllergenDTO
            List<AllergenDTO> allergenDTOs = ingredient.getAllergens().stream()
                    .map(this::mapAllergenToDTO)
                    .collect(Collectors.toList());

            ingredientDetails.setAllergens(allergenDTOs);
            ingredientDetails.setUsedIn(getPlatesUsingIngredient(ingredient));

            return ingredientDetails;
        } else {
            throw new ServiceError(ErrorCode.RESOURCE_NOT_FOUND,"No se encontró ningún ingrediente con el ID proporcionado");
        }
    }

    // Método para mapear un Allergen a un AllergenDTO
    public AllergenDTO mapAllergenToDTO(Allergen allergen) {
        AllergenDTO allergenDTO = new AllergenDTO();
        allergenDTO.setId(allergen.getId());
        allergenDTO.setName(allergen.getName());
        return allergenDTO;
    }

    private List<AllergenDetails_allOf_presentIn> getPlatesUsingIngredient(Ingredient ingredient) {
        List<AllergenDetails_allOf_presentIn> usedInList = new ArrayList<>();
        for (AmountOfIngredient amountOfIngredient : ingredient.getAmountsInDishes()) {
            AllergenDetails_allOf_presentIn presentIn = new AllergenDetails_allOf_presentIn();
            presentIn.setId(amountOfIngredient.getDish().getId());
            presentIn.setName(amountOfIngredient.getDish().getName());
            usedInList.add(presentIn);
        }
        return usedInList;
    }

    @Override
    public List<IngredientDetails> listIngredients(String filter) {
        List<Ingredient> allIngredients = ingredientRepository.findAll();

        // Función para convertir un Ingredient en un IngredientDetails
        Function<Ingredient, IngredientDetails> ingredientToDetails = ingredient -> {
            IngredientDetails ingredientDetails = new IngredientDetails();
            ingredientDetails.setName(ingredient.getName());
            ingredientDetails.setMeasureId(ingredient.getMeasureId().getId());
            ingredientDetails.setPurchasePrice(ingredient.getPurchasePrice());
            ingredientDetails.setSellPrice(ingredient.getSellPrice());

            // Convertir cada Allergen a AllergenDTO
            List<AllergenDTO> allergenDTOs = ingredient.getAllergens().stream()
                    .map(this::mapAllergenToDTO) // Usa el método de mapeo de Allergen a AllergenDTO
                    .collect(Collectors.toList());
            ingredientDetails.setAllergens(allergenDTOs);

            return ingredientDetails;
        };

        if (filter != null && !filter.isEmpty()) {
            String filterLower = filter.toLowerCase();
            return allIngredients.stream()
                    .filter(ingredient -> {
                        // Filtrar por nombre del ingrediente
                        boolean nameMatch = ingredient.getName().toLowerCase().contains(filterLower);

                        // Filtrar por nombre del alérgeno
                        boolean allergenMatch = ingredient.getAllergens().stream()
                                .anyMatch(allergen -> allergen.getName().toLowerCase().contains(filterLower));

                        // Retornar verdadero si alguna de las condiciones coincide
                        return nameMatch || allergenMatch;
                    })
                    .map(ingredientToDetails) // Convertir Ingredient a IngredientDetails
                    .collect(Collectors.toList());
        } else {
            // Devolver la lista completa de ingredientes ordenada y convertida a IngredientDetails
            return allIngredients.stream()
                    .map(ingredientToDetails) // Convertir Ingredient a IngredientDetails
                    .sorted(Comparator.comparing(IngredientDetails::getName))
                    .collect(Collectors.toList());
        }
    }

    @Override
    public String updateIngredient(Integer id, UpdateIngredient updateIngredient) {
        if (id == null || id <= 0) {
            throw new ServiceError(ErrorCode.INVALID_INPUT, "El ID del ingrediente es inválido");
        }
        Measure measure = measureRepository.findById(updateIngredient.getMeasureId())
                .orElseThrow(() -> new ServiceError(ErrorCode.RESOURCE_NOT_FOUND,"La medida especificada no existe."));
        return ingredientRepository.findById(id)
                .map(ingredient -> {
                    if (updateIngredient.getName() != null) {
                        ingredient.setName(updateIngredient.getName());
                    }
                    if (updateIngredient.getMeasureId() != null) {
                        ingredient.setMeasureId(measure);
                    }
                    if (updateIngredient.getPurchasePrice() != null) {
                        ingredient.setPurchasePrice(updateIngredient.getPurchasePrice());
                    }
                    if (updateIngredient.getSellPrice() != null) {
                        ingredient.setSellPrice(updateIngredient.getSellPrice());
                    }
                    if (updateIngredient.getAllergens() != null) {
                        ingredient.setAllergens(updateIngredient.getAllergens());
                    }
                    ingredientRepository.save(ingredient);
                    return "El ingrediente se ha actualizado correctamente";
                })
                .orElseThrow(()-> new ServiceError(ErrorCode.RESOURCE_NOT_FOUND,"No se encontró ningún ingrediente con el ID proporcionado"));

    }
}
