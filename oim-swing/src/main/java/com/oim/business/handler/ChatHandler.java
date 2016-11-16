package com.oim.business.handler;

import com.oim.app.AppContext;
import com.oim.common.app.handler.SendHandler;
import com.oim.net.message.Head;
import com.oim.net.message.Message;
import com.oim.net.message.data.chat.Content;

/**
 * @author XiaHui
 * @date 2015年3月16日 下午3:23:23
 */
public class ChatHandler extends SendHandler {

	public ChatHandler(AppContext appContext) {
		super(appContext);
	}

	/**
	 * 发送聊天信息给用户
	 * 
	 * @Author: XiaHui
	 * @Date: 2016年2月16日
	 * @ModifyUser: XiaHui
	 * @ModifyDate: 2016年2月16日
	 * @param receiveId
	 * @param sendId
	 * @param chatDataList
	 */
	public void sendUserChatMessage(String receiveId, String sendId, Content content) {
		Message message = new Message();
		message.put("receiveUserId", receiveId);// 接受信息的用户id
		message.put("sendUserId", sendId);// 发送人的id
		message.put("content", content);// 聊天内容
		
		Head head = new Head();
		head.setAction("500");
		head.setMethod("1-1001");
		head.setTime(System.currentTimeMillis());
		message.setHead(head);
		this.appContext.write(message);
	}

	/**
	 * 发送抖动窗口
	 * 
	 * @Author: XiaHui
	 * @Date: 2016年2月16日
	 * @ModifyUser: XiaHui
	 * @ModifyDate: 2016年2月16日
	 * @param receiveId
	 * @param sendId
	 */
	public void sendShake(String receiveId, String sendId) {
		Message message = new Message();
		message.put("receiveUserId", receiveId);// 接受信息的用户id
		message.put("sendUserId", sendId);// 发送人的id
		
		Head head = new Head();
		head.setAction("500");
		head.setMethod("1-1002");
		head.setTime(System.currentTimeMillis());
		message.setHead(head);
		this.appContext.write(message);
	}

	/**
	 * 发送群信息
	 * 
	 * @Author: XiaHui
	 * @Date: 2016年2月16日
	 * @ModifyUser: XiaHui
	 * @ModifyDate: 2016年2月16日
	 * @param groupId
	 * @param userId
	 * @param content
	 */
	public void sendGroupChatMessage(String groupId, String userId, Content content) {
		Message message = new Message();

		message.put("groupId", groupId);// 接受信息的群id
		message.put("userId", userId);// 发送人的id
		message.put("content", content);// 聊天内容
		
		Head head = new Head();
		head.setAction("500");
		head.setMethod("1-2001");
		head.setTime(System.currentTimeMillis());
		message.setHead(head);
		this.appContext.write(message);
	}
}
