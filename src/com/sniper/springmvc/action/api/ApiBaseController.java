package com.sniper.springmvc.action.api;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.sniper.springmvc.action.RootController;
import com.sniper.springmvc.freemarker.ViewHomeUtils;
import com.sniper.springmvc.model.SdAttachments;
import com.sniper.springmvc.model.SdViewSubject;
import com.sniper.springmvc.mybatis.service.impl.SdAttachmentsService;
import com.sniper.springmvc.mybatis.service.impl.SdViewSubjectService;
import com.sniper.springmvc.utils.FilesUtil;
import com.sniper.springmvc.utils.StringUtil;
import com.sniper.springmvc.utils.ValidateUtil;

/**
 * 有关api 这个和后台程序可能会分开放，需要单独一个程序处理附件
 * 
 * @author suzhen
 * 
 */

public class ApiBaseController extends RootController {

	@Resource
	SdViewSubjectService viewSubjectService;

	@Resource
	SdAttachmentsService attachmentsService;

	/**
	 * 获取指定时间内的，图片新闻列表，注意新版，老板的区别
	 * 
	 * @param tid
	 * @param st
	 * @param et
	 * @throws ParseException
	 */
	public Map<String, Map<String, Object>> getAttachments(String tid,
			String st, String et) throws ParseException {
		// 如果没有时间，默认是挡天
		Map<String, Object> params = new HashMap<>();
		if (ValidateUtil.isValid(tid) && !tid.equals("0")) {
			params.put("sid", tid);
		} else {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			if (ValidateUtil.isValid(st) && !st.equals("0")) {
				dateFormat.parse(st);
				int stdate = (int) (dateFormat.getCalendar().getTimeInMillis() / 1000);
				params.put("stime", stdate);
			}

			if (ValidateUtil.isValid(et) && !et.equals("0")) {
				dateFormat.parse(et);
				int etdate = (int) (dateFormat.getCalendar().getTimeInMillis() / 1000);
				params.put("etime", etdate);
			}

			if (!ValidateUtil.isValid(st) || st.equals("0")) {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(new Date());
				calendar.add(Calendar.DATE, -1);
				params.put("stime", (int) (calendar.getTimeInMillis() / 1000));
			}
		}

		// 必须是图片
		params.put("isimage", 1);
		List<SdViewSubject> subjects = viewSubjectService.query("select",
				params);
		Map<String, Map<String, Object>> result = new LinkedHashMap<>();
		String weburl = FilesUtil.getImagePrefix();
		String rootDir = FilesUtil.getRootDir();
		// 检测域名是否配置
		if (!ValidateUtil.isValid(weburl)) {
			weburl = "http://sdcom.gov.cn";
		}
		for (SdViewSubject subject : subjects) {
			Map<String, Object> posts = new LinkedHashMap<>();
			posts.put(
					"weburl",
					weburl
							+ StringUtil.getSubjectUrl(subject.getDate(),
									subject.getId().toString()));
			posts.put("subject", subject.getSubject());
			posts.put("date", subject.getDate().toString());
			posts.put("authorid", subject.getAuthorid());
			posts.put("sid", subject.getId().toString());
			posts.put("postTime", subject.getDate().toString());
			posts.put("dateTime",
					ViewHomeUtils.intToDateString(subject.getDate()));
			List<SdAttachments> attachments = subject.getAttachments();
			// 保存图片附件
			List<Map<String, String>> atts = new ArrayList<>();
			for (SdAttachments attachment : attachments) {
				// 如果地址开头不是/说着这是第一版的新闻储存方法
				Map<String, String> att = new LinkedHashMap<>();
				att.put("filetype", attachment.getFiletype());
				String fileName = "";
				String bigFileName = "";
				// 最老的那一版
				if (!attachment.getFilename().startsWith("/")) {
					fileName = "public/public/attachments/"
							+ attachment.getFilename();
					bigFileName = fileName;
					// 第二版
				} else if (attachment.getFilename().startsWith(
						"/public/attachment/kindeditor")) {
					fileName = attachment.getFilename();
					bigFileName = fileName.replace("kindeditor/image",
							"bigimages");
					// 第三版,第三版原图地址被放到了description字段里面，但是根据第二版也进行的图片大图备份
				} else if (attachment.getFilename().startsWith(
						"/public/attachment/three/")) {
					fileName = attachment.getFilename();
					// 最新版的吧原图存放在description里面
					bigFileName = attachment.getDescription();
				}

				File file = new File(rootDir + bigFileName);
				if (file.exists() && file.isFile()) {
					att.put("filename", bigFileName);
				} else {
					att.put("filename", fileName);
				}

				att.put("description", subject.getSubject());
				att.put("sid", subject.getId().toString());
				atts.add(att);
			}
			posts.put("attachment", atts);
			result.put(subject.getId().toString(), posts);
		}

		return result;

	}

	public static void main(String[] args) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.parse("2016-09-22");
		long stdate = dateFormat.getCalendar().getTimeInMillis();
		System.out.println(stdate / 1000);
	}

}
