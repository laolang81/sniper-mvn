var filesBox = {};

filesBox.iframe = false;



/**
 * 绑定要上传的组件
 */
filesBox.band = function() {
	$(document).delegate('.fileupload', 'click', function() {
		filesBox.upload(this);
	})
};

filesBox.delFile = function() {
	$(document).delegate('.delFile', 'click', function() {
		filesBox.del(this);
	})
};

filesBox.setData = function(sourceData, newData, type) {
	var data = [];
	if (sourceData != undefined && sourceData != '') {
		data = sourceData.split(',');
	}
	switch (type) {
	case "add":
		// 检查id是否已经存在
		if (data.indexOf(newData) == -1) {
			data.push(newData);
		}
		break;
	case "del":
		var a = data.indexOf(newData);
		data.splice(a, 1);
		break;
	}
	return data.join(',');
}
/**
 * 删除
 */
filesBox.del = function(obj) {
	var id = $(obj).parent().attr('id');
	var $hidden = $(obj).parents(".uploadFiles").prev().prev().prev();
	console.log($hidden.val());
	// 保存答案
	var resultIDs = filesBox.setData($hidden.val(), id, 'del');
	$.post("file-info/deleteFileByID", {
		id : id
	}, function(result) {
		if (result.code == 200) {
			$hidden.val(resultIDs);
			$(obj).parent().remove();
		}
	})

};

filesBox.upload = function(obj) {
	// 上传元素
	var $fileupload = $(obj);
	// 存在文件id
	var $hidden = $fileupload.prev();
	// 存在文件列表
	var $uploadFiles = $fileupload.next().next();

	var $bar = $fileupload.next().find('.progress-bar');
	$fileupload
			.fileupload({
				dataType : 'json',
				formData : {
					dir : "file"
				},
				iframe : filesBox.iframe,
				maxFileSize : -1,
				done : function(e, data) {
					$bar.css('width', 0 + '%');
					$bar.attr('aria-valuenow', 0);
					$bar.html(0 + "%");
					layer.msg('上传完毕');
					// 保存答案
					var result = filesBox.setData($hidden.val(),
							data.result.id, 'add');
					$hidden.val(result);

					var li = '<li id="' + data.result.id + '">';
					li += data.result.oldName;
					li += '<span class="delFile">(删除)</span>';
					li += '</li>';

					$uploadFiles.append(li);

				},
				progressall : function(e, data) {
					var progress = parseInt(data.loaded / data.total * 100, 10);
					$bar.css('width', progress + '%');
					$bar.attr('aria-valuenow', progress);
					$bar.html(progress + "%");
				},

			});

}
