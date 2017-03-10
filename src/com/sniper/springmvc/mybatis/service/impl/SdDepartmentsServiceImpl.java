package com.sniper.springmvc.mybatis.service.impl;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sniper.springmvc.model.SdDepartments;
import com.sniper.springmvc.mybatis.dao.BaseDao;

@Service("sdDepartmentsService")
public class SdDepartmentsServiceImpl extends BaseServiceImpl<SdDepartments>
		implements SdDepartmentsService {

	public static Map<String, String> TYPES = new HashMap<>();
	static {
		TYPES.put("1", "内设机构");
		TYPES.put("2", "派驻机构");
		TYPES.put("3", "驻外经贸代表处");
		// TYPES.put("4", "频道子站");
		// TYPES.put("5", "内设机构");
		TYPES.put("6", "频道子站");
		TYPES.put("7", "直属单位");
		TYPES.put("8", "市商务局");
		TYPES.put("9", "登录专用");
	}

	@Resource(name = "sdDepartmentsDao")
	@Override
	public void setDao(BaseDao<SdDepartments> dao) {
		super.setDao(dao);
	}

	/**
	 * @param type
	 *            类型，0表示全部
	 * @param 状态
	 * @param 首页显示
	 *            0否，1显示， 10全部
	 */
	@Override
	public List<SdDepartments> getDep(int[] types, int status, int home) {
		Map<String, Object> params = new HashMap<>();
		if (types.length > 0) {
			params.put("type", types);
		}
		params.put("enabled", status);
		if (home < 10) {
			params.put("home", home);
		}

		return this.query("select", params);
	}

	@Override
	public Map<String, String> getMapDep(List<SdDepartments> departments) {
		Map<String, String> result = new LinkedHashMap<>();
		if (departments != null) {
			for (SdDepartments sdDepartments : departments) {
				result.put(sdDepartments.getId().toString(),
						sdDepartments.getName());
			}
		}
		return result;
	}
}
