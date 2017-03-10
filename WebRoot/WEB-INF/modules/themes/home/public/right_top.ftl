<div class="search">
	<div class="search_title"><i class="fa fa-search"></i>Content search</div>
	<div class="search_input">
		<form action="search" method="get">
		<input type="text" class="search_input_text" name="name" value="<#if search??>${search.name!''}</#if>">
		<input type="submit" class="search_input_button" value="GO">
		</form>
	</div>
    <span class="search_e"></span>
</div>

<div class="Shandong_overview">
	<h2>Shandong Overview</h2>
    <p>This is the place where Confucius and Mencius were given birth to, where Mount Tai rose up, where the five-thousand-year Chinese civilization originated, where the Yellow River rushes to the arms of the sea, where Olympic sailing games staged, and where giant companies as Haier and Qingdao Beer go global.</p>
    <a href="http://www.shandongbusiness.gov.cn/public/zhuanti/inshandong/" target="_blank">Learn More</a>
</div>

<div class="Service">
	<h2>Services</h2>
	<#if servicePosts??>
	<ul>
	<#list servicePosts as sp>
		<li>
    		<a href="news/${sp.id}" target="_blank" title="${sp.name}">${sp.name}</a>
		</li>
	</#list>
	</ul>
	</#if>
</div>