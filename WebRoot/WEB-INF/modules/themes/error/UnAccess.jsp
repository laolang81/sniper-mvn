<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://shiro.apache.org/tags" prefix="shiro"%>	
<%@ page trimDirectiveWhitespaces="true"%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta charset="utf-8">
<title>Access Denied</title>
</head>
<body>
<div class="container">
	<div class="alert alert-warning" role="alert">
		<p><shiro:principal />, 警告没有权限访问.</p>
		<p>更换帐号 <a href="admin/login">登录</a></p>
		<h1>Sorry, Access is denied</h1>
	</div>
</div><!-- /container -->

</body>
</html>