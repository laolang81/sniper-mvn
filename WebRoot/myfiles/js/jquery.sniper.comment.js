/**
 * 用户列表也的所有操作
 */
(function ($) {
   $.fn.extend({
        "sniperComment": function (options) {
            //设置默认值
            options = $.extend({
            	//class，统一class
            	bindname: '.oper',
            	//div name
            	div: '.gbook',
            	url : ''
            }, options);
            
            $(document).delegate( options.bindname,"click", function () {
            	start(this);
            })
            //初始化ul
            function start(obj){ 
            	
            	var sendData = {};
            	//获取当前操作框的顶级 li
            	var objLi		= $(obj).parents('li');
            	// id 是必须得
            	sendData['id']  = objLi.attr('id');
            	url				= objLi.attr('data-url');
            	sendData['url']  = url;
            	// 处理一些变量
            	getVar(obj, sendData, objLi);
            	if(sendData['action'] =='Replay' && !sendData['replay']){
            		alert('回复不得为空');
            		return false;
            	}     	
            	operAction(sendData);	
            	
            }
            //获取变量
           var getVar =  function (obj, sendData, objLi)
            {         	
            	info	= $(obj).attr('title');
            	action	= $(obj).attr('data-action');
            	oper	= $(obj).attr('data-oper');
            	if($(obj).attr('data-url')){
            		url	= $(obj).attr('data-url');
            		sendData['url']  = url;
            	}
            	// 操作识别
            	sendData['action']  = action;
            	sendData['info']  = info;
            	sendData['oper']  = oper;
            	
            	switch(action){
            		case "Enable":
            			if($(obj).prev('input[type="checkbox"]').prop("checked") == true){
            				display = false;
            			}else{
            				display = true;
            			}
            			// 是否显示
            			sendData['display']  = display;
            			break;
            		case 'Reply':
            			replay	= $(objLi).find("textarea").val();
            			sendData['replay']  = replay;
            			break;
            	}
            	
            }
            
            var operAction = function (sendData)
            {
            	switch(sendData['oper']){
	        		//跳转的，包含编辑
	            	case '1':         		
	            		window.open(url, "_blank");           		
	            		break;
	            		//非跳转，及当前页操作的，包含删除，审核，等等
	            	case '2':          		
	            		deleteAndReplyHandle(sendData);
	            		break;
	            	case '3':
	            		//
	            		displayHandle(sendData);
	            		break;           	
	        	}  
            }      
            // 留言显示处理
            var displayHandle = function(sendData)
            {
            	delete sendData.oper;
            	$.post(url, sendData,
            			function (data, textStatus){               	    		
        	    	}, "json");
            }
             
            
            /**
             * 包含删除，回收站, 留言回复
             */
            function deleteAndReplyHandle(sendData)
            {	       	
            	info = sendData['info'];
            	url = sendData['url'];
            	
            	delete sendData.oper;
            	delete sendData.info;
            	delete sendData.url;
            	
            	// 询问框
				var confirm = layer.confirm(
						info, {
							btn : [ '取消', '删除' ]
						// 按钮
						}, function() {
							layer.close(confirm);
						}, function() {
							$.post(url
									, sendData 
									, function(data, textStatus) {
								if(data.msg==1){          	    			
	            	    			switch(sendData['action']){
	            		    			case 'Delete':
	            		    				//删除
	            		    				$('#' + sendData['id']).remove();
	            					  		break;
	            		    			case 'DeleteTrue':
	            		    				$('#' + sendData['id']).remove();
	            		    				break;
	            					  	default:
	            					  		
	            					  		break;
	            	    			}
	            			   	}   
							}, 'json');
							layer.close(confirm);
				});
				
            	
            	
            }       
        }            
    });
})(jQuery);