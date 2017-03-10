<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script language="javascript">
function onprint(){
	window.print();
}
</script>
<style>
body {
	text-align: left;
	font-family: "", Arial;
	margin: 0;
	padding: 0;
	background: #FFF;
	font-size: 14px;
	color: #000;
}

div,form,img,ul,ol,li,dl,dt,dd,p {
	margin: 0;
	padding: 0;
	border: 0;
}

li {
	list-style: none;
}

h1,h2,h3,h4,h5,h6 {
	margin: 0;
	padding: 0;
}

table,td,tr,th {
	font-size: 14px;
}

.center {
	width: 850px;
	margin: 30px auto 0;
}

.header {
	text-align: center
}

.box {
	width: 850px;
	border: 1px #ccc solid;
	margin: 15px auto;
}

.box .title {
	background: #fff;
	line-height: 35px;
	margin: 0 10px;
	color: #000;
	font-size: 14px;
	border-bottom: 1px solid #000000;
}

.box .content {
	background: #FFFFFF;
	padding: 10px 15px;
	line-height: 25px;
	font-size: 18px;
	color: #000
}

.box .right {
	padding: 5px 10px;
	text-align: right;
}

.box .content2 {
	background: #FFFFFF;
	margin: 0 30px;
	line-height: 24px;
	font-size: 14px;
	color: #FF0000;
	border-top: 1px solid #000000;
}

.openApply {
	
}

.openApply tr {
	
}

.openApply td {
	padding: 5px;
}
</style>
</head>
<!--   -->
<body onload="onprint()">
	<div class="center">
		<div class="header">
			<h2>
				山东国际商务网<br>申请公开
			</h2>
		</div>
		<div class="box">
			<table style="width: 100%;" cellpadding="0" cellspacing="0"
				border="1" bordercolor="#000000" class="openApply">
				<tbody>
					<tr>
						<td rowspan="10">申请人信息</td>
						<td rowspan="5">公民</td>
						<td>姓名</td>
						<td>${comment.username!''}</td>
						<td>工作单位</td>
						<td>${comment.workunit!''}</td>
					</tr>
					<tr>
						<td>证件名称</td>
						<td>${comment.idname!''}</td>
						<td>证件号码</td>
						<td>${comment.idnum!''}</td>
					</tr>
					<tr>
						<td>通讯地址</td>
						<td colspan="3">${comment.address!''}</td>
					</tr>
					<tr>
						<td>联系电话</td>
						<td>${comment.tel!''}</td>
						<td>邮政编码</td>
						<td>${comment.postcode!''}</td>
					</tr>
					<tr>
						<td>电子邮箱</td>
						<td colspan="3">${comment.email!''}</td>
					</tr>
					<tr>
						<td rowspan="5">法人或者其他组织</td>
						<td>名称</td>
						<td>${comment.frname!''}</td>
						<td>组织机构代码</td>
						<td>${comment.frcode!''}</td>
					</tr>
					<tr>
						<td>营业执照</td>
						<td colspan="3">${comment.frzhizhao!''}</td>
					</tr>
					<tr>
						<td>法人代表</td>
						<td>${comment.frdaibiao!''}</td>
						<td>联系人</td>
						<td>${comment.frmaster!''}</td>
					</tr>
					<tr>
						<td>联系人电话</td>
						<td colspan="3">${comment.frtel!''}</td>
					</tr>
					<tr>
						<td>联系人邮箱</td>
						<td colspan="3">${comment.fremail!''}</td>
					</tr>
					<tr>
						<td rowspan="8">所需信息情况</td>
						<td>所需信息内容描述</td>
						<td colspan="4">${comment.content!''}</td>
					</tr>
					<tr>
						<td colspan="5" style="text-align: center;">选填部分</td>
					</tr>
					<tr>
						<td colspan="2">所需信息的信息索取号</td>
						<td colspan="3">${comment.xtnum!''}</td>
					</tr>
					<tr>
						<td colspan="2">所需信息的用途</td>
						<td colspan="3">${comment.xtuse!''}</td>
					</tr>
					<tr>
						<td colspan="2">是否减免费用</td>
						<td>信息的指定提供方式(可多选)</td>
						<td colspan="2">获取信息方式(可多选)</td>
					</tr>
					<tr>
						<td colspan="2">${comment.xtmoney!''}</td>
						<td>
							<#if comment.xttype??>
							<#assign xttype = comment.xttype?split("|")>
							<#list xttype as x>
							<p>${x}</p>
							</#list>
							</#if>
						</td>
						<td colspan="2">
							<#if comment.xtinfo??>
							<#assign xtinfo = comment.xtinfo?split("|")>
							<#list xtinfo as x>
							<p>${x}</p>
							</#list>
							</#if>
						</td>
					</tr>
					<tr>
						<td colspan="5" style="text-align: center;">(<#if comment.xtothertype?? && comment.xtothertype == 'true'>是<#else>否</#if>)若本机无法按照指定方式提供所需信息,也可以接受其他方式</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
</body>
</html>