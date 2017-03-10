package com.sniper.springmvc.mybatis.service.impl;

import java.util.List;
import java.util.Map;

import com.sniper.springmvc.model.SdTabInfoClass;
import com.sniper.springmvc.mybatis.service.BaseService;

public interface SdTabInfoClassService extends BaseService<SdTabInfoClass> {

	public List<SdTabInfoClass> getTabInfo(Integer id, int status);

	public Map<String, String> getMapInfoClass(List<SdTabInfoClass> infoClasses);
}
