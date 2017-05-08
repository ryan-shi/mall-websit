$(function(){
	var table = $('#role_tb').DataTable({
		"language" : {
			"lengthMenu" : "每页  _MENU_ 条记录",
			"emptyTable" : "当前数据为空",
			"zeroRecords" : "没有找到记录",
			"info":           "显示  _START_ 到  _END_ 条记录，总共  _TOTAL_ 条记录",  
		    "infoEmpty":      "显示  0  到  0  条记录，总共  0  条记录",  
			"infoFiltered" : "(从 _MAX_ 条记录过滤)",
			"processing": "加载中...",
			"paginate" : {
				"first" : "首页",
				"previous" : "上一页",
				"next" : "下一页",
				"last" : "尾页",
			}
		},
		dom : '<"toolbar"><"html5buttons"B>Tfrgt<"row"<"col-lg-4"l><"col-lg-4"i><"col-lg-4"p>>',
		pagingType : "full_numbers",
		buttons : [{
			extend : 'copy'
		},
		{
			extend : 'csv'
		},
		{
			extend : 'excel',
		},
		{
			extend : 'pdf',
		},
		{
			extend : 'print',
			customize : function(win) {
				$(win.document.body).addClass('white-bg');
				$(win.document.body).css('font-size','10px');
				$(win.document.body).find('table').addClass('compact').css('font-size', 'inherit');
			}
		}],
		processing : true,
		serverSide : true,
		stateSave : true,
		autoWidth : false,
		ajax : "/admin/role/list",
		columns : [
			{
				title : "角色ID",
				data : "id",
			},
			{
				title : "角色名称",
				data : "name",
			},
			{
				title : "角色描述",
				data : "description",
			},
			{
				title : "创建时间",
				data : "createTime",
			},
			{
				title : "创建时间",
				data : "updateTime",
			},
			{
				title : "操作",
				data : "null",
				render : function(data, type, row, meta) {
					return '<button class="btn btn-info btn-xs fa fa-shield" onclick="assignPermission('
							+ row.id
							+ ')" ></button>'
							+ '<button class="btn btn-warning btn-xs fa fa-edit" onclick="edit('
							+ row.id
							+ ')" ></button>'
							+ '<button class="btn btn-primary btn-xs fa fa-eye" onclick="view('
							+ row.id
							+ ')"></button>'
							+ '<button class="btn btn-danger btn-xs fa fa-trash" onclick="del('
							+ row.id + ')"></button>';
				},
				width : "100px",
				orderable : false
			} ],
			initComplete:function(settings, json) {
				$("div.dataTables_filter")
				.append(
						'<button class="btn btn-success btn-xs pull-left" onclick="create()"><i class="fa fa-plus"></i>新增</button>');
			},
	});
});

var artdialog;

function assignPermission(id){
	$.get("./assignPermission/" + id, {
		ts : new Date().getTime()
	}, function(data) {
		art.dialog({
			lock : true,
			opacity : 0.3,
			title : "资源权限分配",
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

function edit(id){
	$.get("./update/"+id,{ts:new Date().getTime()},function(data){
        art.dialog({
            lock:true,
            opacity:0.3,
            title: "修改",
            width:'auto',
            height: 'auto',
            left: '50%',
            top: '50%',
            content:data,
            esc: true,
            init: function(){
                artdialog = this;
            },
            close: function(){
                artdialog = null;
            }
        });
    });
}

function create(){
    $.get("./new",function(data){
        art.dialog({
            lock:true,
            opacity:0.3,
            title: "新增",
            width:'auto',
            height: 'auto',
            left: '50%',
            top: '50%',
            content:data,
            esc: true,
            init: function(){
                artdialog = this;
            },
            close: function(){
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
			var table = $('#role_tb').DataTable();
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