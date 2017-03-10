<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://shiro.apache.org/tags" prefix="shrio"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>


<!-- 表演验证，无刷新提交 -->
<link
	href="${baseHref.baseHref }myfiles/Plugin/jQuery-Validation-Engine/css/validationEngine.jquery.css"
	media="screen" rel="stylesheet" type="text/css">

<script type="text/javascript"
	src="${baseHref.baseHref }myfiles/js/jquery.from.js"></script>
<script type="text/javascript"
	src="${baseHref.baseHref }myfiles/Plugin/jQuery-Validation-Engine/js/jquery.validationEngine.js"></script>
<script type="text/javascript"
	src="${baseHref.baseHref }myfiles/Plugin/jQuery-Validation-Engine/js/languages/jquery.validationEngine-zh_CN.js"></script>
<script type="text/javascript">
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
			if (confirm("提交数据?")) {
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
			id = 0;
			for ( var i in responseText) {
				if (!isNaN(responseText[i])) {
					sid = parseInt(responseText[i]);
				} else {
					html += responseText[i] + "\n";
				}
			}
			//设置当前sid的值
			layer.msg(html);
			//$(document).scrollTop(0);
			if (id > 0) {
				//setTimeout("window.location.reload()", 1000);
			}
		} else {
			layer.msg("未知错误");
		}
		layer.close(loadi);
	}
</script>