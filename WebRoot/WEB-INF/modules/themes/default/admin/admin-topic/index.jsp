<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<div class="seach">
	<form:form action="" role="form" cssClass="form-inline" method="get"
		modelAttribute="channelSearch">

		<div class="form-group">
			<label for="" class="sr-only">状态</label>
			<form:select path="status" cssClass="form-control">
				<form:option value="">状态</form:option>
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
			<th></th>
			<th>名称</th>
			<th>启用</th>
			<th>访问地址</th>
			<th>创建时间</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="l" items="${lists }">
			<tr id="sl_${l.id}">
				<td><input type="checkbox" name="list.id" value="${l.id}" /></td>
				<td data-type="name"><a
					href=".${baseHref.adminPath }/admin-topic/update?id=${l.id}"
					target="_blank">${l.name}</a></td>
				<c:set var="enabled" >${l.enabled }</c:set>
				<td data-type="enabled">${sniperMenu.params.enabled[enabled]}</td>
				<td>${l.url}</td>
				<td><fmt:formatDate value="${l.stime}" pattern="yyyy-MM-dd" /></td>
			</tr>
		</c:forEach>
	</tbody>
</table>
<div class="meneame">${pageHtml }</div>
