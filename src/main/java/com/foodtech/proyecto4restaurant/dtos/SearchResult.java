package com.foodtech.proyecto4restaurant.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SearchResult {
//    Indica cuantos registros se han encontrado en la búsqueda
    private Integer totalRecordsFound;
//    Indica cuantas páginas hay en la búsqueda
    private Integer totalPages;
//    Indica cuantos registros hay por página de resultados
    private Integer recordsPerPage;
}
