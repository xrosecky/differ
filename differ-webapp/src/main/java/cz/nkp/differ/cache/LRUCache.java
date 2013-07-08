package cz.nkp.differ.cache;

import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * LRU (Least-Recently-Used) Cache that is capable of persistently storing a certain number of
 * objects, based on how frequently they are accessed. Thus, objects that are accessed most frequently
 * will remain in the cache, while objects that are accessed least frequently will be replaced as 
 * new objects are added.<br><br>
 * 
 * Objects added to the cache are added as {@link SoftReference}s to prevent any OutOfMemoryErrors from
 * occurring. This may be necessary in cases where cached objects might be very large or if the max size
 * of the cache is large.<br><br>
 * 
 * All relevant methods are synchronized for thread-safety. This class also tracks cache hit data. 
 * If no argument is given to the constructor, a default cache size is used. 
 * 
 * @author Thomas Truax
 */
public class LRUCache<K, V> {
	
	private Map<K, SoftReference<V>> cache;
	private int accessCnt = 0;
	private int hitCnt = 0;
	private int maxSize = 0;
	private static int DEFAULT_SIZE = 20;

	/**
	 * No-argument constructor. Creates a new cache with a default size of 20.
	 */
	public LRUCache() {
		this(DEFAULT_SIZE);
	}
	
	/**
	 * Constructor that permits setting a defined size for the cache. Throws an {@link IllegalArgumentException}
	 * if maxSize is less than or equal to zero.
	 * @param maxSize Maximum number of objects allowed in the cache
	 */
	public LRUCache(final int maxSize) {
		if (maxSize <= 0) {
			throw new IllegalArgumentException();
		}
		this.maxSize = maxSize;
		build();
	}
	
	/*
	 * Creates a new hash map object for the cache
	 */
	private void build() {
		cache = new LinkedHashMap<K, SoftReference<V>>(maxSize, 1.1f, true) {
			protected boolean removeEldestEntry(Map.Entry entry) {
			    return size() > maxSize;
			}
		};
	}
	
	/**
	 * Adds a new object to the cache. If the cache is full, the new object will replace the
	 * least-recently-used object in the cache.
	 * @param key - Key to map to the passed object
	 * @param obj - Object to cache
	 */
	public synchronized void add(final K key, final V obj) {
		cache.put(key, new SoftReference<V>(obj));
	}

	/**
	 * Removes an object from the cache, if it exists, and returns it.
	 * @param key - The key for the desired object
	 * @return the object that was removed; otherwise null if the key doesn't exist
	 */
	public synchronized V remove(final K key) {
		hitCheck(key);
		//must explicitly check for null here, otherwise a NullPointerException
		//could potentially be thrown when trying to get() SoftReference
		if (cache.containsKey(key)) {
			return cache.remove(key).get();
		}
		return null;
	}

	/**
	 * Retrieves an object from the cache, if it exists.
	 * @param key - The key for the desired object
	 * @return the object mapped to the passed key; otherwise null if the key doesn't exist
	 */
	public synchronized V get(K key) {
		hitCheck(key);
		//must explicitly check for null here, otherwise a NullPointerException
		//could potentially be thrown when trying to get() SoftReference
		if (cache.get(key) != null) {
			return cache.get(key).get();
		}
		return null;
	}

	/**
	 * Checks whether or not the cache contains the passed key. 
	 * @param key - The key to check
	 * @return true if the key exists in the map, else returns false
	 */
	public synchronized boolean isCached(K key) {
		hitCheck(key);
		return cache.containsKey(key);
	}

	/*
	 * Increments the access count for the cache, as well as the hit count if the key exists
	 */
	private void hitCheck(K key) {
		accessCnt++;
		if (cache.containsKey(key)) {
			hitCnt++;
		}
	}
	
	/**
	 * Resizes the maximum size of the cache. Throws an {@link IllegalArgumentException}
	 * if maxSize is less than or equal to zero.
	 * @param maxSize - The new size for the cache (must be greater than zero)
	 */
	public synchronized void resize(final int maxSize) {
		if (maxSize <= 0) {
			throw new IllegalArgumentException();
		}
		this.maxSize = maxSize;
		Map<K, SoftReference<V>> newCache = new LinkedHashMap<K, SoftReference<V>>(this.maxSize, 1.1f, true) {
			protected boolean removeEldestEntry(Map.Entry entry) {
			    return size() > maxSize;
			}
		};
		
		// *if new maxSize is smaller than current map size(), we must do some extra work 
		// since most-recently-used items are always at end of the list
		
		if (this.maxSize < cache.size()) {
			int i = cache.size() - this.maxSize;
			int j = 0;
			for (Map.Entry<K, SoftReference<V>> entry : cache.entrySet()) {
				j++;
				if (j >= i) {
					newCache.put(entry.getKey(), entry.getValue());
				}
			}
		} else {
			newCache.putAll(cache);
		}
		
		cache = newCache;
	}
	
	/**
	 * Returns the current size of the cache. To obtain the maximum size of the cache, use
	 * getMaxSize() instead.
	 * @return the number of objects in the cache
	 */
	public synchronized int getSize() {
		return cache.size();
	}
	
	/**
	 * Returns the maximum number of objects allowed in the cache.
	 * @return the current max size of the cache
	 */
	public synchronized int getMaxSize() {
		return maxSize;
	}
	
	/**
	 * Returns whether or not the cache is empty.
	 * @return true if cache size is equal to zero, else false
	 */
	public synchronized boolean isEmpty() {
		return cache.size() == 0;
	}
	
	/**
	 * Returns whether or not the cache is full.
	 * @return true if cache size is equal to the max size, else false
	 */
	public synchronized boolean isFull() {
		return cache.size() == maxSize;
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
