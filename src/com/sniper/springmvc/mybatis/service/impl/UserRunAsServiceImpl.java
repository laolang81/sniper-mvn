package com.sniper.springmvc.mybatis.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sniper.springmvc.model.UserRunAs;
import com.sniper.springmvc.mybatis.dao.BaseDao;
import com.sniper.springmvc.utils.FilesUtil;

@Service("userRunAsService")
public class UserRunAsServiceImpl extends BaseServiceImpl<UserRunAs> implements
		UserRunAsService {

	@Resource(name = "userRunAsDao")
	@Override
	public void setDao(BaseDao<UserRunAs> dao) {
		super.setDao(dao);
	}

	@Override
	public void grantRunAs(String fromUserId, String toUserId) {
		UserRunAs as = new UserRunAs();
		as.setFromUserId(fromUserId);
		as.setToUserId(toUserId);
		as.setId(FilesUtil.getUUIDName("", false));
		this.insert(as);

	}

	@Override
	public void revokeRunAs(String fromUserId, String toUserId) {
		Map<String, Object> params = new HashMap<>();
		params.put("fromUserId", fromUserId);
		params.put("toUserId", toUserId);
		this.delete("deleteMap", params);

	}

	@Override
	public boolean exists(String fromUserId, String toUserId) {
		Map<String, Object> params = new HashMap<>();
		params.put("fromUserId", fromUserId);
		params.put("toUserId", toUserId);
		if (this.find("select", params) != null) {
			return true;
		}
		return false;
	}

	@Override
	public List<UserRunAs> findFromUserIds(String fromUserId) {
		Map<String, Object> params = new HashMap<>();
		params.put("fromUserId", fromUserId);
		return this.query("select", params);
	}

	@Override
	public List<UserRunAs> findToUserIds(String toUserId) {
		Map<String, Object> params = new HashMap<>();
		params.put("toUserId", toUserId);
		return this.query("select", params);
	}

}
