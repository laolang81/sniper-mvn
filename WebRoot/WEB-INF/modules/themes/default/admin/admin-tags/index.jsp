<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<div class="seach">
	<form:form action="" role="form" cssClass="form-inline" method="get"
		modelAttribute="channelSearch">

		<div class="form-group">
			<label for="" class="sr-only">状态</label>
			<form:select path="status" cssClass="form-control">
				<form:option value=""></form:option>
				<form:options items="${sniperMenu.params.enabled }" />
			</form:select>
		</div>

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
			<th>搜索</th>
			<th>状态</th>
			<th>别名</th>
			<th>描述</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="l" items="${lists }">
			<tr id="sl_${l.id}">
				<td><input type="checkbox" name="list.id" value="${l.id}" /></td>
				<td data-type="name"><a
					href=".${baseHref.adminPath }/admin-tags/update?id=${l.id}" target="_blank">${l.name}</a></td>
				<td data-type="name">${l.searchNum}</td>
				<td data-type="name">${l.enabled}</td>
				<td data-type="status">${l.plug}</td>
				<td data-type="status">${l.description}</td>
			</tr>
		</c:forEach>
	</tbody>
</table>
<div class="meneame">${pageHtml }</div>