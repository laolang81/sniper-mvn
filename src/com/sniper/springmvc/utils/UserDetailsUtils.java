package com.sniper.springmvc.utils;

import java.io.IOException;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sniper.springmvc.model.AdminGroup;
import com.sniper.springmvc.model.AdminUser;
import com.sniper.springmvc.mybatis.service.impl.AdminUserService;
import com.sniper.springmvc.security.SpringContextUtil;
import com.sniper.springmvc.shiro.oauth.Constants;

/**
 * 当用户登录空的时候这里会出错
 * 
 * @author suzhen
 * 
 */
public class UserDetailsUtils {

	private static AdminUserService adminUserService;
	static {
		adminUserService = (AdminUserService) SpringContextUtil
				.getBean(AdminUserService.class);
	}

	private Subject subject;

	public UserDetailsUtils() {
		subject = SecurityUtils.getSubject();
	}

	public Subject getSubject() {
		return subject;
	}

	public String getPrincipal() {
		if (subject != null) {
			return subject.getPrincipals().getPrimaryPrincipal().toString();
		}
		return "";
	}

	/**
	 * 判断用户是否拥有某个角色的权限 <shiro:hasRole name="admin"> <!— 有权限 —> </shiro:hasRole>
	 * 
	 * @RequiresRoles("admin") 作用于方法 属于粗的控制
	 * @param role
	 * @return
	 */
	public boolean validRole(String role) {
		if (subject != null && subject.hasRole(role)) {
			return true;
		}
		return false;
	}

	public boolean hasRole(String role) {
		AdminUser adminUser = getAminUser();
		if (adminUser != null) {
			List<AdminGroup> adminGroups = adminUser.getAdminGroup();
			for (AdminGroup adminGroup : adminGroups) {
				if (adminGroup.getValue().equals(role)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 为了
	 * 
	 * @return
	 */
	public AdminUser getAminUser() {
		AdminUser adminUser = (AdminUser) subject.getSession().getAttribute(
				Constants.CURRENT_USER.toString());
		// 当用户不是身份专一的时候读取缓存
		// if (!subject.isRunAs()
		// && subject.getSession().getAttribute(
		// Constants.CURRENT_USER.toString()) != null) {
		// return (AdminUser) subject.getSession().getAttribute(
		// Constants.CURRENT_USER.toString());
		// }

		if (adminUser == null && ValidateUtil.isValid(subject.getPrincipal())) {
			adminUser = adminUserService.getUserAndGroupAndRight(subject
					.getPrincipal().toString());
			subject.getSession().setAttribute(
					Constants.CURRENT_USER.toString(), adminUser);
			return adminUser;
		}
		return adminUser;
	}

	/**
	 * 返回用户拥有的处室
	 * 
	 * @param siteid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Integer> getSiteid(String siteid) {

		ObjectMapper mapper = new ObjectMapper();
		try {
			List<Integer> b = mapper.readValue(siteid, List.class);
			return b;
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;

	}
}