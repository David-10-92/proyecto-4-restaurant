package com.foodtech.proyecto4restaurant.repositories;

import com.foodtech.proyecto4restaurant.models.Dish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;
import java.util.List;

public interface DishRepository extends JpaRepository<Dish,Integer> {

    List<Dish> findByNameContainingIgnoreCase(String filter, PageRequest of);

}
