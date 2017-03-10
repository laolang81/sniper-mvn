(function($) {
	$.fn.extend({
				"notices" : function(options) {
					// 设置默认值
					options = $.extend({
						socketUrl : '',
						sockjsUrl : '',
						sendUserName : '',
						chatID : '',
						transports : []
					}, options);

					var ws = null;
					var url = null;
					var users = [];
					var transports = [ 'websocket', 'xdr-streaming',
							'xhr-streaming', 'iframe-eventsource',
							'iframe-htmlfile', 'xdr-polling', 'xhr-polling',
							'iframe-xhr-polling', 'jsonp-polling' ];

					// 链接
					var connect = function() {
						ws = (options.sockjsUrl.indexOf('sockjs') != -1) ? new SockJS(
								options.sockjsUrl, undefined, {
									transports : transports
								})
								: new WebSocket(options.sockjsUrl);

						
						ws.onopen = function() {
							// log('系统消息', '连接成功。');
						};
						// 处理返回数据,当前页第一次连接时执行，发送消息不执行，接受消息执行
						ws.onmessage = function(event) {
							var data = JSON.parse(event.data);
							// console.log('消息处理');
							// console.log(data);
							$("#inboxNum").html(data.count);

							console.log(data.send);
							console.log(options.sendUserName);
							if (data.send != null) {
								log(data.send, data.msg);
							}
						};
						ws.onclose = function(event) {
							// log("系统消息", '断开连接。');
						};
					}
					// 手工关闭连接
					var disconnect = function() {
						if (ws != null) {
							ws.close();
							ws = null;
						}
					}

					var updateTransport = function(transport) {

						transports = (transport == 'all') ? [ 'websocket',
								'xdr-streaming', 'xhr-streaming',
								'iframe-eventsource', 'iframe-htmlfile',
								'xdr-polling', 'xhr-polling',
								'iframe-xhr-polling', 'jsonp-polling' ]
								: [ transport ];
					}
					// 消息显示
					var log = function(sendusername, message) {
						if (sendusername == options.sendUserName) {
							return;
						}
						var $inbox = $("#inbox");
						var div = "<li>";
						div += '<a href="javascript:;">' + sendusername + ':';
						div += message + '</a>';
						div += "</li>";
						$inbox.append(div);

						while ($inbox.find("li").size() > 10) {
							$inbox.find("li:first").remove();
						}
					}
					// 消息发送
					var echo = function() {
						if (ws != null) {

							var message = $("#message").val();
							log(options.sendUserName, message);
							var acceptUserName = "";
							var chatID = ";"
							if ($('input[name="userid"]:checked').val() != null) {
								var acceptUserName = $(
										'input[name="userid"]:checked').val();
							}
							var msg = JSON.stringify({
								'send' : options.sendUserName,
								'accept' : acceptUserName,
								'msg' : message,
								"constants_chat" : options.chatID
							});
							// console.log(msg);
							ws.send(msg);
						} else {
							log('没有建立连接，请连接服务！');
						}
					}

					var bind = function() {
						$(document).delegate("button[name='echo']", "click",
								function() {
									echo();
								});

					}
					// 链接服务器
					connect();
					bind();

				}

			});

})(jQuery);