package com.sniper.springmvc.mybatis.service;

import java.util.List;
import java.util.Map;

import com.sniper.springmvc.datasource.DataSource;
import com.sniper.springmvc.datasource.DataSourceValue;

public interface BaseService<T> {

	/**
	 * 
	 * @param t
	 * @return
	 */

	@DataSource
	public int insert(String sqlId, T t);

	@DataSource
	public int insert(T t);

	/**
	 * 
	 * @param t
	 * @return
	 */
	@DataSource
	public int update(String sqlId, T t);

	@DataSource
	public int update(T t);

	/**
	 * 
	 * @param t
	 * @return
	 */
	@DataSource
	public int delete(String sqlId, String id);

	@DataSource
	public int delete(String sqlId, int id);

	@DataSource
	public int delete(String id);

	@DataSource
	public int delete(String sqlId, Map<String, Object> params);

	/**
	 * 
	 * @param id
	 * @return
	 */
	@DataSource(DataSourceValue.SLAVE)
	public T get(String sqlId, String id);

	@DataSource(DataSourceValue.SLAVE)
	public T get(String sqlId, int id);

	@DataSource(DataSourceValue.SLAVE)
	public T get(String id);

	/**
	 * 
	 * @param t
	 * @return
	 */
	@DataSource(DataSourceValue.SLAVE)
	public List<T> query(String sqlId, Map<String, Object> params);

	@DataSource(DataSourceValue.SLAVE)
	public List<Map<String, Object>> queryMap(String sqlId,
			Map<String, Object> params);

	@DataSource
	public void executeSql(String sqlId, Map<String, Object> params);

	/**
	 * 批量新增
	 * 
	 * @param sqlId
	 * @param list
	 * @return
	 */
	@DataSource
	public int batchInsert(String sqlId, List<T> list);

	/**
	 * 批量修改
	 * 
	 * @param sqlId
	 * @param list
	 * @return
	 */
	@DataSource
	public int batchUpdate(String sqlId, List<T> list);

	/**
	 * 
	 * @param list
	 * @return
	 */
	@DataSource
	public int batchDelete(String sqlId, String[] ids);

	@DataSource
	public int batchDelete(String sqlId, Integer[] ids);

	@DataSource(DataSourceValue.SLAVE)
	public int pageCount(Map<String, Object> params);

	@DataSource(DataSourceValue.SLAVE)
	public List<T> pageList(Map<String, Object> params);

	/**
	 * 获取一些单一结果的 比如技术数，
	 * 
	 * @param sqlId
	 * @param params
	 * @return
	 */

	@DataSource(DataSourceValue.SLAVE)
	public Object find(String sqlId, Map<String, Object> params);
}
