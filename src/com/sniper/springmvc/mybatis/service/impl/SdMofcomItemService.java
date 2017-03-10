package com.sniper.springmvc.mybatis.service.impl;

import java.util.Map;

import com.sniper.springmvc.model.SdMofcomItem;
import com.sniper.springmvc.mybatis.service.BaseService;

public interface SdMofcomItemService extends BaseService<SdMofcomItem> {

	public Map<String, String> getMofcom();
}
