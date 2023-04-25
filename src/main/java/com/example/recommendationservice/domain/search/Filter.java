package com.example.recommendationservice.domain.search;


import com.example.recommendationservice.domain.enums.FilterOperation;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class Filter {

    String filterType;
    String value;
    FilterOperation operation;

}
