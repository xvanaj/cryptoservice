package com.example.recommendationservice.service;

import com.example.recommendationservice.domain.Cryptocurrency;
import com.example.recommendationservice.domain.CryptocurrencySearchRequest;
import com.example.recommendationservice.domain.enums.SortOrder;
import com.example.recommendationservice.domain.enums.SortType;
import com.example.recommendationservice.domain.search.Sort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CryptocurrencyServiceTest {

    private CryptocurrencyService underTest;

    @BeforeEach
    void setUnderTest(){
        underTest = new CryptocurrencyService();
    }

    @Test
    void getCryptocurrencies_noParams_returnsUnfilteredUnsortedRecords() {
        //GIVEN
        Sort sorting = new Sort(SortType.SYMBOL, SortOrder.ASC);
        CryptocurrencySearchRequest request = new CryptocurrencySearchRequest(null,null,null,null, null);

        List<Cryptocurrency> cryptocurrencies = underTest.getCryptocurrencies(request);

        assertThat(cryptocurrencies)
                .isNotNull()
                .hasSize(5);

    }

}