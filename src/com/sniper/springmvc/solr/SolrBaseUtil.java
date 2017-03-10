package com.sniper.springmvc.solr;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sniper.springmvc.utils.SystemConfigUtil;
import com.sniper.springmvc.utils.ValidateUtil;

public abstract class SolrBaseUtil {

	protected static final long serialVersionUID = 1L;
	protected static final Logger LOGGER = LoggerFactory
			.getLogger(SolrUtil.class);
	// 时间格式
	protected static final SimpleDateFormat yearDateFormat = new SimpleDateFormat(
			"yyyyMM");
	/**
	 * solr地址 172.16.8.106,192.168.190.128
	 */
	protected static String solrServerUrl = "http://192.168.190.128:8983/solr/";
	// 实在多线程的调用过程中永远调用最后一次修改的数据 volatile
	protected volatile static Map<String, HttpSolrClient> solrClients = new HashMap<>();
	protected static HttpSolrClient client;

	/**
	 * 是线程安全的 需要采用单例模式 此处实现方法适用于高频率调用查询
	 * 
	 * @return
	 */
	public static HttpSolrClient getInstance(String name) {
		if (solrClients.get(name) == null) {
			// 一般填写返回的类class
			synchronized (HttpSolrClient.class) {
				if (solrClients.get(name) == null) {
					// 在系统中获取配置
					if (ValidateUtil.isValid(SystemConfigUtil
							.get("solrServerUrl"))) {
						solrServerUrl = SystemConfigUtil.get("solrServerUrl");
					}
					if (!solrServerUrl.endsWith("/")) {
						solrServerUrl = solrServerUrl + "/";
					}
					HttpSolrClient.Builder builder = new HttpSolrClient.Builder(
							solrServerUrl + name);
					solrClients.put(name, builder.build());
				}
			}
		}
		return solrClients.get(name);
	}

	/**
	 * 获取solr链接 初始化的HttpSolrServer 对象,并获取此唯一对象
	 * 
	 * @return
	 */
	public static HttpSolrClient getInstanceB(String name) {

		if (ValidateUtil.isValid(SystemConfigUtil.get("solrServerUrl"))) {
			solrServerUrl = SystemConfigUtil.get("solrServerUrl");
		}

		if (!solrServerUrl.endsWith("/")) {
			solrServerUrl = solrServerUrl + "/";
		}
		HttpSolrClient.Builder builder = new HttpSolrClient.Builder(
				solrServerUrl + name);
		client = builder.build();
		return client;
	}

	/**
	 * 获取当前表最大数据
	 * 
	 * @param name
	 * @return
	 */
	public static long getCount(String name) {
		HttpSolrClient client = getInstance(name);
		String queryStr = "*:*";
		SolrQuery params = new SolrQuery(queryStr);
		params.set("rows", 1);
		try {
			QueryResponse response = client.query(params);
			SolrDocumentList list = response.getResults();
			return list.getNumFound();
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}
		return 0l;
	}

	/**
	 * 清空数据
	 * 
	 * @param name
	 */
	public static void clear(String name) {
		try {
			HttpSolrClient client = getInstance(name);
			client.deleteByQuery("*:*");
			client.commit();
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取id内容
	 * 
	 * @param id
	 * @return
	 * @throws IOException
	 */
	public static SolrDocument get(String name, String id) {
		String queryStr = "id:" + id;
		SolrQuery params = new SolrQuery(queryStr);
		HttpSolrClient client = getInstance(name);
		try {
			QueryResponse response = client.query(params);
			SolrDocumentList list = response.getResults();
			for (SolrDocument doc : list) {
				return doc;
			}
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
			LOGGER.error("", e);
		}
		return null;
	}

	/**
	 * 获取solr时间格式
	 * 
	 * @param date
	 * @return
	 */
	public static String getSolrDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		return sdf.format(date);
	}

	/**
	 * 优化
	 * 
	 * @param name
	 */
	public static void optimize(String name) {
		try {
			HttpSolrClient client = getInstance(name);
			client.optimize();
			client.commit();

		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException {
		long solrCount = SolrViewUtil.getCount(SubjectViewModel.CORE_NAME);
		System.out.println(solrCount);

		// 优化测试
		// SolrViewUtil.optimize(SubjectViewModel.CORE_NAME);

		// 获取测试
		// SolrDocument document = SolrViewUtil
		// .get(SubjectModel.CORE_NAME, "1001");
		// System.out.println(document);
		// SolrViewUtil.clear(SubjectViewModel.CORE_NAME);
	}
}
