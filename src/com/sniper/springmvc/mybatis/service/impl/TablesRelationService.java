package com.sniper.springmvc.mybatis.service.impl;

import java.util.List;

import com.sniper.springmvc.model.Channel;
import com.sniper.springmvc.model.SdAttachments;
import com.sniper.springmvc.model.TablesRelation;
import com.sniper.springmvc.mybatis.service.BaseService;

public interface TablesRelationService extends BaseService<TablesRelation> {
	// project-expert
	// 下面定义的绑定的顺序，都是根据下面的顺序绑定
	public static String ITMES_FILES = "items-files";

	public static String SUBJECTS_FILES = "subjects-files";

	public List<TablesRelation> getTableRelation(String source, String type);

	public List<TablesRelation> getTableRelationBind(String bind, String type);

	public List<Channel> getChannel(List<TablesRelation> relations);

	public List<SdAttachments> getFiles(List<TablesRelation> relations);

	public int saveTableRelation(List<TablesRelation> relations, String type);

	public int saveTableRelation(String source, String bind, String type);

	public int removeTableRelation(String source, String type);

	public int removeTableRelation(String source, String bind, String type);

}
