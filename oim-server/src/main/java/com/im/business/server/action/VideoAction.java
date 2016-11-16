package com.im.business.server.action;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import com.im.business.server.message.Head;
import com.im.business.server.message.Message;
import com.im.business.server.message.data.AddressData;
import com.im.business.server.mq.MessageQueueWriteHandler;
import com.im.business.server.thread.SocketThread;
import com.only.action.annotation.ActionMapping;
import com.only.action.annotation.MethodMapping;
import com.only.parameter.annotation.Parameter;

/**
 * 
 * @author XiaHui
 *
 */
@Controller
@ActionMapping(value = "502")
public class VideoAction {

	@Resource
	private SocketThread socketThread;
	@Resource
	MessageQueueWriteHandler messageQueueWriteHandler;

	@MethodMapping(value = "0001")
	public Message getVideoServerPort() {
		AddressData videoAddress = new AddressData();
		int port = socketThread.getPort();
		videoAddress.setPort(port);
		Message message = new Message();
		message.put("videoAddress", videoAddress);
		return message;
	}

	@MethodMapping(value = "0002")
	public Message getUserVideoAddress(@Parameter("userId") String userId) {
		AddressData videoAddress = socketThread.getVideoAddress(userId);
		Message message = new Message();
		message.put("videoAddress", videoAddress);
		return message;
	}

	@MethodMapping(value = "0003")
	public void videoRequest(
			Head head,
			@Parameter("receiveUserId") String receiveUserId,
			@Parameter("sendUserId") String sendUserId) {
		Message message = new Message();
		message.put("receiveUserId", receiveUserId);
		message.put("sendUserId", sendUserId);
		
		message.setHead(head);
		messageQueueWriteHandler.push(receiveUserId, message);
	}

	@MethodMapping(value = "0004")
	public void videoResponse(
			Head head,
			@Parameter("receiveUserId") String receiveUserId,
			@Parameter("sendUserId") String sendUserId,
			@Parameter("actionType") String actionType) {
		Message message = new Message();
		message.put("receiveUserId", receiveUserId);
		message.put("sendUserId", sendUserId);
		message.put("actionType", actionType);
		
		message.setHead(head);
		messageQueueWriteHandler.push(receiveUserId, message);
	}
}
