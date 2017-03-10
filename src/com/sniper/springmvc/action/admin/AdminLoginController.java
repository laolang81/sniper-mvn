package com.sniper.springmvc.action.admin;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import com.sniper.springmvc.action.RootController;
import com.sniper.springmvc.action.VerifyAction;
import com.sniper.springmvc.model.SdLoginLog;
import com.sniper.springmvc.model.SdUser;
import com.sniper.springmvc.mybatis.service.impl.AdminUserService;
import com.sniper.springmvc.mybatis.service.impl.MailService;
import com.sniper.springmvc.mybatis.service.impl.SdLoginLogService;
import com.sniper.springmvc.mybatis.service.impl.SdUserService;
import com.sniper.springmvc.utils.HttpRequestUtils;
import com.sniper.springmvc.utils.UserDetailsUtils;
import com.sniper.springmvc.utils.ValidateUtil;

@RequestMapping(value = "${adminPath}")
@Controller
public class AdminLoginController extends RootController {

	private final static String USER_LOGIN_NUM_KEY = "user_login_num";

	@Resource
	private MailService mailService;

	@Resource
	private JavaMailSenderImpl sender;

	@Resource
	private AdminUserService adminUserService;

	@Resource
	SdLoginLogService loginLogService;

	@Resource
	SdUserService userService;

	@RequestMapping(value = "login", method = RequestMethod.GET)
	public String login(
			Map<String, Object> map,
			@RequestParam(value = "error", required = false, defaultValue = "false") Boolean error) {
		if (isRebot()) {
			return redirect("/");
		}
		return forward("/admin/login/index.ftl", site.getName());
	}

	/**
	 * 用户登录
	 */
	@RequestMapping(value = "login", method = RequestMethod.POST)
	public String login(HttpServletRequest request, Map<String, Object> map,
			HttpServletResponse response) {

		if (isRebot()) {
			return redirect("/");
		}
		int userLoginNum = 0;
		String userLoginError = "";
		// 获取用户登录次数
		String userLoginNumCooke = HttpRequestUtils.getCookieValue(
				USER_LOGIN_NUM_KEY, request.getCookies());
		if (ValidateUtil.isValid(userLoginNumCooke)) {
			userLoginNum = Integer.valueOf(userLoginNumCooke).intValue();
		}
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String shaPassword = DigestUtils.sha1Hex(password);
		if (userLoginNum > 3) {
			// 获取HttpSession中的验证码
			String verifyCode = (String) request.getSession().getAttribute(
					VerifyAction.VALIDATECODE);
			// 获取用户请求表单中输入的验证码
			String submitCode = WebUtils.getCleanParam(request, "verifyCode");
			if (StringUtils.isEmpty(submitCode)
					|| !StringUtils
							.equals(verifyCode, submitCode.toLowerCase())) {
				userLoginError = "验证码不正确";
				map.put("loginError", userLoginError);
				map.put("loginNum", userLoginNum);
				return forward("/admin/login/index.ftl", site.getName());
			}
		}

		// 老板用户登录
		SdUser sdUser = userService.login(username, password);
		if (sdUser != null) {
			// 注册用户，如果用户存在，则修改密码
			if (adminUserService.validateByName(username) == null) {
				adminUserService.regUser(sdUser, password);
			} else {
				adminUserService.changePasswd(username, password);
			}
		}
		// 最终用户登录必须是新版
		UsernamePasswordToken token = new UsernamePasswordToken(username,
				shaPassword);
		if (request.getParameter("rememberMe") != null) {
			token.setRememberMe(true);
		}

		System.out.println("为了验证登录用户而封装的token为"
				+ ReflectionToStringBuilder.toString(token,
						ToStringStyle.MULTI_LINE_STYLE));
		// 获取当前的Subject
		Subject currentUser = SecurityUtils.getSubject();
		try {
			// 在调用了login方法后,SecurityManager会收到AuthenticationToken,并将其发送给已配置的Realm执行必须的认证检查
			// 每个Realm都能在必要时对提交的AuthenticationTokens作出反应
			// 所以这一步在调用login(token)方法时,它会走到MyRealm.doGetAuthenticationInfo()方法中,具体验证方式详见此方法
			// System.out.println("对用户[" + username + "]进行登录验证..验证开始");
			currentUser.login(token);
			// System.out.println("对用户[" + username + "]进行登录验证..验证通过");
		} catch (UnknownAccountException uae) {
			// System.out.println("对用户[" + username + "]进行登录验证..验证未通过,未知账户");
			userLoginError = "未知账户";
			map.put("loginError", userLoginError);
		} catch (IncorrectCredentialsException ice) {
			// System.out.println("对用户[" + username + "]进行登录验证..验证未通过,错误的凭证");
			userLoginError = "密码不正确";
			map.put("loginError", userLoginError);
		} catch (LockedAccountException lae) {
			// System.out.println("对用户[" + username + "]进行登录验证..验证未通过,账户已锁定");
			userLoginError = "账户已锁定";
			map.put("loginError", userLoginError);
		} catch (ExcessiveAttemptsException eae) {
			// System.out.println("对用户[" + username + "]进行登录验证..验证未通过,错误次数过多");
			userLoginError = "用户名或密码错误次数过多";
			map.put("loginError", userLoginError);
		}
		SdLoginLog log = new SdLoginLog();
		log.setIp(HttpRequestUtils.getRealIP(request));
		log.setStime(new Date());
		log.setUname(username);
		log.setAgent(request.getHeader("user-agent"));
		// 验证是否登录成功
		if (currentUser.isAuthenticated()) {
			userLoginNum = 0;
			Cookie cookie = new Cookie(USER_LOGIN_NUM_KEY,
					String.valueOf(userLoginNum));
			response.addCookie(cookie);
			log.setMessage("succeed");
			log.setPwd("");
			loginLogService.insert(log);
			return UrlBasedViewResolver.REDIRECT_URL_PREFIX + adminPath + "/";
		} else {
			token.clear();
			userLoginNum++;
			Cookie cookie = new Cookie(USER_LOGIN_NUM_KEY,
					String.valueOf(userLoginNum));
			response.addCookie(cookie);
			log.setMessage(userLoginError);
			log.setPwd(Base64.encodeBase64String(password.getBytes()));
			loginLogService.insert(log);
		}
		map.put("loginNum", userLoginNum);
		return forward("/admin/login/index.ftl", site.getName());
	}

	/**
	 * 用户登出
	 */
	@RequestMapping("logout")
	public String logout(HttpServletRequest request) {
		UserDetailsUtils detailsUtils = new UserDetailsUtils();
		// 删除和用户相关的数据
		String keyPrefix = REDIS.getKeyName(detailsUtils.getPrincipal());
		// 删除和用户所有有关的缓存
		Set<String> keys = REDIS.keys(keyPrefix + "*");
		for (String key : keys) {
			System.out.println(key);
			REDIS.del(key);
		}
		detailsUtils.getSubject().logout();
		return redirect("/");
	}

	public static void main(String[] args) {
		System.out.println(Base64.encodeBase64String("aa".getBytes()));
	}
}