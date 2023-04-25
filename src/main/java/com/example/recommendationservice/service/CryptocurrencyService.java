package com.example.recommendationservice.service;

import com.example.recommendationservice.domain.Cryptocurrency;
import com.example.recommendationservice.domain.CryptocurrencyDataLine;
import com.example.recommendationservice.domain.search.CryptocurrencySearchRequest;
import com.example.recommendationservice.domain.enums.SortOrder;
import com.example.recommendationservice.domain.search.Sort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

import static com.example.recommendationservice.domain.enums.SortType.*;

@Component
public class CryptocurrencyService {

    @Autowired
    private CryptocurrencySearchService cryptocurrencySearchService;

    public List<Cryptocurrency> getCryptocurrencies(CryptocurrencySearchRequest request) {
        Map<String, List<CryptocurrencyDataLine>> cryptocurrencyDataBySymbol
                = cryptocurrencySearchService.getCryptocurrencyData(request);
        List<Cryptocurrency> cryptocurrencies = new ArrayList<>();

        Set<String> symbols = cryptocurrencyDataBySymbol.keySet();
        symbols.forEach(symbol -> calculateCryptocurrencyStatistics(cryptocurrencyDataBySymbol, cryptocurrencies, symbol));

        if (Objects.isNull(request.getSorting())) {
            return cryptocurrencies;
        } else {
            return sortCryptocurrencies(cryptocurrencies, request.getSorting());
        }
    }

    private static List<Cryptocurrency> sortCryptocurrencies(List<Cryptocurrency> cryptocurrencies, Sort sorting) {
        if (NORMALIZED_PRICE.equals(sorting.getSortType())) {
            cryptocurrencies.sort(Comparator.comparing(Cryptocurrency::getNormalizedPrice, getSortOrder(sorting)));
        } else if (SYMBOL.equals(sorting.getSortType())) {
            cryptocurrencies.sort(Comparator.comparing(Cryptocurrency::getSymbol, getSortOrder(sorting)));
        } else if (MIN_PRICE.equals(sorting.getSortType())) {
            cryptocurrencies.sort(Comparator.comparing(Cryptocurrency::getMinPrice, getSortOrder(sorting)));
        } else if (MAX_PRICE.equals(sorting.getSortType())) {
            cryptocurrencies.sort(Comparator.comparing(Cryptocurrency::getMaxPrice, getSortOrder(sorting)));
        } else if (OLDEST.equals(sorting.getSortType())) {
            cryptocurrencies.sort(Comparator.comparing(Cryptocurrency::getOldest, getSortOrder(sorting)));
        } else if (NEWEST.equals(sorting.getSortType())) {
            cryptocurrencies.sort(Comparator.comparing(Cryptocurrency::getNewest, getSortOrder(sorting)));
        }

        return cryptocurrencies;
    }

    private static <T extends Comparable<T>> Comparator<T> getSortOrder(Sort sorting) {
        SortOrder sortOrder = sorting.getSortOrder();

        if (sortOrder == null || sortOrder == SortOrder.DESC) {
            return Comparator.reverseOrder();
        } else if (sortOrder == SortOrder.ASC) {
            return Comparator.naturalOrder();
        }

        throw new IllegalArgumentException("Invalid sort order defined: " + sortOrder);
    }

    private static void calculateCryptocurrencyStatistics(
            Map<String, List<CryptocurrencyDataLine>> cryptocurrenciesBySymbols, List<Cryptocurrency> cryptocurrencies,
            String symbol) {
        List<CryptocurrencyDataLine> cryptocurrencyData = cryptocurrenciesBySymbols.get(symbol);

        Cryptocurrency cryptocurrency = calculateCryptocurrencyStatistics(cryptocurrencyData);

        cryptocurrencies.add(cryptocurrency);
    }

    private static Cryptocurrency calculateCryptocurrencyStatistics(List<CryptocurrencyDataLine> cryptocurencyData) {
        String symbol = cryptocurencyData.get(0).getSymbol();

        LocalDateTime minDate = cryptocurencyData.stream()
                .map(CryptocurrencyDataLine::getTimestamp)
                .min(LocalDateTime::compareTo)
                .orElseThrow(() -> new NoSuchElementException("Could not get min date for symbol " + symbol));
        LocalDateTime maxDate = cryptocurencyData.stream()
                .map(CryptocurrencyDataLine::getTimestamp)
                .max(LocalDateTime::compareTo)
                .orElseThrow(() -> new NoSuchElementException("Could not get max date for symbol " + symbol));
        Double min = cryptocurencyData.stream()
                .mapToDouble(CryptocurrencyDataLine::getPrice).min()
                .orElseThrow(() -> new NoSuchElementException("Could not calculate minimum price for symbol " + symbol));
        Double max = cryptocurencyData.stream()
                .mapToDouble(CryptocurrencyDataLine::getPrice).max()
                .orElseThrow(() -> new NoSuchElementException("Could not calculate maximum price for symbol " + symbol));
        Double normalized = (max - min) / min;

        return new Cryptocurrency(symbol, min, max, normalized, minDate, maxDate);
    }
}
