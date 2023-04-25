package com.example.recommendationservice.controller;

import com.example.recommendationservice.domain.Cryptocurrency;
import com.example.recommendationservice.domain.CryptocurrencyDataLine;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CryptocurrencyControllerTest {

    @Autowired
    CryptocurrencyController cryptocurrencyController;

    @Test
    void getCryptocurrenciesData() {
        //GIVEN

        //WHEN
        List<CryptocurrencyDataLine> cryptocurrenciesData = cryptocurrencyController.getCryptocurrenciesData();

        //THEN
        assertThat(cryptocurrenciesData)
                .isNotNull()
                .isNotEmpty();
        assertThat(cryptocurrenciesData.get(0).getSymbol()).isNotNull();
        assertThat(cryptocurrenciesData.get(0).getPrice()).isNotNull();
        assertThat(cryptocurrenciesData.get(0).getTimestamp()).isNotNull();
    }

    @Test
    void getCryptocurrencies_noParams_sortsByNormalizedPriceDesc() {
        //GIVEN

        //WHEN
        List<Cryptocurrency> cryptocurrencies
                = cryptocurrencyController.getCryptocurrencies(null, null, null, null, null, null);

        //THEN
        assertThat(cryptocurrencies)
                .isNotNull()
                .isNotEmpty()
                .hasSize(5);

        assertThat(cryptocurrencies.get(0).getNormalizedPrice()).isGreaterThanOrEqualTo(Collections.max(cryptocurrencies.stream().map(Cryptocurrency::getNormalizedPrice).toList()));
    }


}