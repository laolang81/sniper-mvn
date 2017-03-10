// JavaScript Document
$(document).ready(function() {
	var Operation_height = $(".Operation").height();
	var Menu_height = $(".menu").height();
	if (Operation_height <= Menu_height) {
		Operation_height = Menu_height;
	}
	
	$(".Operation").height(Operation_height + 50);
	
	function f_type() {
		var file_type = $("#file_type").val();
		switch (file_type) {
		case "项目":
			$("#search_text").val("请输入项目名称");
			$("#search_text").focus(function() {
				$("#search_text").val("");
			});
			$("#search_text").focusout(function() {
				$("#search_text").val("请输入项目名称");
			});
			break;
		case "专家":
			$("#search_text").val("请输入专家姓名");
			$("#search_text").focus(function() {
				$("#search_text").val("");
			});
			$("#search_text").focusout(function() {
				$("#search_text").val("请输入专家姓名");
			});
			break;
		case "企业":
			$("#search_text").val("请输入企业名称");
			$("#search_text").focus(function() {
				$("#search_text").val("");
			});
			$("#search_text").focusout(function() {
				$("#search_text").val("请输入企业名称");
			});
			break;
		case "档案":
			$("#search_text").val("请输入文件名称");
			$("#search_text").focus(function() {
				$("#search_text").val("");
			});
			$("#search_text").focusout(function() {
				$("#search_text").val("请输入文件名称");
			});
			break;
		}
	}
	;
	f_type();
	$("#file_type").change(function() {
		f_type()
	});
});