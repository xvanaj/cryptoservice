package com.example.recommendationservice.service;

import com.example.recommendationservice.domain.CryptocurrencyDataLine;
import com.example.recommendationservice.domain.CryptocurrencySearchRequest;
import com.example.recommendationservice.utils.CSVUtils;
import com.example.recommendationservice.utils.CryptocurrencyDataNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class CryptocurrencySearchService {

    @Value("${classpath.crypto.files.folder}")
    private String classpathCryptoFilesFolder;
    @Value("${cryptocurrency.files.suffix}")
    private String cryptoFilesSuffix;
    @Autowired
    ResourceLoader resourceLoader;

    private static final Logger logger = LoggerFactory.getLogger(CryptocurrencySearchService.class);

    @Cacheable({"cryptos"})
    public List<CryptocurrencyDataLine> getCryptocurrenciesData() {
        final List<CryptocurrencyDataLine> entitiesFromCSV = new ArrayList<>();

        try {
            File folder = getFolderWithCryptocurrenciesData();
            File[] files = getFilesFromFolderWithSuffix(folder, cryptoFilesSuffix);

            if (files == null || files.length == 0) {
                throw new FileNotFoundException();
            }

            readCryptocurrenciesDataFromFiles(entitiesFromCSV, files);
        } catch (IOException e) {
            throw new CryptocurrencyDataNotFoundException(
                    String.format("No files with cryptocurrency data found in %s folder. " +
                            "Make sure that files have suffix %s. " +
                            "For example BTC_values.csv", classpathCryptoFilesFolder, cryptoFilesSuffix));
        }

        return entitiesFromCSV;
    }

    public Map<String, List<CryptocurrencyDataLine>> searchCryptocurrencies(CryptocurrencySearchRequest request) {
        List<CryptocurrencyDataLine> cryptocurrenciesData = getCryptocurrenciesData();

        cryptocurrenciesData = filterCryptocurrencyData(cryptocurrenciesData, request);

        return cryptocurrenciesData.stream()
                .collect(Collectors.groupingBy(CryptocurrencyDataLine::getSymbol));
    }

    private List<CryptocurrencyDataLine> filterCryptocurrencyData(List<CryptocurrencyDataLine> cryptocurrenciesData,
                                                                  CryptocurrencySearchRequest request) {
        if (Objects.isNull(cryptocurrenciesData) || cryptocurrenciesData.isEmpty()) {
            throw new CryptocurrencyDataNotFoundException("No cryptocurrency data found. " +
                    "Please check that csv files with data are not missing ");
        }

        if (Objects.nonNull(request.getSymbol())) {
            cryptocurrenciesData = cryptocurrenciesData.stream()
                    .filter(item -> item.getSymbol().equals(request.getSymbol()))
                    .toList();
            if (cryptocurrenciesData.isEmpty()) {
                throw new CryptocurrencyDataNotFoundException("No data found for symbol " + request.getSymbol());
            }
        }

        if (Objects.nonNull(request.getDate())) {
            cryptocurrenciesData = cryptocurrenciesData.stream()
                    .filter(item -> item.getTimestamp().toLocalDate().equals(request.getDate()))
                    .toList();
        }

        if (Objects.nonNull(request.getDateFrom())) {
            cryptocurrenciesData = cryptocurrenciesData.stream()
                    .filter(item -> item.getTimestamp().toLocalDate().compareTo(request.getDateFrom()) >= 0)
                    .toList();
        }

        if (Objects.nonNull(request.getDateTo())) {
            cryptocurrenciesData = cryptocurrenciesData.stream()
                    .filter(item -> item.getTimestamp().toLocalDate().compareTo(request.getDateTo()) <= 0)
                    .toList();
        }

        return cryptocurrenciesData;
    }

    private File getFolderWithCryptocurrenciesData() throws IOException {
        Resource resource = getCryptocurrenciesFolderResource();
        String cryptoFolderPath = resource.getURL().getPath();
        File folder = new File(cryptoFolderPath);

        logger.info("Crypto files folder = {}", folder);
        return folder;
    }

    private static void readCryptocurrenciesDataFromFiles(List<CryptocurrencyDataLine> entitiesFromCSV, File[] files) throws IOException {
        for (File file : files) {
            Path path = Path.of(file.toURI());

            logger.info("Reading csv file with cryptocurrency data {}", file.getName());

            List<CryptocurrencyDataLine> cryptocurrencyData = CSVUtils.getEntitiesFromCSV(path, CryptocurrencyDataLine.class);
            entitiesFromCSV.addAll(cryptocurrencyData);

            logger.info("{} records read from csv file", cryptocurrencyData.size());
        }
    }

    private static File[] getFilesFromFolderWithSuffix(File folder, String suffix) {
        return folder.listFiles((dir, name) -> name.endsWith(suffix));
    }

    private Resource getCryptocurrenciesFolderResource() throws FileNotFoundException {
        Resource resource = resourceLoader.getResource(classpathCryptoFilesFolder);

        if (!resource.exists()) {
            throw new FileNotFoundException(String.format("Folder %s not found on classpath",
                    classpathCryptoFilesFolder));
        }

        return resource;
    }

}
