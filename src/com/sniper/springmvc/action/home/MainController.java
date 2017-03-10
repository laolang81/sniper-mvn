package com.sniper.springmvc.action.home;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("main")
public class MainController extends HomeBaseController {

	@RequestMapping("")
	public String main() {

		return "home/index/main.ftl";
	}
}
