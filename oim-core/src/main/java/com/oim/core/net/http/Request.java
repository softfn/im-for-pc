package com.oim.core.net.http;

import java.util.HashMap;
import java.util.Map;

import com.oim.core.common.util.JsonUtil;

public class Request {

	private Map<String, String> dataMap = new HashMap<String, String>();
	private String controller;
	private String method;

	public Request() {

	}

	public Request(String controller, String method) {
		this.controller = controller;
		this.method = method;
	}

	public Map<String, String> getDataMap() {
		return dataMap;
	}

	public void setDataMap(Map<String, String> dataMap) {
		this.dataMap = dataMap;
	}

	public String getController() {
		return controller;
	}

	public void setController(String controller) {
		this.controller = controller;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public void put(String key, String value) {
		dataMap.put(key, value);
	}

	public void put(String key, Object value, boolean json) {
		String data = "";
		if (null != value) {
			if (json) {
				data =JsonUtil.objectToJson(value);
			} else {
				data = value.toString();
			}
			dataMap.put(key, data);
		}
	}
}
