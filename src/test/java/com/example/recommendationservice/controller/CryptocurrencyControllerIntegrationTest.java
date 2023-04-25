package com.example.recommendationservice.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CryptocurrencyControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void getCryptocurrencies_noParams_sortsByNormalizedRangeDesc() throws Exception {
        // GIVEN
        String query = "";

        // WHEN
        callGetCryptocurrenciesEndpoint(query)
                // THEN
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(5)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].symbol").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].symbol").value("ETH"));
    }

    @Test
    void getCryptocurrencies_symbolXRP_returnsXRPDetails() throws Exception {
        // GIVEN
        String query = "?symbol=XRP";

        // WHEN
        callGetCryptocurrenciesEndpoint(query)
                // THEN
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].symbol").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].symbol").value("XRP"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].normalizedPrice").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].minPrice").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].maxPrice").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].oldest").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].newest").exists())
        ;
    }

    @Test
    void getCryptocurrencies_incorrectSymbol_returns404() throws Exception {
        // GIVEN
        String query = "?symbol=HIVE";

        // WHEN
        callGetCryptocurrenciesEndpoint(query)
                // THEN
                .andExpect(status().isNotFound())
        ;
    }

    @Test
    void getCryptocurrencies_fullQuery_filtersBySymbol() throws Exception {
        // GIVEN
        String query = "?symbol=LTC&dateFrom=2022-01-05&dateTo=2022-01-25" +
                "&sort=NORMALIZED_PRICE&sortOrder=DESC";

        // WHEN
        callGetCryptocurrenciesEndpoint(query)
                // THEN
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].symbol").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].symbol").value("LTC"))
        ;
    }

    @Test
    void getCryptocurrencies_queryByDateSortByNormalizedPrice_returnsDOGEFirst() throws Exception {
        // GIVEN
        String query = "?dateFrom=2022-01-25&dateTo=2022-01-25" +
                "&sort=NORMALIZED_PRICE&sortOrder=DESC";

        // WHEN
        callGetCryptocurrenciesEndpoint(query)
                // THEN
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(5)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].symbol").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].symbol").value("DOGE"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].oldest").value(containsString("2022-01-25")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].newest").value(containsString("2022-01-25")))
        ;
    }

    @Test
    void getCryptocurrencies_sortBySymbolDesc_returnsSorted() throws Exception {
        // GIVEN
        String query = "?dateFrom=2022-01-01&dateTo=2022-01-25" +
                "&sort=SYMBOL&sortOrder=DESC";

        // WHEN
        callGetCryptocurrenciesEndpoint(query)
                // THEN
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(5)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].symbol").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].symbol").value("XRP"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[4].symbol").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[4].symbol").value("BTC"))
        ;
    }

    private ResultActions callGetCryptocurrenciesEndpoint(String query) throws Exception {
        return mvc.perform(MockMvcRequestBuilders
                        .get("/cryptocurrency" + query)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
    }
}