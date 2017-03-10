package com.sniper.springmvc.mybatis.service.impl;

import com.sniper.springmvc.model.Topic;
import com.sniper.springmvc.mybatis.service.BaseService;

public interface TopicService extends BaseService<Topic> {

	public Topic getUrl(String path);

}
