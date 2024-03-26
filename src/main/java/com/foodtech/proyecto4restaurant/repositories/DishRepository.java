package com.foodtech.proyecto4restaurant.repositories;

import com.foodtech.proyecto4restaurant.models.Dish;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DishRepository extends JpaRepository<Dish,Integer> {

}
