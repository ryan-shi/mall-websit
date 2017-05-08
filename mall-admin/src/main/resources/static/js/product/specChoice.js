$(function() {
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
										data : $('#saveForm').serialize(),
										headers : {
											"Content-type" : "application/x-www-form-urlencoded;charset=UTF-8"
										},
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
