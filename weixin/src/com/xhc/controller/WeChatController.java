package com.xhc.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.xhc.service.KeFuDispatcherService;
import com.xhc.util.WeChatUtil;

@Controller
public class WeChatController {
	/*
	 * private WeChatService weChatService;
	 * 
	 * @Autowired public void setWeChatService(WeChatService weChatService) {
	 * this.weChatService = weChatService; }
	 */
	private KeFuDispatcherService kefuDispatcher;

	@Autowired
	public void setKefuDispatcher(KeFuDispatcherService kefuDispatcher) {
		this.kefuDispatcher = kefuDispatcher;
	}

	@RequestMapping(value = "/wx.do", method = { RequestMethod.GET })
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		PrintWriter writer = resp.getWriter();
		String signature = req.getParameter("signature");
		String timestamp = req.getParameter("timestamp");
		String nonce = req.getParameter("nonce");
		String echostr = req.getParameter("echostr");
		if (WeChatUtil.checkUrl(signature, timestamp, nonce)) {
			System.out.println("hello");
			writer.write(echostr);
			writer.close();
			// AccessTakenTask.getAccessTaken();
			WeChatUtil.createMenu();
		} else {
			writer.close();
		}
	}

	@RequestMapping(value = "/wx.do", method = { RequestMethod.POST })
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");
		PrintWriter writer = resp.getWriter();
		try {
			Map<String, String> map = WeChatUtil.xmlToMap(req);
			String messageType = map.get("MsgType");
			String fromUserName = map.get("FromUserName");
			String toUserName = map.get("ToUserName");
			String content = map.get("Content");
			String kefuAccount = kefuDispatcher.checkKefu(fromUserName);
			if (messageType.equals(WeChatUtil.MESSAGE_EVENT)
					&& map.get("Event").equals(WeChatUtil.MESSAGE_CLICK)) {
				if (map.get("EventKey").equals("kf")) {
					if (kefuAccount == null) {
						if (!kefuDispatcher.checkWaitUser(fromUserName)) {
							writer.write(WeChatUtil.initText(fromUserName,
									toUserName,
									"您好~欢迎使用在线人工客服，正在为您接通客服代表，请稍后......"));
							writer.close();
							kefuDispatcher.addUsersToWaitList(fromUserName);
						} else {
							writer.write(WeChatUtil.initText(fromUserName,
									toUserName,
									"您好~欢迎使用在线人工客服，正在为您接通客服代表，请稍后......"));
						}
					} else {
						writer.write(WeChatUtil.initText(fromUserName,
								toUserName, "您已接通客服，请输入你的问题"));
					}
				}
			} else {
				if (kefuAccount != null) {
					kefuDispatcher.sendMessageToKefu(kefuAccount, map);
				} else {
					if (messageType.equals(WeChatUtil.MESSAGE_TEXT)) {
						if (content.equals("1")) {
							writer.write(WeChatUtil.initText(fromUserName,
									toUserName, "操你妹"));
						} else if (content.equals("2")) {
							writer.write(WeChatUtil.initText(fromUserName,
									toUserName, "dk"));
						}
					} else if (messageType.equals(WeChatUtil.MESSAGE_EVENT)) {
						if (map.get("Event").equals(
								WeChatUtil.MESSAGE_SUBSCRIBE)) {
							writer.write(WeChatUtil.initText(fromUserName,
									toUserName, "欢迎关注"));
						}
					}
				}
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		} finally {
			writer.close();
		}
	}

}
