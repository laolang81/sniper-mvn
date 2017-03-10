package com.sniper.springmvc.mybatis.service.impl;

import java.util.List;

import com.sniper.springmvc.model.SdSubjectLogs;
import com.sniper.springmvc.mybatis.service.BaseService;

public interface SdSubjectLogsService extends BaseService<SdSubjectLogs> {

	public void log(int sid, String uid, String logCode, String msg);

	public List<SdSubjectLogs> getLogs(String sid);

}
