<style type="text/css">
.p_title{ text-align: center;padding:10px 0; margin: 10px 0; font-weight: bold; line-height: 36px;border-bottom: 1px solid #e1e1e1;}
.q_title{line-height: 20px;padding:5px 0; margin: 5px 0;  color: #444444;font-size: 15px;font-weight: bold;}
.q_body{ /*text-indent: 1em;*/}
.q_body > ul{list-style-type : ${listStyle!'decimal'};}
.q_body > ul > li{}
.q_ul{list-style-type : ${listStyle!'decimal'}; }
.input-group{text-indent: 1em;}
</style>

<div class="jumbotron">
  <h1>${survey.title!''}</h1>

</div>

<form class="form-horizontal" id="mcform" action="" role="form" method="post">
<#if survey.surveyPages??>
<#assign pages=survey.surveyPages>
	<#list pages as p>
		<#if p.name??>
		<div class="p_title">${p.name}</div>
		</#if>
		<#if p.sq??>
			<ul class="q_ul">
			<#assign sq=p.sq>
			<#list sq as q>
			<#if q??>
				<li>${analysisModel.answer(q, answersCount)}</li>
			</#if>
			</#list>
			</ul>
		</#if>
	</#list>
</#if>

</form>