<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta charset="utf-8">
<title><sitemesh:write property="title" />${baseHref.pageTitle}
	- | 后台管理</title>
<sitemesh:write property='head' />
<base href="${baseHref.baseHref }">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="_csrf" content="${_csrf.token}" />
<meta name="_csrf_header" content="${_csrf.headerName}" />
<link href="${baseHref.baseHref }myfiles/Plugin/bootstrap/css/bootstrap.min.css"
	media="screen" rel="stylesheet" type="text/css">
<link href="${baseHref.baseHref }myfiles/Plugin/bootstrap/css/bootstrap-theme.min.css"
	media="screen" rel="stylesheet" type="text/css">
<link href="${baseHref.baseHref }myfiles/Plugin/font-awesome-4.3.0/css/font-awesome.min.css"
	media="screen" rel="stylesheet" type="text/css">
<link href="${baseHref.baseHref }myfiles/Plugin/zTree_v3/css/zTreeStyle/zTreeStyle.css"
	media="screen" rel="stylesheet" type="text/css">
<link href="${baseHref.baseHref }myfiles/css/admin.css" media="screen" rel="stylesheet"
	type="text/css">
<link href="${baseHref.baseHref }myfiles/Plugin/artDialog/css/ui-dialog.css" media="screen"
	rel="stylesheet" type="text/css">

<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
  <script src="${baseHref.baseHref }myfiles/Plugin/bootstrap/js/html5shiv.min.js"></script>
  <script src="${baseHref.baseHref }myfiles/Plugin/bootstrap/js/respond.min.js"></script>
<![endif]-->

<script type="text/javascript" src="${baseHref.baseHref }myfiles/js/jquery-1.11.1.min.js"></script>
<script type="text/javascript" src="${baseHref.baseHref }myfiles/js/js.config.js"></script>
<script type="text/javascript"
	src="${baseHref.baseHref }myfiles/Plugin/zTree_v3/js/jquery.ztree.core-3.5.min.js"></script>
<script type="text/javascript"
	src="${baseHref.baseHref }myfiles/Plugin/zTree_v3/js/jquery.ztree.exhide-3.5.js"></script>
<script type="text/javascript"
	src="${baseHref.baseHref }myfiles/Plugin/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript"
	src="${baseHref.baseHref }myfiles/Plugin/artDialog/dist/dialog-min.js"></script>
<script type="text/javascript" src="myfiles/js/jquery.main.js"></script>

<style type="text/css">
.navbar {
	margin-bottom: 20px;
}

#bs-example-navbar-collapse-1 {
	/* background: url("myfiles/images/images/top.jpg") repeat scroll 0 0 rgba(0, 0, 0, 0); */
	
}

.navbar-default .navbar-brand {
	/* color: white */
	
}

.navbar-default .navbar-nav>li>a {
	/* color: white */
	
}

body {
	background: none repeat scroll 0 0 #f3f3f3;
}

.container-fluid {
	margin: 0px;
}
</style>
</head>

<body>

	<nav class="navbar navbar-default">
		<div class="container-fluid">
			<!-- Brand and toggle get grouped for better mobile display -->
			<div class="navbar-header">
				<button type="button" class="navbar-toggle collapsed"
					data-toggle="collapse" data-target="#bs-example-navbar-collapse-1"
					aria-expanded="false">
					<span class="sr-only">${systemConfig.webAdminName}</span> <span
						class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="${baseHref.baseHref }" target="_blank">${systemConfig.webName}</a>
			</div>

			<!-- Collect the nav links, forms, and other content for toggling -->
			<div class="collapse navbar-collapse"
				id="bs-example-navbar-collapse-1">
				<ul class="nav navbar-nav">
					<!-- <li class="active"><a href="#">Link <span class="sr-only">(current)</span></a></li>
					<li><a href="#">Link</a></li> -->
					<li class="dropdown"><a href="#" class="dropdown-toggle"
						data-toggle="dropdown" role="button" aria-haspopup="true"
						aria-expanded="false">InBox <span class="badge" id="inboxNum">0</span><span
							class="caret"></span></a>
						<ul class="dropdown-menu" id="inbox">
							

						</ul></li>
				</ul>

				<ul class="nav navbar-nav navbar-right">
					<li class="dropdown"><a href="#" class="dropdown-toggle"
						data-toggle="dropdown" role="button" aria-haspopup="true"
						aria-expanded="false"><sec:authentication property="name" />
							<span class="caret"></span></a>
						<ul class="dropdown-menu">
							<li><a href="admin/admin-user/changepassword">更改密码</a></li>
							<li class="divider"></li>
							<li><form action="logout" method="post">
									<button type="submit" class="btn btn-link ml10">退出</button>
									<input type="hidden" name="${_csrf.parameterName}"
										value="${_csrf.token}" />
								</form></li>
						</ul></li>
				</ul>
			</div>
			<!-- /.navbar-collapse -->
		</div>
		<!-- /.container-fluid -->
	</nav>
	<div class="container bs-docs-container">
		<!-- s -->
		<!-- 从被装饰页面获取body标签内容 -->
		<div class="row">
			<div class="col-md-2 hidden-print" role="complementary">
				<nav
					class="bs-docs-sidebar hidden-print hidden-xs hidden-sm affix-top">
					<div data-spy="affix" data-offset-top="60" data-offset-bottom="50">
						<ul id="ztree" class="ztree ztreeMenu nav bs-docs-sidenav mt10"></ul>
					</div>
				</nav>
			</div>
			<div class="col-md-10" style=" margin-top: 20px;" role="main">
				<!-- e -->
				<c:if
					test="${baseHref.adminRight.note != null && baseHref.adminRight.note != '' }">
					<div class="panel panel-default">
						<div class="panel-heading">
							<a data-toggle="collapse" data-parent="#pageParentHelp"
								href="#pageHelp" aria-expanded="true"
								aria-controls="collapseOne"> 帮助 </a>
						</div>
						<div id="pageHelp" class="panel-collapse collapse "
							role="tabpanel">
							<div class="panel-body">${baseHref.adminRight.note }</div>
						</div>
					</div>
				</c:if>

				<!--[if lt IE 9]>
				<div class="alert alert-warning alert-dismissible" role="alert" id="sniper_warning" >
				  <button type="button" class="close" data-dismiss="alert"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
				  <strong>警告!</strong> 你的浏览器版本过低,请升级到<a href="http://windows.microsoft.com/zh-cn/internet-explorer/download-ie" target="_blank">IE9及以上</a>,或者使用<a href="http://rj.baidu.com/soft/detail/14744.html" target="_blank">谷歌</a>,<a href="http://www.firefox.com.cn/download/" target="_blank">火狐等浏览器</a>.
				</div>
				<![endif]-->

				<sitemesh:write property='body' />
				<!-- s -->
			</div>
		</div>
		<!-- e -->

	</div>

	<div id="footer" class="bs-footer" role="contentinfo">
		<div class="container">
			<p class="text-right">Thanks for using(${endTime})</p>
			<p id="footer-upgrade" class="text-right">Version 1.0</p>
		</div>
	</div>
	
	<script type="text/javascript">
	<!--
		$(document).ready(function() {
			var zNodes = [ ${baseHref.zTreeMenu} ];
			$().SniperMain({
				zNodes : zNodes,
				selectID : '${baseHref.adminRight.id}'
			});
			
			
		});
	//-->

	
</script>
</body>
</html>