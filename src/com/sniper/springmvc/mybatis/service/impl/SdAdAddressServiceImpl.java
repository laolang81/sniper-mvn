package com.sniper.springmvc.mybatis.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sniper.springmvc.model.SdAdAddress;
import com.sniper.springmvc.mybatis.dao.BaseDao;
import com.sniper.springmvc.utils.ValidateUtil;

@Service("sdAdAddressService")
public class SdAdAddressServiceImpl extends BaseServiceImpl<SdAdAddress>
		implements SdAdAddressService {

	@Resource(name = "sdAdAddressDao")
	@Override
	public void setDao(BaseDao<SdAdAddress> dao) {
		super.setDao(dao);
	}

	@Override
	public Map<String, String> mapFormat(List<SdAdAddress> adAddresses) {
		Map<String, String> channelsMap = new HashMap<>();
		if (ValidateUtil.isValid(adAddresses)) {
			for (SdAdAddress channel : adAddresses) {
				channelsMap.put(String.valueOf(channel.getId()),
						channel.getName());
			}
		}
		return channelsMap;
	}

}
