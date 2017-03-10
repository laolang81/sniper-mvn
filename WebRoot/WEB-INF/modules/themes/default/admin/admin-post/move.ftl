
<div class="seach" style="text-align: center;">
	<form method="post" action="" role="form" class="form-inline" id="search">
		<div class="form-group">
			<label class="sr-only" for="siteidSelect">处室</label>
			<select class="form-control" name="siteid" id="siteidSelect">
			<#if deps??>
				<#list deps?keys as d>
				<option value="${d}">${deps[d]}</option>
				</#list>
			</#if>
			</select>
		</div>
		<div class="form-group">
			<label class="sr-only" for="siteidSelect">处室</label>
			<select class="form-control" name="siteidMove" id="siteidSelectMove">
				<#if deps??>
				<#list deps?keys as d>
				<option value="${d}">${deps[d]}</option>
				</#list>
			</#if>
			</select>
		</div>
		
		<div class="form-group">
			<button value="search" name="submit" type="submit" class="btn btn-success">确定</button>
		</div>
	</form>
	<p class="text-danger text-center">批量和移动文章首栏目，不可恢复.</p>
	<#if itemidCount??>
	<p class="text-danger text-center">处室修改记录${itemidCount!'0'}，栏目修改记录${siteidCount!'0'}</p>
	</#if>
</div>

<script type="text/javascript" src="myfiles/js/jquery.items.select.js"></script>
<script type="text/javascript">
	//处室栏目操作
	var deprtid = '${search.siteid!''}';
	var itemup = '${search.itemid!''}';
	$().siteid({
		post:$('#siteidSelect').val(),
		selected : 0,
		changeselect:'siteidSelect',
		name : 'itemid',
		gettrue : 1

	});
	
	
	$().siteid({
	post:$('#siteidSelectMove').val(),
	selected:0,
	changeselect:'siteidSelectMove',
	selectselect:'itemidSelectMove',
	name:'itemidMove',
	gettrue : 1
});
	
</script>