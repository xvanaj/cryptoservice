package com.example.recommendationservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ClearCacheTask {
    @Autowired
    private CacheManager cacheManager;

    private static final Logger logger = LoggerFactory.getLogger(ClearCacheTask.class);

    @Scheduled(fixedRateString = "${clear.all.cache.fixed.rate}")
    public void clearCache() {
        cacheManager.getCacheNames().parallelStream()
                .forEach(name -> Objects.requireNonNull(cacheManager.getCache(name)).clear());
        logger.info("All caches cleared");
    }
}

