package com.sniper.springmvc.mybatis.service.impl;

import java.util.List;
import java.util.Map;

import com.sniper.springmvc.model.SdDepartments;
import com.sniper.springmvc.model.SdItems;
import com.sniper.springmvc.mybatis.service.BaseService;

public interface SdItemsService extends BaseService<SdItems> {

	/**
	 * 获取统一上级的栏目
	 * 
	 * @param itemidup
	 * @param status
	 * @return
	 */
	public List<SdItems> getItems(Integer itemidup, int status);

	/**
	 * 获取处室信息
	 * 
	 * @param inid
	 * @return
	 */
	public SdItems getItemInfo(String inid);

	/**
	 * 根据处室列表获取处室
	 * 
	 * @param departments
	 * @return
	 */
	public Map<String, List<SdItems>> getItemsData(
			List<SdDepartments> departments);

	/**
	 * 根据处室id获取栏目
	 * 
	 * @param depid
	 * @param enabled
	 * @return
	 */
	public List<SdItems> getItemsByDep(String depid, int enabled);
}
