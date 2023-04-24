package com.example.recommendationservice.controller;

import com.example.recommendationservice.service.CryptoRecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

public class CryptoRecommendationController {

    @Autowired
    private CryptoRecommendationService cryptoRecommendationService;

    @GetMapping("/")
    public void getRecommendations() {
        cryptoRecommendationService.getRecommendations();
    }
}
