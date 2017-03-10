package com.sniper.springmvc.action.home;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sniper.springmvc.freemarker.FreeMarkerUtil;
import com.sniper.springmvc.freemarker.TopicResolver;
import com.sniper.springmvc.mybatis.service.impl.AdminUserService;
import com.sniper.springmvc.mybatis.service.impl.SdUserService;
import com.sniper.springmvc.mybatis.service.impl.SdViewSubjectService;
import com.sniper.springmvc.mybatis.service.impl.TagsService;
import com.sniper.springmvc.solr.SolrViewUtil;
import com.sniper.springmvc.solr.SubjectViewModel;
import com.sniper.springmvc.utils.ExeclStaticExportUtils;
import com.sniper.springmvc.utils.HttpRequestUtils;

import freemarker.template.TemplateHashModel;

@Controller
public class IndexController extends HomeBaseController {

	@Resource
	TagsService tagsService;

	@Resource
	private AdminUserService adminUserService;

	@Resource
	SdUserService userService;

	/**
	 * 专题接口
	 */
	protected TemplateHashModel subjectUtil = FreeMarkerUtil
			.getFreeMarkerStaticModel(TopicResolver.class);

	@Resource
	SdViewSubjectService viewSubjectService;

	@RequestMapping("")
	public String index(Map<String, Object> map) {

		// List<SdViewSubject> bhot = viewSubjectService.getSolrBhot(2);
		// for (SdViewSubject subject : bhot) {
		// System.out.println(subject);
		// }
		// // System.out.println(bhot);
		//
		// List<SdViewSubject> tops = viewSubjectService.getSolrTops(10);
		// System.out.println(tops);
		//
		// List<SdViewSubject> slides = viewSubjectService.getSolrSlides(0, 10);
		// for (SdViewSubject subject : slides) {
		// System.out.println(subject);
		// }
		// System.out.println(slides);
		//
		// List<Integer> items = new ArrayList<>();
		// items.add(84);
		// items.add(1084);
		// List<SdViewSubject> slideItems = viewSubjectService
		// .getSolrSlidesByItemid(items,
		// SdViewSubjectService.FILED_NAME_IS_IMG, 10);
		// System.out.println(slideItems);

		// 测试solr新闻读取
		// List<SdViewSubject> solrSubjects = viewSubjectService
		// .getSolrSubjectByItem(85, 10);
		// System.out.println(solrSubjects);

		request.getSession().setAttribute("SessionRedis", "SessionRedis");
		System.out.println(request.getSession().getAttribute("SessionRedis"));

		System.out.println(HttpRequestUtils.getRealIP(request));
		map.put("subjectUtil", subjectUtil);
		return "home/index/index.ftl";
	}

	// solr全文匹配搜索
	public static void main(String[] args) throws SolrServerException,
			IOException {
		HttpSolrClient client = SolrViewUtil
				.getInstance(SubjectViewModel.CORE_NAME);
		SolrQuery query = new SolrQuery("*:*");
		// 加引号表示全瓷匹配
		query.addFilterQuery("subject:\"夏耕\"");
		query.set("start", 0);
		query.set("rows", 100000);

		query.setSort("date_dt", ORDER.desc);
		QueryResponse response = client.query(query);
		List<SubjectViewModel> models = response
				.getBeans(SubjectViewModel.class);
		System.out.println(models.size());

		// 主要的数据读取
		Map<String, String> headerFields = new LinkedHashMap<>();
		headerFields.put("id", "序号");
		headerFields.put("subject", "标题");
		headerFields.put("date", "日期");
		List<Map<String, String>> HEAD_FIELDS_ALL = new ArrayList<>();
		HEAD_FIELDS_ALL.add(headerFields);

		ExeclStaticExportUtils exportUtils = new ExeclStaticExportUtils();
		exportUtils.setHeadFieldsAll(HEAD_FIELDS_ALL);
		exportUtils.setHeaderFields(headerFields);
		exportUtils.setRootDir("/Users/suzhen/Desktop/");
		exportUtils.exportSolrSubject("solr.xlsx", models);
	}
}