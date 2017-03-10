package com.sniper.springmvc.action.home;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sniper.springmvc.model.Files;

@Controller
@RequestMapping("/video")
public class VideoController extends HomeBaseController {

	@RequestMapping("{id}")
	public String index(Map<String, Object> map, @PathVariable("id") String id) {

		Files files = filesService.get(id);
		if (files == null) {
			return redirect("/project");
		}
		map.put("files", files);
		return forward("home/index/video-view.ftl");
	}
}
