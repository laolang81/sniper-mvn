package com.sniper.springmvc.mybatis.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sniper.springmvc.model.AdminRight;
import com.sniper.springmvc.mybatis.dao.BaseDao;

@Service("adminRightService")
public class AdminRightServiceImpl extends BaseServiceImpl<AdminRight>
		implements AdminRightService {

	@Override
	@Resource(name = "adminRightDao")
	public void setDao(BaseDao<AdminRight> dao) {
		super.setDao(dao);
	}

	/**
	 * 添加 url
	 */
	@Override
	public void appendRightByURL(String url) {

	}

	/**
	 * 获取spring可用的url 加get可能会生成2个缓存 此方法为spring security专用
	 * 
	 * @return
	 */
	@Override
	public List<AdminRight> springRight() {

		return null;

	}

	@Override
	public AdminRight getCRightByUrl(String url) {
		Map<String, Object> params = new HashMap<>();
		params.put("url", url);
		return (AdminRight) super.find("select", params);
	}

}
