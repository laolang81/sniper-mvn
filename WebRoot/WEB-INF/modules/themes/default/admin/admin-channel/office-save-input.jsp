<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<c:if test="${errors!= null}">
	<div class="alert alert-success alert-dismissible" role="alert">
		<button type="button" class="close" data-dismiss="alert">
			<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
		</button>
		<ul>
			<c:forEach items="${errors }" var="m">
				<li>${m}</li>
			</c:forEach>
		</ul>
	</div>
</c:if>
<form:form method="POST" cssClass="form-horizontal" id="sniperForm"
	role="form" modelAttribute="office">

	<div class="form-group">
		<label for="keyName" class="col-sm-2 control-label text-danger">名称</label>
		<div class="col-sm-8">
			<form:input path="name" cssClass="form-control" placeholder="处室全称" />
		</div>
	</div>

	<div class="form-group">
		<label for="deShortName" class="col-sm-2 control-label text-danger">别称</label>
		<div class="col-sm-8">
			<form:input path="deShortName" cssClass="form-control"
				placeholder="别称，处室简称" />
		</div>
	</div>

	<div class="form-group">
		<label for="type" class="col-sm-2 control-label text-danger">类型</label>
		<div class="col-sm-4">
			<form:select path="type" items="${types }" cssClass="form-control" />
		</div>
	</div>

	<div class="form-group">
		<label for="banner" class="col-sm-2 control-label">图片</label>
		<div class="col-sm-8">
			<form:hidden path="banner" />
			<input type="file" class="fileupload" name="imgFile"
				data-url="upload">
			<div class="progress" style="margin-top:10px;">
				<div class="progress-bar" role="progressbar" aria-valuenow="0"
					aria-valuemin="0" aria-valuemax="100" style="width: 0%;">0%</div>
			</div>
			<div class="help-block">
				<c:if test="${office.banner != null && office.banner != '' }">
					<img src="${systemConfig.imagePrefix }${office.banner}"
						class="img-thumbnail">
				</c:if>
			</div>
		</div>
	</div>

	<div class="form-group">
		<label for="exUrl" class="col-sm-2 control-label">转向地址</label>
		<div class="col-sm-8">
			<form:input path="exUrl" cssClass="form-control" placeholder="点击处室名称去的地方"/>
		</div>
	</div>

	<div class="form-group">
		<label for="deOrder" class="col-sm-2 control-label">排序</label>
		<div class="col-sm-2">
			<form:input path="deOrder" cssClass="form-control" />
		</div>
	</div>

	<div class="form-group">
		<label for="bshowimgnews" class="col-sm-2 control-label">是否显示图片新闻</label>
		<div class="col-sm-2">
			<form:select items="${yes_no }" path="bshowimgnews"
				cssClass="form-control" />
		</div>
	</div>

	<div class="form-group">
		<label for="deTrue" class="col-sm-2 control-label">启用</label>
		<div class="col-sm-2">
			<form:select items="${yes_no }" path="deTrue" cssClass="form-control" />
		</div>
	</div>

	<div class="form-group">
		<label for="deHome" class="col-sm-2 control-label">首页显示</label>
		<div class="col-sm-2">
			<form:select items="${yes_no }" path="deHome" cssClass="form-control" />
		</div>
	</div>

	<div class="form-group">
		<div class="col-sm-10 col-md-offset-2">
			<button type="submit" class="btn btn-danger">保存</button>
		</div>
	</div>
</form:form>

<script
	src="myfiles/Plugin/jQuery-File-Upload-9.12.1/js/vendor/jquery.ui.widget.js"></script>
<script
	src="myfiles/Plugin/jQuery-File-Upload-9.12.1/js/jquery.iframe-transport.js"></script>
<script
	src="myfiles/Plugin/jQuery-File-Upload-9.12.1/js/jquery.fileupload.js"></script>
<script type="text/javascript"
	src="myfiles/js/jquery.file.path.upload.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		filesBox.band();
	})
</script>
