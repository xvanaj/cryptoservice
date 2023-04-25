package com.example.recommendationservice.domain.search;


import com.example.recommendationservice.domain.enums.SortOrder;
import com.example.recommendationservice.domain.enums.SortType;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class Sort {

    SortType sortType;
    SortOrder sortOrder;

}
