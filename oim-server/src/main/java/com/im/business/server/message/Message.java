package com.im.business.server.message;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Only
 * @date 2016年5月19日 下午4:12:15
 */
public class Message extends AbstractMessage {

	private Head head;
	private Info info = new Info();
	private Map<String, Object> body = new HashMap<String, Object>();

	public static final String code_fail = "0";
	public static final String code_success = "1";

	public Message() {
		setInfoCode(code_success);
	}

	public Head getHead() {
		return head;
	}

	public void setHead(Head head) {
		this.head = head;
	}

	public Map<String, Object> getBody() {
		return body;
	}

	public void setBody(Map<String, Object> body) {
		this.body = body;
	}

	@Override
	public void setInfo(Info info) {
		this.info = info;
	}

	public Info getInfo() {
		return info;
	}

	public void put(String key, Object value) {
		body.put(key, value);
	}

	public Object get(String key) {
		return body.get(key);
	}

	public void setInfoCode(String code) {
		info.setCode(code);
	}

	public void setInfoValue(String value) {
		info.setValue(value);
	}

}
