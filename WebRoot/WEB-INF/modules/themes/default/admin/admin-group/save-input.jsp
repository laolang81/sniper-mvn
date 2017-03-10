<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link
	href="${baseHref.baseHref }myfiles/Plugin/zTree_v3/css/zTreeStyle/zTreeStyle.css"
	media="screen" rel="stylesheet" type="text/css">
<script type="text/javascript"
	src="${baseHref.baseHref }myfiles/Plugin/zTree_v3/js/jquery.ztree.core-3.5.min.js"></script>
<script type="text/javascript"
	src="${baseHref.baseHref }myfiles/Plugin/zTree_v3/js/jquery.ztree.exhide-3.5.js"></script>
<script type="text/javascript"
	src="myfiles/Plugin/zTree_v3/js/jquery.ztree.excheck-3.5.js"></script>

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
	role="form" modelAttribute="adminGroup">

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
		<label for="value" class="col-sm-2 control-label">值</label>
		<div class="col-sm-10">
			<form:input path="value" cssClass="form-control" placeholder="value" />
			<div class="help-block">
				一般以ROLE_开头,比如ROLE_ADMIN,ROLE_USER,ROLE_NAME等 <br>
				<form:errors path="value" />
			</div>
		</div>
	</div>

	<div class="form-group">
		<label for="audit" class="col-sm-2 control-label">是否可以审核</label>
		<div class="col-sm-2">
			<form:select path="audit" items="${yes_no }" cssClass="form-control" />
		</div>
		<label for="readLookthrough" class="col-sm-2 control-label">审核列表</label>
		<div class="col-sm-2">
			<form:select path="readLookthrough" cssClass="form-control">
				<form:option value="0">无</form:option>
				<form:options items="${lookthrouth }" />
			</form:select>
		</div>
		<label for="lookthrough" class="col-sm-2 control-label">审核结果</label>
		<div class="col-sm-2">
			<form:select path="lookthrough" cssClass="form-control">
				<form:option value="0">无</form:option>
				<form:options items="${lookthrouth }" />
			</form:select>
		</div>
	</div>

	<div class="form-group">
		<label for="startlookthrough" class="col-sm-2 control-label">新闻发布状态</label>
		<div class="col-sm-2">
			<form:select path="startlookthrough" cssClass="form-control">
				<form:option value="0">无</form:option>
				<form:options items="${lookthrouth }" />
			</form:select>
		</div>
		<label for="move" class="col-sm-2 control-label">是否可以移动新闻</label>
		<div class="col-sm-2">
			<form:select path="move" items="${yes_no }" cssClass="form-control" />
		</div>
	</div>

	<div class="form-group">
		<label for="note" class="col-sm-2 control-label">简介</label>
		<div class="col-sm-10">
			<form:textarea path="note" rows="5" cssClass="form-control" />
		</div>
	</div>

	<div class="form-group">
		<div class="col-sm-10 col-md-offset-2">
			<button type="submit" class="btn btn-danger">保存</button>
		</div>
	</div>
	<c:set var="fromRightSet">
		<c:forEach varStatus="varStatus" var="at"
			items="${adminGroup.adminRight }">${at.id }<c:if
				test="${!varStatus.last}">,</c:if>
		</c:forEach>
	</c:set>
	<c:set var="fromRightSet_tree">
		<c:forEach varStatus="varStatus" var="at"
			items="${adminGroup.adminRight }">'${at.id }'<c:if
				test="${!varStatus.last}">,</c:if>
		</c:forEach>
	</c:set>
	<input name="fromRight" id="fromRight" type="hidden"
		value="${fromRightSet }">

	<div class="form-group" style="position: relative;">
		<label for="adminGroup" class="col-sm-2 control-label">权限列表</label>
		<div class="col-sm-9 well well-sm">
			<ul id="rightGroupMap" class="ztree"></ul>
		</div>
	</div>
</form:form>

<SCRIPT type="text/javascript">
<!--
	var settingGroup = {
		view : {
			selectedMulti : false
		},
		check : {
			enable : true,
			chkStyle : "checkbox",
			chkboxType : {
				"Y" : "ps",
				"N" : "ps"
			}
		},

		data : {
			simpleData : {
				enable : true
			}
		},
		callback : {
			onCheck : onClickRight
		}
	};

	var zNodesGroup = [ ${sniperMenu.key} ];

	function checked() {
		var treeObjGroup = $.fn.zTree.getZTreeObj("rightGroupMap");
		var nodes = treeObjGroup.transformToArray(treeObjGroup.getNodes());
		var checked = new Array(${fromRightSet_tree });
		zTree_Menu = $.fn.zTree.getZTreeObj("rightGroupMap");
		for (var i = 0; i < checked.length; i++) {

			var selected = zTree_Menu.getNodeByParam("id", checked[i], null)
			treeObjGroup.checkNode(selected, true, false);

		}
	}

	function onClickRight(e, treeId, treeNode) {

		var zTree = $.fn.zTree.getZTreeObj("rightGroupMap"), nodes = zTree
				.getSelectedNodes(), v = "";

		var checkCount = zTree.getCheckedNodes(true);
		nodes.sort(function compare(a, b) {
			return a.id - b.id;
		});
		for (var i = 0, l = checkCount.length; i < l; i++) {
			//可以多选
			v += checkCount[i].id + ",";

		}
		//多选字符串截取
		if (v.length > 0)
			v = v.substring(0, v.length - 1);

		$("#fromRight").val(v);
		return true;
	}

	$(document).ready(function() {
		$.fn.zTree.init($("#rightGroupMap"), settingGroup, zNodesGroup);
		checked();

	});
//-->
</SCRIPT>

<div class="alert alert-success alert-dismissible" role="alert">
	<button type="button" class="close" data-dismiss="alert">
		<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
	</button>
	<ul>
		<li>名词介绍</li>
		<li>是否可以审核: 用户是否可以审核新闻</li>
		<li>审核列表： 表示用户可在“新闻管理-》未审核新闻”页面可以看到那些状态的新闻</li>
		<li>审核结果： 表示用户审核新闻之后，新闻获得状态，配合是否可以审核新闻使用</li>
		<li>新闻的状态条： 一级发布-》二级发布-》发布-》回收站</li>
		<li>权限列表：是用户可以看到访问那些资源。</li>
		<li>修改权限之后当前用户组所有用户重新登录才能起作用。</li>
	</ul>
</div>