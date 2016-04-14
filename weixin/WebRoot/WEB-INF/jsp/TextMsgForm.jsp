<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
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

<title>My JSP 'MyJsp.jsp' starting page</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<style type="text/css">
#textMsgDiv {
	width: 70%;
	border: 1px;
	border-style: solid;
	border-color: #d1cece;
	padding: 30px;
	padding-bottom: 10px;
	border-radius: 10px;
	font-size: 22px;
}

.my-button {
	width: 20%;
}
.error{
color: red;
margin-left: 5px;
font-size: 14px;
}
</style>
</head>

<body>
	<div id="textMsgDiv">
		<form:form id="form2" name="form2" commandName="tmLibrary" action="addTextMessage"
			method="post">
			<fieldset>
				<legend>添加文本素材</legend>
				<div class="input-group">
					<span class="input-group-addon" id="basic-addon1"><label
						for="name">标&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;题：</label></span>
					<div>
						<div class="input-text">
							<form:input class="form-control" placeholder="Title"
								aria-describedby="basic-addon1" id="tlTitle" path="tlTitle"
								tabindex="1" />
						</div>
						<form:errors path="tlTitle" cssClass="error" />
					</div>
				</div>
				<br>
				<div class="input-group">
					<span class="input-group-addon" id="basic-addon1"><label
						for="name">内&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;容：</label></span>
					<div>
						<div class="input-text">
							<form:textarea class="form-control" rows="13"
								placeholder="Content" path="tlContent"
								aria-describedby="basic-addon2" id="tlContent" tabindex="2"
								cssStyle="resize:none" />
						</div>
						<form:errors path="tlContent" cssClass="error" />
					</div>
				</div>
				<div style="display: none">
					<form:input path="employee.emNo" id="emNo" tabindex="3" value='<%=session.getAttribute("emNo")%>'></form:input>
				</div>
				<br>
				<div style="text-align: center;">
					<input id="reset" type="reset" tabindex="4" class="btn btn-default my-button">
					&nbsp;&nbsp; <input id="submit2" type="submit" tabindex="5"
						value="保存" class="btn btn-default my-button">
				</div>
			</fieldset>
		</form:form>
	</div>
</body>
</html>
