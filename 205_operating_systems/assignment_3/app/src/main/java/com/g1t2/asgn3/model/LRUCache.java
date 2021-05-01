package com.g1t2.asgn3.model;

/**
 * Interface for a cache using LRU eviction policy.
 */
public interface LRUCache<K, V> {
    V get(K key);
    V put(K key, V value);
    V remove(K key);
    void clear();
    boolean contains(K key);
    long getMemSize();
    long getMemLimit();
}
