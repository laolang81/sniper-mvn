package com.sniper.springmvc.action.topic;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.sniper.springmvc.action.RootController;
import com.sniper.springmvc.freemarker.FreeMarkerUtil;
import com.sniper.springmvc.freemarker.TopicResolver;

import freemarker.template.TemplateHashModel;

@Controller
public class TopicBaseController extends RootController {

	protected TemplateHashModel subjectUtil = FreeMarkerUtil
			.getFreeMarkerStaticModel(TopicResolver.class);

	@ModelAttribute
	@Override
	public void init(Map<String, Object> map) {
		map.put("subjectUtil", subjectUtil);
		super.init(map);
	}

}
