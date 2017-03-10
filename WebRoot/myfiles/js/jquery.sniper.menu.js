/**
 * 用户列表也的所有操作
 */
jQuery.ajaxSettings.traditional = true;
(function($) {
	$.fn
			.extend({
				"snipermenu" : function(options) {
					// 设置默认值
					options = $.extend({
						baseurl : 'doftec',
						// 滚动菜单 object
						id : '#sniper_menu',
						// 被操作区域的object
						div : '.table',
						// 没条记录的值
						valuename : 'list.id',
						// 记录区分前缀
						prefix : '#sl_',
						delid : 'delid',
						// 审核css
						// audit : 'success',
						// 为审css
						// naudit : 'warning',
						// 数据处理url
						url : '',
						// 数据导出url
						exporturl : ''
					}, options);

					var chk_value = [];
					var menuValue = '';
					var menuType = '';

					// 组装url
					var url = options.baseurl + options.url;
					var exporturl = options.baseurl + options.exporturl;
					// 新叶打开
					var targettype = [ "exportword", "exportexecl", "print" ];
					// 区分记录状态

					// $(options.div + "
					// tbody>tr[data-status='1']").addClass(options.audit);
					// $(options.div + "
					// tbody>tr[data-status!='1']").addClass(options.naudit);

					// 获取checkbox的值
					var getcheckboxdata = function() {
						chk_value = [];
						$(options.div).find(
								'input[name="' + options.valuename
										+ '"]:checked').each(function() {
							chk_value.push($(this).val());
						});
					}

					// 文件导出喊出，方式是新页打开
					var exportword = function(chk_value) {
						// ?a[]=1&a[]=3
						paramid = chk_value.join('&delid=');
						// alert(id);
						// alert(exporturl+'?id[]='+paramid);
						// 一般会带着csrf
						window.open(url + '&delid=' + paramid + '&type='
								+ menuType);
					}

					// 数据发送，及获取
					var send = function() {

						var d;
						getcheckboxdata();

						if (chk_value.length == 0) {
							layer.msg("没有选择记录");
							return false;
						}

						if (!confirm('你确定继续？')) {
							return false;
						}

						// 新页打开检测
						if (targettype.toString().indexOf(menuValue) > -1) {
							// exportword(chk_value);
							// return false;
						}

						layer.msg("请求处理中，请稍后...");
						var postData = {};
						postData['menuValue'] = menuValue;
						postData['menuType'] = menuType;
						postData[options.delid] = chk_value;

						$
								.ajax({
									type : "POST",
									url : url,
									data : postData,
									dataType : "json",
									success : function(data, textStatus) {
										// 信息反馈
										if (textStatus == 'success') {
											layer.msg(data.msg);
										} else {
											layer.msg("ERROR");
										}
										if (data.code > 0) {
											switch (menuType) {
											case "download":
												// window.open(data.url);
												window.location.href = data.url;
												// d.content('<a
												// href="'+data.url+'"
												// target="_blank">点击下载</a>');
												d.close()
												break;
											case 'delete':
												for ( var i in chk_value) {
													if ($(options.prefix
															+ chk_value[i]).length != 0) {
														$(
																options.prefix
																		+ chk_value[i])
																.remove();
													}
												}
												// window.location.reload();
												d.close();
												break;
											default:

												// 替换过摸一个值
												for ( var j in chk_value) {
													if ($(options.prefix
															+ chk_value[j]).length != 0) {
														if (data[chk_value[j]]
																&& data[chk_value[j]][menuType] != undefined) {
															$(
																	options.prefix
																			+ chk_value[j])
																	.find(
																			"*[data-type='"
																					+ menuType
																					+ "']")
																	.html(
																			data[chk_value[j]][menuType]);
														} else {
															$(
																	options.prefix
																			+ chk_value[j])
																	.find(
																			"*[data-type='"
																					+ menuType
																					+ "']")
																	.html(
																			menuValue);
														}
													}
												}

												// 替换整个一行
												if (data.html != false) {
													for ( var m in data.html) {
														if ($(options.prefix
																+ m).length != 0) {
															$(
																	options.prefix
																			+ m)
																	.replaceWith(
																			data.html[m]);
														}
													}
												}
											}
										}
									}
								});
					};

					// 执行选择操作
					var click = function(obj) {
						menuValue = $(obj).attr('data-value');
						menuType = $(obj).attr('data-type');
						menuUrl = $(obj).attr('data-url');
						menutarget = $(obj).attr('data-target');
						if (menuUrl) {
							url = menuUrl;
						} else {
							// 要重新复制否则永远停留在上次设置的url
							url = options.baseurl + options.url;
						}
						console.log(menuType);
						switch (menuType) {
						case "select":
							switch (menuValue) {
							case '1':
								$(options.div + ' :checkbox').prop("checked",
										true);
								break;
							case '2':
								$(options.div + ' :checkbox').prop("checked",
										false);
								break;
							case '3':
								$(options.div + ' :checkbox').each(
										function() {
											$(this).prop("checked",
													!$(this).prop("checked"));
										});
								break;
							}
							break;
						// 移动受栏目
						case "mofcom":
						case "move":
							// 获取选中的数据
							getcheckboxdata();
							if (chk_value.length == 0) {
								layer.msg("没有选择记录");
								return false;
							}
							var $moveDiv = $(menutarget);
							var postData = {};
							postData['menuValue'] = menuValue;
							postData['menuType'] = menuType;
							postData[options.delid] = chk_value;
							$moveDiv.find("input[name='delid']").val(chk_value);
							$moveDiv.find("input[name='menuValue']").val(
									menuValue);
							$moveDiv.find("input[name='menuType']").val(
									menuType);
							// 捕获div
							layer.open({
								type : 1,
								shade : false,
								title : "数据操作", // 不显示标题
								area : [ '800px', '300px' ], // 宽高
								content : $moveDiv, // 捕获的元素
								cancel : function(index) {
									layer.close(index);
									this.content.show();
									$moveDiv.hide();
								}
							});

							break;

						default:
							send();
							break;
						}
					}
					// ul li ul li span点击操作
					var run = function() {
						// sniper_menu
						$(options.id + ' a').click(function(event) {
							click(this);
						});

						$(options.id + ' button[data-click="on"]').click(
								function(event) {

									click(this);

								});
					}

					run();
					// end
				}
			});
})(jQuery);