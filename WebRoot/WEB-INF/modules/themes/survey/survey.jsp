<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!doctype html>
<html lang="zh-CN">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="renderer" content="webkit">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title><sitemesh:write property="title" /> |
	${systemConfig.webName }</title>
<base href="${baseHref.baseHref }">
<link href="favicon.ico" rel="shortcut icon"
	type="image/vnd.microsoft.icon">
<link
	href="${baseHref.baseHref }myfiles/Plugin/bootstrap/css/bootstrap.min.css"
	media="screen" rel="stylesheet" type="text/css">
<link
	href="${baseHref.baseHref }myfiles/Plugin/bootstrap/css/bootstrap-theme.min.css"
	media="screen" rel="stylesheet" type="text/css">
<link
	href="${baseHref.baseHref }myfiles/Plugin/font-awesome-4.3.0/css/font-awesome.min.css"
	media="screen" rel="stylesheet" type="text/css">
<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
  <script src="myfiles/Plugin/bootstrap/js/html5shiv.min.js"></script>
  <script src="myfiles/Plugin/bootstrap/js/respond.min.js"></script>
<![endif]-->
<script src="myfiles/js/jquery-1.11.1.min.js"></script>
<script type="text/javascript"
	src="myfiles/Plugin/bootstrap/js/bootstrap.min.js"></script>
<sitemesh:write property='head' />
</head>

<body>

	<div class="container bs-docs-container">
		<!--[if lt IE 9]>
		<div class="alert alert-warning alert-dismissible" role="alert" id="sniper_warning" >
		  <button type="button" class="close" data-dismiss="alert"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
		  <strong>警告!</strong> 你的浏览器版本过低,请升级到<a href="http://windows.microsoft.com/zh-cn/internet-explorer/download-ie" target="_blank">IE9及以上</a>,或者使用<a href="http://rj.baidu.com/soft/detail/14744.html" target="_blank">谷歌</a>,<a href="http://www.firefox.com.cn/download/" target="_blank">火狐等浏览器</a>.
		</div>
		<![endif]-->

		<sitemesh:write property='body' />
	</div>

</body>
</html>