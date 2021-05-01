package com.g1t2.asgn3;

import androidx.core.util.Consumer;

import com.g1t2.asgn3.repo.Repo;

import java.util.NoSuchElementException;
import java.util.concurrent.ExecutorService;

/**
 * A store that can be chained with other stores to facilitate multi-level data fetching and writing
 * operations.
 *
 * Each store must be backed by a Repo instance to provide the data fetching and writing behaviour.
 *
 * @param <K> Primary key type variable.
 * @param <V> Entry value type variable.
 */
public class ChainableStore<K, V> {
    private Repo<K, V> repo;

    /**
     * The upper store generally refers to a data store with lower capacity but faster access time.
     */
    private ChainableStore<K, V> upper;

    /**
     * The lower store generally refers to a data store with higher capacity but slower access time.
     */
    private ChainableStore<K, V> lower;

    public ChainableStore(Repo<K, V> repo) {
        this(repo, null);
    }

    public ChainableStore(Repo<K, V> repo, ChainableStore<K, V> lower) {
        this.repo = repo;
        this.upper = null;
        this.lower = lower;
        if (lower != null) {
            lower.setUpper(this);
        }
    }

    private void setUpper(ChainableStore<K, V> upper) {
        this.upper = upper;
    }

    /**
     * Retrieve a data entry from the store asynchronously according to its primary key.
     *
     * If the entry does not exist within the current store, it is searched for in a lower store.
     * If a lower store contains the entry, it is cached in the current store.
     *
     * The repository check is done in a background thread, which is freed as soon as a result is
     * returned. This allows background threads to be reused for other purposes before entering the
     * lower store with a potentially long blocking time.
     *
     * For example, if a thread is searching for key "A" in a main memory cache but cannot find an
     * entry, it will prepare to search secondary storage. However, secondary storage has a long
     * blocking time. Instead of continuing to explore secondary storage with the same thread, we
     * allow other requests to be serviced first. A request for key "B" in main memory could be
     * serviced quickly before the thread is reassigned to the current task of checking secondary
     * storage, thereby reducing turnaround time.
     *
     * @param key Primary key of the data entry to find.
     * @param executorService The executor service to dispatch repo tasks to.
     * @param completion A handler that is called when the search is complete. If an item is found,
     *                   the handler is called with the item value. Else, it is called with null.
     */
    public final void findById(K key, ExecutorService executorService, Consumer<V> completion) {
        executorService.submit(() -> {
            // the thread will be freed up as soon as this store has checked the repo.
            try {
                V item = repo.get(key); // long blocking call.
                completion.accept(item);
            } catch (NoSuchElementException e) {
                if (lower == null) {
                    // no lower stores, therefore this item does not exist.
                    completion.accept(null);
                    return;
                }
                lower.findById(key, executorService, (V itemFromLower) -> {
                   if (itemFromLower != null) {
                       repo.save(key, itemFromLower);
                   }
                   completion.accept(itemFromLower);
                });
            }
        });
    }

    /**
     * Delete all items in this store, as well as all items in upper level stores.
     */
    public final void deleteAll() {
        repo.deleteAll();
        if (upper != null) {
            upper.deleteAll();
        }
    }
}
