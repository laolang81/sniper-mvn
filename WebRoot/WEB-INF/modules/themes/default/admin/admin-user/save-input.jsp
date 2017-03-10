<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<style type="text/css">
.siteid {
	overflow: hidden;
}

.siteid span {
	display: block;
	float: left;
	width: 45%;
}

.siteid span label {
	margin-left: 5px;
}
</style>
<c:if test="${adminUser.signCode != null }">
	<div class="alert alert-success" role="alert">
		KEY:<br>${adminUser.signCode }</div>
</c:if>

<form:form method="POST" cssClass="form-horizontal" id="sniperForm"
	role="form" modelAttribute="sniperAdminUser">

	<div class="form-group">
		<label for="name" class="col-sm-2 control-label">登录名称</label>
		<div class="col-sm-4">
			<form:input path="name" cssClass="form-control" placeholder="登录专用" />
			<div class="help-block">
				只允许使用a-zA-Z0-9字符
				<form:errors path="name" />
			</div>
		</div>
	</div>

	<div class="form-group">
		<label for="nickName" class="col-sm-2 control-label">显示昵称</label>
		<div class="col-sm-4">
			<form:input path="nickName" cssClass="form-control" placeholder="别名" />
			<div class="help-block">
				<form:errors path="nickName" />
			</div>
		</div>
	</div>

	<div class="form-group">
		<label for="email" class="col-sm-2 control-label">Email</label>
		<div class="col-sm-4">
			<form:input path="email" cssClass="form-control" />
			<div class="help-block">
				<form:errors path="email" />
			</div>
		</div>
	</div>

	<div class="form-group">
		<label for="password_c" class="col-sm-2 control-label">密码</label>
		<div class="col-sm-4">
			<input id="password_c" name="password_c" class="form-control"
				type="password" value="">
			<div class="help-block">
				<form:errors path="password" />
			</div>
		</div>
	</div>

	<div class="form-group">
		<label for="adminGroup" class="col-sm-2 control-label">用户组</label>
		<div class="col-sm-10">
			<div class="btn-group" data-toggle="buttons">
				<c:forEach var="ag" items="${adminGroups }" varStatus="varStatus">
					<label
						class="btn btn-default <c:forEach items="${sniperAdminUser.adminGroup }" var="aag">
			<c:if test="${aag.id == ag.id }">active</c:if>
			</c:forEach>">
						<input type="checkbox" name="adminGroups" autocomplete="off"
						<c:forEach items="${sniperAdminUser.adminGroup }" var="aag">
			<c:if test="${aag.id == ag.id }">checked</c:if>
			</c:forEach>
						value="${ag.id }"> ${ag.name }
					</label>
				</c:forEach>
			</div>
		</div>
	</div>

	<div class="form-group">
		<label for="enabled" class="col-sm-2 control-label">启用</label>
		<div class="col-sm-2">
			<form:checkbox path="enabled" />
		</div>
	</div>

	<div class="form-group">
		<label for="locked" class="col-sm-2 control-label">锁定</label>
		<div class="col-sm-2">
			<form:checkbox path="locked" />
		</div>
	</div>

	<div class="form-group">
		<label for="usernameExpired" class="col-sm-2 control-label">用户名过期时间</label>
		<div class="col-sm-3">
			<form:input path="usernameExpired" cssClass="form-control Wdate" />
		</div>
	</div>

	<div class="form-group">
		<label for="passwordExpired" class="col-sm-2 control-label">密码过期时间</label>
		<div class="col-sm-3">
			<form:input path="passwordExpired" cssClass="form-control Wdate" />
		</div>
	</div>

	<div class="form-group">
		<label for="enabled" class="col-sm-2 control-label">启用数据导出登录</label>
		<div class="col-sm-4">
			<input id="sign" name="sign"
				<c:if test="${adminUser.signCode != null }">checked="checked"</c:if>
				type="checkbox" value="true">
			<div class="help-block">生成的KEY会在顶部显示</div>
		</div>
	</div>

	<div class="form-group">
		<label for="siteid" class="col-sm-2 control-label">绑定的处室</label>
		<div class="col-sm-10 siteid">
			<form:checkboxes items="${deps }" path="siteids"
				cssClass="checkbox-inline" />
		</div>
	</div>

	<div class="form-group">
		<div class="col-sm-10 col-md-offset-2">
			<button type="submit" class="btn btn-danger">保存</button>
		</div>
	</div>
</form:form>

<script type="text/javascript"
	src="${baseHref.baseHref }myfiles/Plugin/My97DatePicker/WdatePicker.js"></script>
<SCRIPT type="text/javascript">
	$('.Wdate').bind('click', function() {
		WdatePicker({
			dateFmt : 'yyyy-MM-dd HH:mm:ss'
		})
	});
</SCRIPT>
