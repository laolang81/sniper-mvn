package com.sniper.springmvc.action.admin;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.web.util.WebUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sniper.springmvc.mybatis.service.impl.SolrSearchKeyService;
import com.sniper.springmvc.searchUtil.SolrSearch;
import com.sniper.springmvc.solr.SolrViewUtil;
import com.sniper.springmvc.solr.SubjectViewModel;
import com.sniper.springmvc.utils.PageUtil;
import com.sniper.springmvc.utils.ValidateUtil;

@Controller
@RequestMapping("${adminPath}/admin-solr")
public class AdminSolrController extends AdminBaseController {

	@Resource
	SolrSearchKeyService solrSearchKeyService;

	@RequestMapping("subject")
	public String index(Map<String, Object> map, SolrSearch search)
			throws IOException, SolrServerException, ParseException {

		HttpSolrClient solrClient = SolrViewUtil
				.getInstance(SubjectViewModel.CORE_NAME);
		int pageSize = search.getPageSize();
		int pageOffset = 0;
		String pageNoStr = WebUtils.getCleanParam(request, "pageNo");
		if (!ValidateUtil.isValid(pageNoStr)) {
			pageNoStr = "1";
		}
		int pageNo = Integer.valueOf(pageNoStr).intValue();
		if (pageNo == 1) {
			pageNo = 1;
			pageOffset = 0;
		} else {
			pageOffset = (pageNo - 1) * pageSize;
		}

		ModifiableSolrParams params = new ModifiableSolrParams();
		params.set("q", "*:*");
		SolrQuery filterQuery = new SolrQuery();
		if (ValidateUtil.isValid(search.getName())) {
			filterQuery.addFilterQuery("subject:" + search.getName());
			filterQuery.addFilterQuery("subjectContent:" + search.getName());
		}
		if (ValidateUtil.isValid(search.getStatus())) {
			filterQuery.addFilterQuery("lookthroughed_i:"
					+ search.getStatus().toString());
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		// 默认结束日日期
		Date endDate = new Date();
		Date startDate = dateFormat.parse("1970-01-01");

		if (ValidateUtil.isValid(search.getEndDate())) {
			endDate = dateFormat.parse(search.getEndDate());
		}
		if (ValidateUtil.isValid(search.getStartDate())) {
			startDate = dateFormat.parse(search.getStartDate());
		}
		if (ValidateUtil.isValid(search.getEndDate())
				|| ValidateUtil.isValid(search.getStartDate())) {
			filterQuery.addDateRangeFacet("date_dt", startDate, endDate,
					"+1DAY");
		}

		filterQuery.setSort("id", ORDER.desc);
		params.add(filterQuery);

		params.set("start", pageOffset);
		params.set("rows", pageSize);
		params.set("fl", "*");
		System.out.println(params.toQueryString());
		QueryResponse response = solrClient.query(params);
		SolrDocumentList lists = response.getResults();

		PageUtil page = new PageUtil(Long.valueOf(lists.getNumFound())
				.intValue(), pageSize);
		page.setRequest(request);
		String pageHtml = page.show();

		map.put("lists", lists);
		map.put("search", search);
		map.put("pageHtml", pageHtml);
		return forward("/admin/admin-solr/index.jsp");
	}

	public String searchKey() {
		return "";
	}

}
