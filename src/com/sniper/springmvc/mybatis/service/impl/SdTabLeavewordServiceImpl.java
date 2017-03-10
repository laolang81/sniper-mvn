package com.sniper.springmvc.mybatis.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sniper.springmvc.model.SdTabLeaveword;
import com.sniper.springmvc.mybatis.dao.BaseDao;
import com.sniper.springmvc.utils.ValidateUtil;

@Service("sdTabLeavewordService")
public class SdTabLeavewordServiceImpl extends BaseServiceImpl<SdTabLeaveword>
		implements SdTabLeavewordService {

	@Resource(name = "sdTabLeavewordDao")
	@Override
	public void setDao(BaseDao<SdTabLeaveword> dao) {
		super.setDao(dao);
	}

	/**
	 * 查询留言记录数
	 */
	@Override
	public int wordCount(int siteid, int answer, String sDate, String eDate) {
		Map<String, Object> params = new HashMap<>();
		params.put("stategt", 0);

		List<Integer> siteids = new ArrayList<>();
		siteids.add(siteid);
		params.put("depids", siteids);
		if (answer == 1) {
			params.put("answer", "1");
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		if (ValidateUtil.isValid(sDate)) {
			try {
				dateFormat.parse(sDate);
				params.put("stime", dateFormat.getCalendar().getTime());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		if (ValidateUtil.isValid(sDate)) {
			try {
				dateFormat.parse(eDate);
				params.put("etime", dateFormat.getCalendar().getTime());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return this.pageCount(params);
	}
}
