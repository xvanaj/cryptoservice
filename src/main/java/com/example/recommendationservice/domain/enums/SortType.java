package com.example.recommendationservice.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SortType {

    SYMBOL("symbol"),
    NORMALIZED_PRICE("normalizedPrice"),
    MIN_PRICE("minPrice"),
    MAX_PRICE("maxPrice"),
    OLDEST("oldest"),
    NEWEST("newest"),
    ;

    private final String fieldName;


    public static SortType fromFieldName(String fieldName) {
        for (SortType sortType : SortType.values()) {
            if (sortType.getFieldName().equalsIgnoreCase(fieldName)) {
                return sortType;
            }
        }

        throw new IllegalArgumentException("No SortType found for field name " + fieldName);
    }

}
