package com.oim.core.net.message;

/**
 * @author: XiaHui
 * @date: 2016年8月22日 下午5:03:51
 */
public class BackMessage extends AbstractMessage {
	
	private Head head;
	private Info info = new Info();
	private Object body;
	public static final String code_fail = "0";
	public static final String code_success = "1";

	public Head getHead() {
		return head;
	}

	public void setHead(Head head) {
		this.head = head;
	}

	@Override
	public void setInfo(Info info) {
		this.info = info;
	}

	public Info getInfo() {
		return info;
	}

	@SuppressWarnings("unchecked")
	public <T> T getBody() {
		return ((T) body);
	}

	public void setBody(Object body) {
		this.body = body;
	}

}
