package com.sniper.springmvc.mybatis.service.impl;

import java.util.List;

import com.sniper.springmvc.model.AdminUserGroup;
import com.sniper.springmvc.mybatis.service.BaseService;

public interface AdminUserGroupService extends BaseService<AdminUserGroup> {

	public List<AdminUserGroup> getAUG(String gid);
}
