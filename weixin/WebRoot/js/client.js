$(document).ready(function() {
	$("#wait_li").click(function() {
		$("#wait_panel").show();
		$("#jieru_panel").hide();
		$("#jieru_li").css("background", "rgb(162, 162, 162)");
		$("#wait_li").css("background", "rgb(149, 149, 149)");
	});
	$("#jieru_li").click(function() {
		$("#jieru_panel").show();
		$("#wait_panel").hide();
		$("#jieru_li").css("background", "rgb(149, 149, 149)");
		$("#wait_li").css("background", "rgb(162, 162, 162)");
	});
	$("#searchText_li").click(function(){
		$("#textMsgList_div").show();
		$("#picMsgList_div").hide();
		$("#searchText_li").css("background", "rgb(149, 149, 149)");
		$("#searchPic_li").css("background", "rgb(162, 162, 162)");
	});
	$("#searchPic_li").click(function(){
		$("#textMsgList_div").hide();
		$("#picMsgList_div").show();
		$("#searchPic_li").css("background", "rgb(149, 149, 149)");
		$("#searchText_li").css("background", "rgb(162, 162, 162)");
	});
})

// 声明一个socket
var socket;
// 初始化方法
var account;
function init(userAccount) {
	$("#searchText_li").click();
	account = userAccount;
	var host = "ws://xhc.ngrok.cc/weixin/websocket/" + userAccount;// 声明host注意：是ws协议
	try {
		socket = new WebSocket(host);// 新创建一个socket对象
		//log('WebSocket - status ' + socket.readyState);// 将连接的状态信息显示在log
		socket.onopen = function(msg) {
			//log("Welcome - status " + this.readyState);
		};// 监听打开连接
	
		socket.onmessage = function(msg) {
			var json = JSON.parse(msg.data);
			if (json instanceof Array) {
				if (json.length > 0) {
					if (json[0].messageType == "dengdai") {
						initWaitPanel(json);
					} else if (json[0].messageType == "jieru") {
						initJieruPanel(json);
					}
				}
			} else {
				if (json.messageType == "dengdai") {
					addWaitUser(json);
				} else if (json.messageType == "jieru") {
					addJieruUser(json);
				} else if (json.messageType == "quxiaodengdai") {
					removeWaitUser(json.openid);
				} else if (json.messageType == "errormsg") {
					if(json.errcode=="45002") alert("消息过长，请分多条发送！");
					else alert(json.errmsg);
				} else if (json.messageType == "message") {
					addMessage(json);
				}else if(json.messageType=="zhuanjie"){
					handleZhuanjie(json);
				}else if(json.messageType=="zhuanjiesuccess"){
					removeJieruUser(json.openid);
					alert(json.name+"成功被转接到客服"+json.kefuAccount);
				}else if(json.messageType=="refusezhuanjie"){
					var name=$("#"+json.openid+"_nickname").text();
					if(name=="") name=openid;
					alert("客服人员"+json.kefuAccount+"拒绝转接客户"+name);
					$("#"+json.openid+"_jieru_user input").show();
				}
			}

		};
		socket.onclose = function(msg) {
			//log("Disconnected - status " + this.readyState);
		};// 关闭连接
	} catch (ex) {
		log(ex);
	}
	$("#talk_panel").focus();
}
function addMessage(json) {
	var prefix = "#" + json.FromUserName;
	var message_panel = $(prefix + "_message_panel");
	if (currentSelectedUser != json.FromUserName) {
		var text = $(prefix + "_jieru_user label").text();
		if (text != "...") {
			text = Number(text) + 1;
			if (text < 100)
				$(prefix + "_jieru_user label").text(text);
			else
				$(prefix + "_jieru_user label").text("...");
		}
		$(prefix + "_jieru_user label").addClass("show-panel");
	}
	if (json.MsgType == "text") {
		var str = "";
		str += "<table class=\"user_msg\"><tr><td class=\"user_image\"><img src=\""
				+ $(prefix + "_headimgurl").text() + "\"></td>";
		str += "<td class=\"message\">" +replaceEmoji(json.Content)+ "</td></tr></table>";
		message_panel.append(str);
		//message_panel.emoji();
	}else if(json.MsgType=="image"){
		var str = "";
		str += "<table class=\"user_msg\"><tr><td class=\"user_image\"><img src=\""
				+ $(prefix + "_headimgurl").text() + "\"></td>";
		str += "<td class=\"img_message\"><img src=\""+json.PicUrl+"\"></td></tr></table>";
		message_panel.append(str);
	}else {
		alert("暂时不支持的消息类型");
	}
	adjustDivScroll(message_panel);
}
function removeWaitUser(openid) {
	var id = "#" + openid + "_wait_user";
	$(id).remove();
}
function removeJieruUser(openid){
	var prefix = "#" + openid;
	$(prefix+"_jieru_user").remove();
	$(prefix+"_message_panel").remove();
	$(prefix+"_talk_panel").remove();
	currentSelectedUser="";
}
function initWaitPanel(json) {
	for (var i = 0; i < json.length; i++) {
		addWaitUser(json[i]);
	}
}
function initJieruPanel(json){
	for (var i = 0; i < json.length; i++) {
		addJieruUser(json[i]);
	}
}
function adjustDivScroll(div) {
	div.scrollTop = div.scrollHeight;
}
function addJieruUser(user) {
	var jieruPanel = $("#jieru_panel");
	var str = "";
	str += "<div onclick=\"showChat('" + user.openid + "')\"  id=\""
			+ user.openid + "_jieru_user\" class=\"jieru_user  parent\">";
	str += "<img alt=\"./image/logo.png\" src=\"" + user.headimgurl + "\">";
	str += "<label>0</label>";
	if (user.nickname != "") {
		str += "<a>" + user.nickname + "</a>";
	} else {
		str += "<a>" + user.openid + "</a>";
	}
	str+="<input  onclick=\"zhuanjie('" + user.openid
	+ "')\" type=\"button\"  value=\"转接\">"
	str += "<ul>";
	str += "<li id=\"" + user.openid + "_sex\">" + user.sex + "</li>";
	str += "<li id=\"" + user.openid + "_city\">" + user.city + "</li>";
	str += "<li id=\"" + user.openid + "_province\">" + user.province + "</li>";
	str += "<li id=\"" + user.openid + "_country\">" + user.country + "</li>";
	str += "<li id=\"" + user.openid + "_openid\">" + user.openid + "</li>";
	str += "<li id=\"" + user.openid + "_nickname\">" + user.nickname + "</li>";
	str += "<li id=\"" + user.openid + "_headimgurl\">" + user.headimgurl
			+ "</li>";
	str += "</ul>";
	str += "</div>";
	jieruPanel.append(str);
	jieruPanel.hide();
	var container1 = $("#container1");
	var str1 = "";
	str1 += "<div style=\"display: none;\" id=\"" + user.openid
			+ "_message_panel\" class=\"message_panel\"></div>";
	str1 += "<div id=\"" + user.openid + "_talk_panel\" class=\"talk_panel\">";
	str1 += "<div><div id=\""+user.openid+"_pic_div\" class=\"pic_div\"><label onclick=emjclick(event) id=\""+user.openid+"_emoji_button\"  class=\"emoji_button\">表情</label>"; 
	str1+=	"<label onclick=picclick(event) id=\""+user.openid+"_pic_button\"  class=\"pic_button\">图片</label></div>"; 
	str1+="<div  contenteditable=\"true\"  id=\"" + user.openid+ "_talk\" class=\"talk\" onkeypress=\"onkey(event)\"></div></div>";
	str1+="<button onclick=\"duankai('" + user.openid
	+ "')\" type=\"button\" id=\""+user.openid+"_duankai\"  class=\"duankai btn btn-default\">断开</button>";
	
	str1 += "<button  onclick=\"sendMessage('" + user.openid
			+ "')\" type=\"button\" id=\"" + user.openid
			+ "_send\" class=\"send btn btn-default\">发送</button></div>";
	container1.append(str1);
	$("#jieru_li").click();
}
function duankai(openid){
	var message={"messageType":"duankai","openid":"","account":""};
	message.openid=openid;
	message.account=account;
	socket.send(JSON.stringify(message));
	removeJieruUser(openid);
}
function sendMessage(openid) {
	var prefix = "#" + openid;
	var message = $(prefix + "_talk").html();
	if ($.trim($(prefix + "_talk").text()) == ""&&message.indexOf("<img")<0) {
		$("#kong").show();
		setTimeout(function() {
			$("#kong").hide();
		}, 3000);
	} else {
		var message_panel = $(prefix + "_message_panel");
		var str = "";
		str += "<table class=\"kefu_msg\"><tr><td class=\"message\">" + message
				+ "</td>";
		str += "<td class=\"kefu_image\"><img src=\"./image/logo.png\"></td></tr></table>";
		message_panel.append(str);
		$(prefix + "_talk").html('');// 清空聊天窗口的内容
		$(prefix + "_talk").focus();// 将焦点重新聚焦到聊天窗口上
		adjustDivScroll(message_panel);
		var json = {
			"messageType" : "talk_message",
			"openid" : "",
			"account" : "",
			"message" : ""
		};
		json.openid = openid;
		json.account = account;
		json.message = message;
		socket.send(JSON.stringify(json));
	}
}
var currentSelectedUser = "";
function showChat(openid) {
	if (currentSelectedUser.length > 0) {
		var prefix = "#" + currentSelectedUser;
		$(prefix + "_talk").hide();
		$(prefix + "_send").hide();
		$(prefix + "_talk_panel").hide();
		$(prefix + "_message_panel").hide();
		$(prefix+"_duankai").hide();
		$(prefix+"_pic_div").hide();
		$(prefix + "_jieru_user").css("background", "#B0B0B0");
	}
	var prefix1 = "#" + openid;
	$(prefix1 + "_talk").show();
	$(prefix1 + "_talk").focus();
	$(prefix1 + "_send").show();
	$(prefix1 + "_talk_panel").show();
	$(prefix1 + "_message_panel").show();
	$(prefix1+"_duankai").show();
	$(prefix1+"_pic_div").show();
	$(prefix1 + "_jieru_user").css("background", "rgb(149, 149, 149)");
	$(prefix1 + "_jieru_user label").text("0");
	$(prefix1 + "_jieru_user label").removeClass("show-panel");
	currentSelectedUser = openid;
}
function addWaitUser(user) {
	var waitPanel = $("#wait_panel");
	var str = "";
	str += "<div id=\"" + user.openid + "_wait_user\" class=\"wait_user\">";
	str += "<img alt=\"./image/logo.png\" src=\"" + user.headimgurl + "\">";
	if (user.nickname != "") {
		str += "<a>" + user.nickname + "</a>";
	} else {
		str += "<a>" + user.openid + "</a>";
	}
	str += "<input  onclick=\"jieru('" + user.openid
			+ "')\" type=\"button\"  value=\"接入\">";
	str += "<ul>";
	str += "<li id=\"" + user.openid + "_sex\">" + user.sex + "</li>";
	str += "<li id=\"" + user.openid + "_city\">" + user.city + "</li>";
	str += "<li id=\"" + user.openid + "_province\">" + user.province + "</li>";
	str += "<li id=\"" + user.openid + "_country\">" + user.country + "</li>";
	str += "<li id=\"" + user.openid + "_openid\">" + user.openid + "</li>";
	str += "<li id=\"" + user.openid + "_nickname\">" + user.nickname + "</li>";
	str += "<li id=\"" + user.openid + "_headimgurl\">" + user.headimgurl
			+ "</li>";
	str += "</ul>";
	str += "</div>";
	waitPanel.append(str);
	// waitPanel.hide();
}
function jieru(openid) {
	$.get("jieru.do?username=" + openid + "&kefuAccount=" + account, function(
			data, status) {
		if (data == "fail"){
			alert("该用户已经被其他客服人员接待了！");
			$("#"+openid+"_wait_user").remove();
		}
	});
}
function zhuanjie(openid){
	$.get("onlinekefu.do?kefuAccount="+account,function(data,status){
		var json=JSON.parse(data);
		var div=$("#container");
		var str="";
		str+="<div id=\"zhuanjie_div\" class=\"alert alert-warning alert-dismissible\" role=\"alert\">";
		str+="<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\"><span aria-hidden=\"true\">&times;</span></button>";
		str+="<label id=\"zhuanjieTitle\">转接客户</label>";
		str+="<div id=\"selectZhuanjieKefu\"><label>选择客服：</label><br><div id=\"zhuanjieKefu\">";
		for(var a in json){
		str+="<input type=\"radio\"  name=\"kefuaccount\" value=\""+json[a]+"\">&nbsp;"+json[a];
		if(a%5==0&&a>0) str+="<br>";
		else str+="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
		}
		str+="</div></div>";
		str+="<div id=\"zhuanjiefuyan\"><label>附言：</label><br><input type=\"text\" name=\"fuyan\" class=\"form-control\" placeholder=\"Postscript\"></div><br>";
		str+="<div id=\"zhuanjiebuttons\"><input onclick=\"ensurezhuanjie('"+openid+"')\" id=\"ensureZhuanjie\" type=\"button\" value=\"确定\" class=\"btn btn-default\">&nbsp;&nbsp;&nbsp;&nbsp;";
		str+="<input onclick=\"cancelzhuanjie()\" id=\"cancelZhuanjie\" type=\"button\" value=\"取消\" class=\"btn btn-default\"></div>";
		str+="</div>";
		div.append(str);
	});
}
function ensurezhuanjie(openid){
	var kefuaccount= $('#selectZhuanjieKefu input[name="kefuaccount"]:checked ').val(); 
	if(!kefuaccount){
	alert("请选择转接客服");
	return false;
	}
	var message={"fromKefuAccount":account,"toKefuAccount":kefuaccount,"fuyan":$("#zhuanjiefuyan input").val(),"openid":openid};
	$.ajax({
        type : 'POST',  
        data :"jsonStr="+JSON.stringify(message),    
        url : 'zhuanjie.do', 
        success : function(data, status) {  
        	$("#zhuanjie_div").remove();
        	$("#"+openid+"_jieru_user input").hide();
        },  
        error : function(error, exception) {  
            alert("未知错误");  
        }  
    });  
}
function handleZhuanjie(json){
	var fromKefuAccount=json.fromKefuAccount;
	var div=$("#container");
	var str="";
	str+="<div id=\"handleZhuanjie_div\" class=\"alert alert-warning alert-dismissible\" role=\"alert\">";
	str+="<label id=\"handleZhuanjieTitle\">转接提醒</label>";
	str+="<div id=\"handleZhuanjieContent\"><label>"+fromKefuAccount+"转给您一个客户</label>";
	str+="<br>附言："+json.fuyan+"</div><br>";
	str+="<div id=\"handleZhuanjiebuttons\"><input onclick=\"ensurehandlezhuanjie('"+json.openid+"','"+fromKefuAccount+"')\" id=\"ensurehandlezhuanjie\" type=\"button\" value=\"接入\" class=\"btn btn-default\">&nbsp;&nbsp;&nbsp;&nbsp;";
	str+="<input onclick=\"cancelhandlezhuanjie('"+json.openid+"','"+fromKefuAccount+"')\" id=\"cancelhandleZhuanjie\" type=\"button\" value=\"拒绝\" class=\"btn btn-default\"></div>";
	str+="</div>";
	div.append(str);
}
function ensurehandlezhuanjie(openid,fromKefuAccount){
	var message={"openid":openid,"fromKefuAccount":fromKefuAccount,"kefuAccount":account};
	$.ajax({
        type : 'POST',  
        data :"jsonStr="+JSON.stringify(message),    
        url : 'handlezhuanjie.do', 
        success : function(data, status) {  
        	$("#handleZhuanjie_div").remove();
        	addJieruUser(JSON.parse(data));
        },  
        error : function(error, exception) {  
            alert("未知错误");  
        }  
    });  
}
function cancelhandlezhuanjie(openid,fromKefuAccount){
	var message={"openid":openid,"fromKefuAccount":fromKefuAccount,"kefuAccount":account};
	$.ajax({
        type : 'POST',  
        data :"jsonStr="+JSON.stringify(message),    
        url : 'cancelhandlezhuanjie.do', 
        success : function(data, status) {  
        	$("#handleZhuanjie_div").remove();
        },  
        error : function(error, exception) {  
            alert("未知错误");  
        }  
    });  
}
function cancelzhuanjie(){
	$("#zhuanjie_div").remove();
}
// 声明一个发送信息方法
function send() {
	var txt, msg;
	txt = $("#talk_panel");
	msg = txt.value;
	if (!msg) {
		alert("Message can not be empty");
		return;
	}
	txt.value = "";
	txt.focus();
	try {
		socket.send(msg);
		log('Sent: ' + msg);
	} catch (ex) {
		log(ex);
	}
}
/*修改接入客服方式事件*/
$(function() {
	$("#select").change(function() {
		var checkText = $("#select").find("option:selected").text();
		var message = {
			"messageType" : "select",
			"message" : ""
		};
		message.message = checkText;
		socket.send(JSON.stringify(message));
	});
});
// 声明一个对开连接，关闭socket方法
function quit() {
	$.get("logout?username="+account,function(data,status){
		if(data=='success'){
			socket.close();
			socket = null;
			location.href = "welcome";
		}else{
			alert("请把接入方式切换为手动接入，并完成当前对话再退出！");
		}
	});
}

// 根据id获取DOM元素
// function $(id){ return document.getElementById(id); }
// 将信息显示在id为log的div中
function log(msg) {
	var divm = $("#message_panel");
	divm.append("<br>" + msg);
}
// 键盘事件（回车）
function onkey(event) {
	if (event.keyCode == 13) {
		event.preventDefault();
		sendMessage(currentSelectedUser);
	}
}
var time=1;
function picclick(event){
    if ( event && event.stopPropagation ) { 
        // this code is for Mozilla and Opera 
    event.stopPropagation(); 
} 
else if (window.event) { 
        // this code is for IE 
    window.event.cancelBubble = true; 
}
    if(time==1){
    	var str="";
    	$.get("get_picture.do",function(data,status){
    		var json=JSON.parse(data);
        	str+="<table>";
    		str+="<tr>";
    		var i=0;
    		for(var a in json){
    			if(i%4==0&&i>0) str+="</tr><tr>";
    			str+="<td><img onclick=\"pictureClick(this)\" src=\"./picture/"+json[a]+"\"></td>";
    			i=i+1;
    		}
    		str+="</tr>";
    			str+="</table>" ;
    			$("#pictures").append(str);
    	    	time++;
    	});
    } 
    $('#accountMessage').hide();
    $('#emojis').hide();
    $('#pictures').show();
}
var time1=1;
function emjclick(event){
	 if ( event && event.stopPropagation ) { 
	        // this code is for Mozilla and Opera 
	    event.stopPropagation(); 
	} 
	else if (window.event) { 
	        // this code is for IE 
	    window.event.cancelBubble = true; 
	}
	    if(time1==1){
	    	$("#emojis").append(getEmojiList());
	    	time1++;
	    }
	    $('#pictures').hide();
	    $('#accountMessage').hide();
	    $('#emojis').show();
}
$(document).click(function(event){
	$('#emojis').hide();
    $('#pictures').hide();
    resetAccountMsg();
    $('#accountMessage').hide();
});

function accountClick(e,event){
	 if ( event && event.stopPropagation ) { 
	        // this code is for Mozilla and Opera 
	    event.stopPropagation(); 
	} 
	else if (window.event) { 
	        // this code is for IE 
	    window.event.cancelBubble = true; 
	}
	$('#emojis').hide();
	$('#pictures').hide();
	$('#accountMessage').show();
}

function pictureClick(pic){
	var prefix="#"+currentSelectedUser;
	$(prefix+"_talk").append("<img src=\""+pic.src+"\">");
}
$(document).on("click",'.message img,.img_message img',function(){
	var div=$("#container");
	var str="";
	str+="<div id=\"big_Pic_div\" class=\"alert alert-warning alert-dismissible\" role=\"alert\">";
	str+="<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\"><span aria-hidden=\"true\">&times;</span></button>";
	str+="<img src=\""+$(this).attr("src")+"\" alt=\"未知错误\">";
	str+="</div>";
	div.append(str);
	});
$('#textMsgList_table')
.bootstrapTable(
		{
			url : "textMsgListNotFenYe",
			dataType : "json",
			pagination : true, //分页
			//pageList:[10,25,50,100],
			//pageSize:10,
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
					} ],	onClickRow:function(row,tr){
						if(currentSelectedUser.length>0)
						$("#"+currentSelectedUser+"_talk").append(row.tlContent);
					}
		});
$('#picMsgList_table')
.bootstrapTable(
		{
			url : "picMsgList",
			dataType : "json",
			pagination : true, //分页
			singleSelect : false,
			search : true, //显示搜索框
			sidePagination : "server", //服务端处理分页
			columns : [
					{
						title : '标题',
						field : 'plTitle',
						align : 'center',
						valign : 'middle'
					},
					{
						title : '图片',
						field : 'plUrl',
						align : 'center',
						valign : 'middle',
						formatter : function(value, row, index) {
							var s;
							s="<img onclick=\"showBigPic('"+row.plUrl+"')\" class=\"preview_pl_pic\" src=\""+row.plUrl+"\"  alt=\"未知错误\"/>"
							return s;
						}
					} ],
			onClickRow : function(row, tr) {
				$("#"+currentSelectedUser+"_talk").append("<img src=\""+row.plUrl+"\">");
			}
		});
