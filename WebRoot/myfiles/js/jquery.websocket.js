(function($){
	$.fn.extend({
		"chat" : function(options) {
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
		    var transports = ['websocket',
		                      'xdr-streaming',
		                      'xhr-streaming',
		                      'iframe-eventsource',
		                      'iframe-htmlfile',
		                      'xdr-polling',
		                      'xhr-polling',
		                      'iframe-xhr-polling',
		                      'jsonp-polling'] ;
            
            var setUrl = function (urlPath) {
                if (urlPath.indexOf('sockjs') != -1) {
                  url = urlPath;
                }
                else {
                  if (window.location.protocol == 'http:') {
                    url = 'ws://' + window.location.host + urlPath;
                  } else {
                    url = 'wss://' + window.location.host + urlPath;
                  }
                }
             }
			
            var connect1 = function ()
            {
            	// 用本身自带的websock无法链接
            	
            	if ('WebSocket' in window) {
                    ws = new WebSocket(options.socketUrl.replace("http","ws"));
                } else if ('MozWebSocket' in window) {
                    ws = new MozWebSocket(options.socketUrl.replace("http","ws"));
                } else {
                     ws = new SockJS(options.sockjsUrl, undefined, {protocols_whitelist: transports})
                }
            	
            	ws.onopen = function () {
			    	  log(null, '连接成功.');
			      };
			      //处理返回数据
			      ws.onmessage = function (event) {
			    	  var data = JSON.parse(event.data);
			    	  console.log(data);
			    	  if(data.send != null){
			    		  log(data.send,  data.msg);
			    	  }
			    	  if(data.users != null){
			    		  console.log(data.users);
				    	  //更新在线用户
				    	  users = data.users;
				    	  $("#users").empty();
				    	  for(var i in users){
				    		  if(users[i] == options.sendUserName){
				    			  continue;
				    		  }
				    		  botton = '<label class="btn btn-default">';
				    		  botton += '<input type="radio" name="userid" value="' + users[i] + '" autocomplete="off">' + users[i]
				    		  botton += '</label>'; 
				    		  //botton = '<button type="button" class="list-group-item">' + users[i] + '</button>';
				    		  $("#users").append(botton);
				    		  console.log(users[i]);
				    	  }
			    	  }
			    	 
			    	
			      };
			      ws.onclose = function (event) {
			    	  log(null, '断开连接.');
			    	  //log(event);
			      };
            }
            
            
			// 链接
			var connect = function () 
			{

			      ws = (options.sockjsUrl.indexOf('sockjs') != -1) ? 
			    		  new SockJS(options.sockjsUrl, undefined, {transports : transports}) : 
			    			  new WebSocket(options.sockjsUrl);
			              
			      ws.onopen = function () {
			    	  log('系统消息', '连接成功。');
			      };
			      //处理返回数据,当前页第一次连接时执行，发送消息不执行，接受消息执行
			      ws.onmessage = function (event) {
			    	  var data = JSON.parse(event.data);
			    	  //console.log('消息处理');
			    	  //console.log(data.users);
			    	  if(data.send != null){
			    		  log(data.send, data.msg);
			    	  }
			    	  if(data.users != null){
				    	  //更新在线用户
				    	  users = data.users;
				    	  $("#users").empty();
				    	  for(var i in users){
				    		  if(users[i] == options.sendUserName){
				    			  continue;
				    		  }
				    		  botton = '<label class="btn btn-default">';
				    		  botton += '<input type="radio" name="userid" value="' + users[i] + '" autocomplete="off">' + users[i]
				    		  botton += '</label>'; 
				    		  //botton = '<button type="button" class="list-group-item">' + users[i] + '</button>';
				    		  $("#users").append(botton);
				    	  }
			    	  }
			    	 
			    	
			      };
			      ws.onclose = function (event) {
			    	  log("系统消息", '断开连接。');
			    	  console.log(event);
			      };
			   }

			   var disconnect = function () 
			   {
			      if (ws != null) {
			        ws.close();
			        ws = null;
			      }
			   }
			    
			   var updateTransport = function (transport) 
			   {
			        
			       transports = (transport == 'all') ?  ['websocket',
			          'xdr-streaming',
			          'xhr-streaming',
			          'iframe-eventsource',
			          'iframe-htmlfile',
			          'xdr-polling',
			          'xhr-polling',
			          'iframe-xhr-polling',
			          'jsonp-polling'] : [transport];
			    }
			    //消息显示
			    var log = function (sendusername, message) 
			    {
			    	var leftOrRight = 'right';
			    	var leftOrRightCss = ' bg-info';
			    	if(sendusername != options.sendUserName){
			    		var leftOrRight = 'left';
			    		var leftOrRightCss = ' bg-success';
			    	}
			        var $console = $("#console");
			        var div = "<div>";
			        	div += '<p class="text-' + leftOrRight + '">' + sendusername + '</p>';
			        	div += '<p class="text-' + leftOrRight + leftOrRightCss + '">' + message + '</p>';
			        	div += "</div>"
			        $console.append(div);
			        while ($console.find("p").size() > 100) {
			          $console.find("p:first").remove();
			        }
			        $console.scrollTop($console.scrollTop() + $console.height());
			    }
			    //消息发送
			    var echo = function()
			    {
		    	      if (ws != null) {
		    	    	  
		    	        var message = $("#message").val();
		    	        log(options.sendUserName, message);
		    	        var acceptUserName = "";
		    	        var chatID = ";"
		    	        if($('input[name="userid"]:checked').val() != null){
		    	        	var acceptUserName = $('input[name="userid"]:checked').val();
		    	        }
		    	        var msg = JSON.stringify(
		    	        		{
		    	        			'send':options.sendUserName, 
		    	        			'accept':acceptUserName, 
		    	        			'msg': message , 
		    	        			"constants_chat": options.chatID
		    	        		}
		    	        );
		    	       console.log(msg);
		    	       ws.send(msg);
		    	     } else {
		    	    	 log('没有建立连接，请连接服务！');
		    	     }
			    }
			    
			    var bind = function ()
			    {
			    	$(document).delegate( "button[name='echo']","click", function () {
			    		echo();
			    	});
			    	
			    }
			    //链接服务器
			    connect();
			    bind();
			    
		}
			    
	});
	
})(jQuery);