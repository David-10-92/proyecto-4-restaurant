package com.foodtech.proyecto4restaurant.dtos;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
public class MeasureDetails {
    //Identificador único de la medida
    private Integer id;
//    Nombre de la unidad
    private String name;
//    Lista de ingredientes que usan esta medida
    private List<MeasureDetails_allOf_ingredients> ingredients;
}
