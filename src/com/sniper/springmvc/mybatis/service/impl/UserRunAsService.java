package com.sniper.springmvc.mybatis.service.impl;

import java.util.List;

import com.sniper.springmvc.model.UserRunAs;
import com.sniper.springmvc.mybatis.service.BaseService;

public interface UserRunAsService extends BaseService<UserRunAs> {

	public void grantRunAs(String string, String toUserId);

	public void revokeRunAs(String fromUserId, String toUserId);

	public boolean exists(String fromUserId, String toUserId);

	public List<UserRunAs> findFromUserIds(String fromUserId);

	public List<UserRunAs> findToUserIds(String toUserId);
}
