package com.sniper.springmvc.action.junit;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.junit.Test;

import com.sniper.springmvc.solr.SolrViewUtil;
import com.sniper.springmvc.solr.SubjectViewModel;

public class SolrJunit {

	@Test
	public void test() throws SolrServerException, IOException {

		HttpSolrClient solrClient = SolrViewUtil
				.getInstance(SubjectViewModel.CORE_NAME);
		int pageOffset = 0;

		ModifiableSolrParams params = new ModifiableSolrParams();
		params.set("q", "*:*");
		SolrQuery filterQuery = new SolrQuery();

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		// 默认结束日日期
		Date endDate = new Date();

		filterQuery.setSort("id", ORDER.desc);
		params.add(filterQuery);

		params.set("start", 0);
		params.set("rows", 10);
		params.set("fl", "*");
		System.out.println(params.toQueryString());
		QueryResponse response = solrClient.query(params);
		SolrDocumentList list = response.getResults();
		List<SubjectViewModel> lists = response
				.getBeans(SubjectViewModel.class);
		System.out.println(lists);

	}
}
