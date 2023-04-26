package com.example.recommendationservice.utils;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND)
public class CryptocurrencyDataNotFoundException extends RuntimeException {

    public CryptocurrencyDataNotFoundException(String message) {
        super(message);
    }


}