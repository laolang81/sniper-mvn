package com.sniper.springmvc.action.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rometools.rome.io.FeedException;
import com.sniper.springmvc.action.RootController;
import com.sniper.springmvc.freemarker.ViewHomeUtils;
import com.sniper.springmvc.model.SdAttachments;
import com.sniper.springmvc.model.SdContent;
import com.sniper.springmvc.model.SdMofcomInfo;
import com.sniper.springmvc.model.SdViewSubject;
import com.sniper.springmvc.mybatis.service.impl.SdAttachmentsService;
import com.sniper.springmvc.mybatis.service.impl.SdContentService;
import com.sniper.springmvc.mybatis.service.impl.SdDepartmentsService;
import com.sniper.springmvc.mybatis.service.impl.SdItemsService;
import com.sniper.springmvc.mybatis.service.impl.SdMofcomInfoService;
import com.sniper.springmvc.mybatis.service.impl.SdSubjectsService;
import com.sniper.springmvc.mybatis.service.impl.SdViewSubjectService;
import com.sniper.springmvc.utils.DataUtil;
import com.sniper.springmvc.utils.FilesUtil;
import com.sniper.springmvc.utils.SystemConfigUtil;
import com.sniper.springmvc.utils.ValidateUtil;

/**
 * 由于数据结构复杂，下面只是一些例子 feedType = RSS/Atom
 * 
 * @author suzhen
 * 
 */
@Controller
@RequestMapping("/public/mofcom/")
public class MofcomController extends RootController {

	public static final String RSS_090 = "rss_0.9";
	public static final String RSS_092 = "rss_0.92";
	public static final String RSS_093 = "rss_0.93";
	public static final String RSS_094 = "rss_0.94";
	public static final String RSS_091_Netscape = "rss_0.91";
	public static final String RSS_091_Userland = "rss_0.91";
	public static final String RSS_100 = "rss_1.0";
	public static final String RSS_200 = "rss_2.0";
	public static final String ATOM_030 = "atom_0.3";
	public static final String ATOM_100 = "atom_1.0";

	@Resource
	SdMofcomInfoService mofcomInfoService;

	@Resource
	SdSubjectsService subjectsService;

	@Resource
	SdViewSubjectService viewSubjectService;

	@Resource
	SdItemsService itemsService;

	@Resource
	SdDepartmentsService departmentsService;

	@Resource
	SdAttachmentsService attachmentsService;

	@Resource
	SdContentService contentService;

	/**
	 * 
	 * @param writer
	 * @param mid
	 * @param syndate
	 * @return
	 * @throws IOException
	 * @throws FeedException
	 * @throws ParseException
	 */
	@RequestMapping(value = { "index.php", "" })
	@ResponseBody
	public void index(PrintWriter writer, @RequestParam("mid") int mid,
			@RequestParam(value = "syndate", required = false) String syndate,
			HttpServletResponse response) throws ParseException {

		response.setHeader("Content-Type", "text/xml");
		String webSite = FilesUtil.getWebUrl();
		String webName = SystemConfigUtil.get("webName");
		writer.write("<?xml version='1.0' encoding='utf-8'?>\n");
		writer.write("<rss version=\"2.0\">\n");
		writer.write("<channel>\n");
		writer.write("<title><![CDATA[" + webName + "——商务之窗信息]]></title>\n");
		writer.write("<link><![CDATA[" + webSite + "]]></link>\n");
		writer.write("<description><![CDATA[" + webName
				+ "——商务之窗信息]]></description>\n");
		writer.write("<language>zh_CN</language>\n");

		StringBuffer rssTemple = new StringBuffer("<item>\n");
		rssTemple.append("<title><![CDATA[{title}]]></title>\n");
		rssTemple.append("<link><![CDATA[{link}]]></link>\n");
		rssTemple.append("<subtitle><![CDATA[{subtitle}]]></subtitle>\n");
		rssTemple.append("<source><![CDATA[{source}]]></source>\n");
		rssTemple.append("<comments><![CDATA[{comments}]]></comments>\n");
		rssTemple.append("<description><![CDATA[{content}]]></description>\n");
		rssTemple.append("<pubDate>{date}</pubDate>\n");
		rssTemple.append("</item>\n");

		Map<String, Object> params = new HashMap<>();
		params.put("mid", mid);

		Calendar calendar = Calendar.getInstance();
		// 组件今天的日期
		if (ValidateUtil.isValid(syndate)) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
			dateFormat.parse(syndate);
			params.put("date",
					dateFormat.getCalendar().getTimeInMillis() / 1000);

		} else {
			// 获取今天凌晨的时间
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			params.put("dateGt", calendar.getTimeInMillis() / 1000);
		}

		List<SdMofcomInfo> infos = mofcomInfoService.query("select", params);

		StringBuffer entries = new StringBuffer();
		for (SdMofcomInfo info : infos) {
			SdViewSubject viewSubject = viewSubjectService.get(String
					.valueOf(info.getSid()));
			if (viewSubject == null || viewSubject.getLookthroughed() != 2) {
				continue;
			}
			// 存放附件
			StringBuffer files = new StringBuffer();
			List<SdAttachments> attachments = null;
			if (info.getMid() == 50127) {
				attachments = attachmentsService.getFiles(
						String.valueOf(info.getSid()), 1, null);
				if (!ValidateUtil.isValid(attachments)) {
					continue;
				}
			} else {
				attachments = attachmentsService.getFiles(
						String.valueOf(info.getSid()), 0, null);
			}
			if (ValidateUtil.isValid(attachments)) {
				int i = 1;
				for (SdAttachments attachment : attachments) {
					files.append("<b><font color=red>附件(");
					files.append(i);
					files.append("): <a href=\"");
					files.append(DataUtil.getImage(attachment.getFilename(),
							FilesUtil.getImagePrefix()));
					files.append("\" target=\"_blank\">");
					files.append(attachment.getDescription());
					files.append("</a></font></b><br>");
					i++;
				}
			}

			String type = "comment";
			StringBuffer title = new StringBuffer();
			title.append(viewSubject.getSubject());
			title.append(viewSubject.getSubtitle() != null ? viewSubject
					.getSubtitle() : "");
			StringBuffer link = new StringBuffer();
			link.append(webSite);
			link.append("/public/html/news/");
			link.append(ViewHomeUtils.intToDateString(viewSubject.getDate())
					.replace("-", "").substring(0, 6));
			link.append("/");
			link.append(viewSubject.getId());
			link.append(".html");
			int subtitle = info.getSubtitle();
			int source = info.getSource();
			// 这里是一张图片的地址
			SdAttachments attachment = attachmentsService
					.getOneImage(viewSubject.getId() + "");
			String comments = webSite;
			if (attachment != null) {
				comments = DataUtil.getImage(attachment.getFilename(),
						FilesUtil.getImagePrefix());
			}
			SdContent sdContent = contentService.get(String.valueOf(info
					.getSid()));
			String content = "";
			if (sdContent != null) {
				content = sdContent.getContent();
			}

			content += files.toString();
			Date date = new Date(info.getDate() * 1000l);
			String entry = rssTemple.toString().replace("{type}", type)
					.replace("{title}", title).replace("{link}", link)
					.replace("{subtitle}", String.valueOf(subtitle))
					.replace("{source}", String.valueOf(source))
					.replace("{comments}", comments)
					.replace("{content}", content)
					.replace("{date}", date.toString());

			entries.append(entry);
		}

		writer.write(entries.toString());
		writer.write("</channel>\n</rss>");
		writer.flush();
		writer.close();
	}

	@ResponseBody
	@RequestMapping(value = { "moftec.php" })
	public void moftec(PrintWriter writer, @RequestParam("mid") int mid,
			@RequestParam(value = "syndate", required = false) String syndate)
			throws ParseException {

	}
}
