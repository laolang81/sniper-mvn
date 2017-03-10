<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<div class="seach">
	<form:form action="" role="form" cssClass="form-inline" method="get"
		modelAttribute="channelSearch">
		<div class="form-group">
			<label for="" class="sr-only">状态</label>
			<form:select path="status" cssClass="form-control">
				<form:option value="">选择</form:option>
				<form:options items="${sniperMenu.params.status }" />
			</form:select>
		</div>

		<div class="form-group">
			<label for="" class="sr-only">类型</label>
			<form:select path="type" cssClass="form-control">
				<form:option value="">选择</form:option>
				<form:options items="${sniperMenu.params.type }" />
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
			<th>修改</th>
			<th>名称</th>
			<th>排序</th>
			<th>首页显示</th>
			<th>类型</th>
			<th>启用</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="l" items="${lists }">
			<tr id="sl_${l.id}">
				<td><input type="checkbox" name="list.id" value="${l.id}" /><a
					href=".${baseHref.adminPath}/admin-channel/officeupdate?id=${l.id}"
					target="_blank">${l.id }</a></td>
				<td data-type="name"><a
					href="http://sdcom.gov.cn/Office-${l.id }-0.html" target="_blank">${l.name}</a></td>
				<td data-type="status">${l.deOrder}</td>
				<td data-type="showHome">${l.deHome}</td>
				<!-- 主要把整数变成字符串 -->
				<c:set var="showType">${l.type }</c:set>
				<td data-type="type">${sniperMenu.getMapValueString("type", showType)}</td>
				<c:set var="dt">${l.deTrue }</c:set>
				<td data-type="sort">${sniperMenu.params.status[dt]}</td>
			</tr>
		</c:forEach>
	</tbody>
</table>
<div class="meneame">${pageHtml }</div>