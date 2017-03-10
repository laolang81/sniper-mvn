<title>登录 | </title>
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
</script>

<div class="login_box">
	<div class="login_win">
    	<div class="login_ico">
        	<img src="myfiles/www/images/login_ico.png" width="131" height="88" alt=""/>
        	<h4>登陆商务网</h4>
        </div>
        <form data-status=''  class="form-signin" role="form" name="login" action="login" method="post">
        <div class="login_user">
        <span>用户名：</span><input type="text" size="40" name="username">
        </div>
      <div class="login_password">
        <span>密&nbsp;&nbsp;&nbsp;码：</span><input type="password" size="40" name="password">
        </div>
        <div class="login_text">
        <span><!--<input type="checkbox" name="rememberMe" value="true">--></span>
        <a href="javascript:void(0)">${loginError!''}</a>
        </div>
        <div class="login_button">
        	<input type="submit" value="&nbsp;">
        </div>
        </form>
  </div>
</div>
