package com.sniper.springmvc.solr;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.FieldStatsInfo;
import org.apache.solr.client.solrj.response.PivotField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.util.NamedList;
import org.junit.Test;

import com.sniper.springmvc.freemarker.ViewHomeUtils;
import com.sniper.springmvc.model.SdAttachments;
import com.sniper.springmvc.model.SdContent;
import com.sniper.springmvc.model.SdDepartments;
import com.sniper.springmvc.model.SdItems;
import com.sniper.springmvc.model.SdSubjects;
import com.sniper.springmvc.model.SdViewSubject;
import com.sniper.springmvc.utils.HtmlUtil;
import com.sniper.springmvc.utils.ValidateUtil;

/**
 * http://www.cnblogs.com/hoojo/archive/2011/10/21/2220431.html
 * http://tianxingzhe.blog.51cto.com/3390077/1748191
 * 
 * @author suzhen
 * 
 */
public class SolrViewUtil extends SolrBaseUtil {

	/**
	 * 插入新闻 Solr添加重复数据只要id一样就会覆盖
	 * 
	 * @param model
	 * @return
	 */
	public static UpdateResponse insert(SubjectViewModel model) {
		try {
			HttpSolrClient client = getInstance(SubjectViewModel.CORE_NAME);
			if (ValidateUtil.isValid(model.getSubject())) {
				client.addBean(model);
				UpdateResponse response = client.commit();
				return response;
			}
		} catch (IOException | SolrServerException e) {
			LOGGER.error("添加错误: ID:" + model.getSid(), e);
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 新闻
	 * 
	 * @param model
	 * @return
	 */
	public static UpdateResponse update(SubjectViewModel model) {
		try {
			HttpSolrClient client = getInstance(SubjectViewModel.CORE_NAME);
			client.deleteById(model.getId());
			client.addBean(model);
			UpdateResponse response = client.commit();
			return response;
		} catch (IOException | SolrServerException e) {
			LOGGER.error("更新错误: ID:" + model.getSid(), e);
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 删除索引
	 * 
	 * @param id
	 * @return
	 */
	public static UpdateResponse delete(String id) {
		try {
			HttpSolrClient client = getInstance(SubjectViewModel.CORE_NAME);
			client.deleteById(id);
			UpdateResponse response = client.commit();
			return response;
		} catch (SolrServerException | IOException e) {
			LOGGER.error("删除错误: ID:" + id, e);
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 组装需要的数据
	 * 
	 * @param subjects
	 * @param departments
	 * @param items
	 */
	public static SubjectViewModel getModel(SdSubjects subjects,
			SdDepartments departments, SdItems items, Integer[] itids) {
		SubjectViewModel model = new SubjectViewModel();
		model.setId(subjects.getSid().toString());
		model.setSid(subjects.getSid());
		model.setBhot(subjects.getBhot());
		model.setDisplayorder(subjects.getDisplayorder());
		model.setIcId(subjects.getIcId());
		model.setLanguage(subjects.getLanguage());
		model.setMoftec(subjects.getMoftec());
		model.setPeriod(subjects.getPeriod());
		model.setPreid(subjects.getPreid());
		model.setSubject(subjects.getSubject());
		model.setFromsite(subjects.getFromsite());
		model.setSubtitle(subjects.getSubtitle());
		model.setSuggested(subjects.getSuggested());
		model.setTodayView(subjects.getTodayView());
		model.setUrl(subjects.getUrl());
		model.setView(subjects.getView());
		model.setViewLastTime(subjects.getViewLastTime());
		model.setViewMobile(subjects.getViewMobile());
		Date date = ViewHomeUtils.intToDate(subjects.getDate());
		model.setDate(date);
		String links = "/public/html/news/201701/389550.html";
		String year = yearDateFormat.format(date);
		links = links.replace("201701", year).replace("389550", model.getId());
		// 组装内容页面地址
		model.setWebUrl(links);
		// 内容设置
		if (ValidateUtil.isValid(subjects.getContent())) {
			// 去除html标签
			model.setContent(HtmlUtil.getTextFromHtml(subjects.getContent()
					.getContent()));
		}
		// 处室设置,栏目设置，
		model.setSiteid(subjects.getSiteid());
		if (ValidateUtil.isValid(departments)) {
			model.setSiteidName(departments.getName());
		}
		model.setItemid(subjects.getItemid());
		if (ValidateUtil.isValid(items)) {
			model.setItemidName(items.getName());
		}
		model.setLookthroughed(subjects.getLookthroughed());
		// 附件设置
		// 图片储存格式
		// 类型：属性：地址
		if (ValidateUtil.isValid(subjects.getAttachments())) {
			String[] fa = new String[subjects.getAttachments().size()];
			int i = 0;
			for (SdAttachments attachments : subjects.getAttachments()) {
				if (attachments == null
						|| !ValidateUtil.isValid(attachments.getAid())) {
					continue;
				}
				// 只读取主要图片
				StringBuffer a = new StringBuffer();

				// 是否是图片
				if (attachments.getIsimage() == 1) {
					a.append("image:");
				} else {
					a.append("file:");
				}
				a.append(attachments.getAid());
				a.append(":");
				// 可以显示的图片
				if (attachments.getIsprimeimage() == 1) {
					a.append("prime:");
				}
				// 重要图片
				if (attachments.getMainsite() == 1) {
					a.append("main:");
				}
				// 最后是图片路径
				a.append(attachments.getFilename());

				fa[i] = a.toString();
				// 值储存一个
				i++;
			}
			model.setAttachments(fa);
		}
		// 设置栏目列表
		model.settItemid(itids);
		// 设置
		if (ValidateUtil.isValid(subjects.getEditDate())) {
			model.setEditdate(ViewHomeUtils.intToDate(subjects.getEditDate()));
		}

		model.setEditUser(subjects.getEditUser());
		model.setAuditUid(subjects.getAuditUid());
		model.setAuditDatetime(subjects.getAuditDatetime());
		model.setAuditIp(subjects.getAuditIp());
		model.setAuthorid(subjects.getAuthorid());
		return model;
	}

	/**
	 * 获取新闻F
	 * 
	 * @param model
	 * @return
	 */
	public static SdViewSubject getSubjectView(SubjectViewModel model) {

		SdViewSubject subject = new SdViewSubject();
		if (model != null) {
			// 只赋值一些基本信息
			subject.setId(model.getSid());
			subject.setSubject(model.getSubject());
			subject.setSubtitle(model.getSubtitle());
			subject.setDate(ViewHomeUtils.getTimeMillis(model.getDate()));
			subject.setItemid(model.getItemid());
			subject.setSiteid(model.getSiteid());
			subject.setFromsite(model.getFromsite());
			subject.setView(model.getView());
			subject.setUrl(model.getUrl());
			subject.setBhot(model.getBhot());
			subject.setPeriod(model.getPeriod());
			subject.setSuggested(model.getSuggested());
			subject.setDisplayorder(model.getDisplayorder());
			subject.setIcId(model.getIcId());
			subject.setTodayView(model.getTodayView());
			SdContent content = new SdContent();
			content.setContent(model.getContent());
			subject.setContent(content);
			SdDepartments departments = new SdDepartments();
			departments.setId(model.getSiteid());
			departments.setName(model.getSiteidName());
			subject.setDepartments(departments);
			SdItems items = new SdItems();
			items.setItemid(model.getItemid());
			items.setName(model.getItemidName());
			subject.setItems(items);
			//
			subject.setEditdate(ViewHomeUtils.getTimeMillis(model.getEditdate()));
			subject.setEditUser(model.getEditUser());
			subject.setAuditDatetime(model.getAuditDatetime());
			subject.setAuditUid(model.getAuditUid());
			subject.setAuditIp(model.getAuditIp());
			subject.setAuthorid(model.getAuthorid());
			// 图片
			if (model.getAttachments().length > 0) {
				for (int i = 0; i < model.getAttachments().length; i++) {
					// 解析图片
					String[] att = model.getAttachments()[i].split(":");
					if (att.length >= 3) {
						SdAttachments attachments = new SdAttachments();
						attachments.setAid(Integer.valueOf(att[1]));
						attachments.setFilename(att[att.length - 1]);
						subject.getAttachments().add(attachments);
					}
				}
			}
		}
		return subject;
	}

	/**
	 * 获取最后一篇新闻
	 * 
	 * @return
	 */
	public static SubjectViewModel getLastModel() {
		SolrQuery params = new SolrQuery("*:*");
		// 按照一天进行时间切分
		params.set("start", 0);
		params.set("rows", 1);
		params.set("fl", "*");
		params.setSort("sid", ORDER.desc);
		HttpSolrClient client = SolrViewUtil
				.getInstance(SubjectViewModel.CORE_NAME);

		try {
			QueryResponse response = client.query(params);
			List<SubjectViewModel> lists = response
					.getBeans(SubjectViewModel.class);
			if (lists.size() == 1) {
				return lists.get(0);
			}
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 攻击功能
	 * 
	 * @return
	 */
	public static Map<String, FieldStatsInfo> count(String... fields) {
		SolrQuery query = new SolrQuery("*:*");
		query.setGetFieldStatistics(true);
		if (fields.length > 0) {
			for (String field : fields) {
				query.setGetFieldStatistics(field);
			}
		}
		// query.addStatsFieldFacets(field, facets);

		HttpSolrClient client = SolrViewUtil
				.getInstance(SubjectViewModel.CORE_NAME);
		try {
			QueryResponse queryResponse = client.query(query);
			Map<String, FieldStatsInfo> map = queryResponse.getFieldStatsInfo();
			return map;
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Test
	public void facetpivot() {

		try {
			HttpSolrClient solrServer = SolrViewUtil
					.getInstance(SubjectViewModel.CORE_NAME);
			SolrQuery query = new SolrQuery("*:*");

			query.setFacet(true);
			// query.add("facet.pivot", "Spare3,UserLocation");//根据这两维度来分组查询
			query.addFacetPivotField("view_i,sid");
			System.out.println(query);
			QueryResponse response = solrServer.query(query,
					SolrRequest.METHOD.POST);
			NamedList<List<PivotField>> namedList = response.getFacetPivot();
			System.out.println(namedList);// 底下为啥要这样判断，把这个值打印出来，你就明白了
			if (namedList != null) {
				List<PivotField> pivotList = null;
				for (int i = 0; i < namedList.size(); i++) {
					pivotList = namedList.getVal(i);
					if (pivotList != null) {
						for (PivotField pivot : pivotList) {
							int pos = 0;
							int neg = 0;
							List<PivotField> fieldList = pivot.getPivot();
							if (fieldList != null) {
								for (PivotField field : fieldList) {
									int proValue = (Integer) field.getValue();
									int count = field.getCount();
									if (proValue == 1) {
										pos = count;
									} else {
										neg = count;
									}
								}
							}
							System.out.println(pos);
							System.out.println(neg);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Map<String, FieldStatsInfo> map = SolrViewUtil.count("view_i", "sid");
		for (String key : map.keySet()) {
			System.out.println("key:" + key);
			FieldStatsInfo fieldStatsInfo = map.get(key);
			System.out.println(fieldStatsInfo);
			System.out.println("name:" + fieldStatsInfo.getName());
			System.out.println("count:" + fieldStatsInfo.getCount());
			System.out.println("max:" + fieldStatsInfo.getMax());
			System.out.println("min:" + fieldStatsInfo.getMin());
			System.out.println("sum:" + fieldStatsInfo.getSum());
			System.out.println("mean:" + fieldStatsInfo.getMean());
			System.out.println("missing:" + fieldStatsInfo.getMissing());
			System.out.println("stddev:" + fieldStatsInfo.getStddev());// 标准差
		}
	}
}
