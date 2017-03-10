package com.sniper.springmvc.mybatis.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sniper.springmvc.model.Topic;
import com.sniper.springmvc.mybatis.dao.BaseDao;

@Service("topicService")
public class TopicServiceImpl extends BaseServiceImpl<Topic> implements
		TopicService {

	@Resource(name = "topicDao")
	@Override
	public void setDao(BaseDao<Topic> dao) {
		super.setDao(dao);
	}

	@Override
	public Topic getUrl(String path) {

		Map<String, Object> params = new HashMap<>();
		params.put("url", path);
		params.put("enabled", true);
		return (Topic) this.find("find", params);
	}

}
