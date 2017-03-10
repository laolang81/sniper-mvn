<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<div class="seach">
	<form:form action="" role="form" cssClass="form-inline" method="get"
		modelAttribute="search">
		<div class="form-group">
			<label for="startDate" class="sr-only">开始时间</label>
			<form:input path="startDate" cssClass="form-control Wdate"
				id="startDate" />
			- <label for="endDate" class="sr-only">结束时间</label>
			<form:input path="endDate" cssClass="form-control Wdate" id="endDate" />
		</div>

		<div class="form-group">
			<label for="menuType" class="sr-only">统计方法</label>
			<form:select path="menuType" items="${showType }"
				cssClass="form-control" />
		</div>

		<div class="form-group">
			<label for="menuType" class="sr-only">读取记录</label>
			<form:select path="limit" items="${search.limits }"
				cssClass="form-control" />
		</div>

		<div class="form-group">
			<button class="btn btn-success" type="submit" name="submit"
				value="search">查询</button>
			<button class="btn btn-success" type="submit" name="submit"
				value="export">导出</button>
		</div>
	</form:form>
</div>
<script type="text/javascript"
	src="${baseHref.baseHref }myfiles/Plugin/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">
	$(document).delegate('.Wdate', 'click', function() {
		WdatePicker({
			dateFmt : 'yyyy-MM-dd'
		})
	});
</script>

<table class="table table-hover">
	<thead>
		<tr>
			<th>ID</th>
			<th>日期</th>
			<th>访问量</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="index" items="${lists }" varStatus="num">
			<tr>
				<td>${num.index + 1 }</td>
				<td>${index.d }</td>
				<td>${index.v }</td>
			</tr>
		</c:forEach>
	</tbody>
</table>
<div class="meneame">${pageHtml }</div>
<div class="alert alert-success alert-dismissible" role="alert">
	<button type="button" class="close" data-dismiss="alert">
		<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
	</button>
	<ul>
		<li>查询参数说明，按照顺序依次介绍：</li>
		<li>开始日期：查询的开始日期。</li>
		<li>结束日期：查询的结束日期。</li>
		<li>显示方式：日、月、年，查询的时候不在显示分页。</li>
		<li>显示读取记录数：控制查询导出的记录数。</li>
	</ul>
</div>
