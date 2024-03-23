package com.foodtech.proyecto4restaurant.dtos;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
public class IngredientCommonData {
    //Nombre del ingrediente
    private String name;
    //Indentificador de la unidad de medida en la que se administra este ingrediente
    private Integer measureId;
    //Indica el valor del precio de compra de cada unidad de este ingrediente
    private BigDecimal purchasePrice;
    //Indica el valor de venta de una unidad de este ingrediente cuando se incluye en un plato
    private BigDecimal sellPrice;
    //Lista de alérgenos presentes en el alimento
    private List<String> allergens;
}
