package com.sniper.springmvc.mybatis.service.impl;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.FieldStatsInfo;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.stereotype.Service;

import com.sniper.springmvc.freemarker.ViewHomeUtils;
import com.sniper.springmvc.model.SdPageIndex;
import com.sniper.springmvc.model.SdViewSubject;
import com.sniper.springmvc.mybatis.dao.BaseDao;
import com.sniper.springmvc.solr.SolrViewUtil;
import com.sniper.springmvc.solr.SubjectViewModel;
import com.sniper.springmvc.utils.DataUtil;
import com.sniper.springmvc.utils.ValidateUtil;

@Service("sdViewSubjectService")
public class SdViewSubjectServiceImpl extends BaseServiceImpl<SdViewSubject>
		implements SdViewSubjectService {

	/**
	 * 获取当前时间戳
	 */
	private int nowTime = DataUtil.getTime();

	@Resource
	SdPageIndexService pageIndexService;

	@Resource(name = "sdViewSubjectDao")
	@Override
	public void setDao(BaseDao<SdViewSubject> dao) {
		super.setDao(dao);
	}

	/**
	 * 检测制定栏目在摸个日期内是否发不过新闻
	 */
	@Override
	public boolean checkItemPostDate(int sitemid, String stime, String etime) {
		Map<String, Object> params = new HashMap<>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		if (ValidateUtil.isValid(stime)) {
			try {
				dateFormat.parse(stime);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			params.put("stime",
					dateFormat.getCalendar().getTimeInMillis() / 1000);
		}

		if (ValidateUtil.isValid(etime)) {
			try {
				dateFormat.parse(etime);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			params.put("etime",
					dateFormat.getCalendar().getTimeInMillis() / 1000);
		}
		List<Integer> itemid = new ArrayList<>();
		itemid.add(sitemid);
		// 栏目限制
		params.put("itemid", itemid);
		List<Map<String, Object>> map = this.queryMap("count", params);
		if (map.size() > 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean solrCheckItemPostDate(int sitemid, String stime, String etime) {
		SolrQuery solrQuery = new SolrQuery("*:*");
		solrQuery.addFilterQuery("lookthroughed_i:2");
		solrQuery.addFilterQuery("siteid_i:" + sitemid);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		// 时间段查询必须设置起止时间
		Date stimeDate = new Date(0);
		if (ValidateUtil.isValid(stime)) {
			try {
				stimeDate = dateFormat.parse(stime);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		Date etimeDate = new Date();
		if (ValidateUtil.isValid(etime)) {
			try {
				etimeDate = dateFormat.parse(etime);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		// 统计
		solrQuery.setGetFieldStatistics("view_i");
		solrQuery.setGetFieldStatistics("sid");
		solrQuery.addDateRangeFacet("date_dt", stimeDate, etimeDate, "+1DAY");
		HttpSolrClient client = SolrViewUtil
				.getInstance(SubjectViewModel.CORE_NAME);
		try {
			QueryResponse response = client.query(solrQuery);
			if (response.getResults().getNumFound() > 0l) {
				return true;
			}
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 处室新闻统计
	 */
	@Override
	public Map<String, Object> siteidCount(int sitemid, String stime,
			String etime) {

		Map<String, Object> params = new HashMap<>();
		List<Integer> lookthroughed = new ArrayList<>();
		lookthroughed.add(2);
		params.put("lookthroughed", lookthroughed);
		params.put("stime", getDate(stime));
		params.put("etime", getDate(etime));

		List<Integer> siteid = new ArrayList<>();
		siteid.add(sitemid);
		params.put("siteid", siteid);
		List<Map<String, Object>> map = this.queryMap("depCount", params);
		Map<String, Object> result = new HashMap<>();
		if (map != null && map.size() == 1) {
			result.put("sid", map.get(0).get("sid"));
			result.put("view", map.get(0).get("view"));
		}
		return result;
	}

	@Override
	public Map<String, Object> solrSiteidCount(int sitemid, String stime,
			String etime) {
		SolrQuery solrQuery = new SolrQuery("*:*");
		solrQuery.addFilterQuery("lookthroughed_i:2");
		solrQuery.addFilterQuery("siteid_i:" + sitemid);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		// 时间段查询必须设置起止时间
		Date stimeDate = new Date(0);
		if (ValidateUtil.isValid(stime)) {
			try {
				stimeDate = dateFormat.parse(stime);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		Date etimeDate = new Date();
		if (ValidateUtil.isValid(etime)) {
			try {
				etimeDate = dateFormat.parse(etime);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		// 统计
		solrQuery.setGetFieldStatistics("view_i");
		solrQuery.setGetFieldStatistics("sid");
		solrQuery.addDateRangeFacet("date_dt", stimeDate, etimeDate, "+1DAY");
		HttpSolrClient client = SolrViewUtil
				.getInstance(SubjectViewModel.CORE_NAME);
		try {
			QueryResponse response = client.query(solrQuery);
			Map<String, FieldStatsInfo> map = response.getFieldStatsInfo();
			Map<String, Object> result = new HashMap<>();
			// 吧新闻统计访问量统计
			for (Entry<String, FieldStatsInfo> entry : map.entrySet()) {
				switch (entry.getKey()) {

				case "view_i":
					result.put("view", entry.getValue().getSum());
					break;
				case "sid":
					result.put(entry.getKey(), entry.getValue().getCount());
					break;
				default:
					break;
				}
			}
			return result;
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 返回时间戳
	 * 
	 * @param date
	 * @return
	 */
	private Long getDate(String date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		if (ValidateUtil.isValid(date)) {
			try {
				dateFormat.parse(date);
				return dateFormat.getCalendar().getTimeInMillis() / 1000;
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 栏目新闻统计
	 */
	@Override
	public Map<String, Map<String, Object>> listSiteidItemidCount(int depid,
			String stime, String etime, Date itemDate) {
		Map<String, Map<String, Object>> result = new HashMap<>();
		Map<String, Object> params = new HashMap<>();
		params.put("stime", getDate(stime));
		params.put("etime", getDate(etime));
		// 增加处室统计
		params.put("depid", depid);
		if (ValidateUtil.isValid(itemDate)) {
			params.put("itemDate", itemDate.getTime() / 1000);
		}
		// 获取结果，处室栏目新闻发布量，访问量，最后新闻发布量
		List<Map<String, Object>> list = this.queryMap("depItemCount", params);
		for (Map<String, Object> map : list) {
			// 更改map里面的date
			Map<String, Object> temp = new HashMap<>();
			temp.putAll(map);
			temp.put("date", ViewHomeUtils.intToDate((long) map.get("date")));
			result.put(String.valueOf(map.get("itemid")), temp);
		}
		return result;
	}

	@Override
	public Map<String, Map<String, Object>> listItemidCount(int itemid,
			String stime, String etime) {
		// 增加处室统计
		List<Integer> itemids = new ArrayList<>();
		itemids.add(itemid);
		return this.listItemidCount(itemids, stime, etime);
	}

	@Override
	public Map<String, Map<String, Object>> listItemidCount(
			List<Integer> itemids, String stime, String etime) {
		Map<String, Object> params = new HashMap<>();
		List<Integer> lookthroughed = new ArrayList<>();
		lookthroughed.add(2);
		params.put("lookthroughed", lookthroughed);
		params.put("stime", getDate(stime));
		params.put("etime", getDate(etime));
		// 增加处室统计
		params.put("itemid", itemids);
		// 获取结果，处室栏目新闻发布量，访问量，最后新闻发布量
		Map<String, Map<String, Object>> result = new HashMap<>();
		List<Map<String, Object>> list = this.queryMap("itemCount", params);
		for (Map<String, Object> map : list) {
			// 更改map里面的date
			Map<String, Object> temp = new HashMap<>();
			temp.putAll(map);
			temp.put("date", ViewHomeUtils.intToDate((long) map.get("date")));
			result.put(String.valueOf(map.get("itemid")), temp);
		}
		return result;
	}

	@Override
	public Map<String, Object> solrItemidCount(int itemid, String stime,
			String etime) {
		SolrQuery solrQuery = new SolrQuery("*:*");
		solrQuery.addFilterQuery("lookthroughed_i:2");
		solrQuery.addFilterQuery("tItemid_is:" + itemid);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		// 时间段查询必须设置起止时间
		Date stimeDate = new Date(0);
		if (ValidateUtil.isValid(stime)) {
			try {
				stimeDate = dateFormat.parse(stime);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		Date etimeDate = new Date();
		if (ValidateUtil.isValid(etime)) {
			try {
				etimeDate = dateFormat.parse(etime);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		// 统计
		solrQuery.setGetFieldStatistics("view_i");
		solrQuery.setGetFieldStatistics("sid");
		solrQuery.addDateRangeFacet("date_dt", stimeDate, etimeDate, "+1DAY");
		HttpSolrClient client = SolrViewUtil
				.getInstance(SubjectViewModel.CORE_NAME);
		try {
			QueryResponse response = client.query(solrQuery);
			Map<String, FieldStatsInfo> map = response.getFieldStatsInfo();
			Map<String, Object> result = new HashMap<>();
			// 吧新闻统计访问量统计
			for (Entry<String, FieldStatsInfo> entry : map.entrySet()) {
				switch (entry.getKey()) {
				case "view_i":
					result.put("view", entry.getValue().getSum());
					break;
				case "sid":
					result.put(entry.getKey(), entry.getValue().getCount());
					break;
				default:
					break;
				}
			}
			return result;
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Map<String, Object>> monthGroupCount(int groupId, String stime,
			String etime) {
		Map<String, Object> params = new HashMap<>();
		List<Integer> lookthroughed = new ArrayList<>();
		lookthroughed.add(2);
		params.put("lookthroughed", lookthroughed);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		try {
			if (ValidateUtil.isValid(stime)) {
				dateFormat.parse(stime);
				params.put("stime",
						dateFormat.getCalendar().getTimeInMillis() / 1000);
			}
			if (ValidateUtil.isValid(etime)) {
				dateFormat.parse(etime);
				params.put("etime",
						dateFormat.getCalendar().getTimeInMillis() / 1000);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		List<Integer> ids = new ArrayList<>();
		if (ValidateUtil.isValid(groupId)) {
			SdPageIndex pageIndex = pageIndexService.get(String
					.valueOf(groupId));
			if (pageIndex != null
					&& ValidateUtil.isValid(pageIndex.getItemid())) {
				String[] items = pageIndex.getItemid().split(",");
				for (String string : items) {
					ids.add(Integer.valueOf(string));
				}
			}
		}

		if (!ValidateUtil.isValid(stime)) {
			params.put("pageSize", 24);
		}
		params.put("itemid", ids);
		return this.queryMap("monthCount", params);
	}

	/**
	 * 按照月份统计返回结果,暂时无法解决
	 */
	@Override
	public List<Map<String, Object>> solrMonthGroupCount(int groupId,
			String stime, String etime) {

		return null;
	}

	@Override
	public List<Map<String, Object>> monthCount(int groupId, String stime,
			String etime) {
		Map<String, Object> params = new HashMap<>();
		List<Integer> lookthroughed = new ArrayList<>();
		lookthroughed.add(2);
		params.put("lookthroughed", lookthroughed);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		try {
			if (ValidateUtil.isValid(stime)) {
				dateFormat.parse(stime);
				params.put("stime",
						dateFormat.getCalendar().getTimeInMillis() / 1000);
			}
			if (ValidateUtil.isValid(etime)) {
				dateFormat.parse(etime);
				params.put("etime",
						dateFormat.getCalendar().getTimeInMillis() / 1000);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		List<Integer> ids = new ArrayList<>();
		if (ValidateUtil.isValid(groupId)) {
			ids.add(groupId);
		}

		if (!ValidateUtil.isValid(stime) || !ValidateUtil.isValid(etime)) {
			params.put("pageSize", 24);
		}
		params.put("itemid", ids);
		return this.queryMap("monthCount", params);
	}

	/**
	 * 查找最后一篇新闻
	 */
	@Override
	public Date postLastDate(int itemid) {
		Map<String, Object> params = new HashMap<>();
		List<Integer> lookthroughed = new ArrayList<>();
		lookthroughed.add(2);
		params.put("lookthroughed", lookthroughed);

		List<Integer> itemidList = new ArrayList<>();
		itemidList.add(itemid);
		params.put("itemid", itemidList);
		params.put("order", "s.date desc");

		SdViewSubject subject = (SdViewSubject) this.find("findDate", params);
		if (ValidateUtil.isValid(subject)
				&& ValidateUtil.isValid(subject.getEditdate())) {
			return ViewHomeUtils.intToDate(subject.getEditdate());
		}
		return new Date();
	}

	@Override
	public Date solrPostLastDate(int itemid) {
		SolrQuery params = new SolrQuery("*:*");
		// 栏目限制
		params.addFilterQuery("tItemid_is:" + itemid);
		params.set("start", 0);
		params.set("rows", 1);
		params.set("fl", "*");
		params.setSort("displayorder_i", ORDER.desc);
		HttpSolrClient client = SolrViewUtil
				.getInstance(SubjectViewModel.CORE_NAME);
		try {
			QueryResponse response = client.query(params);
			List<SubjectViewModel> lists = response
					.getBeans(SubjectViewModel.class);
			if (lists.size() > 0) {
				return lists.get(0).getDate();
			}
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public SdViewSubject getLastSubject() {
		Map<String, Object> params = new HashMap<>();
		// 增加时间限制
		List<Integer> lookthroughed = new ArrayList<>();
		lookthroughed.add(2);
		params.put("pageSize", 1);
		params.put("order", "s.sid desc");
		List<SdViewSubject> subjects = this.query("select", params);
		if (subjects != null && subjects.size() == 1) {
			return subjects.get(0);
		}
		return null;
	}

	/**
	 * 获取主要新闻图片
	 */
	@Override
	public List<SdViewSubject> getSlides(int depid, int limit) {
		Map<String, Object> params = new HashMap<>();
		if (depid > 0) {
			List<Integer> siteid = new ArrayList<>();
			siteid.add(depid);
			params.put("siteid", siteid);
		}
		// 增加时间限制
		params.put("etime", nowTime);

		List<Integer> lookthroughed = new ArrayList<>();
		lookthroughed.add(2);
		params.put("lookthroughed", lookthroughed);
		params.put(SdViewSubjectService.FILED_NAME_IS_MAIN, "1");
		params.put("pageSize", limit);
		return this.query("select", params);
	}

	/**
	 * 
	 */
	@Override
	public List<SdViewSubject> getSolrSlides(int depid, int limit) {
		SolrQuery params = new SolrQuery("*:*");
		params.addFilterQuery("lookthroughed_i:2");
		params.addDateRangeFacet("date_dt", new Date(0), new Date(), "+1DAY");
		// // 主图
		params.addFilterQuery("attachments_ss:*main*");
		// 栏目限制
		if (depid > 0) {
			params.addFilterQuery("siteid_i:" + depid);
		}
		// 按照一天进行时间切分
		params.set("q.op", "and");
		params.set("start", 0);
		params.set("rows", limit);
		params.set("fl", "*");
		params.setSort("displayorder_i", ORDER.desc);
		HttpSolrClient client = SolrViewUtil
				.getInstance(SubjectViewModel.CORE_NAME);

		try {
			QueryResponse response = client.query(params);
			List<SubjectViewModel> lists = response
					.getBeans(SubjectViewModel.class);
			if (lists.size() == 0) {
				return getSlides(depid, limit);
			} else {
				List<SdViewSubject> subjects = new ArrayList<>();
				for (SubjectViewModel subjectViewModel : lists) {
					SdViewSubject subject = SolrViewUtil
							.getSubjectView(subjectViewModel);
					subjects.add(subject);
				}
				return subjects;
			}
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据栏目获取图片新闻
	 */
	@Override
	public List<SdViewSubject> getSlidesByItemid(List<Integer> items,
			String fieldName, int limit) {
		Map<String, Object> params = new HashMap<>();
		// 增加时间限制
		params.put("etime", nowTime);

		params.put("itemid", items);
		List<Integer> lookthroughed = new ArrayList<>();
		lookthroughed.add(2);
		params.put("lookthroughed", lookthroughed);
		params.put("pageSize", limit);
		params.put(SdViewSubjectService.FILED_NAME_IS_IMG, 1);
		params.put("order", "s.displayorder desc");
		return this.query("select", params);
	}

	@Override
	public List<SdViewSubject> getSolrSlidesByItemid(List<Integer> items,
			String fieldName, int limit) {
		SolrQuery params = new SolrQuery("*:*");
		params.addFilterQuery("lookthroughed_i:2");
		params.addDateRangeFacet("date_dt", new Date(0), new Date(), "+1DAY");
		// 普通图片查询
		if (fieldName.equals(SdViewSubjectService.FILED_NAME_IS_IMG)) {
			params.addFilterQuery("attachments_ss:*prime*");
		} else if (fieldName.equals(SdViewSubjectService.FILED_NAME_IS_MAIN)) {
			params.addFilterQuery("attachments_ss:*main*");
		}

		// 栏目限制
		for (Integer it : items) {
			params.addFilterQuery("tItemid_is:" + it);
		}
		params.set("q.op", "and");
		params.set("start", 0);
		params.set("rows", limit);
		params.set("fl", "*");
		params.setSort("displayorder_i", ORDER.desc);
		HttpSolrClient client = SolrViewUtil
				.getInstance(SubjectViewModel.CORE_NAME);

		try {
			QueryResponse response = client.query(params);
			List<SubjectViewModel> lists = response
					.getBeans(SubjectViewModel.class);
			if (lists.size() == 0) {
				return getSlidesByItemid(items, fieldName, limit);
			} else {
				List<SdViewSubject> subjects = new ArrayList<>();
				for (SubjectViewModel subjectViewModel : lists) {
					SdViewSubject subject = SolrViewUtil
							.getSubjectView(subjectViewModel);
					subjects.add(subject);
				}
				return subjects;
			}
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<SdViewSubject> getSubjectByItem(int id, int limit) {
		List<Integer> items = new ArrayList<>();
		items.add(id);
		Map<String, Object> params = new HashMap<>();
		params.put("itemid", items);

		// 增加时间限制
		params.put("etime", nowTime);
		List<Integer> lookthroughed = new ArrayList<>();
		lookthroughed.add(2);
		params.put("lookthroughed", lookthroughed);
		params.put("pageSize", limit);
		params.put("order", "s.displayorder desc");
		return this.query("select", params);
	}

	@Override
	public List<SdViewSubject> getSolrSubjectByItem(int id, int limit) {
		SolrQuery params = new SolrQuery("*:*");
		params.addFilterQuery("lookthroughed_i:2");
		params.addDateRangeFacet("date_dt", new Date(0), new Date(), "+1DAY");
		// 栏目限制
		params.addFilterQuery("tItemid_is:" + id);
		params.set("q.op", "and");
		// 按照一天进行时间切分
		params.set("start", 0);
		params.set("rows", limit);
		params.set("fl", "*");
		params.setSort("displayorder_i", ORDER.desc);
		HttpSolrClient client = SolrViewUtil
				.getInstance(SubjectViewModel.CORE_NAME);
		try {
			QueryResponse response = client.query(params);
			// SolrDocumentList documentList = response.getResults();
			List<SubjectViewModel> lists = response
					.getBeans(SubjectViewModel.class);
			if (lists.size() == 0) {
				return getSubjectByItem(id, limit);
			} else {
				List<SdViewSubject> subjects = new ArrayList<>();
				for (SubjectViewModel subjectViewModel : lists) {
					SdViewSubject subject = SolrViewUtil
							.getSubjectView(subjectViewModel);
					subjects.add(subject);
				}
				return subjects;
			}
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<SdViewSubject> getSubjectByPageIndex(int pid, int limit) {
		List<Integer> ids = new ArrayList<>();
		if (ValidateUtil.isValid(pid)) {
			SdPageIndex pageIndex = pageIndexService.get(String.valueOf(pid));
			if (pageIndex != null
					&& ValidateUtil.isValid(pageIndex.getItemid())) {
				String[] items = pageIndex.getItemid().split(",");
				for (String string : items) {
					ids.add(Integer.valueOf(string));
				}
			}
		}

		Map<String, Object> params = new HashMap<>();

		// 增加时间限制
		params.put("etime", nowTime);
		params.put("itemid", ids);
		List<Integer> lookthroughed = new ArrayList<>();
		lookthroughed.add(2);
		params.put("lookthroughed", lookthroughed);
		params.put("pageSize", limit);
		params.put("order", "s.displayorder desc");
		return this.query("select", params);
	}

	@Override
	public List<SdViewSubject> getSolrSubjectByPageIndex(int pid, int limit) {
		SolrQuery params = new SolrQuery("*:*");
		if (ValidateUtil.isValid(pid)) {
			SdPageIndex pageIndex = pageIndexService.get(String.valueOf(pid));
			if (pageIndex != null
					&& ValidateUtil.isValid(pageIndex.getItemid())) {
				String[] items = pageIndex.getItemid().split(",");
				for (String string : items) {
					params.addFilterQuery("tItemid_is:" + string);
				}
			}
		}
		params.addFilterQuery("lookthroughed_i:2");
		params.addFilterQuery("preid_i:4");
		params.addDateRangeFacet("date_dt", new Date(0), new Date(), "+1DAY");
		params.set("q.op", "and");
		// 栏目限制
		params.set("start", 0);
		params.set("rows", limit);
		params.set("fl", "*");
		params.setSort("displayorder_i", ORDER.desc);
		HttpSolrClient client = SolrViewUtil
				.getInstance(SubjectViewModel.CORE_NAME);

		try {
			QueryResponse response = client.query(params);
			List<SubjectViewModel> lists = response
					.getBeans(SubjectViewModel.class);
			if (lists.size() == 0) {
				return getSolrSubjectByPageIndex(pid, limit);
			} else {
				List<SdViewSubject> subjects = new ArrayList<>();
				for (SubjectViewModel subjectViewModel : lists) {
					SdViewSubject subject = SolrViewUtil
							.getSubjectView(subjectViewModel);
					subjects.add(subject);
				}
				return subjects;
			}
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 重要新闻
	 */
	@Override
	public List<SdViewSubject> getBhot(int limit) {
		Map<String, Object> params = new HashMap<>();
		List<Integer> lookthroughed = new ArrayList<>();
		lookthroughed.add(2);
		params.put("lookthroughed", lookthroughed);
		// 增加时间限制
		params.put("etime", nowTime);
		params.put("bhot", "1");
		params.put("preidNo", 4);
		params.put("pageSize", limit);
		params.put("order", "s.displayorder desc");
		List<SdViewSubject> subjects = this.query("select", params);
		return subjects;
	}

	@Override
	public List<SdViewSubject> getSolrBhot(int limit) {
		SolrQuery params = new SolrQuery("*:*");
		params.addFilterQuery("lookthroughed_i:2");
		params.addFilterQuery("bhot_i:1");
		params.addFilterQuery("-preid_i:4");
		params.addDateRangeFacet("date_dt", new Date(0), new Date(), "+1DAY");
		params.set("q.op", "and");
		// 栏目限制
		params.set("start", 0);
		params.set("rows", limit);
		params.set("fl", "*");
		params.setSort("displayorder_i", ORDER.desc);
		HttpSolrClient client = SolrViewUtil
				.getInstance(SubjectViewModel.CORE_NAME);

		try {
			QueryResponse response = client.query(params);
			// SolrDocumentList documentList = response.getResults();
			List<SubjectViewModel> lists = response
					.getBeans(SubjectViewModel.class);
			if (lists.size() == 0) {
				return getBhot(limit);
			} else {
				List<SdViewSubject> subjects = new ArrayList<>();
				for (SubjectViewModel model : lists) {
					SdViewSubject subject = SolrViewUtil.getSubjectView(model);
					subjects.add(subject);
				}
				return subjects;
			}
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 置顶新闻
	 */
	@Override
	public List<SdViewSubject> getTops(int limit) {
		Map<String, Object> params = new HashMap<>();
		List<Integer> lookthroughed = new ArrayList<>();
		lookthroughed.add(2);
		params.put("lookthroughed", lookthroughed);
		// 增加时间限制
		params.put("etime", nowTime);
		params.put("preid", 4);
		params.put("pageSize", limit);
		params.put("order", "s.displayorder desc");
		List<SdViewSubject> subjects = this.query("select", params);
		return subjects;
	}

	@Override
	public List<SdViewSubject> getSolrTops(int limit) {
		SolrQuery params = new SolrQuery("*:*");
		params.addFilterQuery("lookthroughed_i:2");
		params.addFilterQuery("preid_i:4");
		params.addDateRangeFacet("date_dt", new Date(0), new Date(), "+1DAY");
		params.set("q.op", "and");
		// 栏目限制
		params.set("start", 0);
		params.set("rows", limit);
		params.set("fl", "*");
		params.setSort("displayorder_i", ORDER.desc);
		HttpSolrClient client = SolrViewUtil
				.getInstance(SubjectViewModel.CORE_NAME);

		try {
			QueryResponse response = client.query(params);
			// SolrDocumentList documentList = response.getResults();
			List<SubjectViewModel> lists = response
					.getBeans(SubjectViewModel.class);
			if (lists.size() == 0) {
				return getTops(limit);
			} else {
				List<SdViewSubject> subjects = new ArrayList<>();
				for (SubjectViewModel subjectViewModel : lists) {
					SdViewSubject subject = SolrViewUtil
							.getSubjectView(subjectViewModel);
					subjects.add(subject);
				}
				return subjects;
			}
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
