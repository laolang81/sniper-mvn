<title>${survey.title!''}</title>
<style type="text/css">
.p_title{ text-align: center;padding:10px 0; margin: 10px 0; font-weight: bold; line-height: 36px;border-bottom: 1px solid #e1e1e1; font-size: 24px;}
.q_title{line-height: 20px;padding:5px 0; margin: 5px 0;  color: #444444;font-size: 15px;font-weight: bold;}
.q_body{ /*text-indent: 1em;*/}
.q_body > ul{list-style-type : ${listStyle!'decimal'};}
.q_body > ul > li{}
.q_ul{list-style-type : ${listStyle!'decimal'}; }
.input-group{text-indent: 1em;}
.jumbotron p{font-size:18px;}
</style>
				
				
<div class="jumbotron">
  <h1 style="font-size: 26px; text-align: center;">${survey.title!''}</h1>
  <div style="font-size:18px">${survey.note!''}</div>
</div>

<#if resultErrors ?? && resultErrors?size gt 0>
<div class="alert alert-warning alert-dismissible" role="alert">
  <button type="button" class="close" data-dismiss="alert"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
  <ul>
  	<#list resultErrors?keys as r>
  	<#assign rs = resultErrors[r]>
  	<#list rs as e>
  	<li>${e}</li>
  	</#list>
  	</#list>
  </ul>
</div>
</#if>

<form class="form-horizontal" id="mcform" action="" role="form" method="post">

<#if survey.surveyPages??>
	<#assign pages=survey.surveyPages>
	<#if survey.pageTitle?? && survey.pageTitle == true>
		<#list pages as p>
			<#if p.name??>
			<div class="p_title">${p.name}</div>
			</#if>
			<#if p.sq??>
				<ul class="q_ul">
				<#assign sq=p.sq>
				<#list sq as q>
				<#if q??>
					<li>${surveyModel.answer(q, null)}</li>
				</#if>
				</#list>
				</ul>
			</#if>
		</#list>
	<#else>
		<ul class="q_ul">
		<#list pages as p>
			<#if p.sq??>
				<#assign sq=p.sq>
				<#list sq as q>
				<#if q??>
					<li>${surveyModel.answer(q, null)}</li>
				</#if>
				</#list>
			</#if>
		</#list>
		</ul>
	</#if>
</#if>

<div class="form-group">
	<div class="col-sm-12 text-center">
		<#if allow == true>
		<#if survey.password != ''>
		 密码 : 
		<input type="password"  name="password" id="password">
		</#if>
		</#if>
	</div>
</div>

<div class="form-group">
	<div class="col-sm-12 text-center">
		<#if allow == true>
		<button type="submit" class="btn btn-danger">${survey.submitName!'完成问卷'}</button>
		</#if>
		<#if survey.openResult?? && survey.openResult == true>
			<a href="survey/analysis?id=${survey.id}" target="_blank">查看统计结果</a>
		</#if>
	</div>
</div>


</form>

<link href="${baseHref.baseHref }myfiles/Plugin/jQuery-Validation-Engine/css/validationEngine.jquery.css" media="screen" rel="stylesheet" type="text/css">

<script type="text/javascript" src="${baseHref.baseHref }myfiles/js/jquery.from.js"></script>
<script type="text/javascript" src="${baseHref.baseHref }myfiles/Plugin/jQuery-Validation-Engine/js/jquery.validationEngine.js"></script>
<script type="text/javascript" src="${baseHref.baseHref }myfiles/Plugin/jQuery-Validation-Engine/js/languages/jquery.validationEngine-zh_CN.js"></script>
<script type="text/javascript" src="${baseHref.baseHref }myfiles/js/jquery.sniper.answer.js"></script>
<script type="text/javascript">

var loadi;
$().ready(function(){
	$().sniperAnswer();
	
	var options = { 
        //target:        '#output',
        beforeSubmit:  showRequest,
        success:       showResponse,
        // other available options:      
        type:      'post', 
        dataType:  'json',
        clearForm: false,
        timeout:   5000 
    }; 
	
    $('#mcform').submit(function() {
       	 $(this).ajaxSubmit(options); 
         return false; 
    }); 
    
})


// pre-submit callback 
function showRequest(formData, jqForm, options) { 
	
	if($("#mcform").validationEngine('validate')){
		if(confirm("提交问卷,确定?")){
			loadi = layer.load('稍等...');
			return true;
		}
	}
    return false;
} 
// post-submit callback 
function showResponse(responseText, statusText, xhr, $form)  
{ 
	//关闭所有窗口
	layer.close(loadi);
	if(statusText=='success'){
		for(var i in responseText){
			alert(responseText[i]);
		}
		
		$(document).scrollTop(0);
		//setTimeout("window.location.reload()",1000);
		
	}else{
		layer.msg('Network blocked', 2, -1);
	}
}
</script>