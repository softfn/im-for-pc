package com.im.business.server.message;

/**
 * @author: XiaHui
 * @date: 2016年8月22日 下午5:03:51
 */
public class PushMessage extends AbstractMessage {

	private Head head;
	private Object body;
	private Info info = new Info();

	public Head getHead() {
		return head;
	}

	public void setHead(Head head) {
		this.head = head;
	}

	public Object getBody() {
		return body;
	}

	public void setBody(Object body) {
		this.body = body;
	}

	@Override
	public void setInfo(Info info) {
		this.info = info;
	}

	@Override
	public Info getInfo() {
		return info;
	}
}
