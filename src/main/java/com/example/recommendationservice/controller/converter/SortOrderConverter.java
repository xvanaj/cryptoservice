package com.example.recommendationservice.controller.converter;

import com.example.recommendationservice.domain.enums.SortOrder;
import org.springframework.core.convert.converter.Converter;

public class SortOrderConverter implements Converter<String, SortOrder> {

    @Override
    public SortOrder convert(String source) {
        return SortOrder.valueOf(source.toUpperCase());
    }
}
