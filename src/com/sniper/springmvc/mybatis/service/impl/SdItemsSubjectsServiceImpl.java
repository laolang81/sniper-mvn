package com.sniper.springmvc.mybatis.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sniper.springmvc.model.SdItemsSubjects;
import com.sniper.springmvc.model.SdSubjects;
import com.sniper.springmvc.mybatis.dao.BaseDao;
import com.sniper.springmvc.utils.ValidateUtil;

@Service("sdItemsSubjectsSerive")
public class SdItemsSubjectsServiceImpl extends
		BaseServiceImpl<SdItemsSubjects> implements SdItemsSubjectsService {

	@Resource(name = "sdItemsSubjectsDao")
	@Override
	public void setDao(BaseDao<SdItemsSubjects> dao) {
		super.setDao(dao);
	}

	/**
	 * 栏目新闻关联
	 */
	@Override
	public void itemsSubject(SdSubjects subjects, String[] items) {
		if (ValidateUtil.isValid(subjects.getSid())
				&& ValidateUtil.isValid(items)) {
			// 线删除所有的
			this.delete(subjects.getSid() + "");
			// 在添加
			for (int i = 0; i < items.length; i++) {
				SdItemsSubjects itemsSubjects = new SdItemsSubjects();
				itemsSubjects.setItemid(Integer.valueOf(items[i]));
				itemsSubjects.setSubjectid(subjects.getSid());
				this.insert(itemsSubjects);
			}
		}
	}

	/**
	 * 修改首发栏目
	 */
	@Override
	public void changeFirstItem(int sid, int sourceItem, int toItem) {
		if (sourceItem != toItem) {
			Map<String, Object> params = new HashMap<>();
			params.put("sid", sid);
			params.put("itemid", sourceItem);
			SdItemsSubjects itemsSubjects = (SdItemsSubjects) this.find("find",
					params);
			if (itemsSubjects != null) {
				itemsSubjects.setItemid(toItem);
				this.update(itemsSubjects);
			}
		}
	}

	/**
	 * 返回修改的记录数
	 */
	@Override
	public int changeItem(int itemid, int itemidMove) {
		SdItemsSubjects t = new SdItemsSubjects();
		t.setId(itemid);
		t.setItemid(itemidMove);
		int a = 0;
		try {
			a = this.update("updateItemid", t);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return a;

	}

	@Override
	public Integer[] getItems(String sid) {
		Map<String, Object> params = new HashMap<>();
		params.put("sid", sid);
		List<SdItemsSubjects> itemsSubjects = this.query("select", params);
		Integer[] its = new Integer[itemsSubjects.size()];
		int length = itemsSubjects.size();
		for (int i = 0; i < length; i++) {
			its[i] = itemsSubjects.get(i).getItemid();
		}
		return its;
	}
}
