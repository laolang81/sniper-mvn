<style>
.material_manage {
    list-style: outside none none;
    padding: 0 20px;
    position: relative;
}
.material_manage li {
    background-color: #ffffff;
    border: 1px solid #dedede;
    border-radius: 2px;
    cursor: pointer;
    padding: 4px;
    width: 320px;
}
.material_manage li.inactive {
    opacity: 0;
    visibility: hidden;
}
.material_manage li img {
    display: block;
}
.material_manage li p {
    color: #666;
    font-size: 13px;
    font-weight: 200;
    line-height: 20px;
    margin: 7px 0 2px 7px;
    text-align: center;
}
.material_manage li p span {
    display: inline-block;
    line-height: 15px;
    margin: 2px 0;
}
</style>

<div class="seach">
	<form method="get" action="" role="form" class="form-inline" id="search">
		<div class="form-group">
			<label class="sr-only" for="">name</label>
			<input type="text" value="${search.name!''}" class="form-control" name="name" id="name"></div>
		<div class="form-group">
			<button value="search" name="submit" type="submit" class="btn btn-success">查询</button>
		</div>
	</form>
</div>

<#include "../public/sniper_menu_no.ftl">

<ul id="layer-photos" class="material_manage">
	<#if lists??>
	<#list lists as f>
	<li id="sl_${f.aid}">
	<img title="${f.atTime?string("yyyy-MM-dd")}" layer-pid="${f.aid }" layer-src="${freeMarkerUtils.show(f.filename,1024, 10240)!''}"
				width="310"  src="${freeMarkerUtils.show(f.filename,310, 1024)!''}" alt="${f.prefilename!''}"/>
	<p> 
		<input type="checkbox" name="list.id" value="${f.aid}" />
    	<a href="javascript:;" title="${f.prefilename!''}" class="">
    		<#if f.prefilename?? &&  f.prefilename?length gt 18>${f.prefilename?substring(0,18)}<#else>${f.prefilename!''}</#if>
    	</a>
    </p>
    <p>
    	<#if adminUser?? && adminUser.id?? && adminUser.id == f.uid>
	    <a href=".${baseHref.adminPath}/admin-files/update?id=${f.aid}" target="_blank" class="">(<i class="fa fa-pencil"></i> 编辑)</a>
	    </#if>
	    <a href="file-info/download?id=${f.aid}" target="_blank">(<i class="fa fa-download"></i>下载)</a>
	</p>
	
</li>
</#list>
</#if>
</ul>
<div class="row">
	<div class="col-xs-12">
		<!-- PAGE CONTENT BEGINS -->
		<div>
			<ul class="ace-thumbnails clearfix">
				<!-- #section:pages/gallery -->
				<li>
					<a href="" title="Photo Title" data-rel="colorbox">
						<img width="300" height="300" alt="150x150" src="http://shandongbusiness.gov.cn/public/attachment/kindeditor/image/20170217/s_e9d3991202703cee2e3a5a260aa9a9f0.jpg" />
					</a>
					<div class="tags">
						<span class="label-holder">
							<span class="label label-info">breakfast</span>
						</span>
	
						<span class="label-holder">
							<span class="label label-danger">fruits</span>
						</span>
	
						<span class="label-holder">
							<span class="label label-success">toast</span>
						</span>
	
						<span class="label-holder">
							<span class="label label-warning arrowed-in">diet</span>
						</span>
					</div>
					<div class="tools">
						<a href="#">
							<i class="ace-icon fa fa-link"></i>
						</a>
						<a href="#">
							<i class="ace-icon fa fa-paperclip"></i>
						</a>
						<a href="#">
							<i class="ace-icon fa fa-pencil"></i>
						</a>
						<a href="#">
							<i class="ace-icon fa fa-times red"></i>
						</a>
					</div>
				</li>
			</ul>
		</div>
	</div>
</div>
<div class="meneame">${pageHtml }</div>
<script type="text/javascript" src="myfiles/js/jquery.titlePreview.js"></script>
<script src="myfiles/Plugin/Wookmark/libs/jquery.imagesloaded.js"></script>
<script src="myfiles/Plugin/Wookmark/jquery.wookmark.min.js"></script>
<script src="myfiles/Plugin/layer/layer/layer.js"></script>
<script type="text/javascript">

	$(function(){
	 	$().snipermenu({
	 		div  : "#layer-photos",
	 		url : '${sniperUrl!''}'
	 	 });
	});
	//页面一打开就执行，放入ready是为了layer所需配件（css、扩展模块）加载完毕
	layer.ready(function() {
		//使用相册
		layer.photos({
			photos : '#layer-photos'
		});
	});

	(function($) {
		$('#layer-photos').imagesLoaded(function() {
			// Prepare layout options.
			var options = {
				itemWidth : 320, // Optional min width of a grid item
				autoResize : true, // This will auto-update the layout when the browser window is resized.
				container : $('#layer-photos'), // Optional, used for some extra CSS styling
				offset : 5, // Optional, the distance between grid items
				outerOffset : 10, // Optional the distance from grid to parent
				flexibleWidth : '320' // Optional, the maximum width of a grid item
			};

			// Get a reference to your grid items.
			var handler = $('#layer-photos li');

			var $window = $(window);
			$window.resize(function() {
				var windowWidth = $window.width(), newOptions = {
					flexibleWidth : '320'
				};

				// Breakpoint
				if (windowWidth < 1024) {
					newOptions.flexibleWidth = '320';
				}

				handler.wookmark(newOptions);
			});

			// Call the layout function.
			handler.wookmark(options);
		});
	})(jQuery);
</script>