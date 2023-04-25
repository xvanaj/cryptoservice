package com.example.recommendationservice.domain;

import com.example.recommendationservice.utils.converter.UnixTimestampConverter;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CryptocurrencyDataLine {

    @CsvBindByName(column = "price")
    private Double price;

    @CsvCustomBindByName(column = "timestamp", converter = UnixTimestampConverter.class)
    private LocalDateTime timestamp;

    @CsvBindByName(column = "symbol")
    private String symbol;

}
