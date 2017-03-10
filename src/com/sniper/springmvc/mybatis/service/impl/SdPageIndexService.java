package com.sniper.springmvc.mybatis.service.impl;

import java.util.Map;

import com.sniper.springmvc.model.SdItems;
import com.sniper.springmvc.model.SdPageIndex;
import com.sniper.springmvc.mybatis.service.BaseService;

public interface SdPageIndexService extends BaseService<SdPageIndex> {

	public Map<String, SdPageIndex> getPageIndex();

	public Map<String, SdItems> getItems(int id);

	public Map<String, String> pageIndexMap(Map<String, SdPageIndex> map);
}
