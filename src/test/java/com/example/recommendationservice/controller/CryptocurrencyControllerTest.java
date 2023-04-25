package com.example.recommendationservice.controller;

import com.example.recommendationservice.domain.Cryptocurrency;
import com.example.recommendationservice.domain.CryptocurrencyDataLine;
import com.example.recommendationservice.domain.enums.SortOrder;
import com.example.recommendationservice.domain.enums.SortType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
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
    void getCryptocurrencies_sortByNormalizedPriceDesc() {
        //GIVEN
        SortType sortType = SortType.NORMALIZED_PRICE;
        SortOrder sortOrder = SortOrder.DESC;

        //WHEN
        List<Cryptocurrency> cryptocurrencies
                = cryptocurrencyController.getCryptocurrencies(null, null, null, null, sortType, sortOrder);

        //THEN
        assertThat(cryptocurrencies)
                .isNotNull()
                .isNotEmpty()
                .hasSize(5);

        Double maxNormalizedPrice
                = Collections.max(cryptocurrencies.stream().map(Cryptocurrency::getNormalizedPrice).toList());
        assertThat(cryptocurrencies.get(0).getNormalizedPrice())
                .isEqualTo(maxNormalizedPrice);
    }

    @Test
    void getCryptocurrencies_sortByNormalizedPriceAsc() {
        //GIVEN
        SortType sortType = SortType.NORMALIZED_PRICE;
        SortOrder desc = SortOrder.ASC;

        //WHEN
        List<Cryptocurrency> cryptocurrencies
                = cryptocurrencyController.getCryptocurrencies(null, null, null, null, null, null);

        //THEN
        assertThat(cryptocurrencies)
                .isNotNull()
                .isNotEmpty()
                .hasSize(5);

        Double minNormalizedPrice
                = Collections.min(cryptocurrencies.stream().map(Cryptocurrency::getNormalizedPrice).toList());
        assertThat(cryptocurrencies.get(0).getNormalizedPrice())
                .isEqualTo(minNormalizedPrice);
    }

    @Test
    void getCryptocurrencies_sortBySymbolDesc() {
        //GIVEN
        SortType sortType = SortType.SYMBOL;
        SortOrder sortOrder = SortOrder.DESC;

        //WHEN
        List<Cryptocurrency> cryptocurrencies
                = cryptocurrencyController.getCryptocurrencies(null, null, null, null, sortType, sortOrder);

        //THEN
        assertThat(cryptocurrencies.get(0).getSymbol())
                .isEqualTo("XRP");
    }

    @Test
    void getCryptocurrencies_sortBySymbolAsc() {
        //GIVEN
        SortType sortType = SortType.SYMBOL;
        SortOrder sortOrder = SortOrder.ASC;

        //WHEN
        List<Cryptocurrency> cryptocurrencies
                = cryptocurrencyController.getCryptocurrencies(null, null, null, null, sortType, sortOrder);

        //THEN
        assertThat(cryptocurrencies.get(0).getSymbol())
                .isEqualTo("BTC");
    }

    @Test
    void getCryptocurrencies_sortByMinPriceAsc() {
        //GIVEN
        SortType sortType = SortType.MIN_PRICE;
        SortOrder sortOrder = SortOrder.ASC;

        //WHEN
        List<Cryptocurrency> cryptocurrencies
                = cryptocurrencyController.getCryptocurrencies(null, null, null, null, sortType, sortOrder);

        //THEN
        Double minPrice = Collections.min(cryptocurrencies.stream().map(Cryptocurrency::getMinPrice).toList());
        assertThat(cryptocurrencies.get(0).getMinPrice())
                .isEqualTo(minPrice);
    }

    @Test
    void getCryptocurrencies_filterBySymbolAndDate() {
        //GIVEN
        String symbol = "XRP";
        LocalDate searchDate = LocalDate.of(2022, 1, 5);
        SortType sortType = SortType.MIN_PRICE;
        SortOrder sortOrder = SortOrder.ASC;

        //WHEN
        List<Cryptocurrency> cryptocurrencies
                = cryptocurrencyController.getCryptocurrencies(symbol, searchDate, null, null, sortType, sortOrder);

        //THEN
        assertThat(cryptocurrencies)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);
        assertThat(cryptocurrencies.get(0).getSymbol())
                .isEqualTo("XRP");
        assertThat(cryptocurrencies.get(0).getOldest().toLocalDate())
                .isEqualTo(searchDate);
        assertThat(cryptocurrencies.get(0).getNewest().toLocalDate())
                .isEqualTo(searchDate);
    }

    @Test
    void getCryptocurrencies_filterByDateFromAndDateTo() {
        //GIVEN
        LocalDate dateFrom = LocalDate.of(2022, 1, 5);
        LocalDate dateTo = LocalDate.of(2022, 1, 25);
        SortType sortType = SortType.MIN_PRICE;
        SortOrder sortOrder = SortOrder.ASC;

        //WHEN
        List<Cryptocurrency> cryptocurrencies
                = cryptocurrencyController.getCryptocurrencies(null, null, dateFrom, dateTo, sortType, sortOrder);

        //THEN
        assertThat(cryptocurrencies)
                .isNotNull()
                .isNotEmpty()
                .hasSize(5);
        assertThat(cryptocurrencies.get(0).getOldest().toLocalDate())
                .isEqualTo(dateFrom);
        assertThat(cryptocurrencies.get(0).getNewest().toLocalDate())
                .isEqualTo(dateTo);
    }

    @Test()
    void getCryptocurrencies_incorrectSymbol_throwsException() {
        //GIVEN
        String invalidSymbol = "BTC2";
        SortType sortType = SortType.MIN_PRICE;
        SortOrder sortOrder = SortOrder.ASC;

        //WHEN
        Assertions.assertThrows(Exception.class, () -> {
            cryptocurrencyController.getCryptocurrencies(invalidSymbol, null, null, null, sortType, sortOrder);
        });

        //THEN
    }

}