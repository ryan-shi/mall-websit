$(function() {
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
										url : "./update",
										data : $("#saveForm").serialize(),
										headers : {
											"Content-type" : "application/x-www-form-urlencoded;charset=UTF-8"
										},
										success : function(data) {
											if (data == 1) {
												alert("编辑成功");
												var advertTb = $('#advert_tb')
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
