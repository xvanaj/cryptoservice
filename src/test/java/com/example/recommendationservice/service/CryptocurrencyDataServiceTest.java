package com.example.recommendationservice.service;

import com.example.recommendationservice.domain.CryptocurrencyDataLine;
import com.example.recommendationservice.utils.CryptocurrencyDataNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(properties = { "cryptocurrency.files.folder=./data" })
class CryptocurrencyDataServiceTest {

    @InjectMocks
    private CryptocurrencyDataService cryptocurrencyDataService;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(cryptocurrencyDataService, "cryptocurrencyFilesFolder", "src/test/resources");
        ReflectionTestUtils.setField(cryptocurrencyDataService, "cryptoFilesSuffix", "_values.csv");
    }

    @Test
    void getCryptocurrencyData() {
        // GIVEN

        // WHEN
        List<CryptocurrencyDataLine> data = cryptocurrencyDataService.getCryptocurrencyData();

        // THEN
        assertThat(data)
                .isNotNull()
                .isNotEmpty()
                .hasSize(450);  //all 450 records should be retrieved
        assertThat(data.get(0)).isNotNull();
        assertThat(data.get(0).getTimestamp()).isNotNull();
        assertThat(data.get(0).getPrice()).isNotNull();
        assertThat(data.get(0).getSymbol()).isNotEmpty();
    }

    @Test
    void getCryptocurrencyData_missingFolder_throwsException() {
        // GIVEN
        ReflectionTestUtils.setField(cryptocurrencyDataService, "cryptocurrencyFilesFolder", "nonexistentFolder");

        // WHEN + THEN
        CryptocurrencyDataNotFoundException exception
                = assertThrows(CryptocurrencyDataNotFoundException.class,
                () -> cryptocurrencyDataService.getCryptocurrencyData(),
                "CryptocurrencyDataNotFoundException was expected");

        assertThat(exception.getMessage()).contains(String.format("Folder %s not found", "nonexistentFolder"));
    }

    @Test
    void getCryptocurrencyData_missingFilesWithSuffix_throwsException() {
        // GIVEN
        ReflectionTestUtils.setField(cryptocurrencyDataService, "cryptoFilesSuffix", "_prices.csv");

        // WHEN + THEN
        CryptocurrencyDataNotFoundException exception
                = assertThrows(CryptocurrencyDataNotFoundException.class,
                () -> cryptocurrencyDataService.getCryptocurrencyData(),
                "CryptocurrencyDataNotFoundException was expected");

        assertThat(exception.getMessage()).contains("No files with cryptocurrency data found in");
    }

}