package com.example.recommendationservice.service;

import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Path;
import java.util.List;
import java.util.logging.Logger;

@RestController
@Log4j2
public class CryptoRecommendationService {

    Logger logger = context.getLogger("com");

    public void getRecommendations() {
        try {
            List<String[]> strings = CSVUtils.readAllLines(Path.of("BTC_values.csv"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
