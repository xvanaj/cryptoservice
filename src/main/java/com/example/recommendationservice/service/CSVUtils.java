package com.example.recommendationservice.service;

import com.opencsv.CSVReader;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class CSVUtils {

    public List<String[]> readAllByFilename(String fileName) throws Exception {
        Path path = Paths.get(ClassLoader.getSystemResource(fileName).toURI());

        return readAllLines(path);
    }

    public static List<String[]> readAllLines(Path filePath) throws Exception {

        try (Reader reader = Files.newBufferedReader(filePath)) {
            try (CSVReader csvReader = new CSVReader(reader)) {
                return csvReader.readAll();
            }
        }
    }

}
