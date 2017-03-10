/**
 * 用户列表也的所有操作
 */
(function($) {
	$.fn.extend({
				"sniperpost" : function(options) {
					// 设置默认值
					options = $.extend({
						baseurl : 'doftec',
						webName : '',
						// 新闻id
						sid : '',
						// 获取图片的类型条件
						type : 'post-id',
						// 储存图片id的input名称
						aidname : "postFiles",
						// 网站配置图片域名
						imagePathPrefx : '',
						// 储存图片的div
						fileValue : '#fileValue',
						// 编辑器的操作id
						editor : 'apiContent.content'

					}, options);

					// 初始化百度编辑器
					var ue = UE.getEditor('container', {
						initialFrameHeight : 400
						
					});

					var getFilesPost = function() {
						var filesid = new Array();
						var filesPost = $('input[name="postFiles"]').val();
						if (filesPost != '') {
							filesid = filesPost.split(',');
						}
						return filesid;
					}
					//新的赋值
					var setFilesPost = function(data) {
						filesid = getFilesPost();
						var j = 0;
						for (var i = 0; i < filesid.length; i++) {
							if (filesid[i] == data.id) {
								j++;
							}
						}
						//表示不存在，数据有改动
						if (j == 0) {
							filesid.push(data.id);
						}
						$('input[name="postFiles"]').val(filesid.join(','));
					}
					// qingchu 关系

					var delFilesPost = function(id) {

						filesid = getFilesPost();
						var a = filesid.indexOf(id);
						filesid.splice(a, 1);
						console.log(id);
						console.log(filesid);

						$('input[name="postFiles"]').val(filesid.join(','));
					}

					/**
					 * 设置图片
					 */
					var setImage = function(data) {
						// 新版设置附件id
						setFilesPost(data);
						// 判断图片是否已经显示
						if($("#" + data.id).html() != undefined ){
							return ;
						}
						var filePath = data.filePath;
						if (filePath.substring(0, 4) != "http") {
							filePath = options.imagePathPrefx + filePath;
						}
						html = '<li class="li" id="' + data.id + '">';
						if (data.fileType.substring(0, 5) == 'image') {
							html += '<img src="';
							html += filePath;
							html += '" title="' + data.oldName + '">';
						} else {
							html += '<img src="myfiles/images/webicon/attach_64.png" title="'
									+ data.oldName + '">';
						}
						html += '<div class="info">';
						if (data.fileType.substring(0, 5) == 'image') {
							html += '<input type="checkbox" id="c' + data.id
									+ '"';
							if (data.checked == 1) {
								html += ' checked="checked" ';
							}
							html += ' value="' + data.id + '"> 新闻图片';
						}
						html += '<span class="alertpath" data-value="'
								+ filePath + '"  data-type="path">查看路径</span>';
						html += '<span data-value="' + data.id
								+ '" class="del" data-type="del">删除附件</span>';
						html += '</div>';
						html += '</li>';
						$('#fileValue').append(html);
					}

					// 设置图片新闻
					var setPic = function(obj) {
						id = $('#c' + obj).val();
						checked = $('#c' + obj).attr("checked");
						if (checked == 'checked') {
							checked = 1;
						} else {
							checked = 0
						}
						// 更新新闻图片属性的地址
						$.post('file-info/setprimeimage', {
							id : id
						}, function(data, textStatus) {
							if (data.code == 200) {
								layer.msg('操作成功');
							} else {
								layer.msg('操作失败');
							}
						}, 'json');
					}
					// 设置图片新闻
					var getImage = function(value, type) {
						if (!type)
							type = 'url';

						if (!value) {
							return false;
						}
						if (type == "url") {
							$.post('file-info/getFileByUrl', {
								url : value
							}, function(data, textStatus) {
								if (data.file) {
									setImage(data.file);
								}
							}, 'json');
						} else if (type == 'post-id') {
							$.post('file-info/getFilesByPostID', {
								id : value
							}, function(data, textStatus) {
								for ( var i in data.file) {
									if (data.file[i]) {
										setImage(data.file[i]);
									}
								}
							}, 'json');
						}
					}

					// 弹出当前文件地址以便复制使用
					var copyPath = function(obj) {
						var path = $(obj).attr("data-value");
						// 墨绿深蓝风
						var copypath = layer.alert(path, {
							title : "请手工复制附件地址",
							skin : 'layui-layer-molv',
							closeBtn : 0
						}, function() {
							layer.close(copypath);

						});
					}
					// 删除文件
					var unBindfile = function(id) {
						if (!id) {
							return false;
						}

						// 询问框
						var confirm = layer.confirm(
								'图片删除不可恢复,与其相关联的文章内容图片将无法显示，请及时删除文章内容图片！', {
									btn : [ '取消', '删除' ]
								// 按钮
								}, function() {
									layer.close(confirm);
								}, function() {
									$.post('file-info/deleteFileByID', {
										id : id
									}, function(data, textStatus) {
										if (data.code == 200) {
											$('#' + id).remove();
											delFilesPost(id);
											var oldHtml = ue.getContent();
											layer.msg('删除成功');
										} else {
											layer.msg('删除失败');
										}
									}, 'json');
									layer.close(confirm);
								});

					}

					// 删除文件
					var unBindfile_bak = function(id) {
						if (!id) {
							return false;
						}
						$('#' + id).remove();
						delFilesPost(id);
					}

					var run = function() {
						// sniper_menu
						$(options.fileValue).delegate("span", "click",
								function() {
									var type = $(this).attr("data-type");
									var value = $(this).attr("data-value");
									switch (type) {
									case "path":
										copyPath(this);
										break;
									case "del":
										unBindfile(value);
									default:
										break;
									}
								})
						// 设置图片
						$(options.fileValue).delegate("input", "click",
								function() {
									var value = $(this).attr("value");
									setPic(value);
								})
					//栏目选择背景变色
					$('dd label').click(function() {
						if ($(this).find("input[type='checkbox']").prop("checked")) {
							$(this).addClass('itemSelect');
						} else {
							$(this).removeClass('itemSelect');
						}
					});
					//原创处理
					$("#isyuanchuang").bind("click", function() {
						var value = options.webName;
						var oldValue = $("#fromsite").val();
						if (this.checked) {
							$('#fromsite').val(value)
						} else {
							$('#fromsite').val(oldValue)
						}
					})

						// 获取图片
						getImage(options.sid, options.type);
					}

					// 编辑器添加事件
					ue.addListener('contentChange', function(editor) {
						// 获取编辑器中的内容（html 代码）
						var imgContent = ue.getContent();
						//追加内容
						//ue.setContent("--");
						//console.log("获取的编辑器内容:" + img);
						if (imgContent != "") {
							// 把编辑器中的内容放到临时div中，然后进行获取文件名称。
							$("#temp-img-list").html(imgContent);

							// 循环获取所有图片,没此操作步骤，清空，原有的数据，重新添加所有
							var imgArray = new Array();
							$("#fileValue").empty();
							$("#temp-img-list img").each(function() {
								var src = $(this).attr("src");
								src = src.replace(options.imagePathPrefx, "");
								imgArray.push(src);
								getImage(src);
							});
							
							console.log(imgArray);
							// 循环获取所有链接
							var aArray = new Array();
							$("#temp-img-list a").each(function() {
								var src = $(this).attr("href");
								src = src.replace(options.imagePathPrefx, "");
								aArray.push(src);
								getImage(src);
							});

						}
					});
					
					

					function callbackImg(a) {
						console.log(a);
					}

					run();
					// 返回this，支持jquery链式操作
					return this;
					// end
				}
			});
})(jQuery);