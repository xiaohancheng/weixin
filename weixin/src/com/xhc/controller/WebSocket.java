package com.xhc.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;

import net.sf.json.JSONObject;

import com.xhc.service.KeFuDispatcherService;

@Controller
@ServerEndpoint(value = "/websocket/{client-id}")
public class WebSocket implements ApplicationContextAware {
	@Autowired
	private static ApplicationContext applicationContext;
	private static Map<String, Session> map = new HashMap<String, Session>();

	@OnOpen
	public void onOpen(Session session, @PathParam("client-id") String clientId) {
		System.out.println(session);
		map.put(clientId, session);
		getKeFuDispatcherService().addKefuToKf_user(clientId);
	}

	@OnMessage
	public void onMessage(String message,
			@PathParam("client-id") String clientId) {
		JSONObject jsonObject = JSONObject.fromObject(message);
		if (jsonObject.get("messageType").equals("talk_message")) {
			String msg = getKeFuDispatcherService().sendTalkMessage(jsonObject);
			jsonObject = JSONObject.fromObject(msg);
			if (jsonObject.getInt("errcode") != 0) {
				jsonObject.put("messageType", "errormsg");
				try {
					map.get(clientId).getBasicRemote()
							.sendText(jsonObject.toString());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else if (jsonObject.get("messageType").equals("select")) {
			getKeFuDispatcherService().changeJieruWay(
					jsonObject.get("message"), clientId);
		} else if (jsonObject.get("messageType").equals("duankai")) {
			getKeFuDispatcherService().duankaiKefu(jsonObject);
		}
	}

	@OnClose
	public void onClose(Session peer) {
		map.remove(map.get(peer));
	}

	public static void sendMessage(String string, String account) {
		/*
		 * try { map.get(account).getBasicRemote().sendObject(map2); } catch
		 * (IOException e) { e.printStackTrace(); } catch (EncodeException e) {
		 * e.printStackTrace(); }
		 */
		try {
			map.get(account).getBasicRemote().sendText(string);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void sendMessageToAll(String message) {
		Iterator<Entry<String, Session>> iter = map.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, Session> entry = iter.next();
			Session session = entry.getValue();
			try {
				session.getBasicRemote().sendText(message);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		WebSocket.applicationContext = applicationContext;
	}

	public static KeFuDispatcherService getKeFuDispatcherService() {
		return (KeFuDispatcherService) applicationContext
				.getBean("keFuDispatcherService");
	}
}