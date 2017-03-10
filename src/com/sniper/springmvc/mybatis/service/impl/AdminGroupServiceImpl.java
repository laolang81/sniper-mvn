package com.sniper.springmvc.mybatis.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sniper.springmvc.model.AdminGroup;
import com.sniper.springmvc.model.AdminGroupRight;
import com.sniper.springmvc.model.AdminRight;
import com.sniper.springmvc.mybatis.dao.BaseDao;

@Service("adminGroupService")
public class AdminGroupServiceImpl extends BaseServiceImpl<AdminGroup>
		implements AdminGroupService {

	@Resource
	AdminGroupRightService adminGroupRightService;

	@Override
	@Resource(name = "adminGroupDao")
	public void setDao(BaseDao<AdminGroup> dao) {
		super.setDao(dao);
	}

	@Override
	public int insert(AdminGroup t) {
		int result = super.insert(t);
		List<AdminRight> adminRights = t.getAdminRight();
		for (AdminRight adminRight : adminRights) {
			AdminGroupRight adminGroupRight = new AdminGroupRight();
			adminGroupRight.setGroup_id(t.getId());
			adminGroupRight.setRight_id(adminRight.getId());
			adminGroupRightService.insert(adminGroupRight);
		}
		return result;
	}

	/**
	 * 重写方法更新关联关系
	 */
	@Override
	public int update(AdminGroup t) {

		if (t.getAdminRight().size() > 0) {
			// 删除
			adminGroupRightService.delete(t.getId());
			// 新增
			List<AdminRight> adminRights = t.getAdminRight();
			for (AdminRight adminRight : adminRights) {
				AdminGroupRight adminGroupRight = new AdminGroupRight();
				adminGroupRight.setGroup_id(t.getId());
				adminGroupRight.setRight_id(adminRight.getId());
				adminGroupRightService.insert(adminGroupRight);
			}
		}

		return super.update(t);
	}

	@Override
	public int delete(String id) {
		adminGroupRightService.delete(id);
		return super.delete(id);
	}

}
