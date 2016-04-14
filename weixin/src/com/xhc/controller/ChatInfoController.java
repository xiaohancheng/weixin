package com.xhc.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.xhc.service.ChatService;

@Controller
public class ChatInfoController {
	private ChatService chatService;

	@Autowired
	public void setChatService(ChatService chatService) {
		this.chatService = chatService;
	}

	@RequestMapping(value = "/lastChatList", produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public String getLastChatList(HttpServletRequest request) {
		int offset = request.getParameter("offset") == null ? 1 : Integer
				.parseInt(request.getParameter("offset"));
		// 每页行数
		int showCount = request.getParameter("limit") == null ? 10 : Integer
				.parseInt(request.getParameter("limit"));
		String search = request.getParameter("search") == null ? "" : request
				.getParameter("search");
		Map<String, Object> map = chatService.getLastChatList(offset
				/ showCount + 1, showCount, search);
		Gson gson = new Gson();
		return gson.toJson(map);
	}

	@RequestMapping(value = "/countChatInfo", produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public String countChatInfo(HttpServletRequest request) {
		String time = request.getParameter("time");
		return chatService.countKefuJieruRenshu(time);
	}
}
