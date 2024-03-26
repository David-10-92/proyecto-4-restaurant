package com.foodtech.proyecto4restaurant.repositories;

import com.foodtech.proyecto4restaurant.models.Allergen;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AllergenRepository extends JpaRepository<Allergen,Integer> {
    List<Allergen> findByNameContaining(String filter);
}
