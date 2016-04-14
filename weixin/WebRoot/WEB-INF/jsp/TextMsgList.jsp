<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'TextMsgList.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link rel="stylesheet" href="<%=basePath%>css/bootstrap.min.css">
<link rel="stylesheet" href="<%=basePath%>css/bootstrap-table.css">
<script src="<%=basePath%>js/jquery.js"></script>
<script src="<%=basePath%>js/bootstrap.min.js"></script>
<script src="<%=basePath%>js/bootstrap-table.js"></script>
<script src="<%=basePath%>js/bootstrap-table-multiple-search.js"></script>
<!-- put your locale files after bootstrap-table.js -->
<script src="<%=basePath%>js/bootstrap-table-zh-CN.js"></script>

  </head>
  
  <body>
    <div id="textMsgList_div">
		<table id="textMsgList_table"></table>
	</div>
	<script type="text/javascript">
		$('#textMsgList_table')
				.bootstrapTable(
						{
							url : "textMsgList",
							dataType : "json",
							pagination : true, //分页
							pageList:[10,25,50,100],
							pageSize:10,
							singleSelect : false,
							search : true, //显示搜索框
							sidePagination : "server", //服务端处理分页
							columns : [
									{
										title : '标题',
										field : 'tlTitle',
										align : 'center',
										valign : 'middle'
									},
									{
										title : '内容',
										field : 'tlContent',
										align : 'center',
										valign : 'middle',
									},
									{
										title : '创建者',
										field : 'emNo',
										align : 'center',
										valign : 'middle',
									},
									{
										title : '状态',
										field : 'tlStatus',
										align : 'center',
										valign : 'middle',
										formatter : function(value, row, index) {
											var s;
											if (row.tlStatus == 1)
												s = "启用";
											else
												s = "禁用";
											return s;
										}
									},
									{
										title : '操作',
										field : 'tlId',
										align : 'center',
										formatter : function(value, row, index) {
											var e;
											if(row.tlStatus==0){
												e= '<a class=\"caozuo\" onclick="qiyong1(\''
													+ row.tlId + '\')">启用</a> ';
											}
											else{ e = '<a class=\"caozuo\" onclick="jinyong1(\''
													+ row.tlId + '\')">禁用</a> ';
											}
											var d = '<a  class=\"caozuo\" onclick="del1(\''
													+ row.tlId + '\')">删除</a> ';
											var f= '<a  class=\"caozuo\" onclick="edit1(\''
												+ row.tlId + '\')">修改</a> ';
											return e + d+f;
										}
									} ]
						});
		function del1(emId) {
			if (confirm("确认要删除？")) {
			$.get("delUser?userId=" + emId, function(data, status) {
				if (data == "fail")
					alert("删除失败！");
				else {
					alert("删除成功！");
					$('#table').bootstrapTable('refresh');
				}
			});
			}
		}
		function qiyong1(emId){
			$.get("enableUser?userId=" + emId, function(data, status) {
				if (data == "fail")
					alert("启用失败！");
				else {
					alert("启用成功！");
					$('#table').bootstrapTable('refresh');
				}
			});
		}
		function jinyong1(emId){
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
