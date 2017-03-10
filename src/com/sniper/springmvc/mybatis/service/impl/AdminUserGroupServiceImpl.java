package com.sniper.springmvc.mybatis.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sniper.springmvc.model.AdminUserGroup;
import com.sniper.springmvc.mybatis.dao.BaseDao;

@Service("adminUserGroupService")
public class AdminUserGroupServiceImpl extends BaseServiceImpl<AdminUserGroup>
		implements AdminUserGroupService {

	@Resource(name = "adminUserGroupDao")
	@Override
	public void setDao(BaseDao<AdminUserGroup> dao) {
		super.setDao(dao);
	}

	@Override
	public List<AdminUserGroup> getAUG(String gid) {
		Map<String, Object> params = new HashMap<>();
		params.put("gid", gid);
		return this.query("select", params);
	}
}
