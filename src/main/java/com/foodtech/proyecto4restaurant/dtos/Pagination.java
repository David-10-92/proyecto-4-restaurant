package com.foodtech.proyecto4restaurant.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Pagination {
//    Número de resultados de búsqueda que se devolverán
    private Integer limit;
//    Indica el índice sobre la lista total de resultadosa partir del cual se comenzará a
//    contar. Por ejemplo, si el totalde resultados es de 1500, el _offset_ es 50 y _limit_ vale 100,
//    seincluirían los resultados del 50 al 150
    private Integer offset;
}
