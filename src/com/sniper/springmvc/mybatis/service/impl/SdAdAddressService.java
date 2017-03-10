package com.sniper.springmvc.mybatis.service.impl;

import java.util.List;
import java.util.Map;

import com.sniper.springmvc.model.SdAdAddress;
import com.sniper.springmvc.mybatis.service.BaseService;

public interface SdAdAddressService extends BaseService<SdAdAddress> {

	public Map<String, String> mapFormat(List<SdAdAddress> adAddresses);
}
