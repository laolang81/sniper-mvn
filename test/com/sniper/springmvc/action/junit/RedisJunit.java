package com.sniper.springmvc.action.junit;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.msgpack.MessagePack;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import com.sniper.springmvc.utils.RedisUtil;

public class RedisJunit {

	public void test() throws IOException {
		long start = System.currentTimeMillis();
		RedisUtil redisUtil = RedisUtil.getInstance();
		ShardedJedisPool shardedJedisPool = redisUtil.getShardedPool();
		ShardedJedis shardedJedis = shardedJedisPool.getResource();
		System.out.println(System.currentTimeMillis() - start);
		start = System.currentTimeMillis();
		MessagePack messagePack = new MessagePack();
		for (int i = 0; i < 10000; i++) {
			String name = "sniper" + i;
			String value = "sniper-value" + i;
			byte[] nameByte = messagePack.write(name);
			byte[] valueByte = messagePack.write(value);
			if (!shardedJedis.exists(nameByte)) {
				shardedJedis.set(nameByte, valueByte);
			}
			// System.out.println(shardedJedis.get(nameByte));
			shardedJedis.del(nameByte);
		}
		System.out.println(System.currentTimeMillis() - start);
		start = System.currentTimeMillis();
		for (int i = 0; i < 10000; i++) {
			String name = "sniper" + i;
			String value = "sniper-value" + i;
			if (!shardedJedis.exists(name)) {
				shardedJedis.set(name, value);
			}
			// System.out.println(shardedJedis.get(name));
			shardedJedis.del(name);
		}
		System.out.println(System.currentTimeMillis() - start);

	}

	public void testRedis() throws Exception {
		RedisUtil redisUtil = RedisUtil.getInstance();
		ShardedJedis shardedJedis = redisUtil.getShardedPool().getResource();
		String key = "mac.sdocm.local.admin.dep";
		// shardedJedis.pexpire(key, 1800);
		Map<String, String> deps = shardedJedis.hgetAll(key);
		long ttl = shardedJedis.ttl(key);
		System.out.println(deps);
		System.out.println(ttl);
		redisUtil.flushDB();
	}

	@Test
	public void testKeys() {
		RedisUtil redisUtil = RedisUtil.getInstance();
		// mac.tender:localsniper.de
		String keyPrefix = "mac.tender:localsniper*";
		Jedis jedis = redisUtil.getJedisPool().getResource();
		Set<String> keys = jedis.keys("*");

		for (String key : keys) {
			System.out.println(key);

		}
		redisUtil.getJedisPool().getResource().close();
	}
}
