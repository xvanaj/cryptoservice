package com.example.recommendationservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.security.Timestamp;

@Data
@AllArgsConstructor
public class Cryptocurency {

    @CsvBindByPosition(column = "price")
    private Double price;
    private Timestamp timestamp;
    private String symbol;

}
