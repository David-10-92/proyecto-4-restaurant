package com.foodtech.proyecto4restaurant.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DishDetails_allergens_inner {
    //Identificador del alergeno
    private Integer id;
    //Nombre del alergeno
    private String name;
}
