<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
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

<title>My JSP 'AccountMsg.jsp' starting page</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<script src="./js/accountMsg.js"></script>
<style type="text/css">
#accountMessage {
	padding: 30px;
	position: fixed;
	top: 12%;
	right: 3%;
	overflow-x: hidden;
	overflow-y: auto;
	display: none;
	background-color: white;
	text-align: center;
	border: 1px;
	border-style: solid;
	border-color: #d1cece;
	border-radius: 10px;
	background-color: rgb(243, 243, 243);
}

#accMsgSave,#cancelEdit,#passSave {
	display: none;
}

#chPassDiv {
	display: none;
}
</style>
</head>

<body>
	<div id="accountMessage" onclick="accountMessageClick(event)">
		<form:form commandName="employee1" id="accountMsgForm" method="post">
			<div id="chanMsgDiv">
				<div class="input-group">
					<span class="input-group-addon" id="basic-addon1"> <a>工号：</a></span>
					<div class="input-text">
						<form:input class="form-control" type="text" path="emNo"
							id="ch_emNo" disabled="true" />
					</div>
				</div>
				<br>
				<div class="input-group">
					<span class="input-group-addon" id="basic-addon1"> <a>姓名：</a></span>
					<div class="input-text">
						<form:input class="form-control" type="text" id="ch_emName"
							path="emName" disabled="true" />
					</div>
				</div>
				<br>
				<div class="input-group">
					<span class="input-group-addon" id="basic-addon1"> <a>性别：</a></span>
					<div class="input-text">
						<c:if test="${employee1.emSex==1}">
							<form:select class="form-control" id="ch_emSex" path="emSex"
								disabled="true">
								<option value="1" selected="selected">男</option>
								<option value="0">女</option>
							</form:select>
						</c:if>
						<c:if test="${employee1.emSex==0}">
							<form:select class="form-control" id="ch_emSex" path="emSex"
								disabled="true">
								<option value="1">男</option>
								<option value="0" selected="selected">女</option>
							</form:select>
						</c:if>
					</div>
				</div>
				<br>
				<div class="input-group">
					<span class="input-group-addon" id="basic-addon1"> <a>邮箱：</a></span>
					<div class="input-text">
						<form:input class="form-control" type="text" id="ch_emEmail"
							path="emEmail" disabled="true" />
					</div>
				</div>
				<br>
				<div class="input-group">
					<span class="input-group-addon" id="basic-addon1"> <a>电话：</a></span>
					<div class="input-text">
						<form:input class="form-control" type="text" id="ch_emPhone"
							path="emPhone" disabled="true" />
					</div>
				</div>
			</div>
			<div id="chPassDiv">
				<div class="input-group">
					<span class="input-group-addon" id="basic-addon1"> <a>旧&nbsp;密&nbsp;&nbsp;码：</a>
					</span>
					<div class="input-text">
						<input class="form-control" type="password" id="oldPassword">
					</div>
				</div>
				<br> <br>
				<div class="input-group">
					<span class="input-group-addon" id="basic-addon1"> <a>新&nbsp;密&nbsp;&nbsp;码：</a>
					</span>
					<div class="input-text">
						<input class="form-control" type="password" id="newPassword"
							id="newPassword">
					</div>
				</div>
				<br> <br>
				<div class="input-group">
					<span class="input-group-addon" id="basic-addon1"> <a>确认密码：</a></span>
					<div class="input-text">
						<input class="form-control" type="password" id="ensurePassword"
							id="ensurePassword">
					</div>
				</div>
				<br>
			</div>
			<br>
			<br>
			<input type="button" class="btn btn-default"  onclick="accountEditCilck()" id="accEdit"
				value="编辑">&nbsp;&nbsp;
				<input type="button" class="btn btn-default"  onclick="accChanpass()" id="accChangePass"
				value="修改密码">
			<input type="button" class="btn btn-default" id="accMsgSave" value="保存"
				onclick="updateUser()">
			<input type="button" id="passSave" class="btn btn-default" value="保存"
				onclick="updatePassword()">
				&nbsp;&nbsp;<input type="reset" class="btn btn-default" id="cancelEdit" value="取消">
		</form:form>
	</div>
	<script type="text/javascript">
		document.getElementById("accountMsgForm").onreset = function cancelEdit() {
			$("#chPassDiv").hide();
			$("#chanMsgDiv").show();
			$("#ch_emName").attr("disabled", "disabled");
			$("#ch_emSex").attr("disabled", "disabled");
			$("#ch_emEmail").attr("disabled", "disabled");
			$("#ch_emPhone").attr("disabled", "disabled");
			$("#accChangePass").show();
			$("#accEdit").show();
			$("#accMsgSave").hide();
			$("#passSave").hide();
			$("#cancelEdit").hide();
		}
	</script>
</body>
</html>
