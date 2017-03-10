package com.sniper.springmvc.mybatis.service.impl;

import java.util.Map;

import com.sniper.springmvc.datasource.DataSource;
import com.sniper.springmvc.datasource.DataSourceValue;
import com.sniper.springmvc.model.SdSubjects;
import com.sniper.springmvc.mybatis.service.BaseService;

public interface SdSubjectsService extends BaseService<SdSubjects> {

	public int changeSiteid(int siteid, int siteidMove, int itemid,
			int itemidMove);

	@DataSource(DataSourceValue.SLAVE)
	public Map<String, Object> getSubject(String id);

	@DataSource(DataSourceValue.SLAVE)
	public SdSubjects getSubjectWithFiles(String id);

	public int setSubjectViewZero(String date);
}
