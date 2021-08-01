package com.maxifier.checker;

import com.maxifier.checker.cache.DatasetCache;
import com.maxifier.checker.cache.DatasetCacheCreator;
import com.maxifier.checker.cache.DatasetCacheLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

import static com.maxifier.checker.cache.CacheConstants.*;

public class SimilarityCheckerFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimilarityCheckerFactory.class);

    public static SimilarityChecker createSimilarityChecker() {
        DatasetCache datasetCache;
        try {
            if (!cacheExists()) {
                LOGGER.info("Caching dataset");
                DatasetCacheCreator datasetCacheCreator = new DatasetCacheCreator();
                datasetCacheCreator.createCache(DATA, CACHE_DIRECTORY);
            }
            LOGGER.info("Loading cache");
            DatasetCacheLoader datasetCacheLoader = new DatasetCacheLoader();
            datasetCache = datasetCacheLoader.loadCache(CACHE_DIRECTORY);
        } catch (IOException e) {
            LOGGER.error("Cannot create cache");
            datasetCache = null;
        }

        return new SimilarityChecker(datasetCache);
    }

    private static boolean cacheExists() {
        return new File(CACHE_DIRECTORY + "/" + RECORDS_NAME).exists();
    }
}
