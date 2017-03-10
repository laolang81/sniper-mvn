<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<style type="text/css">
.menuContent {
	background: none repeat scroll 0 0 #fff;
	position: absolute;
	left: 0;
	top: 0;
	width: auto;
	z-index: 1000;
}
</style>
<c:if test="${errors!= null}">
	<div class="alert alert-success alert-dismissible" role="alert">
		<button type="button" class="close" data-dismiss="alert">
			<span aria-hidden="true">&times;</span><span class="sr-only">关闭</span>
		</button>
		<ul>
			<c:forEach items="${errors }" var="m">
				<li>${m}</li>
			</c:forEach>
		</ul>
	</div>
</c:if>
<form:form method="POST" cssClass="form-horizontal" id="sniperForm"
	role="form" modelAttribute="topic">

	<div class="form-group">
		<label for="name" class="col-sm-2 control-label">专题标题</label>
		<div class="col-sm-8">
			<form:input path="name" cssClass="form-control" />
			<div class="help-block">
				这将是它在站点上显示的名字。<br>
				<form:errors path="name" />
			</div>
		</div>
	</div>

	<div class="form-group">
		<label for="keywords" class="col-sm-2 control-label">关键词</label>
		<div class="col-sm-8">
			<form:input path="keywords" cssClass="form-control" />
		</div>
	</div>

	<div class="form-group">
		<label for="url" class="col-sm-2 control-label">访问地址</label>
		<div class="col-sm-4">
			<form:input path="url" cssClass="form-control" />
			<div class="help-block">只填写域名后面那一部分，斜杠开头。</div>
		</div>
	</div>

	<div class="form-group">
		<label for="template" class="col-sm-2 control-label">模板名称</label>
		<div class="col-sm-4">
			<form:input path="template" cssClass="form-control" />
		</div>
	</div>

	<div class="form-group">
		<label for="enabled" class="col-sm-2 control-label">状态</label>
		<div class=" col-sm-8">
			<form:checkbox path="enabled" />
			启用
		</div>
	</div>

	<div class="form-group">
		<label for="description" class="col-sm-2 control-label">简介</label>
		<div class="col-sm-8">
			<form:textarea path="description" rows="5" cssClass="form-control" />
		</div>
	</div>

	<div class="form-group">
		<div class="col-sm-10 col-md-offset-2">
			<button type="submit" class="btn btn-danger">保存</button>
		</div>
	</div>
</form:form>
<div class="alert alert-warning" role="alert">
	<p>模板请上传到网站 WEB-INF/modules/themes/topic 目录下面，吧模板名字写在上面表单处,</p>
	<p>静态文件上传到目录topic下面</p>
</div>
