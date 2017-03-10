package com.sniper.springmvc.mybatis.service.impl;

import java.util.List;
import java.util.Map;

import com.sniper.springmvc.datasource.DataSource;
import com.sniper.springmvc.datasource.DataSourceValue;
import com.sniper.springmvc.model.Channel;
import com.sniper.springmvc.mybatis.service.BaseService;

public interface ChannelService extends BaseService<Channel> {

	public static final String DEPRT = "deprt";
	public static final String ITEM = "item";
	public static final String LINK = "link";
	public static final String OTHER = "other";

	/**
	 * 根据类型获取记录
	 * 
	 * @param type
	 * @param enabled
	 * @param sort
	 * @return
	 */
	@DataSource(DataSourceValue.SLAVE)
	public List<Channel> getChannelsByType(Integer[] type, Boolean enabled,
			String sort);

	/**
	 * 把记录转成 ztree格式
	 * 
	 * @param channels
	 * @param treeFidAttr
	 * @return
	 */
	@DataSource(DataSourceValue.SLAVE)
	public String changeChannelsToTree(List<Channel> channels,
			String treeFidAttr);

	/**
	 * 获取指定条件的所有记录
	 * 
	 * @param fid父级id
	 *            必须参数
	 * @param status
	 *            状态 null表示不设置
	 * @param showHome
	 *            是否是辅助看木 null表示不设置
	 * @param limit
	 *            限制记录数，0表示无限制
	 * @param sort
	 *            排序
	 * @return
	 */
	@DataSource(DataSourceValue.SLAVE)
	public List<Channel> getChannels(String fid, Boolean status,
			Boolean showHome, int limit, String sort);

	@DataSource(DataSourceValue.SLAVE)
	public Map<String, String> getPostChannelsMap(List<Channel> channels);

	@DataSource(DataSourceValue.SLAVE)
	public Channel checkName(String name);
}
