package com.sniper.springmvc.action;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import redis.clients.jedis.ShardedJedis;

import com.sniper.springmvc.config.Global;
import com.sniper.springmvc.data.SystemRunTimeData;
import com.sniper.springmvc.freemarker.FreeMarkerUtil;
import com.sniper.springmvc.freemarker.FreeMarkerViewUtil;
import com.sniper.springmvc.model.Site;
import com.sniper.springmvc.utils.BaseHref;
import com.sniper.springmvc.utils.HttpRequestUtils;
import com.sniper.springmvc.utils.RedisUtil;
import com.sniper.springmvc.utils.SystemConfigUtil;
import com.sniper.springmvc.utils.ValidateUtil;

import freemarker.template.TemplateHashModel;

public abstract class RootController {

	/**
	 * 记录运行时间
	 */
	protected SystemRunTimeData runTimeData;
	/**
	 * 开启调试输出
	 */
	protected static boolean DEBUG = false;

	@Autowired
	protected ResourceBundleMessageSource messageSource;

	protected static final Logger LOGGER = LoggerFactory
			.getLogger(RootController.class);
	/**
	 * 否则保存一些基本信息
	 */
	protected BaseHref baseHref = new BaseHref();

	/**
	 * redis缓存
	 */
	protected static final RedisUtil REDIS = RedisUtil.getInstance();

	/**
	 * 后台地址
	 */
	protected static final String adminPath;
	static {
		adminPath = Global.getConfig("adminPath");
	}
	/**
	 * 默认语言
	 */
	protected Locale locale = LocaleContextHolder.getLocale();

	// 执行原始的request方法
	@Autowired
	protected HttpServletRequest request;

	protected static final Map<String, String> RIGHTTYPE = new HashMap<>();
	static {
		RIGHTTYPE.put("admin", "后台");
	}

	/**
	 * freemarker 一些函数组件
	 */
	protected TemplateHashModel hashModel = FreeMarkerUtil
			.getFreeMarkerStaticModel(FreeMarkerViewUtil.class);

	/**
	 * 站点模板
	 */
	protected static Site site = new Site();

	/**
	 * 检测是否是ajax
	 * 
	 * @return
	 */
	protected boolean isXMLHttpRequest() {
		String header = request.getHeader("X-Requested-With");
		if (header != null && "XMLHttpRequest".equals(header))
			return true;
		else
			return false;
	}

	/**
	 * 在系统初始化的时候加载
	 */
	public RootController() {
		// 这里可以做一些对类变量做一些永久赋值的操作,因为这里只会执行一次
	}

	/**
	 * 获取网页域名+路径
	 */
	private static String basePath = "";

	/**
	 * 获取网站完整域名
	 * 
	 * @return
	 */
	public String getBasePath() {
		if (!ValidateUtil.isValid(basePath)) {
			basePath = request.getScheme() + "://" + request.getServerName();
			if (request.getServerPort() != 80) {
				basePath += ":" + request.getServerPort();
			}
			basePath += request.getContextPath();
		}
		return basePath;
	}

	/**
	 * 所有方法之前必须执行的方法 这里一般做一些赋值操作，不可过度操作数据 否则会拖慢系统
	 * 
	 * @param map
	 */
	@ModelAttribute
	public void init(Map<String, Object> map) {
		// 因为有@ModelAttribute下面所有代码都会在程序之前运行
		runTimeData = new SystemRunTimeData();
		if (isXMLHttpRequest()) {
			return;
		}
		// 下面的数据测试，基本是0毫秒
		if (request.getParameter("debug") != null) {
			DEBUG = Boolean.valueOf(request.getParameter("debug"));
		}

		// 后台路径
		baseHref.setAdminPath(adminPath);
		// 带有后缀的域名
		baseHref.setBaseHref(getBasePath() + "/");
		// 无后缀域名
		baseHref.setWebUrl(getBasePath());
		// 系统配置数据
		map.put("systemConfig", SystemConfigUtil.getSystemConfig());
		map.put("baseHref", baseHref);
		// 处理freemarker模板助手
		map.put("freeMarkerUtils", hashModel);
		
		site.setName("default");
		runTimeData.addModelNode("Root Init 结束");
	}

	protected String redirect(String dir) {
		return UrlBasedViewResolver.REDIRECT_URL_PREFIX + dir;
	}

	protected String forward(String dir) {
		return dir;
	}

	protected String forward(String dir, String prefix) {
		return prefix + dir;
	}

	/**
	 * 检查来源是否是机器人 判断原理 是否是永远相同的浏览器信息，因为机器来源的浏览器信息一般都永远一样 检查短时间内相同ip，相同浏览器信息访问
	 * 
	 * @return
	 */
	public boolean isRebot() {
		ShardedJedis jedis = null;

		try {
			String ip = HttpRequestUtils.getRealIP(request);
			StringBuilder builder = new StringBuilder();
			builder.append("非法访问:");
			builder.append("IP:");
			builder.append(ip);
			builder.append(",");
			builder.append("时间:");
			builder.append(new Date());
			String userAgent = request.getHeader("user-agent");
			String rebotPreg = "bot|spider|crawl|nutch|lycos|robozilla|slurp|search|seek|archive|baidu|google";
			if (!ValidateUtil.isValid(userAgent)) {
				LOGGER.error(builder.toString());
				return true;
			}
			Pattern pattern = Pattern.compile(rebotPreg);
			Matcher matcher = pattern.matcher(userAgent);
			if (matcher.find()) {
				LOGGER.error(builder.toString());
				return true;
			}

			jedis = REDIS.getShardedPool().getResource();

			String sessionId = request.getSession().getId();
			// 用户访问记录
			int countInt = 0;
			// 一定时间内的访问次数
			int maxRebotCount = SystemConfigUtil.getInt("maxRebotCount");
			if (maxRebotCount == 0) {
				maxRebotCount = 3;
			}
			// 如果用户之前来访问过
			if (jedis.exists(sessionId)) {
				String count = jedis.get(sessionId);
				countInt = Integer.valueOf(count);
				if (countInt > maxRebotCount) {
					// 添加ip黑名单
					LOGGER.error(builder.toString());
					return true;
				} else {
					countInt++;
				}
			}
			jedis.set(sessionId, String.valueOf(countInt));
			// 设置访问频率3/2000访问
			jedis.pexpire(sessionId, 2000);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			REDIS.close(jedis);
		}
		return false;
	}
}