package com.sniper.springmvc.mybatis.service.impl;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sniper.springmvc.mybatis.dao.BaseDao;
import com.sniper.springmvc.mybatis.service.BaseService;

/**
 * @Resource写法不能乱写，写在字段上和set上可以用子类覆盖，而字段不能被覆盖
 * @author laolang
 * 
 * @param <T>
 */
public abstract class BaseServiceImpl<T> implements BaseService<T> {

	protected final static Logger LOGGER = LoggerFactory
			.getLogger(BaseService.class);

	protected BaseDao<T> dao;
	private Class<T> clazz;

	public BaseDao<T> getDao() {
		return dao;
	}

	@SuppressWarnings("unchecked")
	public BaseServiceImpl() {

		ParameterizedType type = (ParameterizedType) this.getClass()
				.getGenericSuperclass();
		clazz = (Class<T>) type.getActualTypeArguments()[0];

	}

	public BaseServiceImpl(BaseDao<T> dao, Class<T> clazz) {
		super();
		this.dao = dao;
		this.clazz = clazz;
	}

	// 注入
	@Resource
	public void setDao(BaseDao<T> dao) {
		this.dao = dao;
	}

	public Class<T> getClazz() {
		return clazz;
	}

	@Override
	public int insert(String sqlId, T t) {
		return dao.insert(sqlId, t);
	}

	@Override
	public int insert(T t) {
		return this.insert("insert", t);
	}

	@Override
	public int update(String sqlId, T t) {
		return dao.update(sqlId, t);
	}

	@Override
	public int update(T t) {
		return this.update("update", t);
	}

	@Override
	public int delete(String sqlId, String id) {
		return dao.delete(sqlId, id);
	}

	@Override
	public int delete(String sqlId, int id) {
		return dao.delete(sqlId, id);
	}

	@Override
	public int delete(String id) {
		return this.delete("delete", id);
	}

	@Override
	public int delete(String sqlId, Map<String, Object> params) {
		return dao.delete(sqlId, params);
	}

	@Override
	public T get(String sqlId, String id) {
		return dao.get(sqlId, id);
	}

	@Override
	public T get(String sqlId, int id) {
		return dao.get(sqlId, id);
	}

	@Override
	public T get(String id) {
		return this.get("get", id);
	}

	@Override
	public List<T> query(String sqlId, Map<String, Object> params) {
		return dao.query(sqlId, params);
	}

	@Override
	public List<Map<String, Object>> queryMap(String sqlId,
			Map<String, Object> params) {
		return dao.queryMap(sqlId, params);
	}

	@Override
	public void executeSql(String sqlId, Map<String, Object> params) {
		dao.executeSql(sqlId, params);
	}

	@Override
	public int batchInsert(String sqlId, List<T> list) {
		return dao.batchInsert(sqlId, list);
	}

	@Override
	public int batchUpdate(String sqlId, List<T> list) {
		return dao.batchUpdate(sqlId, list);
	}

	@Override
	public int batchDelete(String sqlId, String[] ids) {
		return dao.batchDelete(sqlId, ids);
	}

	@Override
	public int batchDelete(String sqlId, Integer[] ids) {
		return dao.batchDelete(sqlId, ids);
	}

	@Override
	public int pageCount(Map<String, Object> params) {
		Object object = dao.find("pageCount", params);
		return (int) object;
	}

	@Override
	public List<T> pageList(Map<String, Object> params) {
		return dao.query("page", params);
	}

	@Override
	public Object find(String sqlId, Map<String, Object> params) {
		return dao.find(sqlId, params);
	}

}