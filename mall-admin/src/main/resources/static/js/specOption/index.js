var specOption_tb=null;
$(function() {
	var specId = $("#specId").val();
	specOption_tb = $('#specOption_tb')
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
						dom : '<"toolbarSpecOp">rgt<"row"<"col-lg-4"l><"col-lg-4"i><"col-lg-4"p>>',
						ordering : false,
						paging : false,
						processing : true,
						serverSide : true,
						stateSave : true,
						autoWidth : false,
						ajax : {
							url : "/admin/specOption/list",
							data : {
								id : specId
							}
						},
						columns : [
								{
									title : "规格选项ID",
									data : "id",
								},
								{
									title : "规格选项名称",
									data : "name",
								},
								{
									title : "规格编码",
									data : "code",
								},
								{
									title : "规格排序",
									data : "sort",
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
										return '<button class="btn btn-warning btn-xs fa fa-edit" onclick="editSpecOp('
												+ row.id
												+ ')" ></button>'
												+ '<button class="btn btn-primary btn-xs fa fa-eye" onclick="viewSpecOp('
												+ row.id
												+ ')"></button>'
												+ '<button class="btn btn-danger btn-xs fa fa-trash" onclick="delSpecOp('
												+ row.id + ')"></button>';
									},
									width : "80px",
									orderable : false
								} ],
						initComplete : function(settings, json) {
							$("div.toolbarSpecOp")
									.append(
											'<button class="btn btn-success btn-xs pull-left" onclick="createSpecOp()"><i class="fa fa-plus"></i>新增</button>');
						},
					});
});

var artdialogSpecOp;
function editSpecOp(id) {
	$.get("/admin/specOption/update/" + id, {
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
				artdialogSpecOp = this;
			},
			close : function() {
				artdialogSpecOp = null;
			}
		});
	});
}

function createSpecOp() {
	$.get("/admin/specOption/new", function(data) {
		art.dialog({
			lock : true,
			opacity : 0.3,
			title : "新增",
			width : 'auto',
			height : 'auto',
			left : '50%',
			top : '50%',
			content : data,
			esc : true,
			init : function() {
				artdialogSpecOp = this;
				$("#spec_id").val($("#specId").val());
			},
			close : function() {
				artdialogSpecOp = null;
			}
		});
	});
}

function viewSpecOp(id) {
	$.get("/admin/specOption/" + id, {
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
				artdialogSpecOp = this;
			},
			close : function() {
				artdialogSpecOp = null;
			}
		});
	});
}

function delSpecOp(id) {
	if (!confirm("您确定删除此记录吗？")) {
		return false;
	}
	$.get("/admin/specOption/delete/" + id, {
		ts : new Date().getTime()
	}, function(data) {
		if (data == 1) {
			var table = $('#specOption_tb').DataTable();
			table.ajax.reload(null, false);
			alert("删除成功");
		} else {
			alert(data);
		}
	});
};

function closeDialogSpecOp() {
	artdialogSpecOp.close();
}