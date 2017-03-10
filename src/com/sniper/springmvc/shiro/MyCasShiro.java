package com.sniper.springmvc.shiro;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.annotation.Resource;

import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cas.CasRealm;
import org.apache.shiro.subject.PrincipalCollection;

import com.sniper.springmvc.model.AdminGroup;
import com.sniper.springmvc.model.AdminRight;
import com.sniper.springmvc.model.AdminUser;
import com.sniper.springmvc.mybatis.service.impl.AdminUserService;

/**
 * Shrio-cas已经搬到buji-pac4j
 * 
 * @author suzhen
 * 
 */
public class MyCasShiro extends CasRealm {

	@Resource
	AdminUserService userService;

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {
		String username = (String) principals.getPrimaryPrincipal();
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		AdminUser user = userService.validateByName(username);
		List<AdminGroup> adminGroups = user.getAdminGroup();
		List<String> roles = new ArrayList<>();
		List<String> pers = new ArrayList<>();
		for (AdminGroup adminGroup : adminGroups) {
			roles.add(adminGroup.getName());
			for (AdminRight adminRight : adminGroup.getAdminRight()) {
				pers.add(adminRight.getUrl());
			}
		}

		authorizationInfo.setRoles(new HashSet<>(roles));
		authorizationInfo.setStringPermissions(new HashSet<>(pers));
		return authorizationInfo;
	}
}
