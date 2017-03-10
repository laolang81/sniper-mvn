<div class="row">
	<div class="space-6"></div>

	<div class="col-sm-12 infobox-container">
		<!-- #section:pages/dashboard.infobox -->
		<#if posts??>
		<div class="infobox infobox-blue">
			<div class="infobox-icon">
				<i class="ace-icon fa fa-newspaper-o"></i>
			</div>
			<div class="infobox-data">
				<span class="infobox-data-number">${posts.total!''}</span>
				<div class="infobox-content">
					待处理 + <a href="admin/admin-post/?intStatus=0&type=&name=&submit=search" target="_blank">${posts.not!''}</a>
				</div>
			</div>
			
		</div>
		</#if>
		<#if comments??>
		<div class="infobox infobox-green">
			<div class="infobox-icon">
				<i class="ace-icon fa fa-comments"></i>
			</div>
			<div class="infobox-data">
				<span class="infobox-data-number">${comments.total}</span>
				<div class="infobox-content">留言 + ${comments.not}</div>
			</div>

			<!-- /section:pages/dashboard.infobox.stat -->
		</div>
		</#if>
		<#if calendars??>
		<div class="infobox infobox-pink">
			<div class="infobox-icon">
				<i class="ace-icon fa fa-calendar"></i>
			</div>
			<div class="infobox-data">
				<span class="infobox-data-number">${calendars.total}</span>
				<div class="infobox-content">今天：${calendars.totay}</div>
			</div>
		</div>
		</#if>
		

		<!-- /section:pages/dashboard.infobox -->
		<div class="space-6"></div>

		<!-- /section:pages/dashboard.infobox.dark -->
	</div>

	<div class="vspace-12-sm"></div>


</div>