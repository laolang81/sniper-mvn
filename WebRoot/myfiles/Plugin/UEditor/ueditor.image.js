/**
 * 检测内容的变化，读取所有图片
 */
UE.getEditor('container').addListener('contentChange', function(editor) {
	// 获取编辑器中的内容（html 代码）
	var img = UE.getEditor('container').getPlainTxt();
	if (img != "") {
		// 把编辑器中的内容放到临时div中，然后进行获取文件名称。
		$("#temp-img-list").html(img);
		var imgArray = new Array();
		// 循环获取所有图片
		$("#temp-img-list img").each(function() {
			var src = $(this).attr("src");
			var name = src.replace("/upload/image/", "");
			imgArray.push(name);
		});
		console.log(imgArray);
		
		var aArray = new Array();
		// 循环获取所有图片
		$("#temp-img-list a").each(function() {
			var src = $(this).attr("href");
			var name = src.replace("/upload/image/", "");
			aArray.push(name);
		});
		console.log(aArray);
		// 清空编辑器中的内容，以便下一次添加图片。
		// UE.getEditor('content').execCommand('cleardoc');
		// 调用callbackImg获取懂图片名称
		if (typeof callbackImg === "function") {
			eval("callbackImg('" + imgArray + "')");
		}

	}
});

// 单传图片开始上传,显示等待。
function preUploadSingleImg() {
	if ($("#loading").length > 0) {
		$("#loading").html("<img src='/Scripts/ueditor/loading.gif'>");
	}

}

// 单传图片回调，隐藏等待图片
function uploadSingleImgCallback() {
	if ($("#loading").length > 0) {
		$("#loading").empty();
	}
}