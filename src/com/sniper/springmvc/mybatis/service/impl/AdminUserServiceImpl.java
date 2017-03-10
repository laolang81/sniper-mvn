package com.sniper.springmvc.mybatis.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.CredentialsException;
import org.springframework.stereotype.Service;

import com.sniper.springmvc.model.AdminGroup;
import com.sniper.springmvc.model.AdminUser;
import com.sniper.springmvc.model.AdminUserGroup;
import com.sniper.springmvc.model.SdUser;
import com.sniper.springmvc.mybatis.dao.BaseDao;
import com.sniper.springmvc.utils.FilesUtil;
import com.sniper.springmvc.utils.UserDetailsUtils;
import com.sniper.springmvc.utils.ValidateUtil;

@Service("adminUserService")
public class AdminUserServiceImpl extends BaseServiceImpl<AdminUser> implements AdminUserService {

	@Resource
	AdminGroupService groupService;

	@Resource
	AdminUserGroupService adminUserGroupService;

	@Resource
	AdminRightService rightService;

	@Resource
	SdUserService sdUserService;

	@Override
	@Resource(name = "adminUserDao")
	public void setDao(BaseDao<AdminUser> dao) {
		super.setDao(dao);
	}

	@Override
	public AdminUser validateByName(String username) {
		Map<String, Object> params = new HashMap<>();
		params.put("name", username);
		List<AdminUser> adminUser = this.query("find", params);
		if (adminUser.size() > 0) {
			return adminUser.get(0);
		}
		return null;
	}

	@Override
	public AdminUser getUserAndGroupAndRight(String username) {
		AdminUser adminUser = this.get("get_user_by_name", username);
		List<AdminGroup> adminGroups = adminUser.getAdminGroup();
		if (adminGroups.size() > 0) {
			for (int i = 0; i < adminGroups.size(); i++) {
				AdminGroup adminGroup = groupService.get(adminGroups.get(i).getId());
				adminGroups.set(i, adminGroup);
			}
		}
		return adminUser;
	}

	@Override
	public AdminUser validateByNickName(String username) {
		Map<String, Object> params = new HashMap<>();
		params.put("nickName", username);
		List<AdminUser> adminUser = this.query("find", params);
		if (adminUser.size() > 0) {
			return adminUser.get(0);
		}
		return null;
	}

	@Override
	public AdminUser validateByEmail(String email) {
		Map<String, Object> params = new HashMap<>();
		params.put("email", email);
		List<AdminUser> adminUser = this.query("find", params);
		if (adminUser.size() > 0) {
			return adminUser.get(0);
		}
		return null;
	}

	@Override
	public List<AdminUser> findListByEmail(String email) {
		Map<String, Object> params = new HashMap<>();
		params.put("email", email);
		List<AdminUser> adminUser = this.query("find", params);

		return adminUser;
	}

	@Override
	public boolean validateUser(String name, String password) {
		Map<String, Object> params = new HashMap<>();
		params.put("namePassword", name);
		params.put("password", password);
		AdminUser adminUser = (AdminUser) this.query("find", params);
		if (adminUser != null) {
			return true;
		}
		return false;
	}

	@Override
	public void changePassword(String password_old, String password_c) {
		UserDetailsUtils detailsUtils = new UserDetailsUtils();
		AdminUser adminUser = this.validateByName(detailsUtils.getPrincipal());

		String passwd = DigestUtils.sha1Hex(password_old);
		if (adminUser.getPassword().equals(passwd)) {
			adminUser.setPassword(DigestUtils.sha1Hex(password_c));
			this.update(adminUser);
		} else {
			throw new CredentialsException("旧密码不匹配");
		}
	}

	@Override
	public int insert(AdminUser t) {
		int result = super.insert(t);
		List<AdminGroup> groups = t.getAdminGroup();
		if (groups.size() > 0) {
			for (AdminGroup adminGroup : groups) {
				AdminUserGroup adminUserGroup = new AdminUserGroup();
				adminUserGroup.setGid(adminGroup.getId());
				adminUserGroup.setUid(t.getId());
				adminUserGroupService.insert(adminUserGroup);
			}
		}
		return result;
	}

	@Override
	public int update(AdminUser t) {

		if (t.getAdminGroup().size() > 0) {
			String uid = t.getId();
			adminUserGroupService.delete("delete", uid);
			List<AdminGroup> groups = t.getAdminGroup();
			for (AdminGroup adminGroup : groups) {
				AdminUserGroup adminUserGroup = new AdminUserGroup();
				adminUserGroup.setGid(adminGroup.getId());
				adminUserGroup.setUid(uid);
				// 需要插入的用户组
				adminUserGroupService.insert(adminUserGroup);
			}
		}

		return super.update(t);
	}

	@Override
	public int delete(String id) {
		adminUserGroupService.delete(id);
		return super.delete(id);
	}

	/**
	 * 用户转移，，送老表中转移到新表
	 */
	@Override
	public AdminUser regUser(SdUser sdUser, String password) {

		// 检测用户是否存在
		AdminUser adminUser = this.validateByName(sdUser.getUsername());
		if (adminUser != null) {
			return adminUser;
		}
		// 执行插入操作
		adminUser = new AdminUser();
		adminUser.setId(FilesUtil.getUUIDName("", false));
		adminUser.setName(sdUser.getUsername());
		adminUser.setEnabled(true);
		adminUser.setMobile(sdUser.getMobile());
		adminUser.setLocked(false);
		adminUser.setNickName(sdUser.getFullname());
		adminUser.setPassword(DigestUtils.sha1Hex(password));
		adminUser.setSiteid(sdUser.getSiteid());
		adminUser.setSourceUid(String.valueOf(sdUser.getId()));
		// 插入用户组
		List<AdminGroup> groups = new ArrayList<>();
		AdminGroup adminGroup = new AdminGroup();
		switch (sdUser.getGroup()) {
		// 超级管理员
		case "administrator":
			adminGroup.setId("72672be3-1bfd-4aef-823d-c782e26d7e5d");
			break;
		// 管理员
		case "信息中心":
		case "PicturesAudit":
			adminGroup.setId("b03bdbec-200d-4871-b628-b66ee93d2864");
			break;
		// 一级发布组
		case "member":
		case "author":
			adminGroup.setId("344d64e1-b116-4056-9839-ab3d1f8d9691");
			break;
		default:
			// 默认二级发布组
			adminGroup.setId("9f032ecb-52c0-4543-823f-ccddfd0a4b89");
			break;
		}

		if (ValidateUtil.isValid(adminGroup)) {
			groups.add(adminGroup);
			adminUser.setAdminGroup(groups);
		}

		// ch插入用户
		this.insert(adminUser);
		return adminUser;
	}

	/**
	 * 获取用户这里获取的用户不能用于数据插入使用
	 */
	@Override
	public AdminUser getUser(String sourceUid) {
		if (StringUtils.isNumeric(sourceUid)) {
			SdUser sdUser = sdUserService.get(sourceUid);
			if (sdUser != null) {
				AdminUser adminUser = new AdminUser();
				adminUser.setName(sdUser.getUsername());
				return adminUser;
			}
		}
		return this.get(sourceUid);
	}

	/**
	 * 更改密码
	 */
	@Override
	public int changePasswd(String username, String passwd) {
		AdminUser adminUser = this.validateByName(username);
		if (adminUser != null) {
			adminUser.setPassword(DigestUtils.sha1Hex(passwd));
			this.update(adminUser);
		}
		return 0;
	}

}