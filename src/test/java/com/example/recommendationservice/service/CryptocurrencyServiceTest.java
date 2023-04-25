package com.example.recommendationservice.service;

import com.example.recommendationservice.domain.Cryptocurrency;
import com.example.recommendationservice.domain.CryptocurrencyDataLine;
import com.example.recommendationservice.domain.search.CryptocurrencySearchRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class CryptocurrencyServiceTest {

    @Mock
    private CryptocurrencySearchService cryptocurrencySearchService;
    private CryptocurrencyService underTest;

    @BeforeEach
    void setUnderTest(){
        underTest = new CryptocurrencyService();
    }

    @Test
    void getCryptocurrencies() {
        //GIVEN
        CryptocurrencySearchRequest request = new CryptocurrencySearchRequest(null,null,null,null, null);
        CryptocurrencyDataLine btcData = new CryptocurrencyDataLine(0.5d, LocalDateTime.of(2022, 1, 1, 10, 5, 10), "BTC");
        CryptocurrencyDataLine xrpData = new CryptocurrencyDataLine(0.3d, LocalDateTime.of(2022, 1, 3, 10, 5, 10), "BTC");
        CryptocurrencyDataLine ltcData = new CryptocurrencyDataLine(0.8d, LocalDateTime.of(2022, 1, 5, 10, 5, 10), "BTC");
        when(cryptocurrencySearchService.getCryptocurrencyData()).thenReturn(List.of(btcData, xrpData, ltcData));

        List<Cryptocurrency> cryptocurrencies = underTest.getCryptocurrencies(request);

        assertThat(cryptocurrencies)
                .isNotNull()
                .hasSize(3);
    }

}