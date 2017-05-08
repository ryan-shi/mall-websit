$(function() {
	var table = $('#user_tb')
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
						dom : '<"toolbar"><"html5buttons"B>Tfrgt<"row"<"col-lg-4"l><"col-lg-4"i><"col-lg-4"p>>',
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
									title : '用户信息'
								},
								{
									extend : 'pdf',
									title : '用户信息'
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
						ajax : "/admin/user/list",
						columns : [
								{
									title : "用户ID",
									data : "id",
								},
								{
									title : "用户名",
									data : "username",
								},
								{
									title : "邮箱",
									data : "email",
								},
								{
									title : "性别",
									data : "sex",
									render : function(data, type, row, meta) {
										return data ? "男" : "女";
									}
								},
								{
									title : "账号状态",
									data : "enabled",
									orderable : false,
									render : function(data, type, row, meta) {
										return data ? '<span class="label label-sm label-info">正常</span>'
												: '<span class="label label-sm label-danger">禁用</span>';
									}
								},
								{
									title : "部门",
									data : "departmentDTO.name",
									orderable : false
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
									width:"80px",
									orderable: false
								}],
						initComplete : function(settings, json) {
							$("div.dataTables_filter")
									.append(
											'<button class="btn btn-success btn-xs pull-left" onclick="create()"><i class="fa fa-plus"></i>新增</button>');
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

function create() {
	$.get("./new", function(data) {
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
			var table = $('#user_tb').DataTable();
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