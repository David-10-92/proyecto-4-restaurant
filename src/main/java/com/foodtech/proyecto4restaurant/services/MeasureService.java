package com.foodtech.proyecto4restaurant.services;

import com.foodtech.proyecto4restaurant.dtos.CreateMeasure;
import com.foodtech.proyecto4restaurant.dtos.MeasureDetails;
import com.foodtech.proyecto4restaurant.dtos.UpdateMeasure;
import com.foodtech.proyecto4restaurant.models.Measure;

import java.util.List;

public interface MeasureService {
    String addMeasure(CreateMeasure createMeasure);
    String deleteMeasure(Integer id);
    MeasureDetails getMeasure(Integer id);
    List<MeasureDetails> listMeasures(String filter);
    String updateMeasure(Integer id, UpdateMeasure updateMeasure);

}
