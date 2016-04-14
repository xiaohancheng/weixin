package com.xhc.util;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.xhc.entity.WXToken;

@Component
public class AccessTakenTask {
	public static String accessTaken;
	private static final String URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wx9a96b80ccf8dd805&secret=d4624c36b6795d1d99dcf0547af5443d";

	@Scheduled(fixedRate = 7000000)
	// 每7000秒刷新一次Access Taken
	public static void getAccessTaken() {
		Gson gson = new Gson();
		WXToken taken = gson.fromJson(GetJson.get(URL), WXToken.class);
		accessTaken = taken.getAccess_token();
	}
}
