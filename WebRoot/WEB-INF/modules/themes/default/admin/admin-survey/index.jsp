<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<div class="seach">
	<form:form action="" role="form" cssClass="form-inline" method="get"
		modelAttribute="channelSearch">

		<div class="form-group">
			<label for="" class="sr-only">名称</label>
			<form:input title="名称" path="name" cssClass="form-control" />
		</div>

		<div class="form-group">
			<button class="btn btn-success" type="submit" name="submit"
				value="search">查询</button>
		</div>
	</form:form>
</div>
<c:import url="../public/sniper_menu.jsp" />
<table class="table table-hover">
	<thead>
		<tr>
			<th>选择</th>
			<th>名称</th>
			<th>锁定</th>
			<th>参与数</th>
			<th>问题</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="l" items="${lists }">
			<tr id="sl_${l.id}">
				<td><input type="checkbox" name="list.id" value="${l.id}" /></td>
				<td data-type="title"><a
					href=".${baseHref.adminPath }/admin-survey/update?id=${l.id}"
					target="_blank">${l.title}</a></td>
				<td data-type="locked">${l.locked}</td>
				<td data-type="peopleNum">${l.peopleNum}</td>
				<td data-type="status"><a
					href=".${baseHref.adminPath }/admin-survey/update?id=${l.id}"
					target="_blank">编辑问卷</a> <a href="survey/?id=${l.id}"
					target="_blank">预览问卷</a> <a
					href=".${baseHref.adminPath }/admin-survey/survey?id=${l.id}"
					target="_blank">编辑题库</a> <a
					href=".${baseHref.adminPath }/admin-survey/analysis?id=${l.id}"
					target="_blank">分析题库</a> <a
					href=".${baseHref.adminPath }/admin-survey/analysisuser?id=${l.id}"
					target="_blank">分析用户</a> <a
					href=".${baseHref.adminPath }/admin-survey/download?id=${l.id}"
					target="_blank">下载答题</a></td>
			</tr>
		</c:forEach>
	</tbody>
</table>
<div class="meneame">${pageHtml }</div>