
<dl class="dl-horizontal">
<#if osValues??>
<#list osValues?keys as ov>
  <dt>${keys[ov]!ov}</dt>
  <dd><#switch ov>
  		<#case "time">
  		<#case "vmStartTime">
		${osValues[ov]?string("yyyy-MM-dd hh:mm:ss")}      		
  		<#break>
  		<#default>
  		${osValues[ov]!'--'}
  	 </#switch>
  </dd>
</#list> 
</#if>
  
</dl>