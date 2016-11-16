package com.im.test.controller;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.im.common.util.HttpClientUtil;

/**
 * @author: XiaHui
 * @date: 2016年8月25日 下午5:03:18
 */
public class BanControllerTest {
	String auth = "{\"id\":\"own-000001\",\"key\":\"kkkyyyttt\"}";
	String baseUrl = "http://192.168.1.155:9010";

	@Test
	public void banUserChat() {
		String userId = "FB705E9B729D08C2DB57AAA33178D86C";
		String url = baseUrl + "/ban/banUserChat.do";
		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("auth", auth);
		dataMap.put("userId", userId);
		dataMap.put("banTime", 1000 * 60 * 3 + "");

		System.out.println(HttpClientUtil.post(url, dataMap));
	}

}
