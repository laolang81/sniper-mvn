package com.sniper.springmvc.action.ueditor;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.baidu.ueditor.ActionEnter;
import com.sniper.springmvc.utils.FilesUtil;

/**
 * ueditor后台统一入口 未使用
 * 
 * @author suzhen
 * 
 */
@Controller
@RequestMapping("myfiles")
public class UEditorController {

	/**
	 * 百度自带的
	 * 
	 * @param response
	 * @param request
	 */
	@RequestMapping("ueditor")
	public void ueditor2(HttpServletResponse response,
			HttpServletRequest request) {
		response.setContentType("application/json");
		// 网站根目录
		// String rootPath = request.getSession().getServletContext()
		// .getRealPath("/");
		String rootPath = FilesUtil.getRootDir();
		try {
			String exec = new ActionEnter(request, rootPath).exec();
			PrintWriter writer = response.getWriter();
			writer.write(exec);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
