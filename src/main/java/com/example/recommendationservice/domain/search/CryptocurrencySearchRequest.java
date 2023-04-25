package com.example.recommendationservice.domain.search;

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
