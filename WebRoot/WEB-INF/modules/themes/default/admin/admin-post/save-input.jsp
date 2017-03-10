<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://shiro.apache.org/tags" prefix="shrio"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<title>${depHtml.name}-${itemsHtml.name }</title>
<style>
.fileList {
	list-style: none outside none;
	border: 1px dotted #CCCCCC;
	overflow-y: scroll;
	margin: 0;
	padding: 0;
}

.fileList .li {
	border: 1px solid #CCCCCC;
	height: 100px;
	font-weight: bolder;
	width: 200px;
	position: relative;
	float: left;
	margin: 3px;
	background: none repeat scroll 0 0 ghostwhite;
	text-align: center;
}

.fileList img {
	max-height: 100px;
	max-width: 200px;
}

.fileList .li .info {
	FILTER: alpha(opacity = 50);
	opacity: 0.5;
	height: 20px;
	color: #ffffff;
	line-height: 20px;
	text-align: left;
	bottom: 10px;
	background: none repeat scroll 0 0 #000000;
	left: 3px;
	position: absolute;
	z-index: 100;
	right: 3px;
	padding: 2px;
}

.fileList .li .del {
	right: 3px;
	position: absolute;
	z-index: 100;
	cursor: pointer;
	color: #ffffff;
}

.fileList .li .alertpath {
	left: 75px;
	position: absolute;
	z-index: 100;
	cursor: pointer;
	color: #ffffff;
}

.menuContent {
	background: none repeat scroll 0 0 #fff;
	z-index: 1000;
	overflow: auto;
}

.from label {
	float: none;
	padding: 0;
	text-indent: 10px;
}

dl {
	border: 1px solid gray;
	height: 550px;
	overflow: scroll;
}

dt {
	border-bottom: 1px solid #DDDDDD;
	float: left;
	font-size: 14px;
	line-height: 30px;
	width: 98%;
	margin: 5px 1%;
	text-indent: 10px;
}

dd {
	padding: 1px 0;
	float: left;
	width: 46%;
	line-height: 20px;
	margin-left: 10px;
}

.itemSelect {
	background-color: #CC0000;
	color: #FFFFFF;
}

.log {
	line-height: 25px;
	overflow: visible;
	position: relative;
	background-color: #FFFFFF;
	border-radius: 3px;
	border-spacing: 0;
	border: 1px solid #D1E5EE;
	margin-top: 20px;
}

.log .title {
	background: none repeat scroll 0 0 #FFFFFF;
	position: absolute;
	top: -13px;
	border-radius: 3px;
	left: 30px;
	border: 1px solid #D1E5EE;
	padding: 0 5px;
}

.log ul {
	margin-top: 15px;
	overflow: hidden;
	position: relative;
	background-color: #FFFFFF;
	border-radius: 3px;
	border-spacing: 0;
	list-style: outside none none;
}

.log ul li {
	float: left;
	padding: 0 10px;
}
</style>
<form:form method="POST" cssClass="form-horizontal" id="sniperForm"
	role="form" modelAttribute="post">

	<form:hidden path="sid" />
	<form:hidden path="siteid" />
	<form:hidden path="itemid" />
	<c:if test="${fn:length(infoClasses) > 0 }">
		<div class="form-group">
			<label for="fromsite" class="col-sm-2 control-label">特殊栏目选择</label>
			<div class="col-sm-4">
				<form:select path="icId" cssClass="form-control">
					<form:option value="0">选择</form:option>
					<form:options items="${infoClasses }" />
				</form:select>
			</div>
		</div>
	</c:if>
	<div class="form-group">
		<label for="name" class="col-sm-2 control-label text-danger">新闻标题</label>
		<div class="col-sm-10">
			<form:input path="subject" cssClass="form-control validate[required]"
				placeholder="请填写标题" />
		</div>
	</div>

	<div class="form-group">
		<label for="note" class="col-sm-2 control-label">副标题</label>
		<div class="col-sm-10">
			<form:input path="subtitle" cssClass="form-control" placeholder="" />
		</div>
	</div>
	<div class="form-group">
		<label for="fromsite" class="col-sm-2 control-label text-danger">新闻来源</label>
		<div class="col-sm-4">
			<form:input path="fromsite"
				cssClass="form-control validate[required]" placeholder="新闻来源" />
		</div>
		<div class="col-sm-4">
			<input type="checkbox" class="ml10" value="1" id="isyuanchuang"
				name="isyuanchuang"> 原创
		</div>
	</div>
	<div class="form-group">
		<label for="period" class="col-sm-2 control-label">月度/期刊数</label>
		<div class="col-sm-8">
			<form:input path="period" cssClass="form-control" placeholder="" />
			<div class="help-block">主要用于统计数据属性的信息，格式如:2008-01。</div>
		</div>
	</div>
	<div class="form-group">
		<label for="url" class="col-sm-2 control-label">跳转地址</label>
		<div class="col-sm-10">
			<form:input path="url" cssClass="form-control validate[custom[url]]"
				placeholder="用于第三方页面跳转" />
		</div>
	</div>

	<div class="form-group">
		<label for="tags" class="col-sm-2 control-label">标签</label>
		<div class="col-sm-10">
			<form:input path="tags" cssClass="form-control"
				placeholder="多个标签请用英文逗号（,）分开" />
			<div class="help-block">
				<form:errors path="tags" />
			</div>
		</div>
	</div>

	<div class="form-group">
		<label for="displayorder" class="col-sm-2 control-label text-danger">排序</label>
		<div class="col-sm-3">
			<form:input path="displayorder"
				cssClass="form-control validate[required]" />
		</div>
		<label for="displayorder" class="col-sm-2 control-label text-danger">发布时间</label>
		<div class="col-sm-3">
			<input name="stime" type="text" class="form-control Wdate"
				value="<fmt:formatDate value="${stime }" pattern="yyyy-MM-dd HH:mm:ss" />">
		</div>
	</div>

	<div class="form-group">
		<label for="preid" class="col-sm-2 control-label">推荐首页要闻</label>
		<div class="col-sm-3">
			<form:select path="suggested" items="${suggesteds }"
				cssClass="form-control" />
		</div>
		<%-- <label for="preid" class="col-sm-2 control-label">权重</label>
		<div class="col-sm-2">
			<form:select path="preid" items="${preid }" cssClass="form-control" />
		</div> --%>
	</div>
	<div class="form-group">
		<label for="note" class="col-sm-2 sr-only control-label">内容</label>
		<div class="col-sm-12">
			<div class="help-block">
				<p class="text-danger">上传的图片不能小于450KB。</p>
			</div>
			<script id="container" name="content.content" type="text/plain">
       			${post.content.content}
    		</script>
		</div>
	</div>
	<div id="temp-img-list" style="display: none"></div>
	<form:hidden path="content.cid" />

	<div class="form-group">
		<ol class="fileList mt10" id="fileValue"></ol>
	</div>
	<input type="hidden" name="postFiles" value="${postFilesNo }">
	<div class="form-group">
		<div class="col-sm-8 col-md-offset-0">
			<button type="submit" class="btn btn-danger">保存</button>
		</div>
	</div>
	<div class="form-group">
		<div class="col-sm-12 menuContent well well-sm">
			<dl>
				<c:forEach items="${departments }" var="d">
					<dt>
						<a name="name${d.key }">${d.value }</a>
					</dt>
					<c:if test="${treeData[d.key] != null}">
						<c:forEach items="${treeData[d.key] }" var="tr">
							<dd>
								<label
									<c:if test="${post.itemid != null && post.itemid == tr.key }">class="itemSelect"</c:if>
									title="${tr.key }" for="itemid${tr.key }"> <input
									<c:if test="${post.itemid != null && post.itemid == tr.key }">checked="checked" disabled="true"</c:if>
									<c:if test="${itmidsMap[tr.key] != null }">checked="checked"</c:if>
									type="checkbox" value="${tr.key }" id="itemid${tr.key }"
									name="itemid">&nbsp;${tr.value }(${tr.key })
								</label>
							</dd>
						</c:forEach>
					</c:if>
				</c:forEach>
			</dl>
		</div>
	</div>

</form:form>
<div class="log">
	<div class="title">文章操作记录</div>
	<ul>
		<c:forEach items="${subjectLogs }" var="sl">
			<li>${logcodes[sl.logId] }:<fmt:formatDate value="${sl.time }"
					pattern="yyyy-MM-dd HH:mm:ss" />(${sl.uid })
			</li>
		</c:forEach>
	</ul>
</div>

<div class="alert alert-success alert-dismissible mt10" role="alert">
	<button type="button" class="close" data-dismiss="alert">
		<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
	</button>
	<ul>
		<li>月度/期刊数：主要作用于数据统计之类的数据新闻。</li>
		<li>跳转地址：当需要访问新闻跳转到其他页面的时候，请在此填写要去的网页地址。</li>
		<li>排序：新闻默认都是按照这儿数字从大到小排序。</li>
		<li>发布时间：如果你的发布时间比当前时间要靠后，那新闻将在审核之后在你设置的时间发布(延迟发布效果)。</li>
	</ul>
</div>

<!-- 标签所用 -->
<link
	href="${baseHref.webUrl }/myfiles/Plugin/jquery-ui-1.10.3.custom/css/ui-lightness/jquery-ui-1.10.3.custom.min.css"
	media="screen" rel="stylesheet" type="text/css">
<link
	href="${baseHref.webUrl }/myfiles/Plugin/jQuery-Tags-Input/jquery.tagsinput.css"
	media="screen" rel="stylesheet" type="text/css">
<script type="text/javascript"
	src="${baseHref.webUrl }/myfiles/Plugin/jquery-ui-1.10.3.custom/js/jquery-ui-1.10.3.custom.min.js"></script>
<script type="text/javascript"
	src="${baseHref.webUrl }/myfiles/Plugin/jQuery-Tags-Input/jquery.tagsinput.min.js"></script>
<!-- 百度编辑器 加载编辑器的容器 -->
<script type="text/javascript">
	window.UEDITOR_WEB_URL = '${baseHref.webUrl }';
</script>
<!-- 配置文件 -->
<script type="text/javascript"
	src="${baseHref.webUrl }/myfiles/Plugin/UEditor/ueditor.config.js"></script>
<!-- 编辑器源码文件 -->
<script type="text/javascript"
	src="${baseHref.webUrl }/myfiles/Plugin/UEditor/ueditor.all.js"></script>

<script type="text/javascript"
	src="${baseHref.webUrl }/myfiles/js/jquery.sniper.post.js"></script>

<script type="text/javascript">
	$(function() {
		//标签
		$("input[name='tags']").tagsInput({
			width : '100%',
			height : '60px',
			autocomplete_url : 'public/sendAjaxTags'
		});
		//处理内容，及其附件
		$().sniperpost({
			sid : '${post.sid}',
			imagePathPrefx : "${systemConfig.imagePrefix}",
			webName : "${systemConfig.webName}"
		});

	});
</script>
<!-- 表演验证，无刷新提交 -->
<link
	href="${baseHref.webUrl }/myfiles/Plugin/jQuery-Validation-Engine/css/validationEngine.jquery.css"
	media="screen" rel="stylesheet" type="text/css">

<script type="text/javascript"
	src="${baseHref.webUrl }/myfiles/js/jquery.from.js"></script>
<script type="text/javascript"
	src="${baseHref.webUrl }/myfiles/Plugin/jQuery-Validation-Engine/js/jquery.validationEngine.js"></script>
<script type="text/javascript"
	src="${baseHref.webUrl }/myfiles/Plugin/jQuery-Validation-Engine/js/languages/jquery.validationEngine-zh_CN.js"></script>
<script type="text/javascript"
	src="${baseHref.webUrl }/myfiles/Plugin/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">
	$(document).delegate('.Wdate', 'click', function() {
		WdatePicker({
			dateFmt : 'yyyy-MM-dd HH:mm:ss'
		})
	});

	var loadi;
	$().ready(function() {

		var options = {
			//target:        '#output',
			beforeSubmit : showRequest,
			success : showResponse,
			// other available options:      
			type : 'post',
			dataType : 'json',
			clearForm : false,
			timeout : 5000
		};

		$("#sniperForm").submit(function() {
			$(this).ajaxSubmit(options);
			return false;
		});

	})

	$("#sniperForm").validationEngine('attach', {
		promptPosition : "topLeft"
	});
	// pre-submit callback 
	function showRequest(formData, jqForm, options) {

		if ($("#sniperForm").validationEngine('validate')) {
			if (confirm("提交新闻?")) {
				loadi = layer.msg("稍等...");
				return true;
			}
		}
		return false;
	}
	// post-submit callback 
	function showResponse(responseText, statusText, xhr, $form) {
		//关闭所有窗口

		if (statusText == 'success') {
			var html = "";
			sid = 0;
			for ( var i in responseText) {
				if (!isNaN(responseText[i])) {
					sid = parseInt(responseText[i]);
				} else {
					html += responseText[i] + "\n";
				}
			}
			//设置当前sid的值
			//$("input[name='sid']").val(sid);
			layer.msg(html);
			//$(document).scrollTop(0);
			if (sid > 0) {
				//setTimeout("window.location.reload()", 1000);
			}
		} else {
			layer.msg("未知错误", 2, -1);
		}
		layer.close(loadi);
	}
</script>