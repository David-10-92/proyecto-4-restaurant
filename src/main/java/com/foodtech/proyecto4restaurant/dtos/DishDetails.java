package com.foodtech.proyecto4restaurant.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
public class DishDetails {
    //Identificador del plato
    private Integer id;
    //Nombre del plato
    private String name;
    //Indica el numero de comensales indicados para este plato
    private Integer dinners;
    //Indica el precio que le cuesta al restaurante comprar todos los materiales para elaborar este plato
    private BigDecimal buyPrice;
    //Indica el precio de venta al publico de este plato basándose en el precio individual de cada ingrediente.
    private BigDecimal sellPrice;
    //Lista de ingredientes que incorpora este plato
    private List<DishDetails_ingredients_inner> ingredients;
    //Lista de alergenos en el plato
    private List<DishDetails_allergens_inner> allergens;
}
