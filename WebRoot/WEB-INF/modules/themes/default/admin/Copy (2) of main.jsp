<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="http://shiro.apache.org/tags" prefix="shiro"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://shiro.apache.org/tags" prefix="shiro"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta charset="utf-8" />
<title><sitemesh:write property="title" />${baseHref.pageTitle}
	- | 后台管理</title>
<base href="${baseHref.baseHref }">
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />
<!-- bootstrap & fontawesome -->
<link rel="stylesheet" href="myfiles/temple/assets/css/bootstrap.css" />
<link rel="stylesheet" href="myfiles/temple/assets/css/font-awesome.css" />
<!-- page specific plugin styles -->
<!-- text fonts -->
<link rel="stylesheet" href="myfiles/temple/assets/css/ace-fonts.css" />
<!-- ace styles -->
<link rel="stylesheet" href="myfiles/temple/assets/css/ace.css"
	class="ace-main-stylesheet" id="main-ace-style" />
<!--[if lte IE 9]>
	<link rel="stylesheet" href="myfiles/temple/assets/css/ace-part2.css" class="ace-main-stylesheet" />
<![endif]-->
<!--[if lte IE 9]>
  <link rel="stylesheet" href="myfiles/temple/assets/css/ace-ie.css" />
<![endif]-->
<!-- inline styles related to this page -->
<!--[if !IE]> -->
<script type="text/javascript">
	window.jQuery
			|| document
					.write("<script src='myfiles/temple/assets/js/jquery.js'>"
							+ "<"+"/script>");
</script>
<!-- <![endif]-->
<!--[if IE]>
<script type="text/javascript">
 window.jQuery || document.write("<script src='myfiles/temple/assets/js/jquery1x.js'>"+"<"+"/script>");
</script>
<![endif]-->
<!-- ace settings handler -->
<script src="myfiles/temple/assets/js/ace-extra.js"></script>
<!-- HTML5shiv and Respond.js for IE8 to support HTML5 elements and media queries -->
<!--[if lte IE 8]>
<script src="myfiles/temple/assets/js/html5shiv.js"></script>
<script src="myfiles/temple/assets/js/respond.js"></script>
<![endif]-->
<link href="${baseHref.baseHref }myfiles/css/admin.css" media="screen"
	rel="stylesheet" type="text/css">
<link
	href="${baseHref.baseHref }myfiles/Plugin/artDialog/css/ui-dialog.css"
	media="screen" rel="stylesheet" type="text/css">
<script type="text/javascript"
	src="${baseHref.baseHref }myfiles/Plugin/artDialog/dist/dialog-min.js"></script>

<sitemesh:write property='head' />

<style type="text/css">
.btn-group>.btn>.caret {
	margin-top: 0px;
}
</style>
</head>

<body class="no-skin">
	<!-- #section:basics/navbar.layout -->
	<div id="navbar" class="navbar navbar-default">
		<script type="text/javascript">
			try {
				ace.settings.check('navbar', 'fixed')
			} catch (e) {
			}
		</script>

		<div class="navbar-container" id="navbar-container">
			<!-- #section:basics/sidebar.mobile.toggle -->
			<button type="button" class="navbar-toggle menu-toggler pull-left"
				id="menu-toggler" data-target="#sidebar">
				<span class="sr-only">Toggle sidebar</span> <span class="icon-bar"></span>
				<span class="icon-bar"></span> <span class="icon-bar"></span>
			</button>

			<!-- /section:basics/sidebar.mobile.toggle -->
			<div class="navbar-header pull-left">
				<!-- #section:basics/navbar.layout.brand -->
				<a href="#" class="navbar-brand"> <small> <i
						class="fa fa-leaf"></i> ${systemConfig.webName}
				</small>
				</a>

				<!-- /section:basics/navbar.layout.brand -->

				<!-- #section:basics/navbar.toggle -->

				<!-- /section:basics/navbar.toggle -->
			</div>

			<!-- #section:basics/navbar.dropdown -->
			<div class="navbar-buttons navbar-header pull-right"
				role="navigation">
				<ul class="nav ace-nav">
					<li class="sniperCaldenar"><a data-toggle="dropdown"
						class="dropdown-toggle" href="#"> <i
							class="ace-icon fa fa-calendar"></i> <span
							class="badge badge-grey">0</span>
					</a>
						<ul
							class="dropdown-menu-right dropdown-navbar dropdown-menu dropdown-caret dropdown-close">
							<li class="dropdown-header"><i class="ace-icon fa fa-check"></i>
								事件列表</li>
							<li class="dropdown-content">
								<ul class="dropdown-menu dropdown-navbar">

								</ul>
							</li>
							<li class="dropdown-footer"><a href="admin/admin-calendar/">
									查看所有 <i class="ace-icon fa fa-arrow-right"></i>
							</a></li>
						</ul></li>
					<shiro:hasRole name="ROLE_ADMIN">
						<li class="grey"><a data-toggle="dropdown"
							class="dropdown-toggle" href="#"> <i
								class="ace-icon fa fa-tasks"></i> <span class="badge badge-grey">4</span>
						</a>

							<ul
								class="dropdown-menu-right dropdown-navbar dropdown-menu dropdown-caret dropdown-close">
								<li class="dropdown-header"><i class="ace-icon fa fa-check"></i>
									4 Tasks to complete</li>

								<li class="dropdown-content">
									<ul class="dropdown-menu dropdown-navbar">
										<li><a href="#">
												<div class="clearfix">
													<span class="pull-left">Task Name</span> <span
														class="pull-right">65%</span>
												</div>

												<div class="progress progress-mini">
													<div style="width:65%" class="progress-bar"></div>
												</div>
										</a></li>
									</ul>
								</li>

								<li class="dropdown-footer"><a href="#"> See tasks with
										details <i class="ace-icon fa fa-arrow-right"></i>
								</a></li>
							</ul></li>

						<li class="purple"><a data-toggle="dropdown"
							class="dropdown-toggle" href="#"> <i
								class="ace-icon fa fa-bell icon-animated-bell"></i> <span
								class="badge badge-important">8</span>
						</a>

							<ul
								class="dropdown-menu-right dropdown-navbar navbar-pink dropdown-menu dropdown-caret dropdown-close">
								<li class="dropdown-header"><i
									class="ace-icon fa fa-exclamation-triangle"></i> 8
									Notifications</li>

								<li class="dropdown-content">
									<ul class="dropdown-menu dropdown-navbar navbar-pink">
										<li><a href="#">
												<div class="clearfix">
													<span class="pull-left"> <i
														class="btn btn-xs no-hover btn-pink fa fa-comment"></i>
														New Comments
													</span> <span class="pull-right badge badge-info">+12</span>
												</div>
										</a></li>
									</ul>
								</li>

								<li class="dropdown-footer"><a href="#"> See all
										notifications <i class="ace-icon fa fa-arrow-right"></i>
								</a></li>
							</ul></li>

						<li class="green"><a data-toggle="dropdown"
							class="dropdown-toggle" href="#"> <i
								class="ace-icon fa fa-envelope icon-animated-vertical"></i> <span
								class="badge badge-success">5</span>
						</a>
							<ul
								class="dropdown-menu-right dropdown-navbar dropdown-menu dropdown-caret dropdown-close">
								<li class="dropdown-header"><i
									class="ace-icon fa fa-envelope-o"></i> 5 Messages</li>

								<li class="dropdown-content">
									<ul class="dropdown-menu dropdown-navbar">
										<li><a href="#" class="clearfix"> <img
												src="myfiles/temple/assets/avatars/avatar.png"
												class="msg-photo" alt="Alex's Avatar" /> <span
												class="msg-body"> <span class="msg-title"> <span
														class="blue">Alex:</span> Message Summary
												</span> <span class="msg-time"> <i
														class="ace-icon fa fa-clock-o"></i> <span>Message
															Time</span>
												</span>
											</span>
										</a></li>
									</ul>
								</li>

								<li class="dropdown-footer"><a href="inbox.html"> See
										all messages <i class="ace-icon fa fa-arrow-right"></i>
								</a></li>
							</ul></li>
					</shiro:hasRole>
					<!-- #section:basics/navbar.user_menu -->
					<li class="light-blue"><a data-toggle="dropdown" href="#"
						class="dropdown-toggle"> <img class="nav-user-photo"
							src="myfiles/temple/assets/avatars/user.jpg" alt="Jason's Photo" />
							<span class="user-info"> <small>Welcome,</small> <shiro:principal />
						</span> <i class="ace-icon fa fa-caret-down"></i>
					</a>
						<ul
							class="user-menu dropdown-menu-right dropdown-menu dropdown-yellow dropdown-caret dropdown-closer">
							<li><a href=".${baseHref.adminPath }/admin-center/profile">
									<i class="ace-icon fa fa-cog"></i> 设置
							</a></li>
							<li class="divider"></li>
							<li><a href="admin/logout"> <i
									class="ace-icon fa fa-power-off"></i> Logout
							</a></li>
						</ul></li>

					<!-- /section:basics/navbar.user_menu -->
				</ul>
			</div>
			<!-- /section:basics/navbar.dropdown -->
		</div>
		<!-- /.navbar-container -->
	</div>
	<!-- /section:basics/navbar.layout -->
	<div class="main-container" id="main-container">
		<!-- #section:basics/sidebar -->
		<div id="sidebar" class="sidebar responsive">
			<script type="text/javascript">
				try {
					ace.settings.check('sidebar', 'fixed')
				} catch (e) {
				}
			</script>

			<div class="sidebar-shortcuts" id="sidebar-shortcuts">
				<div class="sidebar-shortcuts-large" id="sidebar-shortcuts-large">
					<button class="btn btn-success">
						<i class="ace-icon fa fa-signal"></i>
					</button>

					<button class="btn btn-info">
						<i class="ace-icon fa fa-pencil"></i>
					</button>

					<!-- #section:basics/sidebar.layout.shortcuts -->
					<button class="btn btn-warning">
						<i class="ace-icon fa fa-user"></i>
					</button>

					<button class="btn btn-danger">
						<i class="ace-icon fa fa-cogs"></i>
					</button>

					<!-- /section:basics/sidebar.layout.shortcuts -->
				</div>

				<div class="sidebar-shortcuts-mini" id="sidebar-shortcuts-mini">
					<span class="btn btn-success"></span> <span class="btn btn-info"></span>
					<span class="btn btn-warning"></span> <span class="btn btn-danger"></span>
				</div>
			</div>
			<!-- /.sidebar-shortcuts -->
			<ul class="nav nav-list">
				<c:set var="menuTopBar">首页</c:set>
				<c:forEach items="${menuTop }" var="mt">
					<c:set var="id">${mt.id }</c:set>
					<c:set var="icon">desktop</c:set>
					<c:if test="${mt.icon != null }">
						<c:set var="icon">${mt.icon }</c:set>
					</c:if>
					<li id="menuSub_${mt.id }" class=""><a
						<c:if test="${menuSub[id] != null }">class="dropdown-toggle"</c:if>
						href="${baseHref.baseHref }${fn:substring(mt.url, 1, 100)}"> <i
							class="menu-icon fa fa-${icon }"></i> <span class="menu-text">
								${mt.name } </span> <c:if test="${menuSub[id] != null }">
								<b class="arrow fa fa-angle-down"></b>
							</c:if>
					</a> <b class="arrow"></b> <c:if test="${menuSub[id] != null }">
							<ul class="submenu">
								<c:forEach var="sms" items="${menuSub[id] }">
									<c:set var="tid">${sms.id }</c:set>
									<li id="menuSub_${sms.id }" class=""><a
										<c:if test="${menuThree[tid] != null }">class="dropdown-toggle"</c:if>
										href="${baseHref.baseHref }${fn:substring(sms.url, 1, 100)}">
											<i class="menu-icon fa fa-caret-right"></i> <span
											class="menu-text">${sms.name }</span> <c:if
												test="${menuThree[tid] != null }">
												<b class="arrow fa fa-angle-down"></b>
											</c:if>
									</a> <b class="arrow"></b> <c:if test="${menuThree[tid] != null }">
											<ul class="submenu">
												<c:forEach var="tms" items="${menuThree[tid] }">
													<li id="menuSub_${tms.id }" class=""><a
														href="${baseHref.baseHref }${fn:substring(tms.url, 1, 100)}">
															<i class="menu-icon fa fa-caret-right"></i> ${tms.name }
													</a> <b class="arrow"></b></li>
												</c:forEach>
											</ul>
										</c:if></li>
								</c:forEach>
							</ul>
						</c:if></li>
				</c:forEach>
			</ul>
			<!-- /.nav-list -->

			<!-- #section:basics/sidebar.layout.minimize -->
			<div class="sidebar-toggle sidebar-collapse" id="sidebar-collapse">
				<i class="ace-icon fa fa-angle-double-left"
					data-icon1="ace-icon fa fa-angle-double-left"
					data-icon2="ace-icon fa fa-angle-double-right"></i>
			</div>

			<!-- /section:basics/sidebar.layout.minimize -->
			<script type="text/javascript">
				try {
					ace.settings.check('sidebar', 'collapsed')
				} catch (e) {
				}
			</script>
		</div>

		<!-- /section:basics/sidebar -->
		<div class="main-content">
			<div class="page-content">
				<!-- #section:settings.box -->
				<div class="ace-settings-container" id="ace-settings-container">
					<div class="btn btn-app btn-xs btn-warning ace-settings-btn"
						id="ace-settings-btn">
						<i class="ace-icon fa fa-cog bigger-130"></i>
					</div>

					<div class="ace-settings-box clearfix" id="ace-settings-box">
						<div class="pull-left width-50">
							<!-- #section:settings.skins -->
							<div class="ace-settings-item">
								<div class="pull-left">
									<select id="skin-colorpicker" class="hide">
										<option data-skin="no-skin" value="#438EB9">#438EB9</option>
										<option data-skin="skin-1" value="#222A2D">#222A2D</option>
										<option data-skin="skin-2" value="#C6487E">#C6487E</option>
										<option data-skin="skin-3" value="#D0D0D0">#D0D0D0</option>
									</select>
								</div>
								<span>&nbsp; Choose Skin</span>
							</div>

							<!-- /section:settings.skins -->

							<!-- #section:settings.navbar -->
							<div class="ace-settings-item">
								<input type="checkbox" class="ace ace-checkbox-2"
									id="ace-settings-navbar" /> <label class="lbl"
									for="ace-settings-navbar"> Fixed Navbar</label>
							</div>

							<!-- /section:settings.navbar -->

							<!-- #section:settings.sidebar -->
							<div class="ace-settings-item">
								<input type="checkbox" class="ace ace-checkbox-2"
									id="ace-settings-sidebar" /> <label class="lbl"
									for="ace-settings-sidebar"> Fixed Sidebar</label>
							</div>

							<!-- /section:settings.sidebar -->

							<!-- #section:settings.breadcrumbs -->
							<div class="ace-settings-item">
								<input type="checkbox" class="ace ace-checkbox-2"
									id="ace-settings-breadcrumbs" /> <label class="lbl"
									for="ace-settings-breadcrumbs"> Fixed Breadcrumbs</label>
							</div>

							<!-- /section:settings.breadcrumbs -->

							<!-- #section:settings.rtl -->
							<div class="ace-settings-item">
								<input type="checkbox" class="ace ace-checkbox-2"
									id="ace-settings-rtl" /> <label class="lbl"
									for="ace-settings-rtl"> Right To Left (rtl)</label>
							</div>

							<!-- /section:settings.rtl -->

							<!-- #section:settings.container -->
							<div class="ace-settings-item">
								<input type="checkbox" class="ace ace-checkbox-2"
									id="ace-settings-add-container" /> <label class="lbl"
									for="ace-settings-add-container"> Inside <b>.container</b>
								</label>
							</div>

							<!-- /section:settings.container -->
						</div>
						<!-- /.pull-left -->

						<div class="pull-left width-50">
							<!-- #section:basics/sidebar.options -->
							<div class="ace-settings-item">
								<input type="checkbox" class="ace ace-checkbox-2"
									id="ace-settings-hover" /> <label class="lbl"
									for="ace-settings-hover"> Submenu on Hover</label>
							</div>

							<div class="ace-settings-item">
								<input type="checkbox" class="ace ace-checkbox-2"
									id="ace-settings-compact" /> <label class="lbl"
									for="ace-settings-compact"> Compact Sidebar</label>
							</div>

							<div class="ace-settings-item">
								<input type="checkbox" class="ace ace-checkbox-2"
									id="ace-settings-highlight" /> <label class="lbl"
									for="ace-settings-highlight"> Alt. Active Item</label>
							</div>

							<!-- /section:basics/sidebar.options -->
						</div>
						<!-- /.pull-left -->
					</div>
					<!-- /.ace-settings-box -->
				</div>
				<!-- /.ace-settings-container -->
				<div class="page-header">
					<h1>
						<small> <i class="ace-icon fa fa-angle-double-right"></i>
							${baseHref.adminRight.name }
						</small>
					</h1>
				</div>
				<div class="row">
					<div class="col-xs-12">
						<!-- PAGE CONTENT BEGINS -->
						<sitemesh:write property='body' />
						<!-- PAGE CONTENT ENDS -->
					</div>
					<!-- /.col -->
				</div>
				<!-- /.row -->
			</div>
			<!-- /.page-content -->
		</div>
		<!-- /.main-content -->
		<div class="footer">
			<div class="footer-inner">
				<div class="footer-content">
					${systemConfig.webAdminName } Application ©
					2014-${baseHref.nowYear}
					<!-- footer content here -->
				</div>
			</div>
		</div>
	</div>
	<!-- /.main-container -->

	<!-- basic scripts -->
	<script type="text/javascript" src="myfiles/js/jquery.main.js"></script>
	<script type="text/javascript">
	<!--
		/* 菜单选中设值 */
		$(document)
				.ready(
						function() {
							var openid = '${baseHref.adminRight.id}';
							if (openid) {
								$("#menuSub_" + openid).addClass("active");
								$parent = $("#menuSub_" + openid).parent()
										.parent();
								// 选中自己
								$("#menuSub_" + openid).addClass("active");
								// 打开父级
								$parent.addClass("active open");
								var menu_text = $parent.find('a > span').html();

								$parents = $parent.parents("li");
								if ($parents != undefined) {
									$parents.addClass("active open");
									var menu_text1 = $parents.find('a > span')
											.html();
									if (menu_text1 != undefined) {
										$('.page-header h1 small').before(
												"<small>" + menu_text
														+ "</small>");
										$('.page-header h1')
												.prepend(
														menu_text1
																+ ' <i class="ace-icon fa fa-angle-double-right"></i>');
									} else {
										$('.page-header h1 small').before(
												menu_text);
									}
								} else {
									$('.page-header h1 small')
											.before(menu_text);
								}
							}
						});

		// 获取事件

		$().SniperMain();
	//-->
	</script>

	<script type="text/javascript">
		if ('ontouchstart' in document.documentElement)
			document
					.write("<script src='myfiles/temple/assets/js/jquery.mobile.custom.js'>"
							+ "<"+"/script>");
	</script>
	<script src="myfiles/temple/assets/js/bootstrap.js"></script>

	<!-- page specific plugin scripts -->

	<!-- ace scripts -->
	<script src="myfiles/temple/assets/js/ace/elements.scroller.js"></script>
	<script src="myfiles/temple/assets/js/ace/elements.colorpicker.js"></script>
	<script src="myfiles/temple/assets/js/ace/elements.fileinput.js"></script>
	<script src="myfiles/temple/assets/js/ace/elements.typeahead.js"></script>
	<script src="myfiles/temple/assets/js/ace/elements.wysiwyg.js"></script>
	<script src="myfiles/temple/assets/js/ace/elements.spinner.js"></script>
	<script src="myfiles/temple/assets/js/ace/elements.treeview.js"></script>
	<script src="myfiles/temple/assets/js/ace/elements.wizard.js"></script>
	<script src="myfiles/temple/assets/js/ace/elements.aside.js"></script>
	<script src="myfiles/temple/assets/js/ace/ace.js"></script>
	<script src="myfiles/temple/assets/js/ace/ace.ajax-content.js"></script>
	<script src="myfiles/temple/assets/js/ace/ace.touch-drag.js"></script>
	<script src="myfiles/temple/assets/js/ace/ace.sidebar.js"></script>
	<script src="myfiles/temple/assets/js/ace/ace.sidebar-scroll-1.js"></script>
	<script src="myfiles/temple/assets/js/ace/ace.submenu-hover.js"></script>
	<script src="myfiles/temple/assets/js/ace/ace.widget-box.js"></script>
	<script src="myfiles/temple/assets/js/ace/ace.settings.js"></script>
	<script src="myfiles/temple/assets/js/ace/ace.settings-rtl.js"></script>
	<script src="myfiles/temple/assets/js/ace/ace.settings-skin.js"></script>
	<script src="myfiles/temple/assets/js/ace/ace.widget-on-reload.js"></script>
	<script
		src="myfiles/temple/assets/js/ace/ace.searchbox-autocomplete.js"></script>

	<!-- inline scripts related to this page -->

	<!-- the following scripts are used in demo only for onpage help and you don't need them -->
	<link rel="stylesheet"
		href="myfiles/temple/assets/css/ace.onpage-help.css" />
	<link rel="stylesheet"
		href="myfiles/temple/docs/assets/js/themes/sunburst.css" />

	<script type="text/javascript">
		ace.vars['base'] = 'myfiles/temple/';
	</script>
	<script src="myfiles/temple/assets/js/ace/elements.onpage-help.js"></script>
	<script src="myfiles/temple/assets/js/ace/ace.onpage-help.js"></script>
	<script src="myfiles/temple/docs/assets/js/rainbow.js"></script>
	<script src="myfiles/temple/docs/assets/js/language/generic.js"></script>
	<script src="myfiles/temple/docs/assets/js/language/html.js"></script>
	<script src="myfiles/temple/docs/assets/js/language/css.js"></script>
	<script src="myfiles/temple/docs/assets/js/language/javascript.js"></script>

</body>
</html>