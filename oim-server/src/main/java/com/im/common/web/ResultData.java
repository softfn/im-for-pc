package com.im.common.web;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Only
 * @date 2016年5月20日 下午1:49:57
 */
public class ResultData {

	public static final String code_fail = "0";
	public static final String code_success = "1";

	private String code;
	private String message;
	private Map<String, Object> data = new HashMap<String, Object>();

	public static ResultData createSuccessMessage(String message) {
		ResultData m = new ResultData();
		m.setCode(code_success);
		m.setMessage(message);
		return m;
	}

	public static ResultData createFailMessage(String message) {
		ResultData m = new ResultData();
		m.setCode(code_fail);
		m.setMessage(message);
		return m;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void put(String key, Object value) {
		data.put(key, value);
	}

	@SuppressWarnings("unchecked")
	public <T> T get(String key) {
		return (T) data.get(key);
	}
}
