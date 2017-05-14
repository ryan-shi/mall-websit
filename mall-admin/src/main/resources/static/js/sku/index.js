$(function() {
	var table = $('#sku_tb')
			.DataTable(
					{
						"language" : {
							"lengthMenu" : "每页  _MENU_ 条记录",
							"emptyTable" : "当前数据为空",
							"zeroRecords" : "没有找到记录",
							"info" : "显示  _START_ 到  _END_ 条记录，总共  _TOTAL_ 条记录",
							"infoEmpty" : "显示  0  到  0  条记录，总共  0  条记录",
							"infoFiltered" : "(从 _MAX_ 条记录过滤)",
							"processing" : "加载中...",
							"paginate" : {
								"first" : "首页",
								"previous" : "上一页",
								"next" : "下一页",
								"last" : "尾页",
							}
						},
						dom : '<"toolbar"><"html5buttons"B>Tfrgt<"row"<"col-lg-4"l><"col-lg-3"i><"col-lg-5"p>>',
						pagingType : "full_numbers",
						buttons : [
								{
									extend : 'copy'
								},
								{
									extend : 'csv'
								},
								{
									extend : 'excel',
									title : '部门信息'
								},
								{
									extend : 'pdf',
									title : '部门信息'
								},
								{
									extend : 'print',
									customize : function(win) {
										$(win.document.body).addClass(
												'white-bg');
										$(win.document.body).css('font-size',
												'10px');
										$(win.document.body).find('table')
												.addClass('compact').css(
														'font-size', 'inherit');
									}
								} ],
						processing : true,
						serverSide : true,
						stateSave : true,
						autoWidth : false,
						ajax : "/admin/sku/list",
						columns : [
								{
									title : "SKUID",
									data : "id",
								},
								{
									title : "商品名称",
									data : "productDTO.name",
									render : $.fn.dataTable.render.ellipsis(15),
								},
								{
									title : "原价",
									data : "price",
								},
								{
									title : "现价",
									data : "priceNow",
								},
//								{
//									title : "商品状态",
//									data : "status",
//									render : function(data, type, row, meta) {
//										switch (data) {
//										case 1:
//											return "新增";
//											break;
//										case 2:
//											return "已上架";
//											break;
//										default:
//											return "已下架";
//											break;
//										}
//									},
//									orderable : false
//								},
//								{
//									title : "商品简介",
//									data : "introduce",
//									render : $.fn.dataTable.render.ellipsis(15),
//									orderable : false
//								},
								{
									title : "库存",
									data : "stock",
								},
								{
									title : "图片地址",
									data : "picture",
									render : $.fn.dataTable.render.ellipsis(15),
								},
								{
									title : "积分",
									data : "score",
								},
								{
									title : "创建时间",
									data : "createTime",
								},
								{
									title : "更新时间",
									data : "updateTime",
								},
								{
									title : "操作",
									data : "null",
									render : function(data, type, row, meta) {
										return '<button class="btn btn-warning btn-xs fa fa-edit" onclick="edit('
												+ row.id
												+ ')" ></button>'
												+ '<button class="btn btn-primary btn-xs fa fa-eye" onclick="view('
												+ row.id
												+ ')"></button>'
												+ '<button class="btn btn-danger btn-xs fa fa-trash" onclick="del('
												+ row.id + ')"></button>';
									},
									width : "80px",
									orderable : false
								} ],
						initComplete : function(settings, json) {
//							$("div.dataTables_filter")
//									.append(
//											'<button class="btn btn-success btn-xs pull-left" onclick="create()"><i class="fa fa-plus"></i>新增</button>');
						},
					});
});

var artdialog;
function edit(id) {
	$.get("./update/" + id, {
		ts : new Date().getTime()
	}, function(data) {
		art.dialog({
			lock : true,
			opacity : 0.3,
			title : "修改",
			width : 'auto',
			height : 'auto',
			left : '50%',
			top : '50%',
			content : data,
			esc : true,
			init : function() {
				artdialog = this;
			},
			close : function() {
				artdialog = null;
			}
		});
	});
}

function view(id) {
	$.get("./" + id, {
		ts : new Date().getTime()
	}, function(data) {
		art.dialog({
			lock : true,
			opacity : 0.3,
			title : "查看",
			width : 'auto',
			height : 'auto',
			left : '50%',
			top : '50%',
			content : data,
			esc : true,
			init : function() {
				artdialog = this;
			},
			close : function() {
				artdialog = null;
			}
		});
	});
}

function del(id) {
	if (!confirm("您确定删除此记录吗？")) {
		return false;
	}
	$.get("./delete/" + id, {
		ts : new Date().getTime()
	}, function(data) {
		if (data == 1) {
			var table = $('#sku_tb').DataTable();
			table.ajax.reload(null, false);
			alert("删除成功");
		} else {
			alert(data);
		}
	});
};

function closeDialog() {
	artdialog.close();
}