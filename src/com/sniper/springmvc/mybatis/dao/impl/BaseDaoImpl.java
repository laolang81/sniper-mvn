package com.sniper.springmvc.mybatis.dao.impl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;

import com.sniper.springmvc.mybatis.dao.BaseDao;

/**
 * 单例
 * 
 * @author laolang
 * 
 * @param <T>
 */
@SuppressWarnings("unchecked")
public abstract class BaseDaoImpl<T> implements BaseDao<T> {

	// 声明反射用，获取的类反射类型
	private Class<T> clazz;
	// 注入session
	@Resource
	private SqlSession session;

	public BaseDaoImpl() {
		// 得到泛型话的超类，
		Type type = this.getClass().getGenericSuperclass();
		// 如果type集成与ParameterizedType,以为内ParameterizedType可以带有参数，可以从他里面获取参数
		Type[] args = null;
		if (type instanceof ParameterizedType) {
			ParameterizedType parameterizedType = (ParameterizedType) type;
			// 获取参数列表
			args = parameterizedType.getActualTypeArguments();
			if (args != null && args.length > 0) {
				// 获取地一个参数
				Type arg = args[0];
				if (arg instanceof Class) {
					clazz = (Class<T>) arg;
				}
			}
		}
	}

	@Override
	public int insert(String sqlId, T t) {
		return session.insert(clazz.getName() + "." + sqlId, t);
	}

	@Override
	public int insert(String sqlId, Map<String, Object> params) {
		return session.insert(clazz.getName() + "." + sqlId, params);
	}

	@Override
	public int update(String sqlId, T t) {
		return session.update(clazz.getName() + "." + sqlId, t);
	}

	@Override
	public int update(String sqlId, Map<String, Object> params) {
		return session.update(clazz.getName() + "." + sqlId, params);
	}

	@Override
	public int delete(String sqlId, String id) {
		return session.delete(clazz.getName() + "." + sqlId, id);
	}

	@Override
	public int delete(String sqlId, int id) {
		return session.delete(clazz.getName() + "." + sqlId, id);
	}

	@Override
	public int delete(String sqlId, Map<String, Object> params) {
		return session.delete(clazz.getName() + "." + sqlId, params);
	}

	@Override
	public T get(String sqlId, String id) {
		return session.selectOne(clazz.getName() + "." + sqlId, id);
	}

	@Override
	public T get(String sqlId, int id) {
		return session.selectOne(clazz.getName() + "." + sqlId, id);
	}

	@Override
	public List<T> query(String sqlId, Map<String, Object> params) {
		return session.selectList(clazz.getName() + "." + sqlId, params);
	}

	@Override
	public List<Map<String, Object>> queryMap(String sqlId, Map<String, Object> params) {
		return session.selectList(clazz.getName() + "." + sqlId, params);
	}

	@Override
	public void executeSql(String sqlId, Map<String, Object> params) {

		session.update(clazz.getName() + "." + sqlId, params);
	}

	@Override
	public int batchInsert(String sqlId, List<T> list) {
		int i = 0;
		for (T t : list) {
			i += this.insert(sqlId, t);
		}
		return i;
	}

	@Override
	public int batchInsertMap(String sqlId, List<Map<String, Object>> list) {
		int i = 0;
		for (Map<String, Object> map : list) {
			i += this.insert(sqlId, map);
		}
		return i;
	}

	@Override
	public int batchUpdateMap(String sqlId, List<Map<String, Object>> list) {
		int i = 0;
		for (Map<String, Object> map : list) {
			i += this.insert(sqlId, map);
		}
		return i;
	}

	@Override
	public int batchUpdate(String sqlId, List<T> list) {
		int i = 0;
		for (T t : list) {
			i += this.update(sqlId, t);
		}
		return i;
	}

	@Override
	public int batchDelete(String sqlId, String[] ids) {
		int i = 0;
		for (int j = 0; j < ids.length; j++) {
			i += this.delete(sqlId, ids[j]);
		}
		return i;
	}

	@Override
	public int batchDelete(String sqlId, Integer[] ids) {
		int i = 0;
		for (int j = 0; j < ids.length; j++) {
			i += this.delete(sqlId, ids[j]);
		}
		return i;
	}

	@Override
	public Object find(String sqlId, Map<String, Object> params) {
		return session.selectOne(clazz.getName() + "." + sqlId, params);
	}

}
