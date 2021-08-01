package com.maxifier.checker.cache;

import gnu.trove.iterator.TIntObjectIterator;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

import static com.maxifier.checker.cache.CacheConstants.*;

public class DatasetCacheCreator {
    private static final Logger LOGGER = LoggerFactory.getLogger(DatasetCacheCreator.class);

    public void createCache(String datasetName, String pageCacheDirectoryName) throws IOException {
        File cacheDirectory = new File(pageCacheDirectoryName);
        File dataset = new File(datasetName);
        setupCacheDirectory(cacheDirectory);

        TIntObjectMap<PageCacheProcessor> pageCacheProcessors = new TIntObjectHashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(dataset))) {
            int lineCount = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                lineCount++;
                String[] fields = line.split(",");
                long uid = Long.parseLong(fields[0]);
                int page = Integer.parseInt(fields[1]);
                long timestamp = Long.parseLong(fields[2]);
                timestamp = timestamp - (timestamp % TIME_ACCURACY);

                PageCacheProcessor cacheProcessor;
                if (!pageCacheProcessors.containsKey(page)) {
                    cacheProcessor = new PageCacheProcessor(pageCacheDirectoryName, page);
                    pageCacheProcessors.put(page, cacheProcessor);
                } else {
                    cacheProcessor = pageCacheProcessors.get(page);
                }
                if (cacheProcessor.getLastSeenTimestamp(uid) < timestamp) {
                    cacheProcessor.writeRecord(uid, timestamp);
                }
                if (lineCount % 10_000_000 == 0) {
                    LOGGER.debug("{} millions rows", lineCount / 1_000_000);
                }
            }
        }

        DataOutputStream records = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(new File(cacheDirectory, RECORDS_NAME))));
        records.writeInt(pageCacheProcessors.size());
        TIntObjectIterator<PageCacheProcessor> pageOutputStreamsIterator = pageCacheProcessors.iterator();
        while (pageOutputStreamsIterator.hasNext()) {
            pageOutputStreamsIterator.advance();
            int page = pageOutputStreamsIterator.key();
            PageCacheProcessor pageCacheProcessor = pageOutputStreamsIterator.value();
            pageCacheProcessor.closeStream();
            records.writeInt(page);
            records.writeInt(pageCacheProcessor.getRecordsCount());
        }
        records.close();
    }

    private void setupCacheDirectory(File cacheDirectory) throws IOException {
        if (!cacheDirectory.exists()) {
            cacheDirectory.mkdirs();
        } else {
            FileUtils.cleanDirectory(cacheDirectory);
        }
        new File(cacheDirectory, PAGES_DIRECTORY).mkdir();
    }
}
