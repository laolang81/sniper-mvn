package com.sniper.springmvc.mybatis.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sniper.springmvc.model.SdDepartments;
import com.sniper.springmvc.model.SdItems;
import com.sniper.springmvc.mybatis.dao.BaseDao;

@Service("sdItemsService")
public class SdItemsServiceImpl extends BaseServiceImpl<SdItems> implements
		SdItemsService {

	@Resource
	SdDepartmentsService departmentsService;

	public static Map<String, String> STYLES = new HashMap<>();
	static {
		STYLES.put("0", "默认列表样式");
		STYLES.put("1", "类“统计数据”样式");
		STYLES.put("2", "类《山东外经贸年鉴》样式");
		STYLES.put("3", "类《山东外经贸》样式");
		STYLES.put("4", "类“国别资料”样式");
	}

	@Resource(name = "sdItemsDao")
	@Override
	public void setDao(BaseDao<SdItems> dao) {
		super.setDao(dao);
	}

	@Override
	public List<SdItems> getItems(Integer deprtid, int status) {
		Map<String, Object> params = new HashMap<>();
		params.put("deprtid", deprtid);
		params.put("enabled", status);
		params.put("order", "t.displayorder desc");
		List<SdItems> items = this.query("select", params);
		return items;
	}

	@Override
	public SdItems getItemInfo(String inid) {
		SdItems parentItems = this.get(inid);
		if (parentItems != null) {
			SdDepartments departments = departmentsService.get(parentItems
					.getDeprtid() + "");
			parentItems.setDepartments(departments);
		}
		return parentItems;
	}

	@Override
	public Map<String, List<SdItems>> getItemsData(
			List<SdDepartments> departments) {
		Map<String, List<SdItems>> treeData = new HashMap<>();
		for (SdDepartments sdDepartments : departments) {
			List<SdItems> items = this.getItems(sdDepartments.getId(), 1);
			treeData.put(sdDepartments.getId() + "", items);
		}
		return treeData;
	}

	@Override
	public List<SdItems> getItemsByDep(String depid, int enabled) {
		Map<String, Object> params = new HashMap<>();
		params.put("order", "t.itemid desc");
		params.put("deprtid", depid);
		params.put("enabled", enabled);
		List<SdItems> items1 = this.query("select", params);
		return items1;
	}
}
