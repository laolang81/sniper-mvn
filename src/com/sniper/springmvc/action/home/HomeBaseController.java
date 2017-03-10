package com.sniper.springmvc.action.home;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import com.sniper.springmvc.action.RootController;
import com.sniper.springmvc.mybatis.service.impl.ChannelService;
import com.sniper.springmvc.mybatis.service.impl.FilesService;

@Controller
public class HomeBaseController extends RootController {

	@Resource
	ChannelService channelService;

	

	@Resource
	FilesService filesService;

	public HomeBaseController() {

	}

	
}