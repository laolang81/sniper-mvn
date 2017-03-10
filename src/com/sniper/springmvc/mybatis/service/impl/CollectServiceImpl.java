package com.sniper.springmvc.mybatis.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sniper.springmvc.model.AdminUser;
import com.sniper.springmvc.model.Collect;
import com.sniper.springmvc.mybatis.dao.BaseDao;
import com.sniper.springmvc.utils.FilesUtil;
import com.sniper.springmvc.utils.ValidateUtil;

@Service("collectSerivce")
public class CollectServiceImpl extends BaseServiceImpl<Collect> implements
		CollectService {

	@Resource(name = "collectDao")
	@Override
	public void setDao(BaseDao<Collect> dao) {
		super.setDao(dao);
	}

	@Override
	public List<Collect> getCollect(int uid, String type) {
		Map<String, Object> params = new HashMap<>();
		params.put("uid", uid);
		params.put("type", type);
		return this.query("select", params);
	}

	@Override
	public void insertValue(String url, String name, String type,
			AdminUser adminUser) {
		if (!ValidateUtil.isValid(url)) {
			return;
		}
		Map<String, Object> params = new HashMap<>();
		params.put("value", url);
		params.put("type", type);
		params.put("uid", adminUser.getId());
		Collect collect = (Collect) this.find("select", params);
		if (collect != null) {
			collect.setNum(collect.getNum() + 1);
			collect.setEtime(new Date());
			this.update(collect);
		} else {
			collect = new Collect();
			collect.setId(FilesUtil.UUID());
			collect.setStime(new Date());
			collect.setName(name);
			collect.setType(type);
			collect.setValue(url);
			collect.setNum(1);
			collect.setAdminUser(adminUser);
			this.insert(collect);
		}
	}
}
