<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>商业事项评议表</title>
<base href="${baseHref.webUrl }/">
<link rel="stylesheet" type="text/css" href="myfiles/score/css/style.css">
<link rel="stylesheet" type="text/css" href="myfiles/score/css/style.css" media="print">
</head>
<body>
<div class="table_box_1" id="table_box">
	<h1 class="tproject_title">商业事项评议表</h1>
    <span class="project_number">竞争性评审项目编号：${project.type!''}</span>
    <span class="project_name">购买服务项目名称：${project.name!''}</span>
    <div class="table_main">
    	<table width="500" border="1">
            <thead>
            <tr>
              <td height="60" colspan="2" align="center">评议内容</td>
              </tr>
       		</thead>
          <tbody>
            <tr>
              <td height="30" colspan="2">响应人的合格性</td>
            </tr>
            <tr>
              <td width="118">响应的有效性</td>
              <td width="166" height="30">是否由法定代表人或授权代表签署</td>
            </tr>
            <tr>
              <td rowspan="5">资格证明文件</td>
              <td height="30">资格声明</td>
            </tr>
            <tr>
              <td height="30">服务商资格声明</td>
            </tr>
            <tr>
              <td height="30">作为代理的服务商的资格声明（如适用）</td>
            </tr>
            <tr>
              <td height="30">服务商授权书</td>
            </tr>
            <tr>
              <td height="30">营业执照</td>
            </tr>
            <tr>
              <td height="30" colspan="2">经营范围</td>
            </tr>
            <tr>
              <td height="30" colspan="2">业绩</td>
            </tr>
            <tr>
              <td height="30" colspan="2">完成时限期</td>
            </tr>
            <tr>
              <td height="30" colspan="2">服务质量保证期</td>
            </tr>
            <tr>
              <td height="30" colspan="2">付款条件和方式</td>
            </tr>
            <tr>
              <td height="30" colspan="2">适用法律</td>
            </tr>
            <tr>
              <td height="30" colspan="2">仲裁</td>
            </tr>
            <tr>
              <td height="30" colspan="2">其他</td>
            </tr>
            <tr>
              <td height="30" colspan="2">结论</td>
            </tr>
          </tbody>
        </table>
        <#if scores??>
        <#assign index = 1>
        <#list scores as  s>
        <ul class="table_list">
   	  		<li><span>${s.enterprise.name}</span></li>
        	<li>${s.responsePerson!''}</li>
        	<li>${s.signedLegal!''}</li>
        	<li>${s.qualState!''}</li>
        	<li>${s.serviceState!''}</li>
        	<li>${s.serviceAgentState!''}</li>
        	<li>${s.serviceAuth!''}</li>
        	<li>${s.businessLicense!''}</li>
        	<li>${s.businessScope!''}</li>
        	<li>${s.achievement!''}</li>
        	<li>${s.completionTime!''}</li>
        	<li>${s.serviceDate!''}</li>
        	<li>${s.paymentTermsMethods!''}</li>
        	<li>${s.useLaw!''}</li>
        	<li>${s.arbitration!''}</li>
        	<li>${s.other!''}</li>
        	<li>${s.businessConclusion!''}</li>
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
        	<li>结论填写“合格”或“不合格”。</li>
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