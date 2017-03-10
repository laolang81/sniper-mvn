package com.sniper.springmvc.action.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.sniper.springmvc.model.AdminUser;
import com.sniper.springmvc.model.UserRunAs;
import com.sniper.springmvc.mybatis.service.impl.AdminUserService;
import com.sniper.springmvc.mybatis.service.impl.UserRunAsService;
import com.sniper.springmvc.utils.ValidateUtil;

@Controller
@RequestMapping("${adminPath}/admin-center")
public class AdminCenterCrontroller extends AdminBaseController {

	@Resource
	UserRunAsService asService;

	@Resource
	AdminUserService userService;

	@RequiresPermissions("admin:center:profile")
	@RequestMapping(value = "profile", method = RequestMethod.GET)
	public String profile(Map<String, Object> map) {
		map.put("adminUser", getAdminUser());
		return forward("/admin/admin-center/profile.jsp");
	}

	@RequiresPermissions("admin:center:profile")
	@RequestMapping(value = "profile", method = RequestMethod.POST)
	public String profile(AdminUser adminUser, Map<String, Object> map) {

		List<String> errors = new ArrayList<>();
		if (adminUser.getId().equals(getAdminUser().getId())) {
			adminUserService.update(adminUser);
			errors.add("修改成功");
			map.put("adminUser", adminUser);
		} else {
			errors.add("修改失败");
			map.put("adminUser", getAdminUser());
		}
		map.put("errors", errors);
		return forward("/admin/admin-center/profile.jsp");
	}

	/**
	 * 用户授权列表
	 * 
	 * @param map
	 * @return
	 */
	@RequiresPermissions("admin:center:grantUsers")
	@RequestMapping("grantUsers")
	public String grantUsers(Map<String, Object> map) {
		this.centerPublic(map);
		return forward("/admin/admin-center/grantUsers.ftl");
	}

	/**
	 * 可切换身份列表
	 * 
	 * @return
	 */
	@RequiresPermissions("admin:center:switchUsers")
	@RequestMapping("switchUsers")
	public String switchUsers(Map<String, Object> map) {

		// 我授权的用户列表，这里可以取消授权
		List<UserRunAs> fromUserIds = asService.findToUserIds(getAdminUser()
				.getId());
		map.put("fromUserIds", fromUserIds);

		this.centerPublic(map);
		return forward("/admin/admin-center/switchUsers.ftl");
	}

	/**
	 * 可回收权限列表
	 * 
	 * @return
	 */
	@RequiresPermissions("admin:center:revokeUsers")
	@RequestMapping("revokeUsers")
	public String revokeUsers(Map<String, Object> map) {

		// 这里是别人给我的权限,我可以随意切换
		List<UserRunAs> toUserIds = asService.findFromUserIds(getAdminUser()
				.getId());
		map.put("toUserIds", toUserIds);
		this.centerPublic(map);
		return forward("/admin/admin-center/revokeUsers.ftl");
	}

	/**
	 * 数据组装
	 * 
	 * @param map
	 */
	public void centerPublic(Map<String, Object> map) {
		List<AdminUser> adminUsers = userService.query("select", null);
		Map<String, String> userName = new HashMap<>();
		adminUsers.remove(getAdminUser());
		for (AdminUser adminUser : adminUsers) {
			userName.put(adminUser.getId(), adminUser.getName());
		}
		map.put("adminUsers", userName);
		Subject subject = SecurityUtils.getSubject();
		map.put("isRunas", subject.isRunAs());
		if (subject.isRunAs()) {
			String previousUsername = (String) subject.getPreviousPrincipals()
					.getPrimaryPrincipal();
			map.put("previousUsername", previousUsername);
		}
		map.put("currentUserame", subject.getPrincipal().toString());
	}

	/**
	 * 授权 并不表示用户已经具备权限，需要用户去接受
	 * 
	 * @param toUserId
	 * @param map
	 * @return
	 */
	@RequiresPermissions("admin:center:grant")
	@RequestMapping("grant")
	public String grant(@RequestParam("id") String toUserId,
			Map<String, Object> map) {
		AdminUser adminUser = getAdminUser();
		if (toUserId.equals(adminUser.getId())) {
			map.put("msg", "自己不能切换自己");
		} else {
			if (!asService.exists(adminUser.getId(), toUserId)) {
				asService.grantRunAs(adminUser.getId(), toUserId);
				map.put("msg", "操作成功");
			} else {
				map.put("msg", "授权已存在");
			}
		}
		return redirect("/admin-center/grantUsers");
	}

	/**
	 * 真正的用户权限切换
	 * 
	 * @param map
	 * @param toUserId
	 * @return
	 */
	@RequiresPermissions("admin:center:switchTo")
	@RequestMapping("switchTo")
	public String switchTo(Map<String, Object> map,
			@RequestParam("id") String toUserId) {

		Subject subject = SecurityUtils.getSubject();
		if (!toUserId.equals(getAdminUser().getId())) {
			AdminUser adminUser = userService.get(toUserId);
			if (asService.exists(toUserId, getAdminUser().getId())) {
				subject.runAs(new SimplePrincipalCollection(
						adminUser.getName(), ""));
			}
		}
		return redirect("/admin-center/revokeUsers");
	}

	@RequiresPermissions("admin:center:switchBack")
	@RequestMapping("switchBack")
	public String switchBack() {
		Subject subject = SecurityUtils.getSubject();
		if (subject.isRunAs()) {
			subject.releaseRunAs();
		}
		return redirect("/admin-center/revokeUsers");
	}

	@RequiresPermissions("admin:center:revoke")
	@RequestMapping("revoke")
	public String revoke(@RequestParam("id") String toUserId,
			Map<String, Object> map) {
		asService.revokeRunAs(getAdminUser().getId(), toUserId);
		map.put("msg", "操作成功");
		return redirect("/admin-center/revokeUsers");
	}

	@RequiresPermissions("admin:center:changepassword")
	@RequestMapping(value = "changepassword", method = RequestMethod.GET)
	public String changepassword(Map<String, Object> map) {
		map.put("adminUser", getAdminUser());
		return forward("/admin/admin-center/change-password.jsp");
	}

	@RequiresPermissions("admin:center:changepassword")
	@RequestMapping(value = "changepassword", method = RequestMethod.POST)
	public String changepassword(
			Map<String, Object> map,
			AdminUser adminUser,
			@RequestParam(value = "password_old", required = true, defaultValue = "") String password_old,
			@RequestParam(value = "password_n", required = true, defaultValue = "") String password_n,
			@RequestParam(value = "password_c", required = true, defaultValue = "") String password_c) {

		List<String> errors = new ArrayList<>();

		if (!ValidateUtil.isValid(password_old)) {
			errors.add("旧密码不得为空");
		}

		if (!ValidateUtil.isValid(password_n) || password_n.length() < 6) {
			errors.add("新密码必须6位以上");
		}

		if (!ValidateUtil.isValid(password_c) || !password_c.equals(password_n)) {
			errors.add("两次密码输入不一致");
		}

		try {
			if (errors.size() == 0) {
				adminUserService.changePassword(password_old, password_c);
				errors.add("密码修改成功");
			} else {
				map.put("adminUser", getAdminUser());
			}
		} catch (Exception e) {
			errors.add(e.getMessage());
		}

		map.put("errors", errors);
		return forward("/admin/admin-center/change-password.jsp");
	}
}
