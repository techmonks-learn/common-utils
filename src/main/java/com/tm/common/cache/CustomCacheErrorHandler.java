package com.tm.common.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.CacheErrorHandler;

import java.util.Objects;

public class CustomCacheErrorHandler implements CacheErrorHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomCacheErrorHandler.class);

    @Override
    public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
        String logMessage = new StringBuilder().append(exception.getMessage())
                .append(Objects.nonNull(key) ? key.toString() : "").toString();
        LOGGER.error(logMessage, exception);
        LOGGER.info("Cache provider is failing while retrieving the cached items.");
    }

    @Override
    public void handleCachePutError(RuntimeException exception, Cache cache, Object key, Object value) {
        String logMessage = new StringBuilder().append(exception.getMessage())
                .append(Objects.nonNull(key) ? key.toString() : "").toString();
        LOGGER.error(logMessage, exception);
        LOGGER.info("Cache provider is failing while adding the item to cache.");
    }

    @Override
    public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
        String errorMessage = new StringBuilder().append(exception.getMessage())
                .append(Objects.nonNull(key) ? key.toString() : "").toString();
        LOGGER.error(errorMessage, exception);
        LOGGER.info("Cache provider is failing while evicting the cache.");
    }

    @Override
    public void handleCacheClearError(RuntimeException exception, Cache cache) {
        LOGGER.error(exception.getMessage(), exception);
        LOGGER.info("Cache provider is failing while clearing the cache.");
    }
}
