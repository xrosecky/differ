package cz.nkp.differ.cache;

import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * LRU (Least-Recently-Used) Cache that is capable of persistently storing a certain number of objects, based on how
 * frequently they are accessed. Thus, objects that are accessed most frequently will remain in the cache, while 
 * objects that are accessed least frequently will be replaced as new objects are added.<br><br>
 * 
 * Objects added to the cache are added as both hard (normal) references and {@link SoftReference}s. Since the 
 * Garbage Collector may decide to evict {@link SoftReference}s prematurely, hard references are also used to protect
 * the reliability of the cache. The most recently used items will always be moved to the hard cache, while the older
 * items will be moved to the soft cache. Additionally, no items will be put into the soft cache until the hard
 * cache has been filled. The ratio of hard cache items to soft cache items can be set via the
 * <b>LRUCache(int maxSize, float hardSoftRatio)</b> constructor and the <b>resize(int maxSize, float hardSoftRatio)</b>
 * method; this ratio must be between 0 and 1.0. The hard-soft ratio should be set to a relatively low value, especially
 * if the max cache size is large, else there is an increased risk of triggering an {@link OutOfMemoryError}.<br><br>
 * 
 * Example: If maxSize=100 and hardSoftRatio=0.2, the hard cache will store 20 items and the soft cache will store the 
 * other 80 items.<br><br>
 * 
 * All relevant methods are synchronized for thread-safety. This class also tracks cache hit data. If no argument is 
 * given to the constructor, a default cache size and hard-soft ratio are used. 
 * 
 * @author Thomas Truax
 */
public class LRUCache<K, V> {

    private Map<K, SoftReference<V>> softcache;
    private Map<K, V> hardcache;
    private float hardSoftRatio;
    private int accessCnt;
    private int hitCnt;
    private int maxSize;
    private int hardSize;
    private int softSize;
    private static int DEFAULT_SIZE = 50;
    private static float DEFAULT_HARDSOFT_RATIO = 0.2f;

    /**
     * No-argument constructor. Creates a new cache with a default size of 50 and default hard-soft ratio of 0.2.
     */
    public LRUCache() {
        this(DEFAULT_SIZE);
    }

    /**
     * Constructor that permits setting a defined maximum size for the cache while leaving the hard-soft ratio at the default
     * 0.2. Throws an {@link IllegalArgumentException} if maxSize is less than or equal to zero.
     * @param maxSize Maximum number of objects allowed in the cache
     */
    public LRUCache(final int maxSize) {
        this(maxSize, DEFAULT_HARDSOFT_RATIO);
    }

    /**
     * Constructor that permits setting a defined maximum size for the cache and its hard-soft ratio.
     * @param maxSize Maximum number of objects allowed in the cache; throws an {@link IllegalArgumentException}
     * if this value is less than 1
     * @param hardSoftRatio Ratio of hard cache items to soft cache items, represented as a float value; so 
     * if a value of 0.25 is passed, 1/4 of the maxSize will be stored in the hard cache; throws an
     * {@link IllegalArgumentException} if this value of less than 0 or greater than 1
     */
    public LRUCache(final int maxSize, final float hardSoftRatio) {
        if (maxSize <= 0 || hardSoftRatio < 0 || hardSoftRatio > 1) {
            throw new IllegalArgumentException();
        }
        this.maxSize = maxSize;
        this.hardSoftRatio = hardSoftRatio;
        hardSize = (int) (maxSize * hardSoftRatio);
        softSize = maxSize - hardSize;
        build();
    }

    /*
     * Creates a new hash map object for the cache
     */
    private void build() {
        hardcache = new LinkedHashMap<K, V>(hardSize, 1.0f, true) {
                    @Override
                    protected boolean removeEldestEntry(Map.Entry<K, V> entry) {
                        if (size() > hardSize) {
                            if (softSize > 0) {
                                //if hard cache is full, push eldest entry to the soft cache
                                softcache.put(entry.getKey(), new SoftReference<V>(entry.getValue()));
                            }
                            return true;
                        }
                        return false;
                    }
        };
        softcache = new LinkedHashMap<K, SoftReference<V>>(softSize, 1.0f, true) {
                    @Override
                    protected boolean removeEldestEntry(Map.Entry<K, SoftReference<V>> entry) {
                        if (size() > softSize) {
                            return true;
                        }
                        return false;
                    }
        };
    }

    /**
     * Adds a new object to the cache. If the cache is full, the new object will replace the
     * least-recently-used object in the cache.
     * @param key - Key to map to the passed object
     * @param obj - Object to cache
     */
    public synchronized void put(final K key, final V obj) {
        hardcache.put(key, obj);
    }

    /**
     * Removes an object from the cache, if it exists, and returns it.
     * @param key - The key for the desired object
     * @return the object that was removed; otherwise null if the key doesn't exist
     */
    public synchronized V remove(final K key) {
        if (hitCheck(key)) {
            if (hardcache.containsKey(key)) {
                return hardcache.remove(key);
            }
            //must explicitly check for null here, otherwise a NullPointerException
            //could potentially be thrown when trying to get() SoftReference
            if (softcache.get(key) != null) {
                return softcache.remove(key).get();
            }
        }
        return null;
    }

    /**
     * Retrieves an object from the cache, if it exists.
     * @param key - The key for the desired object
     * @return the object mapped to the passed key; otherwise null if the key doesn't exist
     */
    public synchronized V get(final K key) {
        if (hitCheck(key)) {
            if (hardcache.containsKey(key)) {
                return hardcache.get(key);
            }
            //must explicitly check for null here, otherwise a NullPointerException
            //could potentially be thrown when trying to get() SoftReference
            if (softcache.get(key) != null) {
                return softcache.get(key).get();
            }
        }
        return null;
    }

    /**
     * Checks whether or not the cache contains the passed key. 
     * @param key - The key to check
     * @return true if the key exists in the map, else returns false
     */
    public synchronized boolean isCached(final K key) {
        return hitCheck(key);
    }

    /*
     * Increments the access count for the cache, as well as the hit count if the key exists.
     * If a hit was successful and the item is in the soft cache, it is moved to the hard cache.
     */
    private boolean hitCheck(final K key) {
    accessCnt++;
    if (hardcache.containsKey(key)) {
        hitCnt++;
        return true;
    } else if (softcache.containsKey(key)) {
            hitCnt++;
            if (hardSize > 0) {
                    V harden = softcache.remove(key).get();
                    hardcache.put(key, harden);
            }
            return true;
    }
    return false;
    }

    /**
     * Resizes the maximum size of the cache.
     * @param maxSize Maximum number of objects allowed in the cache; throws an {@link IllegalArgumentException}
     * if this value is less than 1
     */
    public synchronized void resize(final int maxSize) {
        resize(maxSize, hardSoftRatio);
    }

    /**
     * Resizes the maximum size of the cache and adjusts its hard-soft ratio.
     * @param maxSize Maximum number of objects allowed in the cache; throws an {@link IllegalArgumentException}
     * if this value is less than 1
     * @param hardSoftRatio Ratio of hard cache items to soft cache items, represented as a float value; so 
     * if a value of 0.25 is passed, 1/4 of the maxSize will be stored in the hard cache; throws an
     * {@link IllegalArgumentException} if this value of less than 0 or greater than 1
     */
    public synchronized void resize(final int maxSize, final float hardSoftRatio) {
    if (maxSize <= 0 || hardSoftRatio < 0 || hardSoftRatio > 1) {
        throw new IllegalArgumentException();
    }
    this.maxSize = maxSize;
    this.hardSoftRatio = hardSoftRatio;
    hardSize = (int) (maxSize * hardSoftRatio);
    softSize = maxSize - hardSize;
    Map<K, V> newHardCache = new LinkedHashMap<K, V>(hardSize, 1.0f, true) {
                @Override
                protected boolean removeEldestEntry(Map.Entry<K,V> entry) {
                    if (size() > hardSize) {
                            if (softSize > 0) {
                                //if hard cache is full, push eldest entry to the soft cache
                                softcache.put(entry.getKey(), new SoftReference<V>(entry.getValue()));
                            }
                        return true;
                    }
                    return false;
                }
    };
    Map<K, SoftReference<V>> newSoftCache = new LinkedHashMap<K, SoftReference<V>>(softSize, 1.0f, true) {
                @Override
                protected boolean removeEldestEntry(Map.Entry<K, SoftReference<V>> entry) {
                    return size() > softSize;
                }
    };

    // *if new hardSize/softSize is smaller than current hard/soft size(), we must
    //do some extra work since most-recently-used items are always at end of the list

    if (hardSize < hardcache.size()) {
        int i = hardcache.size() - hardSize;
        int j = 0;
        for (Map.Entry<K, V> entry : hardcache.entrySet()) {
            j++;
            if (j >= i) {
                newHardCache.put(entry.getKey(), entry.getValue());
            }
        }
    } else {
        newHardCache.putAll(hardcache);
    }

    hardcache = newHardCache;

    if (softSize < softcache.size()) {
        int i = softcache.size() - softSize;
        int j = 0;
        for (Map.Entry<K, SoftReference<V>> entry : softcache.entrySet()) {
            j++;
            if (j >= i) {
                newSoftCache.put(entry.getKey(), entry.getValue());
            }
        }
    } else {
        newSoftCache.putAll(softcache);
    }

        softcache = newSoftCache;
    }

    /**
     * Returns the current size of the cache. To obtain the maximum size of the cache, use
     * getMaxSize() instead.
     * @return the number of objects in the cache
     */
    public synchronized int getSize() {
        return hardcache.size() + softcache.size();
    }

    /**
     * Returns the maximum number of objects allowed in the cache.
     * @return the current max size of the cache
     */
    public synchronized int getMaxSize() {
        return maxSize;
    }

    /**
     * Returns the value of the hard-soft ratio.
     * @return the hard-soft ratio represented as a float
     */
    public synchronized float getHardSoftRatio() {
        return hardSoftRatio;
    }

    /**
     * Returns whether or not the cache is empty.
     * @return true if cache size is equal to zero, else false
     */
    public synchronized boolean isEmpty() {
        return hardcache.isEmpty() && softcache.isEmpty();
    }

    /**
     * Returns whether or not the cache is full.
     * @return true if cache size is equal to the max size, else false
     */
    public synchronized boolean isFull() {
        return getSize() == maxSize;
    }

    /**
     * Clears the cache, removing all objects from it. To reset the hit count as well, use the reset()
     * method instead.
     */
    public synchronized void clear() {
        build();
    }

    /**
     * Clears the cache and also resets the hit-count and access-count. This method essentially resets
     * the cache to its original state. 
     */
    public synchronized void reset() {
        build();
        accessCnt = 0;
        hitCnt = 0;
    }

    /**
     * Returns the number of times the cache was accessed, regardless of whether or not the access
     * resulted in a hit or a miss.<br><br>Methods that increment the access count include remove(),
     * get(), and isCached().
     * @return the total number of accesses to the cache
     */
    public synchronized int getAccessCount() {
        return accessCnt;
    }

    /**
     * Returns the number of times an access to the cache resulted in a hit (The object that was queried
     * existed in the cache.) 
     * @return the total number of successful hits to the cache
     */
    public synchronized int getHitCount() {
        return hitCnt;
    }

    /**
     * Returns the number of times an access to the cache resulted in a miss. (The object that was queried
     * did not exist in the cache.)
     * @return the total number of misses to the cache
     */
    public synchronized int getMissCount() {
        return accessCnt - hitCnt;
    }
}
