package com.example.recommendationservice;

import com.example.recommendationservice.controller.CryptocurrencyController;
import com.example.recommendationservice.domain.CryptocurrencyDataLine;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CryptocurrencyServiceApplicationTests {

	@Autowired
	CryptocurrencyController controller;

	@Test
	void contextLoads() {
		List<CryptocurrencyDataLine> cryptocurrenciesData = controller.getCryptocurrenciesData();

		assertThat(cryptocurrenciesData)
				.isNotNull()
				.isNotEmpty();
	}

}
