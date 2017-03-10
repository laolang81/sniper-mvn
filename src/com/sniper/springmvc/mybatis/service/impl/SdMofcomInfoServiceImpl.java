package com.sniper.springmvc.mybatis.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sniper.springmvc.model.SdMofcomInfo;
import com.sniper.springmvc.model.SdMofcomItem;
import com.sniper.springmvc.mybatis.dao.BaseDao;
import com.sniper.springmvc.utils.DataUtil;

@Service("sdMofcomInfoService")
public class SdMofcomInfoServiceImpl extends BaseServiceImpl<SdMofcomInfo>
		implements SdMofcomInfoService {

	public static Map<String, String> TYPES = new HashMap<>();
	static {
		TYPES.put("0", "新增");
		TYPES.put("1", "修改");
		TYPES.put("2", "删除");

	}

	public static Map<String, String> SUBTITLE = new HashMap<>();
	static {
		SUBTITLE.put("1", "政策");
		SUBTITLE.put("2", "新闻");
		SUBTITLE.put("3", "调研");
		SUBTITLE.put("4", "供求");
		SUBTITLE.put("5", "其他");

	}

	public static Map<String, String> SOURCE = new HashMap<>();
	static {
		SOURCE.put("1", "原创");
		SOURCE.put("2", "编译");
		SOURCE.put("3", "摘编");
		SOURCE.put("4", "转载");

	}

	@Resource(name = "sdMofcomInfoDao")
	@Override
	public void setDao(BaseDao<SdMofcomInfo> dao) {
		super.setDao(dao);
	}

	/**
	 * 更新或添加新闻和商务部的关系
	 */
	@Override
	public int updateMofcom(String sid, SdMofcomItem mofcomItem) {
		int result = 0;
		Map<String, Object> params = new HashMap<>();
		params.put("sid", sid);
		// params.put("mid", mofcomItem.getMid());
		SdMofcomInfo info = (SdMofcomInfo) this.find("find", params);
		if (info == null) {
			info = new SdMofcomInfo();
			info.setMid(mofcomItem.getMid());
			info.setDate(DataUtil.getTime());
			info.setDate(0);
			result = this.insert(info);
		} else {
			info.setMid(mofcomItem.getMid());
			info.setLastdate(DataUtil.getTime());
			result = this.update(info);
		}
		return result;
	}
}
