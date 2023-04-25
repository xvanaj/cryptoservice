package com.example.recommendationservice.service;

import com.example.recommendationservice.domain.CryptocurrencyDataLine;
import com.example.recommendationservice.utils.CSVUtils;
import com.example.recommendationservice.utils.CryptocurrencyDataNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Component
public class CryptocurrencyDataService {

    @Value("${cryptocurrency.files.folder}")
    private String cryptocurrencyFilesFolder;
    @Value("${cryptocurrency.files.suffix}")
    private String cryptoFilesSuffix;

    private static final Logger logger = LoggerFactory.getLogger(CryptocurrencyDataService.class);

    @Cacheable({"cryptos"})
    public List<CryptocurrencyDataLine> getCryptocurrencyData() {
        final List<CryptocurrencyDataLine> entitiesFromCSV = new ArrayList<>();

        try {
            File folder = getFolderWithCryptocurrenciesData();
            File[] files = getFilesFromFolderWithSuffix(folder, cryptoFilesSuffix);

            if (files == null || files.length == 0) {
                throw new CryptocurrencyDataNotFoundException(
                        String.format("Failed to load cryptocurrency data from folder %s. " +
                                "Make sure that files have suffix %s. ", cryptocurrencyFilesFolder, cryptoFilesSuffix));
            }

            readCryptocurrenciesDataFromFiles(entitiesFromCSV, files);
        } catch (IOException e) {
            throw new CryptocurrencyDataNotFoundException(
                    String.format("Failed to load cryptocurrency data from folder %s. " +
                            "Make sure that files have suffix %s. ", cryptocurrencyFilesFolder, cryptoFilesSuffix));
        }

        return entitiesFromCSV;
    }

    private File getFolderWithCryptocurrenciesData() {
        File folder = new File(cryptocurrencyFilesFolder);

        if (!folder.exists()) {
            throw new CryptocurrencyDataNotFoundException(String.format("Folder %s not found",
                    cryptocurrencyFilesFolder));
        }

        logger.info("Cryptocurrency files folder = {}", folder);
        return folder;
    }

    private void readCryptocurrenciesDataFromFiles(List<CryptocurrencyDataLine> entitiesFromCSV, File[] files) throws IOException {
        for (File file : files) {
            Path path = Path.of(file.toURI());

            logger.info("Reading csv file with cryptocurrency data {}", file.getName());

            List<CryptocurrencyDataLine> cryptocurrencyData = CSVUtils.getEntitiesFromCSV(path, CryptocurrencyDataLine.class);
            entitiesFromCSV.addAll(cryptocurrencyData);

            logger.info("{} records read from csv file", cryptocurrencyData.size());
        }
    }

    private File[] getFilesFromFolderWithSuffix(File folder, String suffix) {
        return folder.listFiles((dir, name) -> name.endsWith(suffix));
    }
}
