<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
  <title>Spring4  websocket实例</title>
  <meta charset="utf-8">
  </head>
<body>
  <style type="text/css">
    #connect-container {
      float: left;
      width: 100%
    }

    #connect-container div {
      padding: 5px;
    }

    #console-container {
      float: left;
      margin-left: 15px;
      width: 400px;
    }

    #console {
      border: 1px solid #CCCCCC;
      border-right-color: #999999;
      border-bottom-color: #999999;
      height: 300px;
      overflow-y: scroll;
      padding: 5px;
      width: 100%;
    }

    #console p {
      padding: 5px;
      margin: 0;
    }
    
     #console div {
      padding: 2px 0;
      margin: 3px 0;
      width: 100%;
    }
  </style>



<noscript>
	<h2 style="color: #ff0000">Seems your browser doesn't support Javascript! Websockets
  rely on Javascript being enabled. Please enable
  Javascript and reload this page!</h2></noscript>
<div class="row">
 	<div class="col-sm-2" data-toggle="buttons">
 		
 		<div class="list-group" data-toggle="buttons" id="users">
		 
		</div>
 		
 	</div>
 	<div class="col-sm-10">
	 <div class="well well-lg">
	 	<div id="console"></div>
	 </div>
	 <div id="connect-container">
	    <div>
	    <textarea id="message" class="form-control" style="width: 100%; height: 100px">测试消息!</textarea>
	    </div>
	    <div>
	    <p class="text-right"><button type="botton" name="echo" class="btn btn-default">发送</button></p>
	    </div>
	  </div>
	 </div>
</div>

<script type="text/javascript" src="myfiles/Plugin/sockjs-client/dist/sockjs.min.js"></script>
<script type="text/javascript" src="myfiles/js/jquery.websocket.js"></script>
<script type="text/javascript">
	$(function() {
		$().chat ({
			socketUrl : '${baseHref.baseHref }websocket',
			sockjsUrl : '${baseHref.baseHref }sockjs/websocket',
			chatID : '${chatid}',
			sendUserName : '${sessionusername}'
			
		});
	});
</script>
  
</body>
</html>