package com.sniper.springmvc.mybatis.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sniper.springmvc.model.SystemConfig;
import com.sniper.springmvc.mybatis.dao.BaseDao;

@Service("systemConfigService")
public class SystemConfigServiceImpl extends BaseServiceImpl<SystemConfig>
		implements SystemConfigService {

	@Override
	@Resource(name = "systemConfigDao")
	public void setDao(BaseDao<SystemConfig> dao) {
		super.setDao(dao);
	}

	@Override
	public Map<String, String> getAdminConfig(boolean autoload) {
		Map<String, Object> map = new HashMap<>();
		map.put("autoload", autoload);
		List<SystemConfig> configs = this.query("select", map);
		Map<String, String> map2 = new HashMap<>();
		for (SystemConfig systemConfig : configs) {
			map2.put(systemConfig.getKeyName(), systemConfig.getKeyValue());
		}
		return map2;
	}

}
