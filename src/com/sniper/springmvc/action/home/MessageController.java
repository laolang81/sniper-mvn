package com.sniper.springmvc.action.home;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.web.util.WebUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import com.sniper.springmvc.action.VerifyAction;
import com.sniper.springmvc.model.SdOpenApply;
import com.sniper.springmvc.model.SdTabLeaveword;
import com.sniper.springmvc.mybatis.service.impl.SdOpenApplyService;
import com.sniper.springmvc.mybatis.service.impl.SdTabLeavewordService;
import com.sniper.springmvc.searchUtil.CommentSearch;
import com.sniper.springmvc.utils.HttpRequestUtils;
import com.sniper.springmvc.utils.PageUtil;
import com.sniper.springmvc.utils.ValidateUtil;

/**
 * 留言处理
 * 
 * @author suzhen
 * 
 */
@RequestMapping("/leaveword")
@Controller
public class MessageController extends HomeBaseController {

	@Resource
	SdTabLeavewordService leavewordService;

	@Resource
	SdOpenApplyService applyService;
	// 类型
	private String t;
	// 文章id
	private String s;
	// 处室id
	private String d;

	/**
	 * 类型，文章，处室
	 * 
	 * @param t
	 *            类型
	 * @param s
	 *            文章
	 * @param d
	 *            处室
	 * @return
	 */
	@RequestMapping("{t}-{s}-{d}.html")
	public String index(@PathVariable("t") String t,
			@PathVariable("t") String s, @PathVariable("t") String d,
			Map<String, Object> map, CommentSearch commentSearch) {
		this.t = t;
		this.s = s;
		this.d = d;
		return "";
	}

	/**
	 * 厅长信箱
	 */
	public void MailToMag(Map<String, Object> map, CommentSearch commentSearch) {
		Map<String, Object> params = new HashMap<>();
		params.put("state", "0");
		params.put("show", 1);
		params.put("type", 6);
		if (ValidateUtil.isValid(commentSearch.getContent())) {
			params.put("content", "%" + commentSearch.getContent() + "%");
		}

		if (ValidateUtil.isValid(this.d)) {
			List<String> depids = new ArrayList<>();
			depids.add(this.d);
			params.put("depids", depids);
		}
		params.put("answer", 1);
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH, -1);

		int count = leavewordService.pageCount(params);
		PageUtil page = new PageUtil(count, 20);
		page.setRequest(request);
		String pageHtml = page.show();
		params.put("order", "id desc");
		params.put("pageOffset", page.getFristRow());
		params.put("pageSize", page.getListRow());
		List<SdTabLeaveword> lists = leavewordService.pageList(params);
		map.put("lists", lists);
		map.put("pageHtml", pageHtml);
		map.put("commentSearch", commentSearch);
	}

	/**
	 * 普通留言
	 */
	public void LeaveWord(Map<String, Object> map, CommentSearch commentSearch) {
		Map<String, Object> params = new HashMap<>();
		params.put("state", "0");
		params.put("show", 1);
		if (ValidateUtil.isValid(this.s)) {
			params.put("type", this.s);
		}

		if (ValidateUtil.isValid(commentSearch.getContent())) {
			params.put("content", "%" + commentSearch.getContent() + "%");
		}

		if (ValidateUtil.isValid(this.d)) {
			List<String> depids = new ArrayList<>();
			depids.add(this.d);
			params.put("depids", depids);
		}
		// 回复不得为空
		params.put("answer", 1);
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH, -1);

		int count = leavewordService.pageCount(params);
		PageUtil page = new PageUtil(count, 20);
		page.setRequest(request);
		String pageHtml = page.show();
		params.put("order", "id desc");
		params.put("pageOffset", page.getFristRow());
		params.put("pageSize", page.getListRow());
		List<SdTabLeaveword> lists = leavewordService.pageList(params);
		map.put("lists", lists);
		map.put("pageHtml", pageHtml);
		map.put("commentSearch", commentSearch);
	}

	/**
	 * 新闻留言
	 */
	public void LeavePost() {
		// 在线访谈
	}

	/**
	 * 新闻留言,负责新闻的添加
	 */
	public void message(PrintWriter out) {

		Map<String, Object> result = new HashMap<>();
		result.put("message", false);
		List<String> errors = new ArrayList<>();
		if (isXMLHttpRequest()) {
			String verity = WebUtils.getCleanParam(request, "verify");
			String verifyCode = (String) request.getSession().getAttribute(
					VerifyAction.VALIDATECODE);
			if (!verifyCode.equals(verity)) {
				errors.add("验证码不对");
			}

			SdTabLeaveword leaveword = new SdTabLeaveword();

			String ip = HttpRequestUtils.getRealIP(request);
			String name = WebUtils.getCleanParam(request, "name");
			if (name.length() < 2) {
				errors.add("姓名/称谓不得少于2个字");
			}
			leaveword.setUser(HtmlUtils.htmlEscape(name));
			String email = WebUtils.getCleanParam(request, "email");
			if (!ValidateUtil.isEmail(email)) {
				errors.add("无效邮箱");
			}
			leaveword.setEmail(HtmlUtils.htmlEscape(email));
			String content = WebUtils.getCleanParam(request, "content");
			if (content.length() > 1000 || content.length() < 10) {
				errors.add("内容请保持10-1000字符");
			}
			leaveword.setContent(HtmlUtils.htmlEscape(content));
			// 检测ip同一时间留言的数量，防止有人恶意留言
			String tel = WebUtils.getCleanParam(request, "tel");
			leaveword.setTel(HtmlUtils.htmlEscape(tel));
			leaveword.setState(1);

			String type = WebUtils.getCleanParam(request, "type");
			String sid = WebUtils.getCleanParam(request, "sid");
			if (ValidateUtil.isValid(sid)) {
				leaveword.setSid(Integer.valueOf(sid).intValue());
				leaveword.setType(4);
			}
			String open = WebUtils.getCleanParam(request, "open");
			if (ValidateUtil.isValid(open)) {
				leaveword.setBopen(1);
			} else {
				leaveword.setBmob(0);
			}
			leaveword.setIp(ip);
			leaveword.setPhpServerObject(request.getHeader("User-Agent"));

		}
	}

	/**
	 * 统一外部调用留言显示页面
	 */
	private String office(Map<String, Object> map) {
		String w = WebUtils.getCleanParam(request, "w");
		String h = WebUtils.getCleanParam(request, "h");
		Map<String, Map<String, String>> whs = new HashMap<>();
		Map<String, String> wh1 = new HashMap<>();
		wh1.put("w", "320");
		wh1.put("h", "400");
		whs.put("1", wh1);
		Map<String, String> wh2 = new HashMap<>();
		wh2.put("w", "240");
		wh2.put("h", "600");
		whs.put("2", wh2);

		if (ValidateUtil.isValid(w) && ValidateUtil.isValid(h)) {
			Map<String, String> wh3 = new HashMap<>();
			wh3.put("w", w);
			wh3.put("h", h);
			whs.put("3", wh3);
		}

		Map<String, String> resultWh = new HashMap<>();
		if (ValidateUtil.isValid(this.s)) {
			resultWh = whs.get(this.s);
		} else {
			resultWh = whs.get(whs.size() + "");
		}

		Map<String, Object> params = new HashMap<>();
		params.put("stategt", "1");
		params.put("show", 1);
		params.put("typelt", 6);

		if (ValidateUtil.isValid(this.d)) {
			List<String> depids = new ArrayList<>();
			depids.add(this.d);
			params.put("depids", depids);
		}
		int count = leavewordService.pageCount(params);
		PageUtil page = new PageUtil(count, 20);
		page.setRequest(request);
		String pageHtml = page.show();
		params.put("order", "id desc");
		params.put("pageOffset", page.getFristRow());
		params.put("pageSize", page.getListRow());
		List<SdTabLeaveword> lists = leavewordService.pageList(params);
		map.put("lists", lists);
		map.put("pageHtml", pageHtml);

		return "";
	}

	/**
	 * 只有留言界面
	 */
	public void LeaveWordSearch() {

	}

	/**
	 * /** 申请公开提交
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "openapply", method = RequestMethod.POST)
	public Map<String, String> openapply(SdOpenApply openApply) {

		Map<String, String> result = new HashMap<>();
		if (!ValidateUtil.isValid(openApply.getUsername())
				|| !ValidateUtil.isValid(openApply.getWorkunit())
				|| !ValidateUtil.isValid(openApply.getContent())) {
			result.put("message", "申请失败");
		} else {
			openApply.setXttype(openApply.getXttype().replace(",", "|"));
			openApply.setXtinfo(openApply.getXtinfo().replace(",", "|"));
			openApply.setStime(new Date());
			openApply.setEnabled(0);
			applyService.insert(openApply);
			result.put("message", "申请成功");
		}
		return result;
	}
}
