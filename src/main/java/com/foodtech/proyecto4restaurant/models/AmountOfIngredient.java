package com.foodtech.proyecto4restaurant.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
@NoArgsConstructor
@Entity
public class AmountOfIngredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
//    Identificador del ingrediente
    private Integer ingredientId;
//    Indica cuantas unidades de la medida indicada se usarán del ingrediente dado
    private BigDecimal value;
}
