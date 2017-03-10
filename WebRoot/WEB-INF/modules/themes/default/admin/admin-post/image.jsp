<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/subjectDate" prefix="sd"%>
<div class="seach">
	<form:form action="" role="form" cssClass="form-inline" method="get"
		modelAttribute="search">
		<div class="form-group">
			<label for="startDate" class="sr-only">开始时间</label>
			<form:input path="startDate" cssClass="form-control Wdate"
				id="startDate" />
			- <label for="endDate" class="sr-only">结束时间</label>
			<form:input path="endDate" cssClass="form-control Wdate" />
		</div>
		<c:choose>
			<c:when test="${indexMap != null }">
				<div class="form-group">
					<label for="group" class="sr-only">聚合</label>
					<form:select path="group" cssClass="form-control">
						<form:option value="0">选择</form:option>
						<form:options items="${indexMap }" />
					</form:select>
				</div>
			</c:when>
			<c:otherwise>
				<div class="form-group">
					<label for="siteidSelect" class="sr-only">处室</label>
					<form:select path="siteid" cssClass="form-control">
						<form:option value="0">选择</form:option>
						<form:options items="${departments }" />
					</form:select>
				</div>
			</c:otherwise>
		</c:choose>

		<div class="form-group">
			<label for="" class="sr-only">关键词</label>
			<form:input path="name" cssClass="form-control" />
		</div>
		<div class="form-group">
			<button class="btn btn-success" type="submit" name="submit"
				value="search">查询</button>
		</div>
	</form:form>
</div>


<style>
.item {
	padding: 50px;
	width: 100%;
	height: 100%;
	display: none;
}
</style>
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

.material_manage .yz_zy {
	background: url("myfiles/images/default/yinzhang_zy.png");
	width: 110px;
	height: 110px;
	position: absolute;
	left: 105px;
	position: absolute;
	top: 0px;
	z-index: 1000;
}

.material_manage .yz_pt {
	background: url("myfiles/images/default/yinzhang_pt.png");
	width: 110px;
	height: 110px;
	position: absolute;
	left: 105px;
	position: absolute;
	top: 0px;
	z-index: 1000;
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
<ul id="layer-photos" class="material_manage">
	<c:if test="${ lists != null}">
		<c:forEach items="${lists }" var="f">
			<li id="sl_${f.aid}"><c:if test="${f.mainsite ==1 }">
					<div class="yz_zy"></div>
				</c:if> <c:if test="${f.mainsite !=1 }">
					<div class="yz_pt"></div>
				</c:if> <img title=" ${f.atTime }" layer-pid="${f.aid }"
				layer-src="${systemConfig.imagePrefix}${f.filename }" width="310"
				src="${systemConfig.imagePrefix}${f.filename }" alt="${f.prefilename}" />
				<p>
					<input type="checkbox" name="list.id" value="${f.aid}" /> <a
						href="http://sdcom.gov.cn/public/html/news/201609/${f.subjects.sid}.html" title="${f.prefilename}"
						class=""> ${f.subjects.subject}</a>
				</p>
				<p>
					<a
						href=".${baseHref.adminPath}/admin-post/update?sid=${f.subjects.sid}"
						target="_blank" class="">(<i class="fa fa-pencil"></i> 编辑)
					</a>
				</p>
				<p>
					<input data-type="click" data-id="${f.aid }" id="mainsite${f.aid }"
						data-value="mainsite" data-url="file-info/setmainsite"
						type="checkbox" title="主站新闻,将会在首页幻灯片中出现"
						<c:if test="${f.mainsite == 1 }">checked="checked"</c:if>
						value="1"> <label for="mainsite${f.aid }">重要新闻</label>
				</p>
				<p>
					<input data-type="click" data-id="${f.aid }"
						id="isprimeimage${f.aid }" data-value="isprimeimage"
						data-url="file-info/setprimeimage" type="checkbox"
						title="图片新闻,将会出现在图片新闻中出现"
						<c:if test="${f.isprimeimage == 1 }">checked="checked"</c:if>
						value="1"> <label for="isprimeimage${f.aid }">普通新闻</label>
				</p></li>
		</c:forEach>
	</c:if>
</ul>
<div class="meneame">${pageHtml }</div>
<script type="text/javascript" src="myfiles/js/jquery.titlePreview.js"></script>
<script src="myfiles/Plugin/Wookmark/libs/jquery.imagesloaded.js"></script>
<script src="myfiles/Plugin/Wookmark/jquery.wookmark.min.js"></script>
<script src="myfiles/Plugin/layer/layer/layer.js"></script>
<script type="text/javascript">
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
<script type="text/javascript"
	src="${baseHref.baseHref }myfiles/Plugin/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="myfiles/js/jquery.items.select.js"></script>
<script type="text/javascript"
	src="myfiles/js/jquery.sniper.clickPost.js"></script>
<script type="text/javascript">
	//处室栏目操作
	var deprtid = '${search.siteid}';
	var itemup = '${search.itemid}';
	$().siteid({
		post : deprtid,
		selected : itemup,
		name : 'itemid',
		gettrue : 1

	});

	$(document).delegate('.Wdate', 'click', function() {
		WdatePicker({
			dateFmt : 'yyyy-MM-dd'
		})
	});

	ClickPost.init({
		click : ''
	});
</script>