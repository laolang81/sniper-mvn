package com.sniper.springmvc.converter;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.sniper.springmvc.model.AdminRight;
import com.sniper.springmvc.mybatis.service.impl.AdminRightService;
import com.sniper.springmvc.utils.ValidateUtil;

/**
 * 自定义类型转换器
 * 
 * @author sniper
 * 
 */
@Component
final public class GroupAddRightConverter implements
		Converter<String, List<AdminRight>> {

	public GroupAddRightConverter() {

	}

	@Resource
	private AdminRightService adminRightService;

	@Override
	public List<AdminRight> convert(String source) {

		List<AdminRight> adminRights = new ArrayList<>();

		if (ValidateUtil.isValid(source)) {
			String[] sIDs = source.split(",");
			for (int i = 0; i < sIDs.length; i++) {
				AdminRight adminRight = adminRightService.get(sIDs[i]);
				adminRights.add(adminRight);
			}
		}
		return adminRights;
	}

}
