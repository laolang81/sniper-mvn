package com.sniper.springmvc.mybatis.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import com.sniper.springmvc.model.AdminGroup;
import com.sniper.springmvc.model.AdminUser;
import com.sniper.springmvc.model.SdDepartments;
import com.sniper.springmvc.model.SdUser;
import com.sniper.springmvc.model.SdUserConnect;
import com.sniper.springmvc.mybatis.dao.BaseDao;
import com.sniper.springmvc.utils.FilesUtil;

@Service("sdUserConnectService")
public class SdUserConnectServiceImpl extends BaseServiceImpl<SdUserConnect>
		implements SdUserConnectService {

	public static String DEFAULT_PASSWORD = "swt@89013372";

	@Resource
	SdDepartmentsService departmentsService;

	@Resource
	AdminUserService adminUserService;

	@Resource
	SdUserService userService;

	@Override
	public void setDao(BaseDao<SdUserConnect> dao) {
		super.setDao(dao);
	}

	@Override
	public boolean login(String uname, String did, String dname) {
		// 检查连接表是否已经存在
		Map<String, Object> params = new HashMap<>();
		params.put("uname", uname);
		params.put("sourceid", did);
		if (this.find("find", params) != null) {
			return true;
		}
		// 查找处室id
		Map<String, Object> depMap = new HashMap<>();
		depMap.put("shortName", dname);
		SdDepartments departments = (SdDepartments) departmentsService.find(
				"find", params);
		if (departments == null) {
			return false;
		}
		// 查找当前表用户,在新表中查询
		Map<String, Object> userMap = new HashMap<>();
		userMap.put("name", uname);
		AdminUser adminUser = (AdminUser) this.find("find", userMap);
		if (adminUser == null) {
			// 新表查询中是空，在查询老表
			SdUser sdUser = userService.getUserByName(uname);
			// 如果旧表也是空F
			if (sdUser == null) {
				adminUser = new AdminUser();
				adminUser.setId(FilesUtil.getUUIDName("", false));
				adminUser.setName(uname);
				adminUser.setEnabled(true);
				adminUser.setLocked(false);
				adminUser.setNickName(dname);
				adminUser.setSiteid("");
				adminUser.setPassword(DigestUtils.sha1Hex(DEFAULT_PASSWORD));
				// 插入用户组
				List<AdminGroup> groups = new ArrayList<>();
				AdminGroup adminGroup = new AdminGroup();
				switch (dname) {
				case "信息中心":
					// 管理员
					adminGroup.setId("72672be3-1bfd-4aef-823d-c782e26d7e5d");
					break;
				default:
					// 普通管理员
					adminGroup.setId("b03bdbec-200d-4871-b628-b66ee93d2864");
					break;
				}
				groups.add(adminGroup);
				adminUser.setAdminGroup(groups);
				adminUserService.insert(adminUser);
			} else {
				// 更改当前用户组
				sdUser.setGroup(dname);
				adminUser = adminUserService.regUser(sdUser, DEFAULT_PASSWORD);
			}
		}
		SdUserConnect connect = new SdUserConnect();
		connect.setDepid(departments.getId().toString());
		connect.setDname(dname);
		connect.setSourceid(did);
		connect.setUname(uname);
		connect.setUid(adminUser.getId());
		this.insert(connect);
		return true;
	}
}
