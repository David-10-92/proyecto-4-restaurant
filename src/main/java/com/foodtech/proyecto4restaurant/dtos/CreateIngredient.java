package com.foodtech.proyecto4restaurant.dtos;

import com.foodtech.proyecto4restaurant.models.Allergen;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
public class CreateIngredient {
//    Nombre del ingrediente
    private String name;
//    Identificador de la unidad de medida en la que se administra este ingrediente
    private Integer measureId;
//    Indica el valor del precio de compra de cada unidad de este ingrediente
    private BigDecimal purchasePrice;
//    Indica el valor de venta de una unidad de este ingrediente cuando se incluye en un plato
    private  BigDecimal sellPrice;
//    Lista de alérgenos presentes en el alimento
    private List<Allergen> allergens;
}
