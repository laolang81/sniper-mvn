package com.sniper.springmvc.solr;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SolrTest {

	private static Logger logger = LoggerFactory.getLogger(SolrTest.class);

	private static final String URL = "http://192.168.190.128:8983/solr/demo";

	private HttpSolrClient server = null;

	@Before
	public void init() {
		// 创建 server
		server = new HttpSolrClient.Builder(URL).build();
	}

	public void addSubject() {

		SubjectModel model = new SubjectModel();
		model.setId("389872");
		model.setContent("佘春明出席山东外贸职业学院干部大会");
		model.setDepId(2);
		model.setDepName("人事处");
		model.setEnabled(true);
		model.setItemid(85);
		model.setItemidName("新闻资讯");
		model.setSource("商务网");
		model.setSubject("佘春明出席山东外贸职业学院干部大会");
		model.setTime(new Date());

		try {

			UpdateResponse response = server.addBean(model);
			// 提交
			server.commit();

			logger.info("########## Query Time :" + response.getQTime());
			logger.info("########## Elapsed Time :" + response.getElapsedTime());
			logger.info("########## Status :" + response.getStatus());

		} catch (SolrServerException | IOException e) {
			logger.error("", e);
		}
	}

	/**
	 * 添加文档
	 */
	public void addDoc() {

		SolrInputDocument doc = new SolrInputDocument();

		doc.addField("id", "11");
		doc.addField("title", "this is my document !!");

		try {

			UpdateResponse response = server.add(doc);
			// 提交
			server.commit();

			logger.info("########## Query Time :" + response.getQTime());
			logger.info("########## Elapsed Time :" + response.getElapsedTime());
			logger.info("########## Status :" + response.getStatus());

		} catch (SolrServerException | IOException e) {
			logger.error("", e);
		}
	}

	/**
	 * 添加多个文档
	 */
	public void addDocs() {

		String[] titles = new String[] { "aaaaaaa", "bbbbbbb", "ccccccc",
				"dddddd", "eeeeee" };

		List<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();

		int i = 0;
		for (String str : titles) {
			SolrInputDocument doc = new SolrInputDocument();
			doc.addField("id", i++);
			doc.addField("title", str);
			docs.add(doc);
		}

		try {

			UpdateResponse response = server.add(docs);
			server.commit();

			logger.info("########## Query Time :" + response.getQTime());
			logger.info("########## Elapsed Time :" + response.getElapsedTime());
			logger.info("########## Status :" + response.getStatus());

		} catch (SolrServerException | IOException e) {
			logger.error("", e);
		}
	}

	/**
	 * 添加一个Entity到索引库
	 */
	public void addBean() {

		Message msg = new Message(
				"1001",
				"What is Fluentd?",
				new String[] {
						"Fluentd is an open source data collector for unified logging layer",
						"Fluentd allows you to unify data collection and consumption for a better use and understanding of data.",
						"Fluentd decouples data sources from backend systems by providing a unified logging layer in between.",
						"Fluentd proves you can achieve programmer happiness and performance at the same time. A great example of Ruby beyond the Web.",
						"Fluentd to differentiate their products with better use of data." });

		try {

			UpdateResponse response = server.addBean(msg);
			server.commit();

			logger.info("########## Query Time :" + response.getQTime());
			logger.info("########## Elapsed Time :" + response.getElapsedTime());
			logger.info("########## Status :" + response.getStatus());

		} catch (SolrServerException | IOException e) {
			logger.error("", e);
		}
	}

	/**
	 * 添加多个Entity到索引库
	 */
	@Test
	public void addBeans() {

		List<Message> msgs = new ArrayList<Message>();

		Message msg = new Message(
				"1001",
				"What is Fluentd?",
				new String[] {
						"Fluentd is an open source data collector for unified logging layer",
						"Fluentd allows you to unify data collection and consumption for a better use and understanding of data.",
						"Fluentd decouples data sources from backend systems by providing a unified logging layer in between.",
						"Fluentd proves you can achieve programmer happiness and performance at the same time. A great example of Ruby beyond the Web.",
						"Fluentd to differentiate their products with better use of data." });

		Message msg2 = new Message(
				"1004",
				"山东在这?",
				new String[] {
						"Fluentd is an open source data collector for unified logging layer",
						"Fluentd allows you to unify data collection and consumption for a better use and understanding of data.",
						"Fluentd decouples data sources from backend systems by providing a unified logging layer in between.",
						"Fluentd proves you can achieve programmer happiness and performance at the same time. A great example of Ruby beyond the Web.",
						"Fluentd to differentiate their products with better use of data." });

		msgs.add(msg);
		msgs.add(msg2);

		try {

			UpdateResponse response = server.addBeans(msgs);
			server.commit();

			logger.info("########## Query Time :" + response.getQTime());
			logger.info("########## Elapsed Time :" + response.getElapsedTime());
			logger.info("########## Status :" + response.getStatus());

		} catch (SolrServerException | IOException e) {
			logger.error("", e);
		}
	}

	/**
	 * 删除索引
	 */
	public void deleteDoc() {
		try {

			SolrQuery params = new SolrQuery("*.*");
			System.out.println(params.toQueryString());
			UpdateResponse response = server.deleteByQuery(params
					.toQueryString());
			System.out.println(response.getStatus());
			server.commit();
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}

	/**
	 * 更新索引<br>
	 * solr索引库不同于数据库，没有更新的功能。如果想更新，先通过id删除对应的文档，再添加新的文档。
	 */
	public void updateDoc() {
		// ... ...
	}

	/**
	 * 查询
	 * 
	 * @throws IOException
	 */

	public void testQuery() throws IOException {
		String queryStr = "*:*";
		SolrQuery params = new SolrQuery(queryStr);
		params.set("q", "subject_s:葵缇亚");
		// params.addFilterQuery("subject_s:葵缇亚");
		params.set("rows", 10);
		System.out.println(params.toQueryString());
		try {
			QueryResponse response = server.query(params);
			SolrDocumentList list = response.getResults();
			logger.info("########### 总共 ： " + list.getNumFound() + "条记录");
			for (SolrDocument doc : list) {
				System.out.println(doc);
				logger.info("######### id : " + doc.get("id") + "  title : "
						+ doc.get("title"));
			}
		} catch (SolrServerException e) {
			logger.error("", e);
		}
	}

	/**
	 * 简单查询(分页)
	 * 
	 * @throws IOException
	 */
	public void querySimple() throws IOException {

		ModifiableSolrParams params = new ModifiableSolrParams();
		params.set("q", "*:*");
		SolrQuery filterQuery = new SolrQuery();
		filterQuery.addFilterQuery("enabled_b:true");
		// 2017-02-07T02:27:43.850Z
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		// []表示包含两边的范围取值，{}不包含两边的范围取值
		String time = "date_dt:[" + sdf.format(new Date()) + " TO "
				+ sdf.format(new Date()) + "]";
		String time2 = "date_dt:[2017-02-07T02:27:40Z TO 2017-02-07T02:27:44Z]";
		filterQuery.addFilterQuery(time2);
		params.add(filterQuery);
		params.set("q.op", "and");
		params.set("start", 0);
		params.set("rows", 5);
		params.set("fl", "*");
		// ?q=subject_s:*&fq=enabled_b:true&fq=date_dt:[2017-02-07T02:27:40Z%20TO%202017-02-07T02:27:44Z]&indent=on&wt=json
		// ?q=subject_s%3A*&fq=enabled_b%3Atrue&fq=fq&fq=date_dt%3A%5B2017-02-07T02%3A27%3A40Z+TO+2017-02-07T02%3A27%3A44Z%5D&q.op=and&start=0&rows=5&fl=*
		System.out.println(params.toQueryString());

		try {
			QueryResponse response = server.query(params);
			SolrDocumentList list = response.getResults();
			logger.info("########### 总共 ： " + list.getNumFound() + "条记录");
			for (SolrDocument doc : list) {
				System.out.println(doc);
				logger.info("######### id : " + doc.get("id") + "  title : "
						+ doc.get("title"));
			}
		} catch (SolrServerException e) {
			logger.error("", e);
		}
	}

	/**
	 * 查询(分页,高亮)
	 * 
	 * @throws IOException
	 */
	@Test
	public void queryCase() throws IOException {
		String queryStr = "postValue:Fluentd";
		SolrQuery params = new SolrQuery(queryStr);
		params.set("start", 0);
		params.set("rows", 10);

		System.out.println(params.toQueryString());
		// 启用高亮组件, 设置高亮
		params.setHighlight(true).addHighlightField("postValue")
				.setHighlightSimplePre("<span class=\"red\">")
				.setHighlightSimplePost("</span>").setHighlightSnippets(2)
				.setHighlightFragsize(1000).setStart(0).setRows(10)
				.set("hl.useFastVectorHighlighter", "true")
				.set("hl.fragsize", "200");

		try {
			QueryResponse response = server.query(params);
			SolrDocumentList list = response.getResults();
			logger.info("########### 总共 ： " + list.getNumFound() + "条记录");
			for (SolrDocument doc : list) {
				System.out.println(doc);
				logger.info("######### id : " + doc.get("id") + "  title : "
						+ doc.get("title"));
			}

			Map<String, Map<String, List<String>>> map = response
					.getHighlighting();
			Iterator<String> iterator = map.keySet().iterator();

			while (iterator.hasNext()) {
				String key = iterator.next();
				Map<String, List<String>> values = map.get(key);
				logger.info("############################################################");
				logger.info("############ id : " + key);

				for (Map.Entry<String, List<String>> entry : values.entrySet()) {
					String subKey = entry.getKey();
					List<String> subValues = entry.getValue();
					System.out.println(subKey);
					logger.info("############ subKey : " + subKey);
					for (String str : subValues) {
						System.out.println(str);
						logger.info("############ subValues : " + str);
					}
				}

			}

		} catch (SolrServerException e) {
			e.printStackTrace();
			logger.error("", e);
		}
	}
}
