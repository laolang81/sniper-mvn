package com.sniper.springmvc.converter;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.sniper.springmvc.model.AdminGroup;
import com.sniper.springmvc.mybatis.service.impl.AdminGroupService;

/**
 * 用户添加是用户组的转换
 * 
 * @author sniper
 * 
 */
@Component
public class GroupAddUserConverter implements
		Converter<String[], List<AdminGroup>> {

	public GroupAddUserConverter() {
		// TODO Auto-generated constructor stub
	}

	@Resource
	AdminGroupService adminGroupService;

	@Override
	public List<AdminGroup> convert(String[] f) {
		List<AdminGroup> adminGroups = new ArrayList<>();
		if (f.length > 0) {
			for (int i = 0; i < f.length; i++) {
				AdminGroup adminGroup = adminGroupService.get("getOne", f[i]);
				adminGroups.add(adminGroup);
			}
		}
		return adminGroups;
	}

}
