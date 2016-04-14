package com.xhc.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.xhc.controller.WebSocket;
import com.xhc.entity.UserBaseMessage;
import com.xhc.model.BaseMessage;
import com.xhc.model.ChatGroup;
import com.xhc.model.PictureMessage;
import com.xhc.model.TextMessage;
import com.xhc.util.AccessTakenTask;
import com.xhc.util.Download;
import com.xhc.util.EmojiUtil;
import com.xhc.util.GetJson;
import com.xhc.util.MsgIdGen;
import com.xhc.util.PostJson;
import com.xhc.util.Upload;
import com.xhc.util.WeChatUtil;

@Service
public class KeFuDispatcherService extends BaseService {
	protected static List<UserBaseMessage> waitUsers = new ArrayList<UserBaseMessage>();// 等待接入的人
	protected static Map<String, List<UserBaseMessage>> kf_user = new HashMap<String, List<UserBaseMessage>>();// 每个客服人员已接入的人
	protected static Map<String, Boolean> isAuto = new HashMap<String, Boolean>();// 装客服人员的接入方式信息（true自动接入，false手动接入，初始化时为手动接入）
	protected static Map<String, Long> group = new HashMap<String, Long>();// 装聊天组id信息

	/* 检查是否已经接入客服 */
	public String checkKefu(String fromUserName) {
		Iterator<Entry<String, List<UserBaseMessage>>> iter = kf_user
				.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, List<UserBaseMessage>> entry = iter.next();
			String key = entry.getKey();
			for (UserBaseMessage str : entry.getValue()) {
				if (str.getOpenid().equals(fromUserName))
					return key;
			}
		}
		return null;
	}

	/* 发送消息到客服 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void sendMessageToKefu(String kefuAccount, Map<String, String> map) {
		map.put("messageType", "message");// 聊天信息类型
		if (map.get("MsgType").equals("image")) {
			String url = Download
					.downloadMedia(AccessTakenTask.accessTaken,
							map.get("MediaId"),
							"D:/Program Files (x86)/Tomcat 7.0/webapps/weixin/download/");
			map.put("PicUrl", url);
		}
		WebSocket.sendMessage(JSONObject.fromObject(map).toString(),
				kefuAccount);
		saveMessage(map, kefuAccount);
	}

	private void saveMessage(Map<String, String> map, String kefuAccount) {
		map.remove("messageType");
		String MsgType = map.get("MsgType");
		BaseMessage baseMessage = null;
		if (MsgType.equals("text")) {
			baseMessage = toTextMessage(map, kefuAccount);
		} else if (MsgType.equals("image")) {
			baseMessage = toPictureMessage(map, kefuAccount);
		} else if (MsgType.equals("voice")) {
			// add(gson.fromJson(JSONObject.fromObject(map).toString(),
			// VoiceMessage.class));
		}
		if (baseMessage != null) {
			add(baseMessage);
			add(new ChatGroup(baseMessage, group.get(map.get("FromUserName"))));// 把消息的分组信息保存到数据库中
		}
	}

	private TextMessage toTextMessage(Map<String, String> map,
			String kefuAccount) {
		TextMessage tm = new TextMessage();
		tm.setMsgId(Long.parseLong(map.get("MsgId")));
		tm.setContent(map.get("Content"));
		tm.setCreateTime(Long.parseLong(map.get("CreateTime")));
		tm.setFromUserName(map.get("FromUserName"));
		tm.setToUserName(kefuAccount);
		tm.setMsgType(map.get("MsgType"));
		return tm;
	}

	private PictureMessage toPictureMessage(Map<String, String> map,
			String kefuAccount) {
		PictureMessage pm = new PictureMessage();
		pm.setMsgId(Long.parseLong(map.get("MsgId")));
		pm.setPicUrl(map.get("PicUrl"));
		pm.setMediaId(map.get("MediaId"));
		pm.setCreateTime(Long.parseLong(map.get("CreateTime")));
		pm.setFromUserName(map.get("FromUserName"));
		pm.setToUserName(kefuAccount);
		pm.setMsgType(map.get("MsgType"));
		return pm;
	}

	/* 加入等待队列 */
	public void addUsersToWaitList(String userName) {
		System.out.println("addUsersToWaitList");
		// 获取客人基本信息
		String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token="
				+ AccessTakenTask.accessTaken
				+ "&openid="
				+ userName
				+ "&lang=zh_CN";
		Gson gson = new Gson();
		UserBaseMessage userBaseMessage = gson.fromJson(GetJson.get(url),
				UserBaseMessage.class);
		userBaseMessage.setMessageType("dengdai");// 等待类型
		waitUsers.add(userBaseMessage);
		// 给所有客服发送消息，在等待队列中添加该用户
		WebSocket.sendMessageToAll(JSONObject.fromObject(userBaseMessage)
				.toString());
	}

	/* 加入客服人员到服务列表中 */
	public void addKefuToKf_user(String kefuAccount) {
		if (kf_user.get(kefuAccount) == null) {
			kf_user.put(kefuAccount, new ArrayList<UserBaseMessage>());
			isAuto.put(kefuAccount, false);
		} else {
			WebSocket.sendMessage(JSONArray
					.fromObject(kf_user.get(kefuAccount)).toString(),
					kefuAccount);
		}
		WebSocket.sendMessage(JSONArray.fromObject(waitUsers).toString(),
				kefuAccount);// 将等待列表信息发送到客服人员
	}

	/* 建立客服连接 */
	@Scheduled(fixedRate = 2000)
	// 每1秒调用一次方法
	public void buildKefu() {
		if (waitUsers.size() > 0) {// 判断是否有人等待接入
			String[] strs = assignKefu();// 指定客服人员
			if (strs != null) {
				sendHelloMessage(strs[0], strs[1]);
			}
		}
	}

	public void sendHelloMessage(String openid, String kefuAccount) {
		JSONObject json = new JSONObject();
		json.accumulate("touser", openid);
		json.accumulate("msgtype", "text");
		JSONObject json1 = new JSONObject();
		json1.accumulate("content", "亲，人工客服工号" + kefuAccount + "很高兴为您服务！");
		json.accumulate("text", json1);
		String url = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="
				+ AccessTakenTask.accessTaken;
		PostJson.post(url, json.toString());
	}

	@Transactional
	public String sendTalkMessage(JSONObject jsonObject) {
		String str = jsonObject.get("message").toString();
		str = str.replaceAll("&nbsp;", " ").replaceAll("<br>", "");
		Object openid = jsonObject.get("openid");
		str = emojiMatcher(str);
		List<Map<String, String>> list = imgMatcher(str);
		String msg = null;
		if (list.size() > 0) {
			int index = 0;
			int start;
			for (Map<String, String> map : list) {
				start = Integer.parseInt(map.get("start"));
				if (index != start) {
					msg = sendTextMessage(openid, str.substring(index, start));
					if ((int) JSONObject.fromObject(msg).get("errcode") == 0) {
						long msgId = MsgIdGen.getFlowIdWorkerInstance()
								.nextId();
						addTextMsg(msgId, System.currentTimeMillis() / 1000,
								str.substring(index, start),
								(String) jsonObject.get("account"), "text",
								(String) openid);
						// add(new chatGroup(group.get(openid), msgId));//
						// 把消息的分组信息保存到数据库中
					} else
						return msg;
				}
				msg = sendImgMessage(openid, jsonObject.get("account"),
						map.get("url"));
				if ((int) JSONObject.fromObject(msg).get("errcode") != 0) {
					return msg;
				}
				index = Integer.parseInt(map.get("end")) + 1;
			}
			if (index != str.length()) {
				msg = sendTextMessage(openid, str.substring(index));
				if ((int) JSONObject.fromObject(msg).get("errcode") == 0) {
					long msgId = MsgIdGen.getFlowIdWorkerInstance().nextId();
					addTextMsg(msgId, System.currentTimeMillis() / 1000,
							str.substring(index),
							(String) jsonObject.get("account"), "text",
							(String) openid);
					// add(new chatGroup(group.get(openid), msgId));//
					// 把消息的分组信息保存到数据库中
				} else
					return msg;
			}
			return msg;
		} else {
			msg = sendTextMessage(jsonObject.get("openid"), str);
			if ((int) JSONObject.fromObject(msg).get("errcode") == 0) {
				long msgId = MsgIdGen.getFlowIdWorkerInstance().nextId();
				addTextMsg(msgId, System.currentTimeMillis() / 1000, str,
						(String) jsonObject.get("account"), "text",
						(String) openid);
				// add(new chatGroup(group.get(openid), msgId));//
				// 把消息的分组信息保存到数据库中
			}
			return msg;
		}

	}

	private void addTextMsg(long msgId, long time, String content,
			String fromuser, String type, String touser) {
		TextMessage tm = new TextMessage();
		tm.setContent(content);
		tm.setCreateTime(time);
		tm.setFromUserName(fromuser);
		tm.setMsgType(type);
		tm.setToUserName(touser);
		tm.setMsgId(msgId);
		add(tm);
		add(new ChatGroup(tm, group.get(touser)));// 把消息的分组信息保存到数据库中
	}

	public String sendImgMessage(Object openid, Object fromuser, String path) {
		String url = "https://api.weixin.qq.com/cgi-bin/media/upload?access_token="
				+ AccessTakenTask.accessTaken + "&type=image";
		String media_id;
		try {
			media_id = Upload.send(url, path);
			JSONObject json = new JSONObject();
			json.accumulate("touser", openid);
			json.accumulate("msgtype", "image");
			JSONObject json1 = new JSONObject();
			json1.accumulate("media_id", media_id);
			json.accumulate("image", json1);
			url = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="
					+ AccessTakenTask.accessTaken;
			String msg = PostJson.post(url, json.toString());
			if ((int) JSONObject.fromObject(msg).get("errcode") == 0) {
				long msgId = MsgIdGen.getFlowIdWorkerInstance().nextId();
				addImgMsg(System.currentTimeMillis() / 1000, (String) fromuser,
						media_id, msgId, path, "image", (String) openid);
				// add(new chatGroup(group.get(openid), msgId));//
				// 把消息的分组信息保存到数据库中
			}
			return msg;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void addImgMsg(long createTime, String fromUserName,
			String mediaId, long msgId, String picUrl, String msgType,
			String toUserName) {
		PictureMessage pm = new PictureMessage();
		pm.setCreateTime(createTime);
		pm.setFromUserName(fromUserName);
		pm.setMediaId(mediaId);
		pm.setMsgId(msgId);
		pm.setMsgType(msgType);
		picUrl = "./picture/" + picUrl.substring(picUrl.lastIndexOf("\\") + 1);
		pm.setPicUrl(picUrl);
		pm.setToUserName(toUserName);
		add(pm);
		add(new ChatGroup(pm, group.get(toUserName)));// 把消息的分组信息保存到数据库中
	}

	public String sendTextMessage(Object openid, Object content) {
		JSONObject json = new JSONObject();
		json.accumulate("touser", openid);
		json.accumulate("msgtype", "text");
		JSONObject json1 = new JSONObject();
		json1.accumulate("content", content);
		json.accumulate("text", json1);
		String url = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="
				+ AccessTakenTask.accessTaken;
		return PostJson.post(url, json.toString());
	}

	private List<Map<String, String>> imgMatcher(String content) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Map<String, String> map;
		String imagePatternStr = "<img\\s+(?:[^>]*)src\\s*=\\s*([^>]+)";
		Pattern imagePattern = Pattern.compile(imagePatternStr,
				Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
		Matcher matcher = imagePattern.matcher(content);
		while (matcher.find()) {
			map = new HashMap<String, String>();
			map.put("start", matcher.start() + "");
			map.put("end", matcher.end() + "");
			String str = matcher.group(1).replaceAll("\"", "");
			str = System.getProperty("user.dir").replace("bin",
					"webapps\\weixin\\picture\\")
					+ str.substring(str.lastIndexOf("/") + 1);
			map.put("url", str);
			// System.out.println(
			// matcher.start()+"//"+matcher.end()+"//"+map.get("url"));
			list.add(map);
		}
		return list;
	}

	private String emojiMatcher(String content) {
		String newString = "";
		String imagePatternStr = "<img\\s+(?:[^>]*)src\\s*=\\s*([^>]+)";
		Pattern imagePattern = Pattern.compile(imagePatternStr,
				Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
		Matcher matcher = imagePattern.matcher(content);
		int index = 0;
		while (matcher.find()) {
			String str = matcher.group(1).replaceAll("\"", "");
			if (index < matcher.start())
				newString += content.substring(index, matcher.start());
			if (str.contains("emoji")) {
				newString += EmojiUtil.getEmojiText(str.substring(str
						.lastIndexOf("/") + 1));
			} else {
				newString += content.substring(matcher.start(),
						matcher.end() + 1);
			}
			index = matcher.end() + 1;
		}
		if (index < content.length())
			newString += content.substring(index);
		return newString;
	}

	/* 指定客服人员 */
	public String[] assignKefu() {
		int count = 10000;
		String kefuAccount = null;
		Iterator<Entry<String, List<UserBaseMessage>>> iter = kf_user
				.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, List<UserBaseMessage>> entry = iter.next();
			String key = entry.getKey();
			int size = entry.getValue().size();
			if (size < count && isAuto.get(key)) {
				System.out.println(size);
				count = size;
				kefuAccount = key;
			}
		}
		if (count < WeChatUtil.MAX_NUM) {
			UserBaseMessage user = waitUsers.remove(0);
			user.setMessageType("jieru");// 发送接入信息给客服
			kf_user.get(kefuAccount).add(user);
			group.put(user.getOpenid(), MsgIdGen.getFlowIdWorkerInstance()
					.nextId());
			// 发送客人的基本消息给客服人员
			WebSocket.sendMessage(JSONObject.fromObject(user).toString(),
					kefuAccount);
			cancelWait(user.getOpenid());
			return new String[] { user.getOpenid(), kefuAccount };
		}
		return null;
	}

	public void cancelWait(String openid) {
		// 给所有客服发送消息，从等待队列中移除该用户
		Map<String, String> message = new HashMap<String, String>();
		message.put("openid", openid);
		message.put("messageType", "quxiaodengdai");// 取消等待类型信息
		WebSocket.sendMessageToAll(JSONObject.fromObject(message).toString());
	}

	public synchronized boolean assignKefu(String username, String kefuAccount) {
		UserBaseMessage u;
		for (int i = 0; i < waitUsers.size(); i++) {
			u = waitUsers.get(i);
			if (u.getOpenid().equals(username)) {
				waitUsers.remove(u);
				kf_user.get(kefuAccount).add(u);
				group.put(u.getOpenid(), MsgIdGen.getFlowIdWorkerInstance()
						.nextId());
				cancelWait(username);
				u.setMessageType("jieru");
				WebSocket.sendMessage(JSONObject.fromObject(u).toString(),
						kefuAccount);
				System.out.println(username + "//" + kefuAccount);
				sendHelloMessage(username, kefuAccount);
				return true;
			}
		}
		return false;
	}

	public void changeJieruWay(Object object, String kefuAccount) {
		if (object.equals("自动接入")) {
			isAuto.put(kefuAccount, true);
		} else {
			isAuto.put(kefuAccount, false);
		}
	}

	public void duankaiKefu(JSONObject jsonObject) {
		String openid = jsonObject.getString("openid");
		List<UserBaseMessage> userBaseMessages = kf_user.get(jsonObject
				.get("account"));
		for (UserBaseMessage u : userBaseMessages) {
			if (u.getOpenid().equals(openid)) {
				userBaseMessages.remove(u);
				sendGoodByeMessage(openid);
				break;
			}
		}
	}

	private void sendGoodByeMessage(String openid) {
		JSONObject json = new JSONObject();
		json.accumulate("touser", openid);
		json.accumulate("msgtype", "text");
		JSONObject json1 = new JSONObject();
		json1.accumulate("content", "感谢使用在线客服，再见~~");
		json.accumulate("text", json1);
		String url = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="
				+ AccessTakenTask.accessTaken;
		PostJson.post(url, json.toString());
	}

	public boolean checkWaitUser(String fromUserName) {
		for (UserBaseMessage u : waitUsers) {
			if (u.getOpenid().equals(fromUserName))
				return true;
		}
		return false;
	}

	public String getPictures() {
		String path = System.getProperty("user.dir").replace("bin",
				"webapps\\weixin\\picture\\");
		File dir = new File(path);
		File[] files = dir.listFiles();
		Map<String, String> map = new HashMap<String, String>();
		for (int i = 0; i < files.length; i++) {
			map.put(i + "", files[i].getName());
		}
		System.out.println(JSONObject.fromObject(map).toString());
		return JSONObject.fromObject(map).toString();
	}

	public List<String> getOnlineKeFu(String account) {
		Set<Entry<String, List<UserBaseMessage>>> entrySet = kf_user.entrySet();
		List<String> list = new ArrayList<String>();
		for (Entry<String, List<UserBaseMessage>> entry : entrySet) {
			if (!entry.getKey().equals(account))
				list.add(entry.getKey());
		}
		return list;
	}

	public void zhuanjie(String str) {
		JSONObject json = JSONObject.fromObject(str);
		String fromKefuAccount = (String) json.get("fromKefuAccount");
		String toKefuAccount = (String) json.get("toKefuAccount");
		String fuyan = (String) json.get("fuyan");
		String openid = (String) json.get("openid");
		Map<String, String> map = new HashMap<String, String>(8);
		map.put("fromKefuAccount", fromKefuAccount);
		map.put("fuyan", fuyan);
		map.put("messageType", "zhuanjie");
		map.put("openid", openid);
		WebSocket.sendMessage(JSONObject.fromObject(map).toString(),
				toKefuAccount);
	}

	public String handlezhuanjie(String str) {
		JSONObject json = JSONObject.fromObject(str);
		String fromKefuAccount = (String) json.get("fromKefuAccount");
		String kefuAccount = (String) json.get("kefuAccount");
		String openid = (String) json.get("openid");
		for (UserBaseMessage u : kf_user.get(fromKefuAccount)) {
			if (u.getOpenid().equals(openid)) {
				kf_user.get(kefuAccount).add(u);
				kf_user.get(fromKefuAccount).remove(u);
				sendZhuanjieHelloMessage(openid, kefuAccount);
				String name = u.getNickname() == null ? openid : u
						.getNickname();
				sendEnsureMsgToKefu(name, openid, fromKefuAccount, kefuAccount);
				return JSONObject.fromObject(u).toString();
			}
		}
		return null;
	}

	public void sendEnsureMsgToKefu(String name, String openid,
			String fromKefuAccount, String kefuAccount) {
		Map<String, String> map = new HashMap<String, String>(8);
		map.put("messageType", "zhuanjiesuccess");
		map.put("name", name);
		map.put("kefuAccount", kefuAccount);
		map.put("openid", openid);
		WebSocket.sendMessage(JSONObject.fromObject(map).toString(),
				fromKefuAccount);
	}

	public void sendZhuanjieHelloMessage(String openid, String kefuAccount) {
		JSONObject json = new JSONObject();
		json.accumulate("touser", openid);
		json.accumulate("msgtype", "text");
		JSONObject json1 = new JSONObject();
		json1.accumulate("content", "亲，为了更好的解决您的问题，您已经被成功转接，人工客服工号"
				+ kefuAccount + "很高兴为您服务！");
		json.accumulate("text", json1);
		String url = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="
				+ AccessTakenTask.accessTaken;
		PostJson.post(url, json.toString());
	}

	public void cancelhandlezhuanjie(String str) {
		JSONObject json = JSONObject.fromObject(str);
		Map<String, String> map = new HashMap<String, String>(8);
		map.put("openid", (String) json.get("openid"));
		map.put("messageType", "refusezhuanjie");
		map.put("kefuAccount", (String) json.get("kefuAccount"));
		WebSocket.sendMessage(JSONObject.fromObject(map).toString(),
				(String) json.get("fromKefuAccount"));
	}
}
