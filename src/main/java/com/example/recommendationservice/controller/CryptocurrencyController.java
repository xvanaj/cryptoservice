package com.example.recommendationservice.controller;

import com.example.recommendationservice.domain.Cryptocurrency;
import com.example.recommendationservice.domain.CryptocurrencyDataLine;
import com.example.recommendationservice.domain.enums.SortOrder;
import com.example.recommendationservice.domain.enums.SortType;
import com.example.recommendationservice.domain.search.CryptocurrencySearchRequest;
import com.example.recommendationservice.domain.search.Sort;
import com.example.recommendationservice.service.*;
import io.github.bucket4j.Bucket;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@Tags(value = {@Tag(name = "Cryptocurrency")})
public class CryptocurrencyController {

    private static final Logger logger = LogManager.getLogger(CryptocurrencyController.class);

    @Autowired
    private IpRateLimitService ipRateLimitService;

    @Autowired
    private CryptocurrencyService cryptocurrencyService;

    @Autowired
    private CryptocurrencySearchService cryptocurrencySearchService;

    @Autowired
    private CryptocurrencyDataService cryptocurrencyDataService;

    @Autowired
    private ClearCacheTask clearCacheTask;

    @Operation(operationId = "getCryptocurrenciesData",
            summary = "Gets all cryptocurrencies data from csv files stored in system")
    @GetMapping("/cryptocurrency-data")
    public List<CryptocurrencyDataLine> getCryptocurrenciesData() {
        return cryptocurrencyDataService.getCryptocurrencyData();
    }

    @Operation(operationId = "getCryptocurrencies",
            summary = "Gets cryptocurrencies with basic statistics. Can be filtered and sorted. ",
            tags = "Cryptocurrency")
    @Parameter(in = ParameterIn.PATH, example = "2022-01-15", name = "date", description = "date in format yyyy-mm-dd")
    @Parameter(in = ParameterIn.PATH, example = "2022-01-25", name = "fromDate", description = "date in format yyyy-mm-dd")
    @Parameter(in = ParameterIn.PATH, example = "2022-01-05", name = "toDate", description = "date in format yyyy-mm-dd")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retrieved cryptocurrencies with data",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Cryptocurrency.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid parameters supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Requested resource not found. " +
                    "For example when incorrect symbol is supplied", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)})
    @GetMapping("/cryptocurrency")
    public ResponseEntity<List<Cryptocurrency>> getCryptocurrencies(
            HttpServletRequest httpServletRequest,
            @RequestParam(value = "symbol", required = false) String symbol,
            @RequestParam(value = "date", required = false) LocalDate date,
            @RequestParam(value = "dateFrom", required = false) LocalDate dateFrom,
            @RequestParam(value = "dateTo", required = false) LocalDate dateTo,
            @RequestParam(value = "sort", defaultValue = "normalizedPrice") SortType sortType,
            @RequestParam(value = "sortOrder", defaultValue = "desc") SortOrder sortOrder) {
        Bucket bucket = ipRateLimitService.resolveBucket(httpServletRequest.getRemoteAddr());

        if (bucket.tryConsume(1)) {
            Sort sorting = new Sort(sortType, sortOrder);
            CryptocurrencySearchRequest request = new CryptocurrencySearchRequest(symbol, date, dateFrom, dateTo, sorting);

            logger.info("Received request GET cryptocurrency with parameters {}", request);
            return ResponseEntity.ok(cryptocurrencyService.getCryptocurrencies(request));
        } else {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        }
    }

    @Operation(operationId = "clearCache",
            summary = "Clears all application caches. " +
                    "To prevent frequent loading of CSV files, it's contents are stored in the cache")
    @GetMapping("/clear-cache")
    public void clearCache() {
        logger.info("Received request to clear cache");
        clearCacheTask.clearCache();
    }

}
