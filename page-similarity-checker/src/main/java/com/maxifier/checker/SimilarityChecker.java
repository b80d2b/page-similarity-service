package com.maxifier.checker;

import com.maxifier.checker.cache.DatasetCache;
import gnu.trove.iterator.TIntIntIterator;
import gnu.trove.iterator.TLongIterator;
import gnu.trove.set.TLongSet;

import java.util.ArrayList;
import java.util.List;

public class SimilarityChecker {
    private final DatasetCache datasetCache;

    public SimilarityChecker(DatasetCache datasetCache) {
        this.datasetCache = datasetCache;
    }

    public double getSimilarity(int page1, int page2, long from, long to) {
        if (!datasetCache.containsPage(page1) || !datasetCache.containsPage(page2)) return 0;
        if (page1 == page2) return 1;
        TLongSet uids1 = datasetCache.getUids(page1, from, to);
        TLongSet uids2 = datasetCache.getUids(page2, from, to);
        if (uids1.isEmpty() || uids2.isEmpty()) return 0;
        int similarCount = 0;
        TLongIterator iterator = uids1.iterator();
        while (iterator.hasNext()) {
            long uid1 = iterator.next();
            if (uids2.contains(uid1)) similarCount++;
        }
        return ((double) similarCount) / (uids1.size() + uids2.size() - similarCount);
    }

    public List<String> getPages() {
        List<String> pages = new ArrayList<>();
        if (datasetCache == null) return pages;
        TIntIntIterator iterator = datasetCache.getPagesId().iterator();
        while (iterator.hasNext()) {
            iterator.advance();
            pages.add(Integer.toString(iterator.key()));
        }
        return pages;
    }
}
