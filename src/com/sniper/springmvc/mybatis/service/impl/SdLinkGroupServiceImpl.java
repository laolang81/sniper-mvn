package com.sniper.springmvc.mybatis.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sniper.springmvc.model.SdLinkGroup;
import com.sniper.springmvc.mybatis.dao.BaseDao;
import com.sniper.springmvc.utils.ValidateUtil;

@Service("sdLinkGroupService")
public class SdLinkGroupServiceImpl extends BaseServiceImpl<SdLinkGroup>
		implements SdLinkGroupService {

	@Resource(name = "sdLinkGroupDao")
	@Override
	public void setDao(BaseDao<SdLinkGroup> dao) {
		super.setDao(dao);
	}

	@Override
	public Map<String, String> mapFormat(List<SdLinkGroup> groups) {
		Map<String, String> channelsMap = new HashMap<>();
		if (ValidateUtil.isValid(groups)) {
			for (SdLinkGroup channel : groups) {
				channelsMap.put(String.valueOf(channel.getId()),
						channel.getName());
			}
		}
		return channelsMap;
	}
}
