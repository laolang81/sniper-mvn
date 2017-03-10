package com.sniper.springmvc.mybatis.service.impl;

import javax.annotation.Resource;

import org.springframework.cache.Cache;
import org.springframework.cache.ehcache.EhCacheCache;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.stereotype.Service;

import com.sniper.springmvc.model.AdminUser;
import com.sniper.springmvc.model.OauthClient;
import com.sniper.springmvc.mybatis.dao.BaseDao;
import com.sniper.springmvc.utils.RedisUtil;

@Service("oauthUserService")
public class OauthUserServiceImpl extends BaseServiceImpl<AdminUser> implements OauthUserService {

	private EhCacheCache cache;

	private static final String redisPrefi = "oauth_redis_";

	@Resource(name = "cacheManager")
	EhCacheCacheManager cacheManager;
	/**
	 * redis
	 */
	private static RedisUtil redisUtil = RedisUtil.getInstance();

	@Resource
	OauthClientService clientService;

	@Resource(name = "adminUserDao")
	@Override
	public void setDao(BaseDao<AdminUser> dao) {
		super.setDao(dao);
	}

	public OauthUserServiceImpl() {

	}

	public Cache getCache() {
		if (cache == null) {
			this.cache = (EhCacheCache) cacheManager.getCache("cache-cache");
		}
		return cache;
	}

	@Override
	public void addAuthCode(String authCode, String username) {

		redisUtil.set(redisPrefi + authCode, username, getExpireIn());
		// ehcache缓存
		// getCache().put(authCode, username);

	}

	@Override
	public void addAccessToken(String accessToken, String username) {
		redisUtil.set(redisPrefi + accessToken, username, getExpireIn());
		// getCache().put(accessToken, username);

	}

	@Override
	public boolean checkAuthCode(String authCode) {
		return redisUtil.exists(redisPrefi + authCode);
		// return getCache().get(redisPrefi + authCode) != null;
	}

	@Override
	public boolean checkAccessToken(String accessToken) {
		return redisUtil.exists(redisPrefi + accessToken);
		// return getCache().get(redisPrefi + accessToken) != null;
	}

	@Override
	public String getUsernameByAuthCode(String authCode) {

		return redisUtil.get(redisPrefi + authCode);
		// return (String) getCache().get(redisPrefi + authCode).get();
	}

	@Override
	public String getUsernameByAccessToken(String accessToken) {

		return redisUtil.get(redisPrefi + accessToken);
		// return (String) getCache().get(accessToken).get();
	}

	/**
	 * 数据临时储存保存时间
	 */
	@Override
	public long getExpireIn() {
		return 180000l;
	}

	@Override
	public boolean checkClientId(String clientId) {

		OauthClient client = clientService.findByClientId(clientId);
		if (client != null) {
			return true;
		}
		return false;
	}

	@Override
	public boolean checkClientSecret(String clientSecret) {
		OauthClient client = clientService.findByClientSecret(clientSecret);
		if (client != null) {
			return true;
		}
		return false;
	}

}
