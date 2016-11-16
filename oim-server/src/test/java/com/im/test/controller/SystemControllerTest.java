package com.im.test.controller;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.im.common.util.HttpClientUtil;

/**
 * @author: XiaHui
 * @date: 2016年8月25日 下午5:44:04
 */
public class SystemControllerTest {
	String auth = "{\"id\":\"own-000001\",\"key\":\"kkkyyyttt\"}";
	String baseUrl = "http://192.168.1.155:9010";

	@Test
	public void reloadQuery() {
		String url = baseUrl + "/system/reloadQuery.do";
		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("auth", auth);
		System.out.println(HttpClientUtil.post(url, dataMap));
	}
}
