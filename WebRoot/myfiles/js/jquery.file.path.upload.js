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

filesBox.upload = function(obj) {
	// 上传元素
	var $fileupload = $(obj);
	// 存在文件id
	var $hidden = $fileupload.prev();
	var $img = $fileupload.parent().find(".help-block");
	var $bar = $fileupload.next().find('.progress-bar');
	$fileupload
			.fileupload({
				dataType : 'json',
				formData : {
					dir : "image"
				},
				iframe : filesBox.iframe,
				maxFileSize : -1,
				done : function(e, data) {
					$bar.css('width', 0 + '%');
					$bar.attr('aria-valuenow', 0);
					$bar.html(0 + "%");
					layer.msg('上传完毕');
					// 保存答案
					
					if($img.find("img").size() != 0){
						$img.find("img").attr("src", data.result.fileShotPath);
					}else{
						$imgSrc = $("<img/>");
						$imgSrc.attr("src", data.result.url);
						$imgSrc.css("maxHeight",200);
						$img.append($imgSrc);
						
					}
					$hidden.val(data.result.fileShotPath);
				},
				progressall : function(e, data) {
					var progress = parseInt(data.loaded / data.total * 100, 10);
					$bar.css('width', progress + '%');
					$bar.attr('aria-valuenow', progress);
					$bar.html(progress + "%");
				},

			});

}
