<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>My JSP 'UserList.jsp' starting page</title>

<meta http-equiv="content-type" content="utf-8">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<%-- <link rel="stylesheet" href="<%=basePath%>css/bootstrap.min.css">
<link rel="stylesheet" href="<%=basePath%>css/bootstrap-table.css">
<script src="<%=basePath%>js/jquery.js"></script>
<script src="<%=basePath%>js/bootstrap.min.js"></script>
<script src="<%=basePath%>js/bootstrap-table.js"></script>
<script src="<%=basePath%>js/bootstrap-table-multiple-search.js"></script>
<!-- put your locale files after bootstrap-table.js -->
<script src="<%=basePath%>js/bootstrap-table-zh-CN.js"></script> --%>
<style type="text/css">
.fixed-table-body {
	height: auto;
}

#userList_div {
	width:100%;
	margin: 0 auto;
}
.caozuo:HOVER {
	cursor: pointer;
}

</style>
</head>

<body>
	<div id="userList_div">
		<table id="table"></table>
	</div>
	<script type="text/javascript">
		$('#table')
				.bootstrapTable(
						{
							url : "userList",
							dataType : "json",
							pagination : true, //分页
							pageList:[10,25,50,100],
							pageSize:10,
							singleSelect : false,
							search : true, //显示搜索框
							sidePagination : "server", //服务端处理分页
							columns : [
									{
										title : '用户名',
										field : 'emName',
										align : 'center',
										valign : 'middle'
									},
									{
										title : '工号',
										field : 'emNo',
										align : 'center',
										valign : 'middle',
									},
									{
										title : '性别',
										field : 'emSex',
										align : 'center',
										valign : 'middle',
										formatter : function(value, row, index) {
											var s;
											if (row.emSex == 1)
												s = "男";
											else
												s = "女";
											return s;
										}
									},
									{
										title : '邮箱',
										field : 'emEmail',
										align : 'center',
										valign : 'middle',
									},
									{
										title : '联系电话',
										field : 'emPhone',
										align : 'center',
										valign : 'middle',
									},
									{
										title : '身份',
										field : 'emIdentity',
										align : 'center',
										valign : 'middle',
										formatter : function(value, row, index) {
											var s;
											if (row.emIdentity == "user")
												s = "客服人员";
											else
												s = "管理员";
											return s;
										}
									},
									{
										title : '状态',
										field : 'emStatus',
										align : 'center',
										valign : 'middle',
										formatter : function(value, row, index) {
											var s;
											if (row.emStatus == 1)
												s = "启用";
											else
												s = "禁用";
											return s;
										}
									},
									{
										title : '操作',
										field : 'emId',
										align : 'center',
										formatter : function(value, row, index) {
											var e;
											if(row.emStatus==0){
												e= '<a class=\"caozuo\" onclick="qiyong(\''
													+ row.emId + '\')">启用</a> ';
											}
											else{ e = '<a class=\"caozuo\" onclick="jinyong(\''
													+ row.emId + '\')">禁用</a> ';
											}
											var d = '<a  class=\"caozuo\" onclick="del(\''
													+ row.emId + '\')">删除</a> ';
											return e + d;
										}
									} ]
						});
		function del(emId) {
			if (confirm("确认要删除？")) {
			$.get("delUser?userId=" + emId, function(data, status) {
				if (data == "fail")
					alert("删除失败！");
				else {
					alert("删除成功！");
					refresh();
				}
			});
			}
		}
		function refresh(){
			$('#table').bootstrapTable('refresh');
		}
		function qiyong(emId){
			$.get("enableUser?userId=" + emId, function(data, status) {
				if (data == "fail")
					alert("启用失败！");
				else {
					alert("启用成功！");
					$('#table').bootstrapTable('refresh');
				}
			});
		}
		function jinyong(emId){
			$.get("disableUser?userId=" + emId, function(data, status) {
				if (data == "fail")
					alert("禁用失败！");
				else {
					alert("禁用成功！");
					$('#table').bootstrapTable('refresh');
				}
			});
		}
	</script>
</body>
</html>
