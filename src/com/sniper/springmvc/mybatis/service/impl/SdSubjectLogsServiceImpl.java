package com.sniper.springmvc.mybatis.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.sniper.springmvc.model.AdminUser;
import com.sniper.springmvc.model.SdSubjectLogs;
import com.sniper.springmvc.mybatis.dao.BaseDao;
import com.sniper.springmvc.utils.ValidateUtil;

@Service("sdSubjectLogsService")
public class SdSubjectLogsServiceImpl extends BaseServiceImpl<SdSubjectLogs>
		implements SdSubjectLogsService {

	@Resource
	AdminUserService adminUserService;

	@Resource
	SdUserService userService;

	// log_code
	public static Map<String, String> LOG_CODE = new HashMap<>();
	static {
		LOG_CODE.put("1", "审核");
		LOG_CODE.put("2", "取消审核");
		LOG_CODE.put("3", "批量删除");
		LOG_CODE.put("4", "移动首栏目");
		LOG_CODE.put("5", "交换排序");
		LOG_CODE.put("6", "取消置顶");
		LOG_CODE.put("7", "置顶");
		LOG_CODE.put("8", "取消首页要闻");
		LOG_CODE.put("9", "首页要闻");
		LOG_CODE.put("10", "提交到商务部");
		LOG_CODE.put("11", "取消到商务部");
		LOG_CODE.put("12", "移动到回收站");
		LOG_CODE.put("13", "添加");
		LOG_CODE.put("14", "编辑");
		LOG_CODE.put("15", "设为图片新闻");
		LOG_CODE.put("16", "取消图片新闻");
		LOG_CODE.put("17", "图片新闻审核");
		LOG_CODE.put("18", "取消图片审核");
		LOG_CODE.put("19", "删除静态页");
	}

	@Resource(name = "sdSubjectLogsDao")
	@Override
	public void setDao(BaseDao<SdSubjectLogs> dao) {
		super.setDao(dao);
	}

	@Override
	public void log(int sid, String uid, String logCode, String msg) {
		SdSubjectLogs logs = new SdSubjectLogs();
		logs.setSid(sid);
		logs.setLogId(logCode);
		logs.setMessage(msg);
		logs.setTime(new Date());
		logs.setUid(uid);
		this.insert(logs);

	}

	@Override
	public List<SdSubjectLogs> getLogs(String sid) {
		Map<String, Object> params = new HashMap<>();
		params.put("sid", sid);
		List<SdSubjectLogs> subjectLogs = this.query("select", params);
		// 查找用户名
		for (SdSubjectLogs sdSubjectLogs : subjectLogs) {
			AdminUser adminUser = new AdminUser();
			// 判断id是否是数字
			if (StringUtils.isNumeric(sdSubjectLogs.getUid())) {
				adminUser = adminUserService.getUser(sdSubjectLogs.getUid());
			} else {
				adminUser = adminUserService.get(sdSubjectLogs.getUid());
			}
			if (ValidateUtil.isValid(adminUser)) {
				sdSubjectLogs.setUid(adminUser.getName());
			}

		}
		return subjectLogs;
	}

	public static void main(String[] args) {
		System.out.println(StringUtils.isNumeric("111"));
	}
}
