package com.sniper.springmvc.mybatis.service.impl;

import java.util.List;
import java.util.Map;

import com.sniper.springmvc.model.SdDepartments;
import com.sniper.springmvc.mybatis.service.BaseService;

public interface SdDepartmentsService extends BaseService<SdDepartments> {

	/**
	 * @param type
	 *            类型，0表示全部
	 * @param 状态
	 * @param 首页显示
	 *            0否，1显示， 10全部
	 */
	public List<SdDepartments> getDep(int[] types, int status, int home);

	public Map<String, String> getMapDep(List<SdDepartments> departments);

}
