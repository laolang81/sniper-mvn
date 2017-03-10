<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<div class="seach">
	<form:form action="" role="form" cssClass="form-inline" method="get"
		modelAttribute="search">

		<div class="form-group">
			<label for="" class="sr-only">name</label>
			<form:input path="name" cssClass="form-control" />
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
			<th>修改</th>
			<th>名称</th>
			<th>备注</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="l" items="${lists }">
			<tr id="sl_${l.id}">
				<td><input type="checkbox" name="list.id" value="${l.id}" /><a
					href=".${baseHref.adminPath }/admin-links/groupupdate?id=${l.id}" target="_blank">${l.id}</a></td>
				<td><a href=".${baseHref.adminPath }/admin-links/groupupdate?id=${l.id}"
					target="_blank">${l.name}</a></td>
				<td>${l.note }</td>
			</tr>
		</c:forEach>

	</tbody>
</table>
<div class="meneame">${pageHtml }</div>
<script type="text/javascript" src="myfiles/js/jquery.titlePreview.js"></script>
<script type="text/javascript">
	
</script>
