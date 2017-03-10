package com.sniper.springmvc.converter;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.sniper.springmvc.model.Files;
import com.sniper.springmvc.mybatis.service.impl.FilesService;
import com.sniper.springmvc.utils.ValidateUtil;

/**
 * 自定义类型转换器
 * 
 * @author sniper
 * 
 */
@Component
public class PostAddFilesConverter implements Converter<String, List<Files>> {

	@Resource
	private FilesService filesService;

	public PostAddFilesConverter() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<Files> convert(String source) {

		List<Files> adminRights = new ArrayList<>();

		if (ValidateUtil.isValid(source)) {
			String[] sIDs = source.split(",");
			for (int i = 0; i < sIDs.length; i++) {

				Files adminRight = filesService.get(sIDs[i]);
				adminRights.add(adminRight);
			}
		}

		return adminRights;
	}

}
