package com.sniper.springmvc.action.error;

import org.apache.shiro.authz.AuthorizationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.sniper.springmvc.action.RootController;
import com.sniper.springmvc.utils.BaseHref;

/**
 * 设置设置shrio的非法访问显示页面
 * 
 * @author suzhen
 * 
 */
@ControllerAdvice
public class ErrorController extends RootController {
	/**
	 * 异常优先级，里错误越近，越优先被打印出 方法不能传入map，如果把异常信息传到页面上，需要使用modelAndView
	 * 
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(value = AuthorizationException.class)
	public ModelAndView NuAccess(Exception ex) {
		BaseHref baseHref = new BaseHref();
		baseHref.setBaseHref(getBasePath());
		ModelAndView mv = new ModelAndView("/error/UnAccess.jsp");
		mv.addObject("baseHref", baseHref);
		return mv;
	}

}
