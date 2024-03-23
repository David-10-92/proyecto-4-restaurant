package com.foodtech.proyecto4restaurant.models.dish.dtos;


import com.foodtech.proyecto4restaurant.models.AmountOfIngredient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
public class DishCommonData {
    //Nombre del plato
    private String name;
    //Descripcion del plato para el menu
    private String description;
    //Indica el numero de comensales indicador para este plato
    private Integer dinners;
    //Lista de ingredientes que incorpora este plato
    private List<AmountOfIngredient> ingrediens;
}
