package com.sniper.springmvc.mybatis.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sniper.springmvc.model.Channel;
import com.sniper.springmvc.model.ChannelGroup;
import com.sniper.springmvc.model.ChannelsGroups;
import com.sniper.springmvc.mybatis.dao.BaseDao;

@Service("channelGroupService")
public class ChannelGroupServiceImpl extends BaseServiceImpl<ChannelGroup>
		implements ChannelGroupService {

	@Resource
	ChannelsGroupsService channelsGroupsService;

	@Resource(name = "channelGroupDao")
	@Override
	public void setDao(BaseDao<ChannelGroup> dao) {
		super.setDao(dao);
	}

	@Override
	public int insert(ChannelGroup t) {
		int r = super.insert(t);
		List<Channel> channels = t.getChannels();
		for (Channel channel : channels) {
			ChannelsGroups channelsGroups = new ChannelsGroups();
			channelsGroups.setChannel_id(channel.getId());
			channelsGroups.setGroup_id(t.getId());
			channelsGroupsService.insert(channelsGroups);
		}
		return r;
	}

	@Override
	public int update(ChannelGroup t) {
		if (t.getChannels().size() > 0) {
			channelsGroupsService.delete(t.getId());
			List<Channel> channels = t.getChannels();
			for (Channel channel : channels) {
				ChannelsGroups channelsGroups = new ChannelsGroups();
				channelsGroups.setChannel_id(channel.getId());
				channelsGroups.setGroup_id(t.getId());
				channelsGroupsService.insert(channelsGroups);
			}
		}

		return super.update(t);
	}

	@Override
	public int delete(String id) {
		channelsGroupsService.delete(id);
		return super.delete(id);
	}

}
