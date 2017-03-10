package com.sniper.springmvc.action.junit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sniper.springmvc.model.SdDepartments;
import com.sniper.springmvc.model.SdItems;
import com.sniper.springmvc.model.SdPageIndex;
import com.sniper.springmvc.model.SdSubjects;
import com.sniper.springmvc.model.SdViewSubject;
import com.sniper.springmvc.mybatis.service.impl.SdDepartmentsService;
import com.sniper.springmvc.mybatis.service.impl.SdItemsService;
import com.sniper.springmvc.mybatis.service.impl.SdPageIndexService;
import com.sniper.springmvc.mybatis.service.impl.SdSubjectsService;
import com.sniper.springmvc.mybatis.service.impl.SdViewSubjectService;
import com.sniper.springmvc.utils.ItemsUtil;

@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:beans.xml")
public class ItemsJunit {

	@Resource
	SdItemsService itemsService;

	@Resource
	SdViewSubjectService viewSubjectService;

	@Resource
	SdSubjectsService subjectsService;

	@Resource
	SdDepartmentsService departmentsService;

	@Resource
	SdPageIndexService pageIndexService;

	@Test
	public void items() {
		Map<String, Object> params = new HashMap<>();
		params.put("order", "t.itemid desc");
		params.put("deprtid", 406);
		params.put("enabled", 1);
		List<SdItems> items = itemsService.query("select", params);

		// 按照添加顺序倒叙排序
		// 自然排序，如果不自然排序的话，相同的栏目就无法靠在一起
		Map<String, SdItems> treeMap = ItemsUtil.sortNamed(items);
		for (Entry<String, SdItems> entry : treeMap.entrySet()) {
			System.out.println(entry.getKey() + ":"
					+ entry.getValue().getName());

		}
		System.out.println("id DESC排序");
		treeMap = ItemsUtil.sortItemidDesc(treeMap);
		for (Entry<String, SdItems> entry : treeMap.entrySet()) {
			System.out.println(entry.getKey() + ":"
					+ entry.getValue().getName());

		}

		System.out.println("id ASC排序");
		treeMap = ItemsUtil.sortItemidAsc(treeMap);
		for (Entry<String, SdItems> entry : treeMap.entrySet()) {
			System.out.println(entry.getKey() + ":"
					+ entry.getValue().getName());

		}
	}

	public void subjectView() {
		List<SdViewSubject> viewSubjects = viewSubjectService.query("select",
				null);
		System.out.println(viewSubjects.size());
	}

	public void subject() {
		List<SdSubjects> subjects = subjectsService.query("select", null);
		System.out.println(subjects.size());
	}

	public void dep() {
		List<SdDepartments> departments = departmentsService.query("select",
				null);
		System.out.println(departments.size());
	}

	public void pageinde() {
		List<SdPageIndex> indexs = pageIndexService.query("select", null);
		System.out.println(indexs.size());
	}
}
