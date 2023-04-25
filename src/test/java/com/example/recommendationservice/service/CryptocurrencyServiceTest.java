package com.example.recommendationservice.service;

import com.example.recommendationservice.domain.Cryptocurrency;
import com.example.recommendationservice.domain.CryptocurrencyDataLine;
import com.example.recommendationservice.domain.search.CryptocurrencySearchRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CryptocurrencyServiceTest {

    @Mock
    private CryptocurrencySearchService cryptocurrencySearchService;
    @InjectMocks
    private CryptocurrencyService underTest;

    @Test
    void getCryptocurrencies() {
        //GIVEN
        CryptocurrencySearchRequest request = new CryptocurrencySearchRequest(null, null, null, null, null);
        CryptocurrencyDataLine btcData = new CryptocurrencyDataLine(0.5d, LocalDateTime.of(2022, 1, 1, 10, 5, 10), "BTC");
        CryptocurrencyDataLine xrpData = new CryptocurrencyDataLine(0.3d, LocalDateTime.of(2022, 1, 3, 10, 5, 10), "XRP");
        CryptocurrencyDataLine ltcData = new CryptocurrencyDataLine(0.8d, LocalDateTime.of(2022, 1, 5, 10, 5, 10), "LTC");
        when(cryptocurrencySearchService.getCryptocurrencyData(any()))
                .thenReturn(Map.of("btc", List.of(btcData), "XRP", List.of(xrpData), "LTC", List.of(ltcData)));

        List<Cryptocurrency> cryptocurrencies = underTest.getCryptocurrencies(request);

        assertThat(cryptocurrencies)
                .isNotNull()
                .hasSize(3);
    }

}