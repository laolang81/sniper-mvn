package com.sniper.springmvc.mybatis.service.impl;

import java.util.List;
import java.util.Map;

import com.sniper.springmvc.model.SdLinkGroup;
import com.sniper.springmvc.mybatis.service.BaseService;

public interface SdLinkGroupService extends BaseService<SdLinkGroup> {

	public Map<String, String> mapFormat(List<SdLinkGroup> groups);
}
