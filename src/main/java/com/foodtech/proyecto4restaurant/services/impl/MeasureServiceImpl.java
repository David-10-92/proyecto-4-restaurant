package com.foodtech.proyecto4restaurant.services.impl;

import com.foodtech.proyecto4restaurant.dtos.CreateMeasure;
import com.foodtech.proyecto4restaurant.dtos.MeasureDetails;
import com.foodtech.proyecto4restaurant.dtos.UpdateMeasure;
import com.foodtech.proyecto4restaurant.models.Measure;
import com.foodtech.proyecto4restaurant.repositories.AllergenRepository;
import com.foodtech.proyecto4restaurant.repositories.MeasureRepository;
import com.foodtech.proyecto4restaurant.services.MeasureService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MeasureServiceImpl implements MeasureService {

    @Autowired
    MeasureRepository measureRepository;
    @Autowired
    AllergenRepository allergenRepository;
    @Override
    public String addMeasure(CreateMeasure createMeasure) {
        Measure measure = new Measure();
        measure.setName(createMeasure.getName());
        measureRepository.save(measure);
        return measure.getName();
    }

    @Override
    public String deleteAllergen(Integer id) {
        return allergenRepository.findById(id).map(allergen -> {
            allergenRepository.deleteById(id);
            return ("El alergeno se ha eliminado correctamente");
        }).orElse("No se encontró ningún allergeno con el ID proporcionado");
    }

    @Override
    public String deleteMeasure(Integer id) {
        return measureRepository.findById(id)
                .map(measure -> {
                    measureRepository.deleteById(id);
                    return ("La medida ha sido eliminada correctamente");
                }).orElse("No se encontró ninguna medida con el ID proporcionado");
    }

    @Override
    public MeasureDetails getMeasure(Integer id) {
        Optional<Measure> optionalMeasure = measureRepository.findById(id);
        if (optionalMeasure.isPresent()) {
            Measure measure = optionalMeasure.get();
            MeasureDetails measureDetails = new MeasureDetails();
            measureDetails.setId(measure.getId());
            measureDetails.setName(measure.getName());
            //aqui el mismo prblema de siempre no se como hacerlo por que estoy algo perdido
            //la lista esta formada de otra clase que no se donde colocarla si model o dto
            return measureDetails;
        } else {
            return null;
        }
    }

    @Override
    public List<Measure> listMeasures(String filter) {
        List<Measure> allMeasures = measureRepository.findAll();
        //este metodo es para que compruebe parcialmente no lo he probado por que no me funciona
        //el proyecto por el tema de las entidades y las relaciones entre ellas
        if (filter != null && !filter.isEmpty()) {
            return allMeasures.stream()
                    .filter(measure -> measure.getName().toLowerCase().contains(filter.toLowerCase()))
                    .sorted((m1, m2) -> m1.getName().compareToIgnoreCase(m2.getName())).collect(Collectors.toList());
        } else {
            allMeasures.sort((m1, m2) -> m1.getName().compareToIgnoreCase(m2.getName()));
            return allMeasures;
        }
    }

    @Override
    public String updateMeasure(Integer id, UpdateMeasure updateMeasure) {
        return measureRepository.findById(id)
                .map(measure -> {
                    if (updateMeasure.getName() != null) {
                        measure.setName(updateMeasure.getName());
                    }
                    measureRepository.save(measure);
                    return "La medida se ha actualizado correctamente";
                }).orElse("No se encontró ninguna medida con el ID proporcionado");
    }
}