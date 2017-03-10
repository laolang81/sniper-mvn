<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/subjectDate" prefix="sd"%>
<div class="seach">
	<form:form action="" role="form" cssClass="form-inline" method="get"
		modelAttribute="search">
		<c:if test="${imageType != null }">
			<div class="form-group">
				<label for="imageType" class="sr-only">新闻显示</label>
				<form:select path="imageType" cssClass="form-control" id="imageType">
					<form:options items="${imageType }" />
				</form:select>
			</div>
		</c:if>
		<div class="form-group">
			<label for="startDate" class="sr-only">开始时间</label>
			<form:input path="startDate" cssClass="form-control Wdate"
				id="startDate" />
			- <label for="endDate" class="sr-only">结束时间</label>
			<form:input path="endDate" cssClass="form-control Wdate" id="endDate" />
		</div>
		<c:if test="${hotMap != null }">
			<div class="form-group">
				<label for="bhot" class="sr-only">热点</label>
				<form:select path="bhot" cssClass="form-control" id="bhot">
					<form:options items="${hotMap }" />
				</form:select>
			</div>
		</c:if>
		<c:choose>
			<c:when test="${indexMap != null }">
				<div class="form-group">
					<label for="group" class="sr-only">聚合</label>
					<form:select path="group" cssClass="form-control" id="group">
						<form:option value="">全部</form:option>
						<form:options items="${indexMap }" />
					</form:select>
				</div>
			</c:when>
			<c:otherwise>
				<div class="form-group">
					<label for="siteidSelect" class="sr-only">处室</label>
					<form:select path="siteid" cssClass="form-control"
						id="siteidSelect">
						<form:option value="">选择</form:option>
						<form:options items="${departments }" />
					</form:select>
				</div>
			</c:otherwise>
		</c:choose>
		<div class="form-group">
			<label for="limit" class="sr-only">记录</label>
			<form:select path="limit" cssClass="form-control" id="limit">
				<form:options items="${search.limits }" />
			</form:select>
		</div>
		<div class="form-group">
			<label for="" class="sr-only">关键词</label>
			<form:input path="name" cssClass="form-control" />
		</div>
		<div class="form-group">
			<button class="btn btn-success" type="submit" name="submit"
				value="search">查询</button>
		</div>
		<c:if test="${indexMap!= null }">
			<div class="form-group">
				<button class="btn btn-success" type="submit" name="submit"
					value="export">导出</button>
			</div>
		</c:if>
	</form:form>
</div>
<c:import url="../public/sniper_menu.jsp" />

<style>
.item {
	padding: 50px;
	width: 100%;
	height: 100%;
	display: none;
}
</style>

<table class="table table-hover">
	<thead>
		<tr>
			<th>编辑</th>
			<th>标题/查看</th>
			<th>处室</th>
			<th>栏目</th>
			<th>发布者/发布时间</th>
			<th>访问量</th>
			<th>审核/时间</th>
			<th>状态</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="l" items="${lists }">
			<tr id="sl_${l.id}">
				<td><input type="checkbox" name="list.id" value="${l.id}" /><a
					href=".${baseHref.adminPath }/admin-post/update?sid=${l.id}"
					target="_blank">${l.id}</a></td>
				<td><a
					href="http://sdcom.gov.cn/public/html/news/201609/${l.id }.html"
					target="_blank" title="${l.subject}">${l.subject}</a> <c:if
						test="${l.bhot == 1 }">
						<i class="fa fa-home ml2" title="首页要闻">
					</c:if> <c:if test="${l.preid == 4 }">
						<i class="fa fa-star ml2" title="置顶新闻">
					</c:if> <c:if test="${l.moftec == 1 }">
						<i class="fa fa-share ml2" title="推送到商务部">
					</c:if> <c:if test="${l.suggested >= 1 }">
						<i class="fa fa-thumbs-up ml2" title="推荐首页要闻">
					</c:if> <c:if
						test="${l.attachments != null && fn:length(l.attachments) > 0 }">
						<i class="fa fa-picture-o ml2"
							title="拥有附件(${fn:length(l.attachments)})">
					</c:if></td>
				<c:set var="siteid">${l.siteid }</c:set>
				<td data-type="siteid">${departments[siteid]}</td>
				<c:set var="itemid">${l.itemid }</c:set>
				<td data-type="itemid">${postitems[siteid][itemid] }</td>
				<td>${l.authorid }<br> <fmt:formatDate value="${l.date }"
						pattern="yyyy-MM-dd" />
				</td>
				<td>${l.todayView }/${l.view }</td>
				<td>${l.auditUid }<br> <fmt:formatDate
						value="${l.auditDatetime }" pattern="yyyy-MM-dd" />
				</td>
				<c:set var="lookthroughed">${l.lookthroughed }</c:set>
				<td data-type="lookthroughed">${lookthrouth[lookthroughed] }</td>
			</tr>
		</c:forEach>
	</tbody>
</table>
<div class="meneame">${pageHtml }</div>
<script type="text/javascript" src="myfiles/js/jquery.items.select.js"></script>
<script type="text/javascript">
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

<div id="moveItem" class="item">
	<form id="moveItemForm" class="form-horizontal mcForm" method="post"
		action=".${baseHref.adminPath }/admin-post/itemid" role="form">
		<input type="hidden" value="" name="delid"> <input
			type="hidden" value="move" name="menuType"> <input
			type="hidden" value="" name="menuValue">
		<div class="form-group">
			<label for="item_siteid_select" class="col-sm-2 control-label">栏目处室</label>
			<div class="col-sm-10">
				<select id="item_siteid_select" class="" name="siteid">
					<c:forEach items="${departments }" var="d">
						<option value="${d.key }">${d.value }</option>
					</c:forEach>
				</select>
			</div>
		</div>

		<div class="form-group text-center">
			<button class="btn btn-success" type="submit" name="submit"
				value="search">确定</button>
		</div>
	</form>
</div>

<c:if test="${mofcoms != null }">
	<div id="mofcom" class="item">
		<form id="moveItemForm" class="form-horizontal mcForm" method="post"
			action=".${baseHref.adminPath }/admin-post/mofcom" role="form">
			<input type="hidden" value="" name="delid"> <input
				type="hidden" value="mofcom" name="menuType"> <input
				type="hidden" value="" name="menuValue">
			<div class="form-group">
				<label for="item_mofcom_select" class="col-sm-4 control-label">商务之窗栏目选择</label>
				<div class="col-sm-8">
					<select id="item_mofcom_select" class="" name="mofcom">
						<c:forEach items="${mofcoms }" var="d">
							<option value="${d.key }">${d.value }</option>
						</c:forEach>
					</select>
				</div>
			</div>

			<div class="form-group text-center">
				<button class="btn btn-success" type="submit" name="submit"
					value="search">确定</button>
			</div>
		</form>
	</div>
</c:if>
<script type="text/javascript">
	//处室栏目操作
	$().siteid({
		post : $("#item_siteid_select").val(),
		selected : 0,
		changeselect : 'item_siteid_select',
		selectselect : 'item_item_select',
		name : 'itemid',
		gettrue : 1

	});
</script>
<!-- ajax表单提交 -->
<script type="text/javascript"
	src="${baseHref.baseHref }myfiles/Plugin/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="myfiles/js/jquery.from.js"></script>
<script type="text/javascript">
	$(document).delegate('.Wdate', 'click', function() {
		WdatePicker({
			dateFmt : 'yyyy-MM-dd'
		})
	});

	$().ready(function() {
		var options = {
			beforeSubmit : showRequest,
			success : showResponse,
			type : 'post',
			dataType : 'json',
			clearForm : false,
			timeout : 50000
		};

		$('.mcForm').submit(function() {
			$(this).ajaxSubmit(options);
			return false;
		});

	});

	// pre-submit callback
	function showRequest(formData, jqForm, options) {

		if (confirm('确定')) {
			layer.msg("信息开始提交");
			return true;
		}

		return false;
	}
	// post-submit callback
	function showResponse(responseText, statusText, xhr, $form) {
		if (statusText == 'success') {
			if (responseText.code == 200) {
				if (responseText.html != "") {
					for ( var m in responseText.html) {
						if (responseText.html[m] != null) {
							$("#sl_" + m).replaceWith(responseText.html[m]);
						}
					}
				}
				layer.msg("成功");
				layer.closeAll(); //关闭所有的层
			} else {
				layer.msg("失败");
			}
		} else {
			layer.msg("未知错误");
		}
	}
</script>

<div class="alert alert-success alert-dismissible" role="alert">
	<button type="button" class="close" data-dismiss="alert">
		<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
	</button>
	<ul>
		<li><i class="fa fa-home ml2" title="首页要闻"></i> 首页要闻</li>
		<li><i class="fa fa-star ml2" title="置顶新闻"></i> 置顶新闻</li>
		<li><i class="fa fa-share ml2" title="推送商务部新闻"></i> 推送商务部新闻</li>
		<li><i class="fa fa-thumbs-up ml2" title="推荐首页新闻"></i> 推荐首页新闻</li>
		<li><i class="fa fa-picture-o ml2" title="拥有附件标志"></i> 拥有附件标志</li>

	</ul>
</div>
