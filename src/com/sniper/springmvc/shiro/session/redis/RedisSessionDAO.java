package com.sniper.springmvc.shiro.session.redis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.shiro.cache.CacheException;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.apache.shiro.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.ShardedJedis;

import com.sniper.springmvc.utils.RedisUtil;

/**
 * redis缓存处理
 * 
 * @author suzhen
 * 
 */
public class RedisSessionDAO extends AbstractSessionDAO {

	private static final Logger log = LoggerFactory.getLogger(RedisSessionDAO.class);
	private static final RedisUtil redisUtil = RedisUtil.getInstance();
	/**
	 * 缓存前缀和缓存管理器一致
	 */
	private String prefix = "shiro_redis_session";

	public RedisSessionDAO() {

	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	@Override
	protected Serializable doCreate(Session session) {
		Serializable sessionId = generateSessionId(session);
		assignSessionId(session, sessionId);
		System.out.println("Session创建：" + session);

		saveSession(sessionId, session);
		return sessionId;
	}

	/**
	 * 用于储存更新session
	 * 
	 * @param sessionId
	 * @param session
	 */
	private void saveSession(Serializable sessionId, Session session) {
		if (sessionId == null) {
			throw new NullPointerException("id argument cannot be null.");
		}
		ShardedJedis jedis = redisUtil.getShardedPool().getResource();
		try {
			byte[] values = SerializeUtils.serialize(session);
			byte[] names = this.getKey(session);
			jedis.set(names, values);
			jedis.pexpire(names, session.getTimeout());
		} catch (Exception e) {
			// 释放redis对象
			log.error("redis保存出错", e);
			e.printStackTrace();
		} finally {
			redisUtil.close(jedis);
		}
	}

	@Override
	public Session readSession(Serializable sessionId) throws UnknownSessionException {
		System.out.println("Session读取：" + sessionId);
		return this.doReadSession(sessionId);
	}

	@Override
	protected Session doReadSession(Serializable sessionId) {
		if (sessionId == null) {
			throw new NullPointerException("id argument cannot be null.");
		}

		Session session = super.readSession(sessionId);
		if (session != null) {
			return session;
		}

		ShardedJedis jedis = redisUtil.getShardedPool().getResource();
		try {
			byte[] names = this.getKeyString(sessionId.toString());
			if (jedis.exists(names)) {
				byte[] values = jedis.get(names);
				if (values != null && values.length > 0) {
					session = (Session) SerializeUtils.deserialize(values);
					return session;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			redisUtil.close(jedis);
		}
		return null;
	}

	@Override
	public Collection<Session> getActiveSessions() {
		ShardedJedis jedis = redisUtil.getShardedPool().getResource();
		try {
			Set<byte[]> keys = redisUtil.keys(this.getKeyString("*"));
			if (!CollectionUtils.isEmpty(keys)) {
				List<Session> values = new ArrayList<Session>(keys.size());
				for (byte[] key : keys) {
					byte[] vs = jedis.get(key);
					Session value = (Session) SerializeUtils.deserialize(vs);
					if (value != null) {
						values.add(value);
					}
				}
				return Collections.unmodifiableList(values);
			} else {
				return Collections.emptyList();
			}
		} catch (Throwable t) {
			throw new CacheException(t);
		} finally {
			redisUtil.close(jedis);
		}
	}

	/**
	 * 转移key
	 * 
	 * @param sessionId
	 * @return
	 */
	private byte[] getKey(Session session) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(prefix);
		buffer.append(session.getId());
		return buffer.toString().getBytes();
	}

	private byte[] getKeyString(String id) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(prefix);
		buffer.append(id);
		return buffer.toString().getBytes();
	}

	@Override
	public void update(Session session) throws UnknownSessionException {
		System.out.println("Session更新：" + session.getId());
		this.saveSession(session.getId(), session);
	}

	@Override
	public void delete(Session session) {
		if (session == null) {
			throw new NullPointerException("session argument cannot be null.");
		}

		String id = session.getId().toString();
		ShardedJedis jedis = redisUtil.getShardedPool().getResource();
		try {
			if (id != null) {
				jedis.del(this.getKeyString(id));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			redisUtil.close(jedis);
		}
	}
}
