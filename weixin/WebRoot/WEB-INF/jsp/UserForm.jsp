<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%
	String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>My JSP 'UserForm.jsp' starting page</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<style type="text/css">
#userFormDiv {
	width: 70%;
	border: 1px;
	border-style: solid;
	border-color: #d1cece;
	padding: 30px;
	padding-bottom: 10px;
	border-radius: 10px;
	font-size: 22px;
}

.col-lg-6 {
	padding-right: 0px;
	width: 40%;
}

.form-control {
	padding-left: 15px;
}

.input-text {
	padding-left: 15px;
	width: 80%;
}
.rd{
background-color: #ececec;
text-align: center;
}
.error{
color: red;
margin-left: 5px;
font-size: 14px;
}
.my-button{
width: 20%;
}
</style>
</head>

<body>
	<div id="userFormDiv">
		<form:form id ="form1" commandName="employee" action="addUser" method="post">
			<fieldset>
				<legend>添加账户</legend>
				<div class="input-group">
					<span class="input-group-addon" id="basic-addon1"><label
						for="name">姓&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名：</label></span>
					<div>
						<div class="input-text">
							<form:input class="form-control" placeholder="Username"
								aria-describedby="basic-addon1" id="emName" path="emName"
								tabindex="1" />
						</div>
						<form:errors path="emName" cssClass="error" />
					</div>
				</div>
				<br>
				<div class="input-group">
					<span class="input-group-addon" id="basic-addon1"><label
						for="emNo">编&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号：</label></span>
					<div>
						<div class="input-text">
							<form:input id="emNo" path="emNo" tabindex="2"
								class="form-control" placeholder="UserNo"
								aria-describedby="basic-addon2" />
						</div>
						<form:errors path="emNo" cssClass="error" />
					</div>
				</div><br>
				<div class="input-group">
					<span class="input-group-addon"> <label for="emSex">性&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;别：</label></span>
					<div class="col-lg-6">
						<div class="input-group">
							<span class="input-group-addon"> <form:radiobutton
									id="emSex" path="emSex" value="0" tabindex="3" />
							</span> <label class="form-control rd" for="emSex">女</label>
						</div>
					</div>
					<div class="col-lg-6">
						<div class="input-group">
							<span class="input-group-addon"> <form:radiobutton
									id="emSex1" path="emSex" value="1" tabindex="3" />
							</span> <label class="form-control rd" for="emSex1">男</label>
						</div>
					</div>
				</div><br>
				<div class="input-group">
					<span class="input-group-addon" id="basic-addon1"><label
						for="emEmail">邮&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;箱：</label></span>
					<div>
						<div class="input-text">
							<form:input id="emEmail" path="emEmail" tabindex="4"
								class="form-control" placeholder="UserEmail"
								aria-describedby="basic-addon3" />
						</div>
						<form:errors path="emEmail" cssClass="error" />
					</div>
				</div><br>
				<div class="input-group">
					<span class="input-group-addon" id="basic-addon1"><label
						for="emPhone">移动电话：</label></span>
					<div>
						<div class="input-text">
							<form:input id="emPhone" path="emPhone" tabindex="5"
								class="form-control" placeholder="UserPhone"
								aria-describedby="basic-addon4" />
						</div>
						<form:errors path="emPhone" cssClass="error" />
					</div>
				</div><br>
				<div class="input-group">
					<span class="input-group-addon" id="basic-addon1"><label
						for="emIdentity">身&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;份：</label></span>
					<div class="col-lg-6">
						<div class="input-group">
							<span class="input-group-addon"> <form:radiobutton
									id="emIdentity" path="emIdentity" value="admin" tabindex="6" />
							</span> <label class="form-control rd" for="emIdentity">管理员</label>
						</div>
					</div>
					<div class="col-lg-6">
						<div class="input-group">
							<span class="input-group-addon"> <form:radiobutton
									id="emIdentity1" path="emIdentity" value="user" tabindex="6" />
							</span> <label class="form-control rd" for="emIdentity1">客服人员</label>
						</div>
					</div>
				</div>
				<br>
				<div style="text-align: center;">
					<input id="reset" type="reset" tabindex="7" class="btn btn-default my-button">
					&nbsp;&nbsp; <input id="submit" type="submit" tabindex="8"
						value="保存" class="btn btn-default my-button">
				</div>
			</fieldset>
		</form:form>
	</div>
	<script language="JavaScript">
		var selects = document.getElementsByName("emSex");
		selects[1].checked = true;
		selects = document.getElementsByName("emIdentity");
		selects[1].checked = true;
	</script>
</body>
</html>
