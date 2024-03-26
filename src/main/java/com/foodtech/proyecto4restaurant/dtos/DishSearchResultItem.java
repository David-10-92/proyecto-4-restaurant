package com.foodtech.proyecto4restaurant.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
@NoArgsConstructor
public class DishSearchResultItem {
    //Identificador del plato
    private Integer id;
    //Nombre del plato
    private String name;
    //Precio del plato
    private BigDecimal price;
}
