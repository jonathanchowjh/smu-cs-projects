package com.g1t2.asgn3.repo;

import com.g1t2.asgn3.model.LRUArrayCache;
import com.g1t2.asgn3.model.LRUCache;
import com.g1t2.asgn3.model.ListItem;

import androidx.core.util.Consumer;

import java.util.NoSuchElementException;

/**
 * Repo implementation that retrieves images from a LRU cache in main memory.
 */
public class LRURepo implements Repo<String, ListItem> {
    private LRUCache<String, ListItem> cache;
    private final long limit = 5 * 1024 * 1024;

    /**
     * onCacheUpdate is called whenever the cache is updated.
     * This allows us to observe changes to the cache and perform any effects as required.
     */
    private Consumer<LRUCache<String, ListItem>> onCacheUpdate;

    /**
     * Constructor
     *
     * @param onCacheUpdate A consumer function to call whenever the cache is updated.
     */
    public LRURepo(Consumer<LRUCache<String, ListItem>> onCacheUpdate) {
        cache = new LRUArrayCache<>(limit);
        this.onCacheUpdate = onCacheUpdate;
    }

    /**
     * Retrieve an image and caption from cache.
     *
     * @param url The URL of the image.
     * @return The image and its caption.
     */
    @Override
    public ListItem get(String url) {
        ListItem result = cache.get(url);
        onCacheUpdate.accept(cache);
        if (result == null) {
            throw new NoSuchElementException(
                    String.format("Could not find file on disk for %s", url));
        }
        return result;
    }

    /**
     * Save an image to cache.
     *
     * @param url The URL of the image.
     * @param item ListItem representing caption and image data in bitmap.
     */
    @Override
    public void save(String url, ListItem item) {
        cache.put(url, item);
        onCacheUpdate.accept(cache);
    }

    /**
     * Deletes all images from cache
     */
    @Override
    public void deleteAll() {
        cache.clear();
        onCacheUpdate.accept(cache);
    }
}