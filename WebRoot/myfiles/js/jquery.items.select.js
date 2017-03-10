/**
 * 用户处室栏目下拉菜单
 */
(function($) {
	$.fn.extend({
		"siteid" : function(options) {
			// 设置默认值
			options = $.extend({
				// 提交的之
				post : '',
				// 选定之
				selected : '',
				// 头部
				t : 1,
				// 操作selec
				changeselect : 'siteidSelect',
				// 操作对应的select
				selectselect : 'itemidSelect',
				name : 'itemid',
				url : 'doftec/admin-channel/ajaxitems',
				gettrue : 0,
				// 获取class数据参数，c=1为开启
				c : 0,
				cselected : '',
				cselect : 'classSelect',
				cname : 'icid',
				curl : 'doftec/admin-channel/ajaxclass'
			}, options);
			var post = '';
			// 主要参数为空返回
			if (!options.post) {
				if ($('#' + options.changeselect).length > 0) {
					post = $('#' + options.changeselect).val();
				}
			} else {
				post = options.post;
			}
			
			if (!post){
				//return false;
			}

			// 帮顶操作
			$('#' + options.changeselect).bind('change', function() {
				getitem($(this).val());
			});
			// 在帮顶之后如果相关数据都存在调用一次 ，options.post必须存在
			getitem(post, options.selected);
			// 选中
			if ($('#' + options.changeselect).length > 0 && post) {
				$('#' + options.changeselect + ' option[value=' + post + ']')
						.attr("selected", true);
			}

			// 获取栏目列表
			function getitem(value, selected) {
				// 数据请求
				if (!value) {
					value = post;
				}
				if (!selected) {
					selected = options.selected;
				}
				if(!value || value  == 0 || value == ''){
					// 清空原有数据值
					$('#' + options.selectselect).remove();
					$('#' + options.cselected).remove();
					return ;
				}
				
				$.post(options.url, {
					id : value,
					t : options.t,
					gettrue : options.gettrue
				},
				function(data, textStatus) {
					// 获取数据之
					if (data && data.length > 0 ) {
						//console.log(data.length);
						// 添加下拉菜单
						var selecthtml = '<select id="' + options.selectselect + '" class="ml10" name="' + options.name + '"></select>';
						if ($('#' + options.selectselect).length == 0) {
							$('#' + options.changeselect).after(selecthtml);
						}
						$('#' + options.selectselect).append('<option value="0">选择</option>');
						for ( var i in data) {
							$('#' + options.selectselect).append('<option value="' + data[i].id + '">' + data[i].name + '</option>');
						}

						if (selected) {
							$('#' + options.selectselect + ' option[value=' + selected + ']').attr("selected", true);
						}
						if (options.c == 1) {
							$('#' + options.selectselect).bind('change', function() {getclass($(this).val());});
							// 调用一次
							getclass(options.selected, options.cselected);
						}
					}
				}, "json");
			}

			// 获取class数据
			function getclass(value, selected) {
				// 数据请求
				if (!value) {
					return false
				}
				// 重新赋值
				options.selected = value;
				if (!selected) {
					selected = options.cselected;
				}
				$.post(options.curl, {id : value}
					, function(data, textStatus) {
					// 清空原有数据值
					if ($('#' + options.cselect).length > 0) {
						$('#' + options.cselect).remove();
					}
					// 获取数据之
					if (data && data.length > 0) {
						// 添加下拉菜单
						var selectclasshtml = '<select id="' + options.cselect
								+ '" class="ml10" name="' + options.cname
								+ '"></select>';
						if ($('#' + options.cselect).length == 0) {
							$('#' + options.selectselect).after(selectclasshtml);
						}
						$('#' + options.cselect).append('<option value="">选择</option>');
						for ( var i in data) {
							$('#' + options.cselect).append('<option value="' + data[i].id + '">' + data[i].name + '</option>');
						}
						if (selected) {
							$('#' + options.cselect + ' option[value=' + selected + ']').attr("selected", true);
						}
					}
					// 调用
				}, "json");
			}
			// end
		}
	});
})(jQuery);