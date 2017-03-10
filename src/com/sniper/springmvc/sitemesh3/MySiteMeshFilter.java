package com.sniper.springmvc.sitemesh3;

import org.sitemesh.builder.SiteMeshFilterBuilder;
import org.sitemesh.config.ConfigurableSiteMeshFilter;

import com.sniper.springmvc.config.Global;

public class MySiteMeshFilter extends ConfigurableSiteMeshFilter {

	public String getDefaultTemplate() {
		return Global.getConfig("defaultTemplate");
	}

	public String getAdminPath() {
		return Global.getConfig("adminPath");
	}

	@Override
	protected void applyCustomConfiguration(SiteMeshFilterBuilder builder) {

		builder.addDecoratorPath(
				getAdminPath() + "**",
				"/WEB-INF/modules/themes/" + getDefaultTemplate()
						+ "/admin/main.jsp")
				.addExcludedPath(getAdminPath() + "/login**")
				.addExcludedPath(getAdminPath() + "/admin-print**")
				.addExcludedPath(getAdminPath() + "/file-upload**")
				.addExcludedPath("/upload**")
				.addDecoratorPath("/**",
						"/WEB-INF/modules/themes/home/main.jsp")
				.addExcludedPath("/myfiles/**")
				.addExcludedPath("/oauth2/**")
				.addExcludedPath("/error/**")
				.addExcludedPath(getAdminPath() + "/os/**")
				.addDecoratorPath("/", "/WEB-INF/modules/themes/home/home.jsp")
				.addExcludedPath("/score**")
				.addExcludedPath("/lottey**")
				.addExcludedPath("/topic/**")
				.addDecoratorPath("/survey**",
						"/WEB-INF/modules/themes/survey/survey.jsp")
				.addDecoratorPath("/login",
						"/WEB-INF/modules/themes/home/home.jsp");

	}
}
