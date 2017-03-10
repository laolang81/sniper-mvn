//url定义
//配料删除
var fooddelurl = 'company/trademark/territoryaddress';

/**
 * 菜谱的操作
 */
var foodBox = {};
foodBox.addressIds;
foodBox.ttid;

foodBox.setAddressIds = function(ids, id) {
	foodBox.addressIds = ids;
	foodBox.ttid = id;
}

/**
 * 处理商标和地址之间的关系
 */
foodBox.addressids = function(type, id) {

	var tempdata = foodBox.addressIds.val();

	var data = [];
	if (tempdata != undefined && tempdata != '') {
		data = tempdata.split(',');
	}

	switch (type) {
	case "add":
		// 检查id是否已经存在
		if (data.indexOf(id) == -1) {
			data.push(id);
		}
		break;
	case "del":
		var a = data.indexOf(id);
		data.splice(a, 1);
		break;
	}
	foodBox.addressIds.val(data.join(','));
}

inttodate = function (a){
	var newDate = new Date();
	newDate.setTime(a);
	var b = newDate.format('yyyy-MM-dd');
	return b;
}

foodBox.init = function(data) {
	var $food = $(".food");
	var ids = [];
	
	if (data != undefined) {
		for ( var i in data) {
			data[i].regdate = inttodate(data[i].regdate);
			data[i].usedate = inttodate(data[i].usedate);
			$('#tempdata').setTemplate($('#tpl').html()).processTemplate(data[i]);
			html = $('#tempdata').html();
			ids[i] = data[i].id;

			$food.append(html);
		}
		foodBox.addressIds.val(ids.join(","));
	}
}

// 当前元素,住了,辅料
foodBox.FoodAdd = function() {

	$('.dish').delegate('.addsc_item', 'click', function() {

		// 获取id
		// 获取当前ul最后一个id的数字
		var ul = $(this).parent().prev(".food");
		var data = {};
		data.address = "";
		data.regdate = "";
		data.usedate = "";
		data.master = "";
		data.id = "";

		$('#tempdata').setTemplate($('#tpl').html()).processTemplate(data);
		html = $('#tempdata').html();
		
		$(ul).append(html);
	})

}

// 配料删除
foodBox.FoodDel = function() {

	$('.food').delegate('.input_del', 'click', function() {
		var li = $(this).parent('li');
		foodid = li.find('input[name="id"]').val();
		console.log(foodid);
		foodid = parseInt(foodid);
		var sourceData = {};
		sourceData.id = foodid;

		console.log(foodid);
		var tma = sourceData;
		tma['type'] = "delete";
		if (foodid > 0) {
			$.post(fooddelurl, tma, function(data, textStatus) {
				if (data.id == 1) {
					li.remove();
					alert(data.msg);
				}
			}, 'json');

		}
	})
}

// 配料删除
foodBox.FoodSave = function() {

	$('.food').delegate('.input_save', 'click', function() {

		var $li = $(this).parents("li");
		var sourceData = getData(this);
		if (sourceData['address'] == '') {
			alert('地址不能为空');
			return;
		}
		if (sourceData['regdate'] == '') {
			alert('注册日期为空');
			return;
		}
		if (sourceData['usedate'] == '') {
			alert('使用日期为空');
			return;
		}
		if (sourceData['master'] == '') {
			alert('持有人为空');
			return;
		}

		console.log(sourceData);
		console.log($li.html());
		var tma = sourceData;
		tma['type'] = "add";
		
		$.post(fooddelurl, tma, function(data, textStatus) {
			if (data.id == 1 && data.address.id != '') {
				console.log(data.address.id);
				foodBox.addressids("add", data.address.id);
				$li.find('input[name="id"]').val(data.address.id);
				alert(data.msg);
			}
		}, 'json');

	})
}

var getData = function(obj) {
	var data = {};
	var $li = $(obj).parents('li');
	data['address'] = $li.find('input[name="address"]').val();
	data['regdate'] = $li.find('input[name="regdate"]').val();
	data['usedate'] = $li.find('input[name="usedate"]').val();
	data['master'] = $li.find('input[name="master"]').val();
	data['id'] = $li.find('input[name="id"]').val();
	if (foodBox.ttid.val() != '') {
		data['tmid'] = foodBox.ttid.val();
	}

	return data;
}