<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!doctype html>
<html lang="zh-cn">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="renderer" content="webkit">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title><sitemesh:write property="title" />
	${systemConfig.webName }</title>
<base href="${baseHref.webUrl }/">
<link href="favicon.ico" rel="shortcut icon"
	type="image/vnd.microsoft.icon">
<link rel="stylesheet" type="text/css" href="myfiles/www/css/style.css">
<link rel="stylesheet" type="text/css"
	href="myfiles/www/css/font-awesome.min.css">
<script src="myfiles/www/script/jquery-1.12.2.min.js"></script>
<script src="myfiles/www/script/script.js"></script>
<sitemesh:write property='head' />
</head>
<body>
	<sitemesh:write property='body' />
</body>
</html>