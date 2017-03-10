package com.sniper.springmvc.mybatis.dao;

import java.util.List;
import java.util.Map;

/**
 * 主控操作和关联操作的先后顺序是“先保存one，再保存many；先删除many，再删除one；先update主控方，再update被动方”
 * 
 * @author sniper
 * 
 * @param <T>
 */
public interface BaseDao<T> {

	/**
	 * 插入数据
	 * 
	 * @param t
	 * @return
	 */

	public int insert(String sqlId, T t);

	public int insert(String sqlId, Map<String, Object> params);

	/**
	 * 更新数据
	 * 
	 * @param t
	 * @return
	 */

	public int update(String sqlId, T t);

	public int update(String sqlId, Map<String, Object> params);

	/**
	 * 删除数据
	 * 
	 * @param t
	 * @return
	 */

	public int delete(String sqlId, String id);
	
	public int delete(String sqlId, int id);

	public int delete(String sqlId, Map<String, Object> params);

	/**
	 * 获取数据
	 * 
	 * @param id
	 * @return
	 */

	public T get(String sqlId, String id);
	
	
	public T get(String sqlId, int id);

	/**
	 * 查询数据
	 * 
	 * @param t
	 * @return
	 */

	public List<T> query(String sqlId, Map<String, Object> params);

	public List<Map<String, Object>> queryMap(String sqlId, Map<String, Object> params);

	/**
	 * 执行原始sql。比如创建表
	 * 
	 * @param sqlId
	 * @param params
	 */
	public void executeSql(String sqlId, Map<String, Object> params);

	/**
	 * 批量新增
	 * 
	 * @param sqlId
	 * @param list
	 * @return
	 */

	public int batchInsert(String sqlId, List<T> list);

	public int batchInsertMap(String sqlId, List<Map<String, Object>> list);

	/**
	 * 批量修改
	 * 
	 * @param sqlId
	 * @param list
	 * @return
	 */

	public int batchUpdate(String sqlId, List<T> list);

	public int batchUpdateMap(String sqlId, List<Map<String, Object>> list);

	/**
	 * 批量删除
	 * 
	 * @param list
	 * @return
	 */

	public int batchDelete(String sqlId, String[] ids);
	
	public int batchDelete(String sqlId, Integer[] ids);

	/**
	 * 获取一些单一结果的 比如技术数，
	 * 
	 * @param sqlId
	 * @param params
	 * @return
	 */
	public Object find(String sqlId, Map<String, Object> params);

}
