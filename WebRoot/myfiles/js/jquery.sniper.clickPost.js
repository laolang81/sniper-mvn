/**
 * 用户点击提交
 */
var ClickPost = function() {
	"use strict";
	var config;
	var id;
	var url;
	var type;
	var value;
	var data = {};

	var bindAction = function() {
		$(config.div).find("input[data-type='click']").on("click", function() {
			id = $(this).attr("data-id");
			data['id'] = id;
			value = $(this).attr("data-value");
			url = $(this).attr("data-url");
			type = $(this).attr("data-type");
			ajaxPost();
		});
	};

	/**
	 * 数据提交
	 */
	var ajaxPost = function() {
		$.ajax({
			url : url,
			dataType : 'json',
			method : "post",
			data : data,
			success : function(result) {
				layer.msg(result.msg);
				if (result.code != 200) {
					return;
				}
				;
			}
		});
	};

	return {
		init : function(conf) {
			conf = conf || {};
			config = $.extend({
				div : "#layer-photos",
				click : "",

			}, conf);
			bindAction();
		}
	};
}();