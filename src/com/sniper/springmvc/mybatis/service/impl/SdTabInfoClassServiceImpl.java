package com.sniper.springmvc.mybatis.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sniper.springmvc.model.SdTabInfoClass;
import com.sniper.springmvc.mybatis.dao.BaseDao;
import com.sniper.springmvc.utils.ValidateUtil;

@Service("sdTabInfoClassService")
public class SdTabInfoClassServiceImpl extends BaseServiceImpl<SdTabInfoClass>
		implements SdTabInfoClassService {

	@Resource(name = "sdTabInfoClassDao")
	@Override
	public void setDao(BaseDao<SdTabInfoClass> dao) {
		super.setDao(dao);
	}

	@Override
	public List<SdTabInfoClass> getTabInfo(Integer id, int status) {
		Map<String, Object> params = new HashMap<>();
		params.put("itemid", id);
		params.put("enabled", 1);
		List<SdTabInfoClass> classes = this.query("select", params);

		return classes;
	}

	@Override
	public Map<String, String> getMapInfoClass(List<SdTabInfoClass> infoClasses) {
		Map<String, String> map = new HashMap<>();
		if (ValidateUtil.isValid(infoClasses)) {
			for (SdTabInfoClass sdTabInfoClass : infoClasses) {
				map.put(sdTabInfoClass.getId().toString(),
						sdTabInfoClass.getName());
			}
		}
		return map;
	}
}
