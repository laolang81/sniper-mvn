package com.sniper.springmvc.mybatis.service.impl;

import com.sniper.springmvc.model.LogSmb;
import com.sniper.springmvc.mybatis.service.BaseService;

public interface LogSmbService extends BaseService<LogSmb> {

	public final static String PUT_FILE = "put_file";
	public final static String PUT_DIR = "put_dir";
	public final static String DEL_FILE = "del_file";
	public final static String DEL_DIR = "del_dir";

}
