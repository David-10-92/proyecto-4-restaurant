package com.foodtech.proyecto4restaurant.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
public class AllergenDetails {
//    Identificador del alérgeno
    private Integer id;
//    Nombre del alérgeno
    private String name;
//    Lista de platos en los que este alérgeno está presente
    private List<AllergenDetails_allOf_presentIn> presentIn;
}
