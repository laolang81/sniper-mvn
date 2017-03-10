package com.sniper.springmvc.utils;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.pool2.impl.BaseObjectPoolConfig;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

/**
 * 本程序主要使用非切片连接池
 * 
 * @author suzhen
 * 
 */
public class RedisUtil {

	private final static String redisConfigFile = "properties/redis.properties";
	private static Map<String, Object> redisConfig = new HashMap<>();
	// 非切片连接池。适合单机读取
	private static JedisPool jedisPool;
	private static boolean reloadConfig = false;
	// 切片连接池,适用于添加，这是redis根据算法自己添加
	private static ShardedJedisPool shardedPool;
	// 集群
	private static JedisCluster cluster;
	private static List<JedisShardInfo> hosts = new ArrayList<>();

	/**
	 * 单例模式，急切模式，就是一开始就实例化 volatile 告诉编辑器这是多线程
	 */
	private volatile static RedisUtil redis = null;

	private RedisUtil() {
	}

	/**
	 * 返回所有的链接
	 * 
	 * @return
	 * @throws FileNotFoundException
	 */
	private static void initHost() {
		if (redisConfig.size() == 0 || reloadConfig == true) {
			InputStream in = RedisUtil.class.getClassLoader().getResourceAsStream(redisConfigFile);
			PropertiesUtil pu = null;
			pu = new PropertiesUtil(in);
			Map<String, String> map = pu.getValues();

			for (Map.Entry<String, String> entry : map.entrySet()) {
				if (entry.getKey().startsWith("host")) {
					String host = entry.getValue();
					String[] hosts = host.split(":");
					JedisShardInfo info = new JedisShardInfo(hosts[0], hosts[1]);
					if (hosts.length == 3) {
						info.setPassword(hosts[2]);
					}
					RedisUtil.hosts.add(info);
				} else {
					RedisUtil.redisConfig.put(entry.getKey(), entry.getValue());
				}
			}
		}
	}

	/**
	 * 
	 * 
	 * @return
	 */
	public static RedisUtil getInstance() {
		if (redis == null) {
			redis = new RedisUtil();
			// 初始化
			initHost();
		}
		return redis;
	}

	/**
	 * 非切片负责读取数据 连一台Redis
	 * 
	 * @return
	 */
	public JedisPool getJedisPool() {
		// 双重枷锁.是最合适的单利模式优化方案
		if (jedisPool == null) {
			getInstance().initJedisPool();
		}
		return jedisPool;
	}

	/**
	 * 切片负责写入数据，连Redis集群，通过一致性哈希算法决定把数据存到哪台上，算是一种客户端负载均衡，
	 * 
	 * @return
	 */
	public ShardedJedisPool getShardedPool() {
		if (shardedPool == null) {
			getInstance().initShardePool();
		}
		return shardedPool;
	}

	public static void setReloadConfig(boolean reloadConfig) {
		RedisUtil.reloadConfig = reloadConfig;
	}

	public JedisCluster getCluster() {
		if (cluster == null) {
			Set<HostAndPort> andPorts = new HashSet<>();
			for (JedisShardInfo info : hosts) {
				HostAndPort nodes = new HostAndPort(info.getHost(), info.getPort());
				andPorts.add(nodes);
			}
			cluster = new JedisCluster(andPorts);
		}
		return cluster;
	}

	/**
	 * 非切片连接池初始化
	 */
	private void initJedisPool() {
		if (null == jedisPool) {
			initHost();
			JedisPoolConfig config = new JedisPoolConfig();
			config.setMaxIdle(5000);
			config.setMaxWaitMillis(10000);
			config.setTestOnBorrow(true);
			config.setTestOnReturn(true);
			if (hosts.size() > 0) {
				jedisPool = new JedisPool(config, hosts.get(0).getHost());
			}
		}
	}

	/**
	 * 获取切片连接池初始化
	 * 
	 * @return
	 */
	private void initShardePool() {
		if (null == shardedPool) {
			// 池基本配置
			initHost();
			JedisPoolConfig config = new JedisPoolConfig();
			// 如果为true，表示有一个idle object evitor线程对idle object进行扫描
			config.setTestWhileIdle(true);
			// 表示一个对象至少停留在idle状态的最短时间
			config.setMinEvictableIdleTimeMillis(6000);
			// 表示idle object evitor两次扫描之间要sleep的毫秒数；
			config.setTimeBetweenEvictionRunsMillis(30000);
			// 表示idle object evitor每次扫描的最多的对象数；
			config.setNumTestsPerEvictionRun(-1);
			// 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。
			config.setMaxIdle(-1);
			// 表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；
			config.setMaxWaitMillis(5);
			// 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
			config.setTestOnBorrow(true);
			config.setTestOnReturn(true);
			config.setBlockWhenExhausted(BaseObjectPoolConfig.DEFAULT_BLOCK_WHEN_EXHAUSTED);
			shardedPool = new ShardedJedisPool(config, hosts);
		}
	}

	public static List<JedisShardInfo> getHosts() {
		return hosts;
	}

	/**
	 * 获取缓存前缀
	 * 
	 * @param key
	 * @return
	 */
	public String getKeyName(String key) {
		return redisConfig.get("prefix") + key;
	}

	/**
	 * 返回
	 * 
	 * @param object
	 * @param method
	 * @return
	 */
	public String getKeyName(Class<?> object, String method) {
		return redisConfig.get("prefix") + object.getName() + ":" + method;
	}

	public void close(Jedis jedis) {
		if (jedis != null) {
			jedis.close();
		}
	}

	public void close(ShardedJedis jedis) {
		if (jedis != null) {
			jedis.close();
		}
	}

	/**
	 * 获取数组的前缀
	 * 
	 * @param key
	 * @return
	 */
	public String[] getKeyName(String[] key) {

		String[] newName = new String[key.length];
		for (int i = 0; i < key.length; i++) {
			newName[i] = redisConfig.get("prefix") + key[i];
		}
		return newName;
	}

	/**
	 * get value from redis
	 * 
	 * @param key
	 * @return
	 */
	public byte[] get(byte[] key) {
		byte[] value = null;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			value = jedis.get(key);
		} finally {
			close(jedis);
		}
		return value;
	}

	/**
	 * set
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public byte[] set(byte[] key, byte[] value, long milliseconds) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String result = jedis.set(key, value);
			if (milliseconds != 0 && result.equals("OK")) {
				jedis.pexpire(key, milliseconds);
			}
		} finally {
			close(jedis);
		}
		return value;
	}

	/**
	 * del
	 * 
	 * @param key
	 */
	public void del(byte[] key) {
		Jedis jedis = null;

		try {
			jedis = jedisPool.getResource();
			jedis.del(key);
		} finally {
			close(jedis);
		}
	}

	/**
	 * 
	 * 
	 * @param key
	 */
	public Set<byte[]> keys(byte[] key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.keys(key);
		} finally {
			close(jedis);
		}
	}

	public Set<String> keys(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.keys(key);
		} finally {
			close(jedis);
		}
	}

	public boolean exists(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.exists(key);
		} finally {
			close(jedis);
		}
	}

	public boolean exists(byte[] key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.exists(key);
		} finally {
			close(jedis);
		}
	}

	public String get(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			if (this.exists(key)) {
				return jedis.get(key);
			}
			return null;
		} finally {
			close(jedis);
		}
	}

	public void set(String key, String value) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.set(key, value);
		} finally {
			close(jedis);
		}
	}

	public void set(String key, String value, int seconds) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.set(key, value);
			jedis.expire(key, seconds);
		} finally {
			close(jedis);
		}
	}

	public void set(String key, String value, long time) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.set(key, value);
			jedis.pexpire(key, time);
		} finally {
			close(jedis);
		}
	}

	public void hmset(String key, Map<String, String> map, long milliseconds) {
		ShardedJedis jedis = null;
		try {
			jedis = shardedPool.getResource();
			jedis.hmset(key, map);
			if (milliseconds > 0) {
				jedis.pexpire(key, milliseconds);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	public Map<String, String> hgetAll(String key) {
		ShardedJedis jedis = null;
		Map<String, String> map = new HashMap<>();
		try {
			jedis = shardedPool.getResource();
			if (jedis.exists(key)) {
				map = jedis.hgetAll(key);
				return map;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(jedis);
		}
		return map;
	}

	public long pexpire(String key, long milliseconds) {
		ShardedJedis jedis = null;
		try {
			jedis = shardedPool.getResource();
			return jedis.pexpire(key, milliseconds);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(jedis);
		}
		return 0;
	}

	public void del(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.del(key);
		} finally {
			close(jedis);
		}
	}

	public Long dbSize() {
		Long dbSize = 0L;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			dbSize = jedis.dbSize();
		} finally {
			close(jedis);
		}
		return dbSize;
	}

	public void flushDB() {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.flushDB();
		} finally {
			close(jedis);
		}
	}

	public void flushAll() {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.flushAll();
		} finally {
			close(jedis);
		}
	}
}
