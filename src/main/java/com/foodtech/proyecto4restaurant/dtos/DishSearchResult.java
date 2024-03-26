package com.foodtech.proyecto4restaurant.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
public class DishSearchResult {
    //Indica cuantos registros se han encontrado en la busqueda
    private Integer totalRecordsFound;
    //Indica cuantas paginas hay en la busqueda
    private Integer totalPages;
    //Indica cauntos requistros hay por pagina de resultados
    private Integer recordsPerPage;
    //Lista de platos encontrados en esta pagina
    private List<DishSearchResultItem> records;
}
