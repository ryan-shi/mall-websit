$(function() {
	var $inputImage = $("#product_picture");
	$inputImage.change(function() {
		var files = this.files;
		var file = files[0];
		if (!files.length) {
			return;
		}
		var fileReader = new FileReader();
		if (/^image\/\w+$/.test(file.type)) {
			fileReader.readAsDataURL(file);
			fileReader.onload = function() {
				$("#headImage").attr("src", this.result);
				$("#headImage").css("display","block");
			};
		} else {
			alert("请选择一个图片文件！");
		}
	});
	
	$('#saveForm').validate({
		rules : {
			specOptionId : {
				required : true
			}
		},
		messages : {
			specOptionId : {
				required : "必填 *"
			}
		}
	});
	$('.saveBtn')
			.click(
					function() {
						if ($('#saveForm').valid()) {
							$
									.ajax({
										type : "POST",
										url : "/admin/sku/makeSku",
										data : new FormData($("#saveForm")[0]),
										enctype : 'multipart/form-data',
										processData : false,
										contentType : false,
										cache : false,
										traditional : true,
										success : function(data) {
											if (data == 1) {
												alert("设置成功");
												closeDialog();
											} else if (data == 2) {
												alert("该规格的商品已存在！");
											}
										}
									});
						} else {
							return;
						}
					});
});
