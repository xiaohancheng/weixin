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
#textMsgEditDiv {
	padding: 30px;
	padding-bottom: 10px;
	font-size: 22px;
}

.my-button {
	width: 20%;
}
</style>
</head>

<body>
	<div id="textMsgEditDiv">
		<form:form id="form3" name="form3" commandName="tmLibrary"
			action="editTextMessage" method="post">
			<fieldset>
				<legend>修改文本素材</legend>
				<div class="input-group">
					<span class="input-group-addon" id="basic-addon1"><label
						for="name">标&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;题：</label></span>
					<div>
						<div class="input-text">
							<form:input class="form-control" placeholder="Title"
								aria-describedby="basic-addon1" id="edit_tlTitle" path="tlTitle"
								tabindex="14" />
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
							<form:textarea class="form-control" rows="7"
								placeholder="Content" path="tlContent"
								aria-describedby="basic-addon2" id="edit_tlContent"
								tabindex="15" cssStyle="resize:none" />
						</div>
						<form:errors path="tlContent" cssClass="error" />
					</div>
				</div>
				<div style="display: none">
					<form:input path="employee.emNo" id="edit_emNo" tabindex="16"  value='<%=session.getAttribute("emNo")%>'></form:input>
				</div>
				<div style="display: none">
					<form:input path="tlId" id="edit_tlId" tabindex="17"></form:input>
				</div>
				<br>
				<div style="text-align: center;">
					<input id="submit2" type="submit" value="保存"
						class="btn btn-default my-button">&nbsp;&nbsp; <input
						id="textMsgEditCancel" type="button" value="取消"
						onclick="textMsgEditFormDivHide()"
						class="btn btn-default my-button">

				</div>
			</fieldset>
		</form:form>
	</div>
</body>
</html>
