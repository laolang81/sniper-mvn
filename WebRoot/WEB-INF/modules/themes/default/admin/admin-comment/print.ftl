<!DOCTYPE html><html lang="zh-cn">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script language="javascript">
function onprint(){
	window.print();
}
</script>
<style>
body{text-align: left; font-family:"", Arial; margin:0; padding:0; background: #FFF; font-size:14px; color:#000;}
div, form, img, ul, ol, li, dl, dt, dd, p{margin: 0; padding: 0; border: 0;}
li{list-style:none;}
h1, h2, h3, h4, h5, h6{margin:0; padding:0;}
table, td, tr, th{font-size:14px;}
.center{width:850px;margin:30px auto 0;}
.header{text-align:center}
.box{width:850px;border:1px #ccc solid; margin:15px auto;}
.box .title{background:#fff; line-height:35px;margin: 0 10px; color:#000; font-size:14px;border-bottom: 1px solid #000000;}
.box .content{ background:#FFFFFF; padding:10px 15px; line-height:25px; font-size:18px;color:#000}
.box .right{ padding:5px 10px;text-align: right; }
.box .content2{ background:#FFFFFF;margin: 0 30px; line-height:24px; font-size:14px; color:#FF0000;border-top: 1px solid #000000;}

</style>
</head>
<!--   -->
<body onload="onprint()">
<div class="center">
	<div class="header">
		<h2>山东国际商务网<br>网上互动信息受理单</h2>
		[流水号：${comment.id}-${.now?string("yyyyMMddhhmm")}]
	</div>
	<div class="box">
	    <div class="title">
	    	<strong>网友：</strong>${comment.user!''}&nbsp;&nbsp;
	    	<strong>时间：</strong>${comment.time?string("yyyy-MM-dd")}&nbsp;&nbsp; 
		   	<strong>IP：</strong>${comment.ip!''}
		   	<#assign type = comment.type?c>
		   	<#switch type>
		   		<#case "1">
		   			&nbsp;&nbsp;&nbsp;<strong>类型：</strong>咨询
		   			<#break>
		   		 <#case "2">
		   			&nbsp;&nbsp;&nbsp;<strong>类型：</strong>留言
		   			<#break>
		   		 <#case "3">
		   			&nbsp;&nbsp;&nbsp;<strong>类型：</strong><span style='background-color: #FFFF00;color:#000'>&nbsp;投诉</span>
		   			<#break>
		   		 <#case "6">
		   			&nbsp;&nbsp;&nbsp;<strong>类型：</strong><span style='background-color: #FFFF00;color:#000'>&nbsp;厅长信箱 </span>
		   			<#break>
		   		 <#default>
		   		 	&nbsp;&nbsp;&nbsp;<strong>类型：</strong>留言
		   	</#switch>
	   	</div>
    	<div class="content">
			<p>${comment.content?replace("\n","<br>")}</p>
			<p>[邮箱:${comment.email!''}&nbsp;&nbsp; 电话:${comment.tel!''}]</p>
      </div>
      <div class="content2">
	    <b>回复：</b>
		<div style="width:99%;height:300px" name="ctr_content"></div>
	</div>
	<div class=right>
		承办处室：<u>&nbsp;&nbsp;<b>
		<#if comment.type == 6>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<#else>
		<#if departments[comment.depId?c]??>${departments[comment.depId?c]}</#if>
		</#if>
		</b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</u>
		&nbsp;&nbsp;&nbsp;
		受理时间：
		<u>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</u>
		&nbsp;&nbsp;&nbsp;
		办结时间：
		<u>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</u>
	</div>
</div>
</body>
</html>