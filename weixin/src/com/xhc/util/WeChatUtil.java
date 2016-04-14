package com.xhc.util;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.google.gson.Gson;
import com.thoughtworks.xstream.XStream;
import com.xhc.entity.Button;
import com.xhc.entity.ClickButton;
import com.xhc.entity.Menu;
import com.xhc.model.TextMessage;

public class WeChatUtil {
	public static final String MESSAGE_TEXT = "text";
	public static final String MESSAGE_IMAGE = "image";
	public static final String MESSAGE_VOICE = "voice";
	public static final String MESSAGE_VIDEO = "video";
	public static final String MESSAGE_SHORTVIDEO = "shortvideo";
	public static final String MESSAGE_LOCATION = "location";
	public static final String MESSAGE_LINK = "link";
	public static final String MESSAGE_EVENT = "event";
	public static final String MESSAGE_SUBSCRIBE = "subscribe";
	public static final String MESSAGE_SCAN = "SCAN";
	public static final String MESSAGE_CLICK = "CLICK";
	public static final String MESSAGE_VIEW = "VIEW";
	public static final int MAX_NUM = 10;

	public static boolean checkUrl(String signature, String timestamp,
			String nonce) {
		String[] arr = { "kefu", timestamp, nonce };
		Arrays.sort(arr);
		String str = arr[0].concat(arr[1]).concat(arr[2]);
		if (SHA1(str).equals(signature))
			return true;
		return false;
	}

	public static String SHA1(String decript) {
		try {
			MessageDigest digest = java.security.MessageDigest
					.getInstance("SHA-1");
			digest.update(decript.getBytes());
			byte messageDigest[] = digest.digest();
			// Create Hex String
			StringBuffer hexString = new StringBuffer();
			// 字节数组转换为 十六进制 数
			for (int i = 0; i < messageDigest.length; i++) {
				String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
				if (shaHex.length() < 2) {
					hexString.append(0);
				}
				hexString.append(shaHex);
			}
			return hexString.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}

	@SuppressWarnings("unchecked")
	public static Map<String, String> xmlToMap(HttpServletRequest req)
			throws DocumentException, IOException {
		Map<String, String> map = new HashMap<String, String>();
		SAXReader reader = new SAXReader();
		InputStream input = req.getInputStream();
		Document document = reader.read(input);
		Element root = document.getRootElement();
		List<Element> list = root.elements();
		for (Element e : list) {
			map.put(e.getName(), e.getText());
		}
		input.close();
		return map;
	}

	public static String textMessageToXml(TextMessage textMessage) {
		XStream xStream = new XStream();
		xStream.alias("xml", textMessage.getClass());
		// System.out.println(xStream.toXML(textMessage));
		return xStream.toXML(textMessage);
	}

	public static String initText(String fromUserName, String toUserName,
			String content) {
		TextMessage textMessage = new TextMessage();
		textMessage.setToUserName(fromUserName);
		textMessage.setFromUserName(toUserName);
		textMessage.setCreateTime(new Date().getTime());
		textMessage.setMsgType(WeChatUtil.MESSAGE_TEXT);
		textMessage.setContent(content);
		return WeChatUtil.textMessageToXml(textMessage);
	}

	public static void createMenu() {
		String url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token="
				+ AccessTakenTask.accessTaken;
		ClickButton kf = new ClickButton();
		kf.setName("在线客服");
		kf.setType("click");
		kf.setKey("kf");
		Button help = new Button();
		help.setName("帮助");
		help.setSub_button(new Button[] { kf });
		Menu menu = new Menu();
		menu.setButton(new Button[] { help });
		Gson gson = new Gson();
		String jsonString = gson.toJson(menu);
		PostJson.post(url, jsonString);
	}

	public static String getFileEndWitsh(String headerField) {
		System.out.println(headerField);
		int index = headerField.indexOf("/") + 1;
		return "." + headerField.substring(index);
	}
}
