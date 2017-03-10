(function($) {
	$.fn.extend({
		"scoreStandard" : function(options) {
			options = $.extend({
				url : 'admin/admin-score/scorestandard',
				mainDiv : '.standards',
				childDiv : '.standard',
				addDiv : '.standardAdd',
				templateDiv : '#score'

			}, options);

			var bind = function() {
				$(options.mainDiv).delegate("button", "click", function() {
					// 数据组装
					var data = getData(this);
					//console.log(data);
					sendData(data, this);

				});

				$(options.addDiv).bind(
						"click",
						function() {
							var tmpData = {};
							$('#tempdata').setTemplate(
									$(options.templateDiv).html())
									.processTemplate(tmpData);
							html = $('#tempdata').html();
							$(options.mainDiv).append(html);
						});
			};

			var getData = function(object) {
				var $parnet = $(object).parents(options.childDiv);
				var data = {};

				data['type'] = $(object).attr("data-name");

				$parnet.find("textarea").each(function(i, obj) {
					data[$(obj).attr('name')] = $(obj).val();
				});

				$parnet.find("input").each(function(i, obj) {
					data[$(obj).attr('name')] = $(obj).val();
				});

				$parnet.find("select").each(function(i, obj) {
					data[$(obj).attr('name')] = $(obj).val();
				});

				data['enterprise.id'] = $("input[name='enterprise.id']").val();
				data['expert.id'] = $("input[name='expert.id']").val();
				data['project.id'] = $("input[name='project.id']").val();
				data['score'] = $("input[name='score_id']").val();
				data['standard.id'] = $parnet.attr('id');

				//console.log(data);
				return data;
			}

			var sendData = function(sendData, obj) {
				
				var $parent = $(obj).parents(options.childDiv);
				var divCount = $parent.parent().find(">div").size();
				var divCurrent = $parent.index();
				var currentID = $parent.attr("id");
				switch (sendData['type']) {
				case "save":
					if (!sendData['project.id']) {
						layer.msg('项目不能为空');
						$("input[name='project.name']").focus();
						return;
					}
					if (!sendData['enterprise.id']) {
						layer.msg('响应人不能为空');
						$("input[name='enterprise.name']").focus();
						return;
					}
					if (!sendData['standard.question']) {
						layer.msg('内容不能为空');
						$parent.find("input[name='standard.question']").focus();
						return;
					}
					break;
				case "del":
					if (!sendData['standard.id']) {
						$parent.remove();
						return;
					}
					break;
				case "up":
					if(divCurrent == 0){
						layer.msg('已经位于最顶端');
						return ;
					}
					//获取上一行的排序
					changeSort = $parent.prev().find('input[name="sort"]').val();
					changeId = $parent.prev().attr("id");
					//自己的排序
					sendData['sort'] = $parent.find('input[name="sort"]').val();
					//目标排序
					sendData['changeSort'] = changeSort;
					//目标id
					sendData['changeId'] = changeId;
					break;
				case "down":
					if(divCurrent >= (divCount - 1)){
						layer.msg('已经位于最低端');
						return ;
					}
					//获取上一行的排序
					//sort = $parent.next().find('input[name="sort"]').val();
					//sendData['sort'] = (parseInt(parseInt(sort) + 2)) + "";
					
					//获取上一行的排序
					changeSort = $parent.next().find('input[name="sort"]').val();
					changeId = $parent.next().attr("id");
					//自己的排序
					sendData['sort'] = $parent.find('input[name="sort"]').val();
					//目标排序
					sendData['changeSort'] = changeSort;
					//目标id
					sendData['changeId'] = changeId;
					break;
				default:
					return;
				}
				sendData['sort'] = sendData['sort'] + "";
				//sendData['sort'] =  "";
				//console.log(sendData);
				$.post(options.url, sendData, function(data, textStatus) {
					if (textStatus == 'success') {
						
						$parent.find('input[name="sort"]').val(data.standard.sort);
						switch (sendData['type']) {
						case "save":
							if (data.standard.id != null) {
								$parent.prop('id', data.standard.id);
							}
							break;
						case "del":
							$parent.remove();
							break;
						case "up":
							$parent.find('input[name="sort"]').val(data.changeSort);
							$parent.prev().find('input[name="sort"]').val(data.sort);
							prevID = $parent.prev().prop("id");
							$('#' + currentID).insertBefore($('#' + prevID));
							break;
						case "down":
							$parent.find('input[name="sort"]').val(data.changeSort);
							$parent.next().find('input[name="sort"]').val(data.sort);
							nextID = $parent.next().prop("id");
							$('#' + nextID).insertBefore($('#' + currentID));
							break;
						}

						layer.msg('操作成功');
					} else {
						layer.msg('操作失败');
					}

				}, "json")
				.error(function(error) { 
					layer.msg('error' );
					console.log(error);
				});
			}
			
			

			bind();
			// end
		}
	})
})(jQuery);