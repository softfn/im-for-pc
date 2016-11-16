package com.im.business.server.push;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.im.business.server.message.Head;
import com.im.business.server.message.Message;
import com.im.business.server.mq.MessageQueueWriteHandler;

@Service
public class VideoPush {

	@Resource
	MessageQueueWriteHandler messageQueueWriteHandler;

	/**
	 * 服务器收到客户端视频后
	 * 
	 * @param userId
	 */
	public void pushReceivedVideoAddress(String userId) {
		Head head = new Head();
		head.setAction("502");
		head.setMethod("0005");
		head.setTime(System.currentTimeMillis());
		Message message = new Message();
		message.setHead(head);
		messageQueueWriteHandler.push(userId, message);
	}
}
