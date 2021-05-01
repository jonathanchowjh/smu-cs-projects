package com.g1t2.asgn3.repo;

/**
 * A representation of a data retrieval strategy from an abstract storage device.
 *
 * Behaviours defined in Repo are generally expected from any storage medium. However, if a storage
 * device does not support write operations, then the save(K, V) and deleteAll() methods can be left
 * with a empty body.
 *
 * @param <K> Primary key type variable.
 * @param <V> Entry value type variable.
 */
public interface Repo<K, V> {
    V get(K key);

    void save(K key, V value);

    void deleteAll();
}
