<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!doctype html>
<html lang="zh-cn">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="renderer" content="webkit">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title><sitemesh:write property="title" />
	${systemConfig.webName }</title>
<base href="${baseHref.webUrl }/">
<link href="favicon.ico" rel="shortcut icon"
	type="image/vnd.microsoft.icon">

<script src="myfiles/www/script/jquery-1.12.2.min.js"></script>
<link rel="stylesheet" href="myfiles/temple/assets/css/bootstrap.css" />
<link rel="stylesheet" href="myfiles/temple/assets/css/font-awesome.css" />
<script src="myfiles/Plugin/bootstrap/js/bootstrap.min.js"></script>
<sitemesh:write property='head' />
</head>
<body>
	<div class="box">
		<div class="head">
			<a href="project"><img src="myfiles/www/images/logo.png"
				width="900" height="118" alt="" /></a>
		</div>
		<div class="main">
			<sitemesh:write property='body' />
			<div class="footer">
				<ul>

					<li>版权所有:山东商务网 业务联系:0531-89013333</li>
					<li>技术支持：<a href="#">济南贸诚信息技术有限公司</a>TRADE.CN
					</li>
					<li>鲁ICP备12013505号</li>
				</ul>
			</div>
		</div>

	</div>
</body>
</html>