package com.sniper.springmvc.action.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sniper.springmvc.model.SdLoginLog;
import com.sniper.springmvc.mybatis.service.impl.SdLoginLogService;
import com.sniper.springmvc.mybatis.service.impl.SdUserConnectService;
import com.sniper.springmvc.utils.HttpRequestUtils;
import com.sniper.springmvc.utils.ValidateUtil;

@RequestMapping(value = "${adminPath}")
@Controller
public class ApiLoginController extends ApiBaseController {

	@Resource
	SdUserConnectService userConnectService;

	@Resource
	SdLoginLogService loginLogService;

	public static final List<String> REFERERS = new ArrayList<>();
	static {
		REFERERS.add("http://web.sdswt.gov.cn/");
		REFERERS.add("http://java.laolang.com:8080/java-sdcom/");
	}

	@RequestMapping("login/nologin")
	public Map<String, String> noLogin(@RequestParam("uname") String uname,
			@RequestParam("dname") String dname, @RequestParam("did") String did) {

		String referer = request.getHeader("referer");
		// 判断来源url是否允许
		referer = referer.substring(0,
				referer.indexOf("/", referer.indexOf(".") + 1));
		boolean isReferer = false;
		String loginError = "";
		if (ValidateUtil.isValid(referer)) {
			for (String s : REFERERS) {
				if (referer.startsWith(s)) {
					isReferer = true;
					break;
				}
			}
		}
		Map<String, String> result = new HashMap<>();
		if (isReferer == false) {
			loginError = "不允许登陆的域名";
			result.put("code", "0");
			result.put("msg", loginError);
			return result;
		}

		SdLoginLog log = new SdLoginLog();
		log.setIp(HttpRequestUtils.getRealIP(request));
		log.setStime(new Date());
		log.setUname(uname);
		log.setAgent(request.getHeader("user-agent"));

		if (userConnectService.login(uname, did, dname)) {
			result.put("code", "1");
			log.setMessage("succeed");
			result.put("msg", "登录成功");
		} else {
			loginError = "登录失败";
			result.put("code", "0");
			result.put("msg", loginError);
			log.setMessage(loginError);
		}
		loginLogService.insert(log);
		return result;
	}

	public static void main(String[] args) {
		String a = "http://web.sdswt.gov.cn/sssss";
		System.out.println(a.indexOf("/", a.indexOf(".")));
		System.out.println(a.substring(0, a.indexOf("/", a.indexOf(".")) + 1));
	}
}
