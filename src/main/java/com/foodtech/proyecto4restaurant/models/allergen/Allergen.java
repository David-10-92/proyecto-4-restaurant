package com.foodtech.proyecto4restaurant.models.allergen;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Table
@Entity
public class Allergen {
//    Identificador del alérgeno
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
//    Nombre del alérgeno
    private String name;
}
