package com.foodtech.proyecto4restaurant.models.dish.dtos;

import com.foodtech.proyecto4restaurant.models.AmountOfIngredient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
public class CreateDish {
//    Nombre del plato
    private String name;
//    Descripción del plato para el menú
    private String description;
//    Indica el número de comensales indicados para este plato (orientativo)
    private Integer dinners;
//    Lista de ingredientes que incorpora este plato
    private List<AmountOfIngredient> ingredients;
}
