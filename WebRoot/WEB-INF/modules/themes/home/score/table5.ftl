<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>符合性检查表</title>
<base href="${baseHref.webUrl }/">
<link rel="stylesheet" type="text/css" href="myfiles/score/css/style.css">
<link rel="stylesheet" type="text/css" href="myfiles/score/css/style.css" media="print">
</head>

<body>
<html>
<head>
<div class="table_box" id="table_box">
	<h1 class="tproject_title">符合性检查表</h1>
    <span class="project_number">竞争性评审项目编号：${project.type!''}</span>
    <span class="project_name">购买服务项目名称：${project.name!''}</span>
    <div class="table_main">
    	<table width="300" border="1">
			<thead>
                <tr>
              		<td height="60" colspan="3" align="left">响应人/省（市）</td>
                </tr>
       		</thead>
			<tbody>
                <tr>
                    <td height="30" colspan="3">代理服务商/省（市）</td>
                </tr>
                <tr>
                    <td width="33" rowspan="${standardNames['price']?size + 3}">响应报价</td>
                    <td height="30" colspan="2">响应报价</td>
                </tr>
                <tr>
                    <td height="30" colspan="2">响应声明（折扣或升、降价）</td>
                </tr>
                <tr>
                    <td height="30" colspan="2">响应总价（人民币）</td>
                </tr>
                <#if standardNames?? && standardNames['price']??>
	            <#assign index = 0>
	            <#list standardNames['price'] as sn>
	            <#if index == 0>
                <tr>
                    <td width="108" rowspan="${standardNames['price']?size}">响应总价构成</td>
                    <td width="137" height="30">${sn}</td>
                </tr>
                <#else>
	            <tr>
	              <td height="30">${sn}</td>
	            </tr>
	            </#if>
	            <#assign index =index+1>
	            </#list>
	            </#if>
          </tbody>
        </table>
        <#if scores??>
        <#list scores as  s>
        <ul class="table_list">
   	  	  <li><span>${s.enterprise.name}</span></li>
        	<li>${s.agentServiceName!''}</li>
        	<li>${s.responsePrice!''}</li>
        	<li>${s.responseLicense!''}</li>
        	<li>${s.responseMainPrice!''}</li>
        	<#if pssPrice?? && pssPrice[s.enterprise.id]??>
            <#list pssPrice[s.enterprise.id] as pm>
        	<li>${pm.standard!''}</li>
        	</#list>
            </#if>
        </ul>
        </#list>
      	</#if>
       
    </div>
    <div class="table_sign">
    	<span>各评委（签字）：</span>
        <strong>机关纪委（签字）：</strong>
    </div>
    <div class="table_note">
    	<h2>注：</h2>
        <ul>
        	<li>仅对“商务事项评议表”和“技术事项评议”同时合格者进行价格比较</li>
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
</body>
</html>
