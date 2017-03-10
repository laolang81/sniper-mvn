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
				id="startDate" />
			- <label for="endDate" class="sr-only">结束时间</label>
			<form:input path="endDate" cssClass="form-control Wdate" id="endDate" />
		</div>


		<div class="form-group">
			<label for="group" class="sr-only">聚合栏目</label>
			<form:select path="group" cssClass="form-control" id="group">
				<form:option value="">选择</form:option>
				<form:options items="${indexNames }" />
			</form:select>
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
			<th>日期</th>
			<th>新闻数量</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="index" items="${postMonthCount }">
			<tr>
				<td>${index.ym }</td>
				<td>${index.total }</td>
			</tr>
		</c:forEach>
	</tbody>
</table>
<div class="alert alert-success alert-dismissible" role="alert">
	<button type="button" class="close" data-dismiss="alert">
		<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
	</button>
	<ul>
		<li>这里是统计栏目每个月的发布量。</li>
		<li>新建聚合栏目把你要统计的栏目聚合起来，具体参照聚合栏目添加。</li>
		<li>导出时如果没有开始日期，默认24个月。</li>
	</ul>
</div>
