package com.example.recommendationservice.utils.converter;

import com.opencsv.bean.AbstractBeanField;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class UnixTimestampConverter extends AbstractBeanField<LocalDateTime, String> {

    @Override
    protected LocalDateTime convert(String value) {
        long unixTimestamp = Long.parseLong(value);
        Instant instant = Instant.ofEpochMilli(unixTimestamp);

        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }
}