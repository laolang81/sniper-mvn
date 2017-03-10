<%@ page language="java" import="java.util.*" pageEncoding="US-ASCII"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>My JSP 'MyJsp.jsp' starting page</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

</head>

<body>
	<p>
	<table style="width:100%;" cellpadding="2" cellspacing="0" border="1"
		bordercolor="#000000">
		<tbody>
			<tr>
				<td colspan="2"><br /></td>
			</tr>
			<tr>
				<td><br /></td>
				<td><br /></td>
			</tr>
			<tr>
				<td><br /></td>
				<td><br /></td>
			</tr>
		</tbody>
	</table>

</body>
</html>
