package com.sniper.springmvc.action.junit;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sniper.springmvc.model.SdStatIndex;
import com.sniper.springmvc.model.SdSubjects;
import com.sniper.springmvc.mybatis.service.impl.SdStatIndexService;
import com.sniper.springmvc.mybatis.service.impl.SdSubjectsService;
import com.sniper.springmvc.mybatis.service.impl.SdViewSubjectService;

@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:beans.xml")
public class SubjectViewJunit {

	@Resource
	SdStatIndexService statIndexService;

	@Resource
	SdSubjectsService subjectsService;

	@Resource
	SdViewSubjectService sdViewSubjectService;

	@Test
	public void statIndex() {

//		Map<String, Object> parasm = new HashMap<>();
//		parasm.put("date", new Date());
//
//		List<SdStatIndex> indexs = statIndexService.query("select", parasm);
//
//		System.out.println(indexs);
//
//		SdSubjects subjects = new SdSubjects();
//		subjects.setSubject("2016-09-23");
//		int update = subjectsService.update("subjectViewZero", subjects);
//		System.out.println(update);

		List<Map<String, Object>> postMonthCount = sdViewSubjectService
				.monthCount(1, null, null);
		for (Map<String, Object> map : postMonthCount) {
			System.out.println(map);
		}
		
	}
}
