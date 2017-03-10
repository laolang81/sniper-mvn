<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta charset="utf-8">
<title>登录</title>
<base href="<#if baseHref??>${baseHref.baseHref!''}"></#if>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="IE=edge">

<script type="text/javascript" src="myfiles/js/jquery-1.11.1.min.js"></script>
<script type="text/javascript" src="myfiles/Plugin/bootstrap/js/bootstrap.min.js"></script>

<link href="myfiles/Plugin/bootstrap/css/bootstrap.min.css" media="screen" rel="stylesheet" type="text/css">
<link href="myfiles/Plugin/bootstrap/css/bootstrap-theme.min.css" media="screen" rel="stylesheet" type="text/css">

<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
	<script src="myfiles/Plugin/bootstrap/js/html5shiv.min.js"></script>
<![endif]-->
<link rel="stylesheet" type="text/css" href="myfiles/www/css/style.css">
<link rel="stylesheet" type="text/css"
	href="myfiles/www/css/font-awesome.min.css">
<script src="myfiles/www/script/jquery-1.12.2.min.js"></script>
<script src="myfiles/www/script/script.js"></script>


<style type="text/css">
.sform-signin {
	margin: 0 auto;
	max-width: 330px;
	padding: 15px;
}
.login_verify{}
.login_verify input{padding:0 5px; border-left: 1px solid #ccc;border-right: 1px solid #ccc;}
.login_verify img{height:30px;}
</style>
</head>
<body>
	<div class="container-fluid" style="padding:0px;">
	<div class="login_box">
		<div class="login_win">
	    	<div class="login_ico">
	        	<img src="myfiles/www/images/login_ico.png" width="131" height="88" alt=""/>
	        	<h4>登陆商务网</h4>
	        </div>
	        <form data-status=''  class="form-signin" role="form" name="login" action="" method="post">
		        <div class="login_user">
		        	<span>用户名：</span><input type="text" size="40" name="username">
		        </div>
		      	<div class="login_password">
		        	<span>密&nbsp;&nbsp;&nbsp;码：</span><input type="password" size="40" name="password">
		        </div>
		        <#if loginNum?? && loginNum  gt 2>
		        <div class="login_verify login_password">
		        	<span>验证码：</span><input type="text" size="20" name="verifyCode">
		        	<img src="verify">
		        </div>
		        </#if>
		        <div class="login_button">
		        	<input type="submit" value="&nbsp;">
		        </div>
	        </form>
	  </div>
	</div>
<script type="text/javascript">
	$(document).ready(function(){
		function docu_size(){
		var login_height = $(document).height();
		$(".login_box").height(login_height)
		$(".login_box h2").css("margin-top",login_height/4);
		};
		docu_size()
		$(window).resize(function(){
		  docu_size()
		});
	});
	
	$(function() {
		$('form img').click(function() {
			fleshVerify();
		});
	});
		
	function fleshVerify() {
		var timenow = new Date().getTime();
		var src = $('form img').attr("src");
		var indexof = src.indexOf("?");
		if (indexof != -1) {
			src = src.substring(0, src.indexOf("?"));
		}
		$('form img').attr("src", src + '?d=' + timenow);
	}
</script>
</body>
</html>