$(function() {
	var $inputImage = $("#inputImage");
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
				uploadFile();
				$inputImage.val("");
			};
		} else {
			alert("请选择一个图片文件！");
		}
	});

	function uploadFile() {
		$.ajax({
			url : "/admin/user/uploadAvator",
			type : "POST",
			data : new FormData($("#uploadForm")[0]),
			enctype : 'multipart/form-data',
			processData : false,
			contentType : false,
			cache : false,
			success : function(data) {
				$("#nav_userHead").attr("src",data);
				alert("上传成功！");
			},
			error : function() {
				alert("上传失败！")
			}
		});
	}

	$('#saveForm').validate({
		rules : {
			username : {
				required : true
			},
			password : {
				required : true
			},
			newpassword : {
				required : true
			},
			newpasswdcf : {
				required : true
			}
		},
		messages : {
			username : {
				required : "必填 *"
			},
			password : {
				required : "必填 *"
			},
			newpassword : {
				required : "必填 *"
			},
			newpasswdcf : {
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
										url : "./updateAccount",
										data : $("#saveForm").serialize(),
										headers : {
											"Content-type" : "application/x-www-form-urlencoded;charset=UTF-8"
										},
										success : function(data) {
											alert("修改成功！");
											window.location.reload();
										},
										error : function(data) {
											alert(data.responseText)
										}
									});
						} else {
							alert('数据验证失败，请检查！');
						}
					});
});
