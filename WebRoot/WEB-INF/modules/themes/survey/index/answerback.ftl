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
							<#if answers?? && answers[q.id?c]??>
								<li>${surveyModel.answer(q, answers[q.id?c])}</li>
							<#else>
								<li>${surveyModel.answer(q, null)}</li>
							</#if>
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
						<#if answers?? && answers[q.id?c]??>
							<li>${surveyModel.answer(q, answers[q.id?c])}</li>
						<#else>
							<li>${surveyModel.answer(q, null)}</li>
						</#if>
					</#if>
				</#list>
			</#if>
		</#list>
		</ul>
	</#if>
</#if>
<div class="form-group">
	<div class="col-sm-12 text-center">
		<#if survey.openResult?? && survey.openResult == true>
			<a href="analysis?id=${survey.id}" target="_blank">查看统计结果</a>
		</#if>
	</div>
</div>
</form>