package com.example.recommendationservice.service;

import com.example.recommendationservice.domain.CryptocurrencyDataLine;
import com.example.recommendationservice.domain.search.CryptocurrencySearchRequest;
import com.example.recommendationservice.utils.CryptocurrencyDataNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class CryptocurrencySearchService {

    @Autowired
    private CryptocurrencyDataService cryptocurrencyDataService;

    public Map<String, List<CryptocurrencyDataLine>> getCryptocurrencyData(CryptocurrencySearchRequest request) {
        List<CryptocurrencyDataLine> cryptocurrenciesData = cryptocurrencyDataService.getCryptocurrencyData();
        List<CryptocurrencyDataLine> filteredData = filterCryptocurrencyData(cryptocurrenciesData, request);

        return filteredData.stream()
                .collect(Collectors.groupingBy(CryptocurrencyDataLine::getSymbol));
    }

    private List<CryptocurrencyDataLine> filterCryptocurrencyData(List<CryptocurrencyDataLine> cryptocurrenciesData,
                                                                  CryptocurrencySearchRequest request) {
        if (Objects.isNull(cryptocurrenciesData) || cryptocurrenciesData.isEmpty()) {
            throw new CryptocurrencyDataNotFoundException("No cryptocurrency data found. " +
                    "Please check that csv files with data are not missing ");
        }

        if (Objects.nonNull(request.getSymbol())) {
            cryptocurrenciesData = cryptocurrenciesData.stream()
                    .filter(item -> item.getSymbol().equals(request.getSymbol()))
                    .toList();
            if (cryptocurrenciesData.isEmpty()) {
                throw new CryptocurrencyDataNotFoundException("No data found for symbol " + request.getSymbol());
            }
        }

        if (Objects.nonNull(request.getDate())) {
            cryptocurrenciesData = cryptocurrenciesData.stream()
                    .filter(item -> item.getTimestamp().toLocalDate().equals(request.getDate()))
                    .toList();
        }

        if (Objects.nonNull(request.getDateFrom())) {
            cryptocurrenciesData = cryptocurrenciesData.stream()
                    .filter(item -> item.getTimestamp().toLocalDate().compareTo(request.getDateFrom()) >= 0)
                    .toList();
        }

        if (Objects.nonNull(request.getDateTo())) {
            cryptocurrenciesData = cryptocurrenciesData.stream()
                    .filter(item -> item.getTimestamp().toLocalDate().compareTo(request.getDateTo()) <= 0)
                    .toList();
        }

        return cryptocurrenciesData;
    }

}
