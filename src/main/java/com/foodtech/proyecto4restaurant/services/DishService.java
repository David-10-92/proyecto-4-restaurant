package com.foodtech.proyecto4restaurant.services;

import com.foodtech.proyecto4restaurant.dtos.CreateDish;
import com.foodtech.proyecto4restaurant.dtos.DishDetails;
import com.foodtech.proyecto4restaurant.dtos.DishSearchResult;
import com.foodtech.proyecto4restaurant.dtos.UpdateDish;

public interface DishService {
    String addDish(CreateDish createDish);
    String deleteDish(Integer id);
    DishDetails getDish(Integer id);
    DishSearchResult searchDish(Integer recordsPerpage,Integer page,String filter);
    String updateDish(Integer id, UpdateDish updateDish);
}
