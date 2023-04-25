package com.example.recommendationservice.domain;

import com.example.recommendationservice.domain.search.Sort;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.time.LocalDate;

@Value
@AllArgsConstructor
public class CryptocurrencySearchRequest {

    String symbol;
    LocalDate date;
    LocalDate dateFrom;
    LocalDate dateTo;
    Sort sorting;

}
