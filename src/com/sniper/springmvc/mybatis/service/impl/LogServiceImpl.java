package com.sniper.springmvc.mybatis.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sniper.springmvc.model.Log;
import com.sniper.springmvc.mybatis.dao.BaseDao;
import com.sniper.springmvc.utils.FilesUtil;
import com.sniper.springmvc.utils.LogUtil;

@Service("logService")
public class LogServiceImpl extends BaseServiceImpl<Log> implements LogService {

	@Override
	@Resource(name = "logDao")
	public void setDao(BaseDao<Log> dao) {
		super.setDao(dao);
	}

	@Override
	public void createLogTable(String tableName) {
		String sql = "CREATE TABLE IF NOT EXISTS  " + tableName
				+ " like mc_log";
		Map<String, Object> params = new HashMap<>();
		params.put("sql", sql);
		this.executeSql("createTable", params);
	}

	/**
	 * 重写该方法动态插入数据表
	 */
	@Override
	public int insert(Log t) {
		String sql = "insert into " + LogUtil.generateLogTableNameByYear(0)
				+ " (id,user,name,params,result,resultMsg,ctime) "
				+ "values(?,?,?,?,?,?,?)";

		String id = FilesUtil.getUUIDName("", true);
		t.setId(id);
		return this.insert("wiriteLog", t);
	}

	public void test() {
		String sql = "select table_name from tables where table_schema = 'taishan' "
				+ " and table_name like 'mc_logs_%' and table_name < '2014_8'"
				+ " order table_name";
		System.out.println(sql);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Log> findNearesLogs(int n) {
		String tableName = LogUtil.generateLogTableNameByYear(0);

		// 查询最近的日志表名称
		String sql = "select table_name from information_schema.tables "
				+ " where table_schema = 'taishan' "
				+ " and table_name like 'mc_logs_%' " + " and table_name <= '"
				+ tableName + "'" + " order by table_name desc limit 0," + n;
		List<Log> lists = this.query("select", null);
		String logSql = "";
		String logName = "";
		for (int i = 0; i < lists.size(); i++) {
			// logName = lists.get(i);
			// if (i == (lists.size() - 1)) {
			// logSql = logSql + "select * from " + logName;
			// } else {
			// logSql = logSql + "select * from " + logName + "union";
			// }
		}

		return null;
	}
}
