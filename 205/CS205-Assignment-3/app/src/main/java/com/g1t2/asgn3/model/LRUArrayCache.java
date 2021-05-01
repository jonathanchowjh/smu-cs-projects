package com.g1t2.asgn3.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Array-based implementation of a concurrent-safe LRU cache that comprises a hash table and a
 * priority queue.
 *
 * Each map entry contains an offset that enables us to find the corresponding priority queue item,
 * and each PQ item holds the key of the corresponding map entry. This allows us to update both data
 * structures with minimal cost, thereby providing O(1) insertion, O(logn) update, and O(logn)
 * deletion.
 *
 * For more documentation on the design of the LRU cache, refer to the project documentation
 * submission.
 *
 * @param <K> Key type variable.
 * @param <V> Value type variable.
 */
public class LRUArrayCache<K, V extends ByteCountable> implements LRUCache<K, V> {
    class MapItem {
        V value;
        int pqOffset;

        MapItem(V value, int pqOffset) {
            this.value = value;
            this.pqOffset = pqOffset;
        }

        @Override
        public String toString() {
            return "MapItem{" +
                    "value=" + value +
                    ", pqOffset=" + pqOffset +
                    '}';
        }
    }

    class PQItem {
        long pqLabel;
        K mapKey;

        PQItem(long pqLabel, K mapKey) {
            this.pqLabel = pqLabel;
            this.mapKey = mapKey;
        }

        @Override
        public String toString() {
            return "PQItem{" +
                    "pqLabel=" + pqLabel +
                    ", mapKey=" + mapKey +
                    '}';
        }
    }

    List<PQItem> pq = new ArrayList<>();
    Map<K, MapItem> map = new HashMap<>();

    private final long memLimit;

    public LRUArrayCache(long memLimit) {
        this.memLimit = memLimit;
    }

    private volatile long memSize = 0;

    @Override
    public synchronized V get(K key) {
        MapItem mapItem = map.get(key);
        if (mapItem == null) return null;
        // update latest touched in priority queue.
        pq.get(mapItem.pqOffset).pqLabel = counter++;
        pqDownheapFromOffset(mapItem.pqOffset);
        return mapItem.value;
    }

    @Override
    public synchronized V put(K key, V value) {
        // force update lru in pq if already exists.
        if (map.containsKey(key)) {
            return putIfPresent(key, value);
        }
        putIfAbsent(key, value);
        return null;
    }

    private V putIfPresent(K key, V newValue) {
        MapItem mapItem = map.get(key);

        V oldValue = mapItem.value;
        // clear space for replacement
        while (memSize - oldValue.getByteCount() + newValue.getByteCount() > memLimit) {
            removeLeastRecentlyUsed();
        }
        // replace value
        mapItem.value = newValue;
        // update memSize
        memSize = memSize - oldValue.getByteCount() + newValue.getByteCount();
        // update latest touched in priority queue.
        pq.get(mapItem.pqOffset).pqLabel = counter++;
        pqDownheapFromOffset(mapItem.pqOffset);
        return oldValue;
    }

    private void removeLeastRecentlyUsed() {
        if (pq.size() == 0) return;
        PQItem pqItem = pqRemoveAtOffset(0);
        if (pqItem == null) return;
        MapItem removed = map.remove(pqItem.mapKey);
        // update memSize
        memSize -= removed == null || removed.value == null ? 0 : removed.value.getByteCount();
    };

    private void putIfAbsent(K key, V value) {
        // clear space for replacement
        while (memSize + value.getByteCount() > memLimit) {
            removeLeastRecentlyUsed();
        }
        // update memSize
        memSize += value.getByteCount();
        insert(key, value);
    }

    /**
     * Counter increments at every "use" of the cache, thus providing every "use" with a unique id.
     */
    private long counter = 0;

    private void insert(K key, V value) {
        // max pq label, no need upheap.
        PQItem pqItem = new PQItem(counter++, key);
        // pq size is pq offset for new max item.
        MapItem mapItem = new MapItem(value, pq.size());
        pq.add(pqItem);
        map.put(key, mapItem);
    }

    @Override
    public synchronized V remove(K key) {
        MapItem mapItem = map.remove(key);
        // update memSize
        memSize -= mapItem.value.getByteCount();
        pqRemoveAtOffset(mapItem.pqOffset);
        return mapItem.value;
    }

    private PQItem pqRemoveAtOffset(int offset) {
        if (pq.size() == 0) return null;
        // move right-most pq item to offset position
        PQItem toRemove = pq.get(offset);
        PQItem rightMost = pq.remove(pq.size() - 1);
        if (pq.size() > 0) {
            pq.set(offset, rightMost);
            // update hash table.
            map.get(rightMost.mapKey).pqOffset = offset;
            pqDownheapFromOffset(offset);
        } else {
            pq.add(rightMost);
            map.get(rightMost.mapKey).pqOffset = 0;
        }
        return toRemove;
    }

    private void pqDownheapFromOffset(int offset) {
        while (offset < pq.size()) {
            PQItem curr = pq.get(offset);
            int leftOffset = pqLeft(offset);
            if (leftOffset >= pq.size()) {
                // no more children.
                break;
            }
            PQItem left = pq.get(leftOffset);

            int rightOffset = pqRight(offset);
            if (rightOffset >= pq.size()) {
                // only left child.
                if (left.pqLabel < curr.pqLabel) {
                    pq.set(offset, left); // bring left pq item up.
                    map.get(left.mapKey).pqOffset = offset;
                    pq.set(leftOffset, curr); // drop curr item down left.
                    map.get(curr.mapKey).pqOffset = leftOffset;
                }
                break;
            }
            PQItem right = pq.get(rightOffset);

            if (left.pqLabel < curr.pqLabel || right.pqLabel < curr.pqLabel) {
                if (left.pqLabel < right.pqLabel) {
                    pq.set(offset, left); // bring left pq item up.
                    map.get(left.mapKey).pqOffset = offset;
                    pq.set(leftOffset, curr); // drop curr item down left.
                    map.get(curr.mapKey).pqOffset = leftOffset;
                    offset = leftOffset;
                } else {
                    pq.set(offset, right); // bring right pq item up.
                    map.get(right.mapKey).pqOffset = offset;
                    pq.set(rightOffset, curr); // drop curr item down right.
                    map.get(curr.mapKey).pqOffset = rightOffset;
                    offset = rightOffset;
                }
            } else {
                // no more smaller children to swap with.
                break;
            }
        }
    }

    /**
     * Find the index of an item's left child.
     * @param offset The index of the item to search from.
     * @return The index of the item's left child.
     */
    private static int pqLeft(int offset) {
        return (offset * 2) + 1;
    }

    /**
     * Find the index of an item's right child.
     * @param offset The index of the item to search from.
     * @return The index of the item's right child.
     */
    private static int pqRight(int offset) {
        return (offset + 1) * 2;
    }

    @Override
    public synchronized void clear() {
        memSize = 0;
        map.clear();
        pq.clear();
    }

    @Override
    public synchronized boolean contains(K key) {
        return map.containsKey(key);
    }

    @Override
    public long getMemSize() {
        return memSize;
    }

    @Override
    public long getMemLimit() {
        return memLimit;
    }

    @Override
    public String toString() {
        return "LRUArrayCache{" +
                "pq=" + pq +
                ", map=" + map +
                ", memSize=" + memSize +
                ", memLimit=" + memLimit +
                '}';
    }
}
