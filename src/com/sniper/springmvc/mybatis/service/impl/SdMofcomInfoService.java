package com.sniper.springmvc.mybatis.service.impl;

import com.sniper.springmvc.model.SdMofcomInfo;
import com.sniper.springmvc.model.SdMofcomItem;
import com.sniper.springmvc.mybatis.service.BaseService;

public interface SdMofcomInfoService extends BaseService<SdMofcomInfo> {

	public int updateMofcom(String sid, SdMofcomItem mofcomItem);
}
