package com.sniper.springmvc.action.api;

import java.text.ParseException;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/apii")
public class ImagesController extends ApiBaseController {

	@ResponseBody
	@RequestMapping("sendPostImages/{id}/{st}/{et}")
	public Map<String, Map<String, Object>> sendPostImages(
			@PathVariable("id") String id, @PathVariable("st") String st,
			@PathVariable("et") String et) throws ParseException {
		Map<String, Map<String, Object>> result = getAttachments(id, st, et);
		return result;
	}
}
