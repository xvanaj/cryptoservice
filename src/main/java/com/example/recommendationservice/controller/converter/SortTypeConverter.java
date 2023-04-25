package com.example.recommendationservice.controller.converter;

import com.example.recommendationservice.domain.enums.SortType;
import org.springframework.core.convert.converter.Converter;

public class SortTypeConverter implements Converter<String, SortType> {

    @Override
    public SortType convert(String source) {
        return SortType.fromFieldName(source);

    }
}
