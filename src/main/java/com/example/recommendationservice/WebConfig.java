package com.example.recommendationservice;

import com.example.recommendationservice.controller.converter.SortOrderConverter;
import com.example.recommendationservice.controller.converter.SortTypeConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new SortOrderConverter());
        registry.addConverter(new SortTypeConverter());
    }

}
