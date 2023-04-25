package com.example.recommendationservice.utils;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class CSVUtils {

    private CSVUtils() {
    }

    public static <T> List<T> getEntitiesFromCSV(Path path, Class<T> clazz) throws IOException {
        try (Reader reader = Files.newBufferedReader(path)) {
            CsvToBean<T> cb = new CsvToBeanBuilder<T>(reader)
                    .withType(clazz)
                    .withThrowExceptions(false)
                    .build();

            return cb.parse();
        }
    }

}
