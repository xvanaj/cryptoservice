package com.example.recommendationservice.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class Cryptocurrency {

    private String symbol;
    private Double minPrice;
    private Double maxPrice;
    private Double normalizedPrice;
    private LocalDateTime oldest;
    private LocalDateTime newest;

}
