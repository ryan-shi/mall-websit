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
			productId : {
				required : true
			}
		},
		messages : {
			productId : {
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
										url : "./new",
										data : new FormData($("#saveForm")[0]),
										enctype : 'multipart/form-data',
										processData : false,
										contentType : false,
										cache : false,
										success : function(data) {
											if (data == 1) {
												alert("保存成功！");
												var advertTb = $(
														'#advert_tb')
														.DataTable();
												advertTb.ajax.reload(null,
														false);
												closeDialog();
											} else if (data == 2) {
												alert("商品不存在！");
											} else {
												alert(data);
											}
										}
									});
						} else {
							return;
						}
					});
});
