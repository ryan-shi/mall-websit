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
				$("#picture").val("");
			};
		} else {
			alert("请选择一个图片文件！");
		}
	});
	$('#saveForm').validate({
		rules : {
			name : {
				required : true
			}
		},
		messages : {
			name : {
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
										url : "./update",
										data : new FormData($("#saveForm")[0]),
										enctype : 'multipart/form-data',
										processData : false,
										contentType : false,
										cache : false,
										success : function(data) {
											if (data == 1) {
												alert("编辑成功");
												var productTb = $('#product_tb')
														.DataTable();
												productTb.ajax.reload(null,
														false);
												closeDialog();
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
