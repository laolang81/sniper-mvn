package com.sniper.springmvc.junit;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sniper.springmvc.model.SdDepartments;
import com.sniper.springmvc.mybatis.service.impl.SdAttachmentsService;
import com.sniper.springmvc.mybatis.service.impl.SdDepartmentsService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:beans.xml")
public class SdAttachmentsJuni {

	@Resource
	SdAttachmentsService attachmentsService;
	
	
	@Resource
	SdDepartmentsService departmentsService;

	@Test
	public void test() {
		List<SdDepartments> departments = departmentsService.query("select", null);
		
		System.out.println(departments.size());
		for (SdDepartments sdDepartments : departments) {
			System.out.println(sdDepartments);
		}
	}
}
