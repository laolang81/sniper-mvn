<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<link
	href="${baseHref.baseHref }myfiles/Plugin/zTree_v3/css/zTreeStyle/zTreeStyle.css"
	media="screen" rel="stylesheet" type="text/css">
<script type="text/javascript"
	src="${baseHref.baseHref }myfiles/Plugin/zTree_v3/js/jquery.ztree.core-3.5.min.js"></script>
<script type="text/javascript"
	src="${baseHref.baseHref }myfiles/Plugin/zTree_v3/js/jquery.ztree.exhide-3.5.js"></script>
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

<form:form method="POST" cssClass="form-horizontal" id="sniperForm"
	role="form" modelAttribute="adminRight">
	<div class="form-group">
		<label for="name" class="col-sm-2 control-label">名称</label>
		<div class="col-sm-6">
			<form:input path="name" cssClass="form-control" placeholder="name" />
			<div class="help-block">
				<form:errors path="name" />
			</div>
		</div>
	</div>
	<div class="form-group">
		<label for="permission" class="col-sm-2 control-label">权限码</label>
		<div class="col-sm-10">
			<form:input path="permission" cssClass="form-control"
				placeholder="permission" />
		</div>
	</div>
	<div class="form-group">
		<label for="thePublic" class="col-sm-2 control-label">公共URL</label>
		<div class="col-sm-10">
			<form:checkbox path="thePublic" />
		</div>
	</div>

	<div class="form-group">
		<label for="theMenu" class="col-sm-2 control-label">左侧菜单</label>
		<div class="col-sm-10">
			<form:checkbox path="theMenu" />
		</div>
	</div>
	<div class="form-group">
		<label for="theShow" class="col-sm-2 control-label">左侧菜单显示</label>
		<div class="col-sm-10">
			<form:checkbox path="theShow" />
		</div>
	</div>
	<div class="form-group">
		<label for="url" class="col-sm-2 control-label">Url</label>
		<div class="col-sm-8">
			<form:input path="url" cssClass="form-control" placeholder="name" />
			<div class="help-block">
				<form:errors path="url" />
			</div>
		</div>
	</div>

	<div class="form-group" style="position: relative;">
		<label for="fid" class="col-sm-2 control-label">父级</label>
		<div class="col-sm-6">
			<input type="text" value="" placeholder="fidName"
				class="form-control" name="fidName" id="fidName">
			<form:hidden path="fid" />
		</div>
		<div id="menuContent" class="menuContent well well-sm"
			style="display:none;">
			<ul id="rightMap" class="ztree"></ul>
		</div>
	</div>

	<div class="form-group">
		<label for="target" class="col-sm-2 control-label">Target</label>
		<div class="col-sm-2">
			<form:select path="target" items="${targets }"
				cssClass="form-control" />
		</div>
	</div>

	<div class="form-group">
		<label for="type" class="col-sm-2 control-label">类型</label>
		<div class="col-sm-2">
			<form:select path="type" items="${righttype }"
				cssClass="form-control" />
		</div>
	</div>

	<div class="form-group">
		<label for="sort" class="col-sm-2 control-label">Sort</label>
		<div class="col-sm-2">
			<form:input path="sort" cssClass="form-control" />
		</div>
	</div>
	<div class="form-group">
		<label for="sort" class="col-sm-2 control-label">ICON</label>
		<div class="col-sm-2">
			<form:input path="icon" cssClass="form-control" />
			<div class="help-block">只需要填写Icon的值</div>
		</div>
	</div>

	<div class="form-group">
		<label for="note" class="col-sm-2 control-label">描述</label>
		<div class="col-sm-10">
			<form:textarea path="note" rows="5" cssClass="form-control" />
		</div>
	</div>
	<div class="form-group">
		<div class="col-sm-10 col-md-offset-2">
			<button type="submit" class="btn btn-danger">Save</button>
		</div>
	</div>
</form:form>
<div class="alert alert-success alert-dismissible" role="alert">
	<button type="button" class="close" data-dismiss="alert">
		<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
	</button>
	<ul>
		<li>资源修改完毕之后请清理缓存,<a
			href=".${baseHref.adminPath }/admin-config/cache">清理缓存</a></li>
	</ul>
</div>
<script type="text/javascript"
	src="myfiles/Plugin/kindeditor/kindeditor-min.js"></script>
<script type="text/javascript"
	src="myfiles/Plugin/kindeditor/lang/zh_CN.js"></script>
<script type="text/javascript" src="myfiles/js/jquery.zTree.js"></script>
<SCRIPT type="text/javascript">
	var depMenu = new Array(${treeRightMap});
	$(document).ready(function() {
		$().zTreeMenu({
			zNodesRight : depMenu,
			clickID : '#fidName',
			valueType : 'all',
			valueId : '${adminRight.fid}'

		});
	});

	$(function() {
		var editor = KindEditor
			.create(
					'textarea[name="note"]',
					{
						uploadJson : 'admin/file-upload/upload?${_csrf.parameterName}=${_csrf.token}',
						fileManagerJson : 'admin/file-upload/htmlmanager',
						allowFileManager : true,
						filterMode : false,
						domain : 'domain',
						afterBlur : function() {
							this.sync();
						}
					});
	});
</script>