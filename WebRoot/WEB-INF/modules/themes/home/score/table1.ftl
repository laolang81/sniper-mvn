<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>竞争性评审基本情况一览表</title>
<base href="${baseHref.webUrl }/">
<link rel="stylesheet" type="text/css" href="myfiles/score/css/style.css">
<link rel="stylesheet" type="text/css" href="myfiles/score/css/style.css" media="print">
</head>

<body>
<div class="table_box" id="table_box">
	<h1 class="tproject_title">竞争性评审基本情况一览表</h1>
    <span class="project_number">竞争性评审项目编号：${project.type!''}</span>
    <span class="project_name">购买服务项目名称：${project.name!''}</span>
    <div class="table_main">
    	<table style="width:100%;" border="1">
        	<thead>
                <tr>
                  <td align="center">序号</td>
                  <td align="center">响应人</td>
                  <td align="center">省/市</td>
                  <td align="center">报价方式</td>
                  <td align="center">响应价格（人民币）</td>
                  <td align="center">完成时限</td>
                  <td align="center">备注</td>
                </tr>
            </thead>
            <tbody>
            	<#if scores??>
            	<#assign index = 1>
            	<#list scores as  s>
                <tr>
                  <td>${index}</td>
                  <td>${s.enterprise.name}</td>
                  <td>${s.address!''}</td>
                  <td>${s.priceType!''}</td>
                  <td>${s.price!''}</td>
                  <td>${s.endDate!''}</td>
                  <td>${s.note!''}</td>
                </tr>
                <#assign index=index+1>
                </#list>
                </#if>
            </tbody>
        </table>

    </div>
    <div class="table_sign">
    	<span>各评委（签字）：</span>
        <strong>机关纪委（签字）：</strong>
    </div>
    <div class="table_note">
    	<h2>注：</h2>
        <ul>
        	<li>响应人按评议顺序填写。</li>
        	<li></li>
        </ul>
    </div>
</div><!--
<script>
function myPrint(obj){
    var newWindow=window.open("打印窗口","_blank");//打印窗口要换成页面的url
    var docStr = obj.innerHTML;
    newWindow.document.write(docStr);
    newWindow.document.close();
    newWindow.print();
    newWindow.close();
}
</script>
<button onclick="myPrint(document.getElementById('table_box'))">打 印</button>-->
</body>
</html>