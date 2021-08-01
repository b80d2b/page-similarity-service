package com.maxifier.checker.cache;

import gnu.trove.map.TIntIntMap;
import gnu.trove.set.TLongSet;
import gnu.trove.set.hash.TLongHashSet;

import static com.maxifier.checker.cache.CacheConstants.TIME_ACCURACY;

public class DatasetCache {
    private TIntIntMap pagesId;
    private long[][] records;

    public DatasetCache(TIntIntMap pagesId, long[][] records) {
        this.pagesId = pagesId;
        this.records = records;
    }

    public boolean containsPage(int page) {
        return pagesId.containsKey(page);
    }

    public TLongSet getUids(int page, long from, long to) {
        TLongSet uids = new TLongHashSet();
        if (!pagesId.containsKey(page) || from >= to) return uids;
        from = from - (from % TIME_ACCURACY);
        to = to + (TIME_ACCURACY - (from % TIME_ACCURACY));
        long[] pageRecords = records[pagesId.get(page)];
        for (int i = 0; i < pageRecords.length; i += 2) {
            long timestamp = pageRecords[i];
            if (timestamp < from) continue;
            if (timestamp >= to) break;
            uids.add(pageRecords[i + 1]);
        }
        return uids;
    }

    public TIntIntMap getPagesId() {
        return pagesId;
    }

    public long[][] getRecords() {
        return records;
    }
}
