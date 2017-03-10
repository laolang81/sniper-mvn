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
				<form:options items="${deps }" />
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
			<th>左-中-右-下</th>
			<th>处室</th>
			<th>上级</th>
			<th>类型</th>
			<th>排序</th>
			<th>状态</th>
			<th>新闻数量</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="l" items="${lists }">
			<tr id="sl_${l.itemid}">
				<c:set var="deprtid">${l.deprtid }</c:set>
				<c:set var="itemup">${l.itemup }</c:set>
				<td><input type="checkbox" name="list.id" value="${l.itemid}" /><a
					target="_blank"
					href=".${baseHref.adminPath}/admin-channel/itemsupdate?id=${l.itemid}">${l.itemid }</a></td>
				<td data-type="name"><a target="_blank"
					href="http://sdcom.gov.cn/Office-${l.deprtid }-${l.itemid }.html">${l.name}</a></td>
				<td>${l.pro3 }-${l.pro1 }-${l.pro2 }-${l.pro4 }</td>
				<td data-type="dep"><a target="_blank"
					href="http://sdcom.gov.cn/Office-${l.deprtid }-0.html">${deps[deprtid]}</a></td>
				<td data-type="itemup"><a target="_blank"
					href="http://sdcom.gov.cn/Office-${l.deprtid }-${itemup }.html">${items[deprtid][itemup] }</a></td>
				<!-- 主要把整数变成字符串 -->
				<c:set var="style">${l.style }</c:set>
				<td data-type="type">${styles[style]}</td>
				<td data-type="displayorder">${l.displayorder}</td>
				<c:set var="status">${l.status }</c:set>
				<td data-type="status">${yes_no[status]}</td>
				<td data-type="subjectnum">${l.subjectnum}</td>
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