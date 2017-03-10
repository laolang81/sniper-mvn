package com.sniper.springmvc.mybatis.service.impl;

import com.sniper.springmvc.model.SdItemsSubjects;
import com.sniper.springmvc.model.SdSubjects;
import com.sniper.springmvc.mybatis.service.BaseService;

public interface SdItemsSubjectsService extends BaseService<SdItemsSubjects> {

	public void itemsSubject(SdSubjects subjects, String[] items);

	public void changeFirstItem(int sid, int sourceItem, int toItem);

	public int changeItem(int itemid, int itemidMove);

	public Integer[] getItems(String sid);
}
