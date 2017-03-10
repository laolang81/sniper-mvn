package com.sniper.springmvc.action.topic;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sniper.springmvc.model.Topic;
import com.sniper.springmvc.mybatis.service.impl.TopicService;

/**
 * 专题
 * 
 * @author suzhen
 * 
 */
@Controller
@RequestMapping("topic")
public class TopicController extends TopicBaseController {

	@Resource
	TopicService topicService;

	@RequestMapping("{path}")
	public String newTopic(@PathVariable("path") String path,
			Map<String, Object> map) {

		Topic topic = topicService.getUrl(path);
		if (topic == null || !topic.getEnabled()) {
			return "topic/fileNotFound.ftl";
		}
		return "topic/" + topic.getTemplate() + ".ftl";
	}
}
