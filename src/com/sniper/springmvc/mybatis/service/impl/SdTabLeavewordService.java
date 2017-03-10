package com.sniper.springmvc.mybatis.service.impl;

import com.sniper.springmvc.model.SdTabLeaveword;
import com.sniper.springmvc.mybatis.service.BaseService;

public interface SdTabLeavewordService extends BaseService<SdTabLeaveword> {

	public int wordCount(int sitid, int answer, String sDate, String eDate);
}
