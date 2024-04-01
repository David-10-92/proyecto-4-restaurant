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
public class Allergen {
//    Identificador del alérgeno
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
//    Nombre del alérgeno
    private String name;
    @ManyToMany(mappedBy = "allergens")
    private List<Ingredient> ingredients;
}
