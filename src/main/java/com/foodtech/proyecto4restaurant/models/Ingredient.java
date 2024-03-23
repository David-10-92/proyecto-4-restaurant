package com.foodtech.proyecto4restaurant.models;

import com.foodtech.proyecto4restaurant.models.allergen.Allergen;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@Table
@Entity
public class Ingredient {
//    Identificador del ingrediente
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
//    Nombre del ingrediente
    private String name;
//    Identificador de la unidad de medida en la que se administra este ingrediente
    private Integer measureId;
//    Indica el valor del precio de compra de cada unidad de este ingrediente.
    private BigDecimal purchasePrice;
//    Indica el valor de venta de una unidad de este ingrediente cuando se incluye en un plato.
    private BigDecimal sellPrice;
//    Lista de alérgenos presentes en el alimento
    @OneToMany
    @JoinTable(
            name = "ingredient_allergen",
            joinColumns = @JoinColumn(name = "ingredient_id"),
            inverseJoinColumns = @JoinColumn(name = "allergen_id")
    )
    private List<Allergen> allergens;
}
