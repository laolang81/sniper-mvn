package com.sniper.springmvc.solr;

import java.io.IOException;
import java.util.Date;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.UpdateResponse;

import com.sniper.springmvc.freemarker.ViewHomeUtils;
import com.sniper.springmvc.model.SdAttachments;
import com.sniper.springmvc.model.SdDepartments;
import com.sniper.springmvc.model.SdItems;
import com.sniper.springmvc.model.SdSubjects;
import com.sniper.springmvc.utils.HtmlUtil;
import com.sniper.springmvc.utils.ValidateUtil;

public class SolrUtil extends SolrBaseUtil {

	/**
	 * 插入新闻 Solr添加重复数据只要id一样就会覆盖
	 * 
	 * @param model
	 * @return
	 */
	public static UpdateResponse insert(SubjectModel model) {
		try {
			HttpSolrClient client = getInstance(SubjectModel.CORE_NAME);
			if (ValidateUtil.isValid(model.getSubject())) {
				client.addBean(model);
				UpdateResponse response = client.commit();
				return response;
			}
		} catch (IOException | SolrServerException e) {
			LOGGER.error("添加错误: ID:" + model.getId(), e);
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
	public static UpdateResponse update(SubjectModel model) {
		try {
			HttpSolrClient client = getInstance(SubjectModel.CORE_NAME);
			client.deleteById(model.getId());
			client.addBean(model);
			UpdateResponse response = client.commit();

			return response;
		} catch (IOException | SolrServerException e) {
			LOGGER.error("添加错误: ID:" + model.getId(), e);
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
			HttpSolrClient client = getInstance(SubjectModel.CORE_NAME);
			client.deleteById(id);
			UpdateResponse response = client.commit();

			return response;
		} catch (SolrServerException | IOException e) {
			LOGGER.error("删除错误", e);
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
	public static SubjectModel getModel(SdSubjects subjects,
			SdDepartments departments, SdItems items) {
		SubjectModel model = new SubjectModel();
		model.setId(subjects.getSid().toString());
		model.setSubject(subjects.getSubject());
		model.setSource(subjects.getFromsite());
		Date date = ViewHomeUtils.intToDate(subjects.getDate());
		model.setTime(date);
		String links = "/public/html/news/201701/389550.html";
		String year = yearDateFormat.format(date);
		links = links.replace("201701", year).replace("389550", model.getId());
		// 组装内容页面地址
		model.setLinks(links);
		if (ValidateUtil.isValid(subjects.getContent())) {
			// 去除html标签
			model.setContent(HtmlUtil.getTextFromHtml(subjects.getContent()
					.getContent()));
		} else {
			model.setContent("");
		}
		model.setDepId(subjects.getSiteid());
		model.setDepName(departments.getName());
		model.setItemid(subjects.getItemid());
		model.setItemidName(items.getName());
		if (subjects.getLookthroughed() == 2) {
			model.setEnabled(true);
		} else {
			model.setEnabled(false);
		}
		if (ValidateUtil.isValid(subjects.getAttachments())) {
			for (SdAttachments attachments : subjects.getAttachments()) {
				// 只读取主要图片
				if (attachments.getMainsite() == 1) {
					model.setAttachment(attachments.getFilename());
					// 值储存一个
					continue;
				}
			}
		}
		return model;
	}

	/**
	 * 组装需要的数据
	 * 
	 * @param subjects
	 * @param departments
	 * @param items
	 */
	public static SubjectModel getModel(SdSubjects subjects) {
		SubjectModel model = new SubjectModel();
		model.setId(subjects.getSid().toString());
		model.setSubject(subjects.getSubject());
		model.setSource(subjects.getFromsite());
		Date date = ViewHomeUtils.intToDate(subjects.getDate());
		model.setTime(date);
		String links = "/public/html/news/201701/389550.html";
		String year = yearDateFormat.format(date);
		links = links.replace("201701", year).replace("389550", model.getId());
		// 组装内容页面地址
		model.setLinks(links);
		if (ValidateUtil.isValid(subjects.getContent())) {
			// 去除html标签
			model.setContent(HtmlUtil.getTextFromHtml(subjects.getContent()
					.getContent()));
		} else {
			model.setContent("");
		}
		model.setDepId(subjects.getSiteid());
		model.setDepName("");
		model.setItemid(subjects.getItemid());
		model.setItemidName("");
		if (subjects.getLookthroughed() == 2) {
			model.setEnabled(true);
		} else {
			model.setEnabled(false);
		}
		if (ValidateUtil.isValid(subjects.getAttachments())) {
			for (SdAttachments attachments : subjects.getAttachments()) {
				// 只读取主要图片
				if (attachments.getMainsite() == 1) {
					model.setAttachment(attachments.getFilename());
				}
			}
		}

		return model;
	}

}
