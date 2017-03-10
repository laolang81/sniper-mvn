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
				<form:options items="${sniperMenu.getMapValue('status') }" />
			</form:select>
		</div>

		<div class="form-group">
			<label for="" class="sr-only">处室</label>
			<form:select path="depid" cssClass="form-control" id="siteidSelect">
				<form:option value="0">选择</form:option>
				<form:options items="${departments }" />
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
			<th>编辑</th>
			<th>名称</th>
			<th>处室</th>
			<th>栏目</th>
			<th>排序</th>
			<th>状态</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="l" items="${lists }">
			<tr id="sl_${l.itemid}">
				<td><input type="checkbox" name="list.id" value="${l.itemid}" /><a
					target="_blank"
					href=".${baseHref.adminPath}/admin-channel/infoclassupdate?id=${l.id}">${l.id }</a></td>
				<td data-type="name"><a target="_blank"
					href=".${baseHref.adminPath}/admin-channel/infoclassupdate?id=${l.id}">${l.name}</a></td>
				<c:set var="itemid">${l.itemid }</c:set>
				<c:set var="deprtid">${parents[itemid].deprtid}</c:set>
				<td data-type="showHome">${departments[deprtid] }</td>
				<td>${parents[itemid].name}</td>
				<!-- 主要把整数变成字符串 -->
				<td data-type="order">${l.sort}</td>
				<td data-type="enabled">${l.enabled}</td>
			</tr>
		</c:forEach>
	</tbody>
</table>
<div class="meneame">${pageHtml }</div>
<script type="text/javascript" src="myfiles/js/jquery.items.select.js"></script>
<script type="text/javascript">
	//处室栏目操作
	var deprtid = '${search.depid}';
	var itemup = '${search.itemUp}';
	$().siteid({
		post : deprtid,
		selected : itemup,
		name : 'itemUp',
		gettrue : 1

	});
</script>