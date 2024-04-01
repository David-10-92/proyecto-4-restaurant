package com.foodtech.proyecto4restaurant.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Measure {
//    Identificador único de la medida
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
//    Nombre de la unidad
    private String name;

    @OneToMany(mappedBy = "measureId")
    private List<Ingredient> ingredients;

}
