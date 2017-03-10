<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<div class="seach">
	<form id="searchFrom" class="form-inline" role="form" name="searchFrom"
		method="get" action="">
		<div class="form-group">
			<label for="" class="sr-only">name</label>
		</div>
		<div class="form-group">
			<button class="btn btn-success" type="submit" name="submit"
				value="search">查询</button>
		</div>
	</form>
</div>

<c:import url="../public/sniper_menu.jsp" />

<table class="table table-hover">
	<thead>
		<tr>

			<th>App ID</th>
			<th>App Secret</th>
			<th>名称</th>
			<th>启用</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="l" items="${lists }">
			<tr id="sl_${l.id}">
				<td><input type="checkbox" name="list.id" value="${l.id}" />${l.id}</td>
				<td><a href="admin/admin-oauth/update?id=${l.id}"
					target="_blank">${l.clientSecret}</a></td>
				<td>${l.clientName}</td>
				<td>${l.enabled}</td>
			</tr>
		</c:forEach>

	</tbody>
</table>
<div class="meneame">${pageHtml }</div>

