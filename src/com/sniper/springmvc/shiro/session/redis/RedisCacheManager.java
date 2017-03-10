package com.sniper.springmvc.shiro.session.redis;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;

public class RedisCacheManager implements CacheManager {

	// fast lookup by name map
	private final ConcurrentMap<String, Cache> caches = new ConcurrentHashMap<String, Cache>();

	@Override
	public <K, V> Cache<K, V> getCache(String name) throws CacheException {

		Cache c = caches.get(name);
		if (c == null) {
			// create a new cache instance
			c = new RedisCache<K, V>();
			// add it to the cache collection
			caches.put(name, c);
		}
		return c;
	}
}