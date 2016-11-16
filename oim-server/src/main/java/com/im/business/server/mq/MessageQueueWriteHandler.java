package com.im.business.server.mq;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.only.net.server.DataQueueItem;

@Component
public class MessageQueueWriteHandler {
	@Resource
	MessageQueueReadHandler messageQueueReadHandler;

	public void push(DataQueueItem dataQueueItem) {
		messageQueueReadHandler.push(dataQueueItem);
	}

	public void push(Object data) {
		messageQueueReadHandler.push(data);
	}

	public void push(String key, Object data) {
		messageQueueReadHandler.push(key, data);
	}

	public void push(List<String> keyList, Object data) {
		messageQueueReadHandler.push(keyList, data);
	}

	public void pushWithout(String key, Object data) {
		messageQueueReadHandler.pushWithout(key, data);
	}

	public void pushWithout(List<String> keyList, Object data) {
		messageQueueReadHandler.pushWithout(keyList, data);
	}
}
