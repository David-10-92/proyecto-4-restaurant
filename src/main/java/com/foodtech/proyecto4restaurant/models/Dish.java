package com.foodtech.proyecto4restaurant.models;

import com.foodtech.proyecto4restaurant.models.AmountOfIngredient;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
public class Dish {
    //    Identificador único del plato
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    //    Nombre del plato
    private String name;
    //    Descripción del plato para el menú
    private String description;
    //    Indica el número de comensales indicados para este plato (orientativo)
    private Integer dinners;
    //    Lista de ingredientes que incorpora este plato
    @OneToMany(mappedBy = "dish", cascade = CascadeType.ALL)
    private List<AmountOfIngredient> amountsOfIngredients;
}
