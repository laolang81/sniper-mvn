<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<form:form method="post" cssClass="form-horizontal" id="sniperForm"
	role="form" modelAttribute="links" action="">

	<form:hidden path="linkid" />

	<div class="form-group">
		<label for="name" class="col-sm-2 control-label">名称</label>
		<div class="col-sm-10">
			<form:input path="name" cssClass="form-control" placeholder="name" />
			<div class="help-block">
				<form:errors path="name" />
			</div>
		</div>
	</div>

	<div class="form-group">
		<label for="url" class="col-sm-2 control-label">跳转地址</label>
		<div class="col-sm-10">
			<form:input path="url" cssClass="form-control" placeholder="" />
			<div class="help-block">
				<form:errors path="url" />
			</div>
		</div>
	</div>

	<div class="form-group">
		<label for="url" class="col-sm-2 control-label">组选择</label>
		<div class="col-sm-4">
			<form:select path="linkGroup.id" items="${groups }" cssClass="form-control" />
		</div>
	</div>

	<div class="form-group">
		<label for="attachement" class="col-sm-2 control-label">附件(图片)</label>
		<div class="col-sm-10">

			<form:hidden path="logo" />
			<input type="file" class="fileupload" name="imgFile"
				data-url="upload">
			<div class="progress" style="margin-top:10px;">
				<div class="progress-bar" role="progressbar" aria-valuenow="0"
					aria-valuemin="0" aria-valuemax="100" style="width: 0%;">0%</div>
			</div>
			<div class="help-block">
				<c:if test="${links.logo != null && links.logo != '' }">
					<img src="${systemConfig.webSite }${links.logo}" class="img-thumbnail">
				</c:if>
			</div>
		</div>
	</div>

	<div class="form-group">
		<label for="sort" class="col-sm-2 control-label">排序</label>
		<div class="col-sm-2">
			<form:input path="displayorder" cssClass="form-control" />
		</div>
	</div>

	<div class="form-group">
		<label for="viewnum" class="col-sm-2 control-label">启用</label>
		<div class="col-sm-2">
			<form:select path="viewnum" items="${yes_no }"
				cssClass="form-control" />
		</div>
	</div>

	<div class="form-group">
		<label for="note" class="col-sm-2 control-label">备注</label>
		<div class="col-sm-10">
			<form:textarea path="description" rows="6" cssClass="form-control"
				cssStyle="height:100px" />
		</div>
	</div>

	<div class="form-group">
		<div class="col-sm-10 col-md-offset-2">
			<button type="submit" class="btn btn-danger">保存</button>
		</div>
	</div>
</form:form>

<script src="myfiles/Plugin/layer/layer/layer.js"></script>
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