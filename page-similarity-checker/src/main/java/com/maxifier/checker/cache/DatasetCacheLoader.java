package com.maxifier.checker.cache;

import gnu.trove.map.TIntIntMap;
import gnu.trove.map.hash.TIntIntHashMap;

import java.io.*;

import static com.maxifier.checker.cache.CacheConstants.PAGES_DIRECTORY;
import static com.maxifier.checker.cache.CacheConstants.RECORDS_NAME;

public class DatasetCacheLoader {
    public DatasetCache loadCache(String cacheDirectoryName) throws IOException {
        File cacheDirectory = new File(cacheDirectoryName);
        File pagesDirectory = new File(cacheDirectory, PAGES_DIRECTORY);
        File recordsFile = new File(cacheDirectory, RECORDS_NAME);
        TIntIntMap pagesId;
        long[][] records;
        try (DataInputStream recordsInputStream = new DataInputStream(new BufferedInputStream(new FileInputStream(recordsFile)))) {
            int pagesCount = recordsInputStream.readInt();
            pagesId = new TIntIntHashMap(pagesCount, 1.0F);
            records = new long[pagesCount][];
            for (int pageId = 0; pageId < pagesCount; pageId++) {
                int page = recordsInputStream.readInt();
                int recordsCount = recordsInputStream.readInt();
                pagesId.put(page, pageId);
                long[] pageRecords = new long[recordsCount * 2];
                try (DataInputStream pageInputStream = new DataInputStream(new BufferedInputStream(new FileInputStream(new File(pagesDirectory, Integer.toString(page)))))) {
                    for (int recordNumber = 0; recordNumber < recordsCount; recordNumber++) {
                        pageRecords[2*recordNumber] = pageInputStream.readLong();
                        pageRecords[2*recordNumber + 1] = pageInputStream.readLong();
                    }
                }
                records[pageId] = pageRecords;
            }
        }
        return new DatasetCache(pagesId, records);
    }
}
