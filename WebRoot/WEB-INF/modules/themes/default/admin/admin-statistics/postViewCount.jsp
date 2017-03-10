<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<div class="seach">
	<form:form action="" role="form" cssClass="form-inline" method="post"
		modelAttribute="search">
		<div class="form-group">
			<label for="startDate" class="sr-only">开始时间</label>
			<form:input path="startDate" cssClass="form-control Wdate"
				data-toggle="tooltip" data-placement="left" title="新闻搜索时间开始"
				id="startDate" />
			- <label for="endDate" class="sr-only">结束时间</label>
			<form:input path="endDate" cssClass="form-control Wdate" id="endDate"
				title="新闻搜索结束时间" />
		</div>

		<div class="form-group">
			<label for="siteidSelect" class="sr-only">处室</label>
			<form:select path="siteid" cssClass="form-control" id="siteidSelect"
				title="处室">
				<form:option value="">选择</form:option>
				<form:options items="${deps }" />
			</form:select>
		</div>
		<div class="form-group">
			<label for="limit" class="sr-only">记录</label>
			<form:select path="limit" cssClass="form-control" id="limit">
				<form:options items="${search.limits }" />
			</form:select>
		</div>

		<div class="form-group">
			<button class="btn btn-success" type="submit" name="submit"
				data-toggle="tooltip" data-placement="left" title="搜索"
				value="search">查询</button>
		</div>
	</form:form>
</div>

<script type="text/javascript"
	src="${baseHref.baseHref }myfiles/Plugin/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="myfiles/js/jquery.items.select.js"></script>
<script type="text/javascript">
	$(document).delegate('.Wdate', 'click', function() {
		WdatePicker({
			dateFmt : 'yyyy-MM-dd'
		})
	});
	//处室栏目操作
	var deprtid = '${search.siteid}';
	var itemup = '${search.itemid}';
	$().siteid({
		post : deprtid,
		selected : itemup,
		name : 'itemid',
		gettrue : 1,
		c : 1
	});
</script>
<table class="table table-hover">
	<thead>
		<tr>
			<th>处室 > 栏目</th>
			<th>标题</th>
			<th>发布日期</th>
			<th>今天/总访问量</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="s" items="${lists }">
			<tr>
				<c:set var="siteid">${s.siteid }</c:set>
				<c:set var="itemid">${s.itemid }</c:set>
				<td><strong>${deps[siteid] } > ${items[siteid][itemid] }</strong></td>
				<td><a
					href="http://sdcom.gov.cn/public/html/news/201609/${s.id }.html"
					target="_blank" title="${s.subject}">${s.subject}</a></td>
				<td><fmt:formatDate value="${s.date }" pattern="yyyy-MM-dd" /></td>
				<td>${s.todayView}/${s.view }</td>
			</tr>
		</c:forEach>
	</tbody>
</table>
<div class="meneame">${pageHtml }</div>
