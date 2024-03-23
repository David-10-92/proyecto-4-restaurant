package com.foodtech.proyecto4restaurant.models.dish.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DishDetails_ingredients_inner {
    //Identificador del ingrediente
    private Integer id;
    //Nombre del ingrediente
    private String name;
}
