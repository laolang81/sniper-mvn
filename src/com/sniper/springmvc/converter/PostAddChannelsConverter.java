package com.sniper.springmvc.converter;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.sniper.springmvc.model.Channel;
import com.sniper.springmvc.mybatis.service.impl.ChannelService;
import com.sniper.springmvc.utils.ValidateUtil;

/**
 * 自定义类型转换器
 * 
 * @author sniper
 * 
 */
@Component
public class PostAddChannelsConverter implements
		Converter<String, List<Channel>> {

	@Resource
	private ChannelService channelService;

	public PostAddChannelsConverter() {
	}

	@Override
	public List<Channel> convert(String source) {

		List<Channel> channels = new ArrayList<>();
		if (ValidateUtil.isValid(source)) {
			String[] sIDs = source.split(",");
			for (int i = 0; i < sIDs.length; i++) {
				Channel channel = channelService.get(sIDs[i]);
				channels.add(channel);
			}
		}

		return channels;
	}

}
