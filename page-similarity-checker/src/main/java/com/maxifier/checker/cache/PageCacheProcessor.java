package com.maxifier.checker.cache;

import gnu.trove.map.TLongLongMap;
import gnu.trove.map.hash.TLongLongHashMap;

import java.io.*;

import static com.maxifier.checker.cache.CacheConstants.PAGES_DIRECTORY;

public class PageCacheProcessor {
    private DataOutputStream outputStream;
    private TLongLongMap uidLastSeenTimestamp;
    private int recordsCount = 0;

    public PageCacheProcessor(String pageCacheDirectoryName, int page) throws FileNotFoundException {
        this.outputStream = new DataOutputStream(
                new BufferedOutputStream(new FileOutputStream(pageCacheDirectoryName + "/" + PAGES_DIRECTORY + "/" + page), 256));
        this.uidLastSeenTimestamp = new TLongLongHashMap();
    }

    public long getLastSeenTimestamp(long uid) {
        return uidLastSeenTimestamp.get(uid);
    }

    public void writeRecord(long uid, long timestamp) throws IOException {
        uidLastSeenTimestamp.put(uid, timestamp);
        outputStream.writeLong(timestamp);
        outputStream.writeLong(uid);
        recordsCount++;
    }

    public void closeStream() throws IOException {
        outputStream.close();
    }

    public int getRecordsCount() {
        return recordsCount;
    }
}
