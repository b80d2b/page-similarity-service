package com.maxifier.service;

import com.maxifier.checker.SimilarityChecker;
import com.maxifier.checker.SimilarityCheckerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequestExecutor {
    private final static Logger LOGGER = LoggerFactory.getLogger(RequestExecutor.class);
    private SimilarityChecker checker = SimilarityCheckerFactory.createSimilarityChecker();

    public double getSimilarity(int page1, int page2, long from, long to) {
        long start = System.currentTimeMillis();
        double similarity = checker.getSimilarity(page1, page2, from, to);
        LOGGER.debug("Request time: {} millis", System.currentTimeMillis() - start);
        return similarity;
    }

    public List<String> getPages() {
        return checker.getPages();
    }
}
