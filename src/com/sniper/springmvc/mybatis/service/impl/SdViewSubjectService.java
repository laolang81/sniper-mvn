package com.sniper.springmvc.mybatis.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.sniper.springmvc.datasource.DataSource;
import com.sniper.springmvc.datasource.DataSourceValue;
import com.sniper.springmvc.model.SdViewSubject;
import com.sniper.springmvc.mybatis.service.BaseService;

public interface SdViewSubjectService extends BaseService<SdViewSubject> {

	/**
	 * 是否是图片
	 */
	public static String FILED_NAME_IS_IMG = "isprimeimage";
	/**
	 * 是否是主图
	 */
	public static String FILED_NAME_IS_MAIN = "mainsite";

	/**
	 * 检测栏目是否在制定时间发过新闻
	 * 
	 * @param sitemid
	 * @param stime
	 * @param etime
	 * @return
	 */
	@DataSource(DataSourceValue.SLAVE)
	public boolean checkItemPostDate(int sitemid, String stime, String etime);

	public boolean solrCheckItemPostDate(int sitemid, String stime, String etime);

	/**
	 * 处室统计
	 * 
	 * @param sitemid
	 * @param stime
	 * @param etime
	 * @return
	 */
	@DataSource(DataSourceValue.SLAVE)
	public Map<String, Object> siteidCount(int sitemid, String stime,
			String etime);

	public Map<String, Object> solrSiteidCount(int sitemid, String stime,
			String etime);

	/**
	 * 栏目统计
	 * 
	 * @param itemid
	 * @param stime
	 * @param etime
	 * @return
	 */
	@DataSource(DataSourceValue.SLAVE)
	public Map<String, Map<String, Object>> listSiteidItemidCount(int depid,
			String stime, String etime, Date itemDate);

	@DataSource(DataSourceValue.SLAVE)
	public Map<String, Map<String, Object>> listItemidCount(int itemid,
			String stime, String etime);

	@DataSource(DataSourceValue.SLAVE)
	public Map<String, Map<String, Object>> listItemidCount(
			List<Integer> itemids, String stime, String etime);

	public Map<String, Object> solrItemidCount(int itemid, String stime,
			String etime);

	/**
	 * 统计按照月份统计储存
	 * 
	 * @param groupId
	 * @param stime
	 * @param etime
	 * @return
	 */
	@DataSource(DataSourceValue.SLAVE)
	public List<Map<String, Object>> monthGroupCount(int groupId, String stime,
			String etime);

	public List<Map<String, Object>> solrMonthGroupCount(int groupId,
			String stime, String etime);

	@DataSource(DataSourceValue.SLAVE)
	public List<Map<String, Object>> monthCount(int groupId, String stime,
			String etime);

	/**
	 * 
	 * @param itemid
	 * @return
	 */
	@DataSource(DataSourceValue.SLAVE)
	public Date postLastDate(int itemid);

	public Date solrPostLastDate(int itemid);

	/**
	 * 读取最后新闻
	 * 
	 * @param itemid
	 * @return
	 */
	@DataSource(DataSourceValue.SLAVE)
	public SdViewSubject getLastSubject();

	/**
	 * 获取幻灯片
	 * 
	 * @param depid
	 *            处室id
	 * @param limit
	 *            记录数
	 * @return
	 */
	public List<SdViewSubject> getSlides(int depid, int limit);

	public List<SdViewSubject> getSolrSlides(int depid, int limit);

	/**
	 * 获取幻灯片
	 * 
	 * @param items
	 * @param fieldName
	 * @param limit
	 * @return
	 */
	public List<SdViewSubject> getSlidesByItemid(List<Integer> items,
			String fieldName, int limit);

	public List<SdViewSubject> getSolrSlidesByItemid(List<Integer> items,
			String fieldName, int limit);

	/**
	 * 获取新闻根据栏目id
	 * 
	 * @param pid
	 * @param limit
	 * @return
	 */
	public List<SdViewSubject> getSubjectByItem(int id, int limit);

	public List<SdViewSubject> getSolrSubjectByItem(int id, int limit);

	/**
	 * 获取新闻根据聚合看么id
	 * 
	 * @param pid
	 * @param limit
	 * @return
	 */
	public List<SdViewSubject> getSubjectByPageIndex(int pid, int limit);

	public List<SdViewSubject> getSolrSubjectByPageIndex(int pid, int limit);

	/**
	 * 获取置顶的新闻
	 * 
	 * @param id
	 * @return
	 */
	public List<SdViewSubject> getBhot(int limit);

	public List<SdViewSubject> getSolrBhot(int limit);

	/**
	 * 获取重要新闻
	 * 
	 * @return
	 */
	public List<SdViewSubject> getTops(int limit);

	public List<SdViewSubject> getSolrTops(int limit);

}
