package com.im.business.server.action;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.im.business.server.message.Head;
import com.im.business.server.message.Message;
import com.im.business.server.message.data.PageData;
import com.im.business.server.message.data.chat.ChatQueryData;
import com.im.business.server.message.data.chat.Content;
import com.im.business.server.mq.MessageQueueWriteHandler;
import com.im.bean.GroupMember;
import com.im.business.common.service.BanService;
import com.im.business.common.service.ChatService;
import com.im.business.common.service.GroupService;
import com.im.business.common.service.InfoService;
import com.im.business.common.service.RoomChatLogService;
import com.only.action.annotation.ActionMapping;
import com.only.action.annotation.MethodMapping;
import com.only.net.session.SocketSession;
import com.only.parameter.annotation.Parameter;

@Component
@ActionMapping(value = BaseAction.action_chat)
public class ChatAction {
	protected final Logger logger = LogManager.getLogger(this.getClass());
	@Resource
	ChatService chatService;
	@Resource
	InfoService infoService;
	@Resource
	BanService banService;
	@Resource
	RoomChatLogService roomChatLogService;
	@Resource
	MessageQueueWriteHandler messageQueueWriteHandler;
	@Resource
	GroupService groupService;
	
	@MethodMapping(value = "1-1001")
	public void userChat(Head head,
			@Parameter("sendUserId") String sendUserId,
			@Parameter("receiveUserId") String receiveUserId,
			@Parameter("content") Content content) {
		chatService.userChat(head, sendUserId, receiveUserId, content);
	}

	@MethodMapping(value = "1-1002")
	public void userShake(Head head, SocketSession socketSession,@Parameter("receiveUserId")String receiveUserId) {
		Message message =new Message();
		message.setHead(head);
		message.put("sendUserId", socketSession.getKey());
		message.put("receiveUserId", receiveUserId);
		messageQueueWriteHandler.push(receiveUserId, message);
	}
	
	@MethodMapping(value = "1-2001")
	public void groupChat(Head head,
			@Parameter("userId") String userId,
			@Parameter("groupId") String groupId,
			@Parameter("content") Content content) {
		
		content.setTime(System.currentTimeMillis());
		
		Message message =new Message();
		message.setHead(head);
		message.put("content", content);
		message.put("userId", userId);
		message.put("groupId", groupId);
		List<GroupMember> list = groupService.getGroupMemberListByGroupId(groupId);
		List<String> ids = new ArrayList<String>();
		for (GroupMember gm : list) {
			ids.add(gm.getUserId());
		}
		messageQueueWriteHandler.push(ids, message);
	}
	

	@MethodMapping(value = "1-3001")
	public void roomChat(Head head, SocketSession socketSession,
			@Parameter("userId") String userId,
			@Parameter("roomId") String roomId,
			@Parameter("content") Content content) {
		roomId = (null == roomId || "".equals(roomId)) ? "default" : roomId;
		String key = socketSession.getKey();
		String address = socketSession.getRemoteAddress();
		boolean isBan = banService.isBan(head, roomId, key, address);
		if (!isBan) {
			chatService.roomChat(head, userId, roomId, content);
		}
	}

	@MethodMapping(value = "1-3002")
	public Object queryRoomChatLog(Head head,
			@Parameter("roomId") String roomId,
			@Parameter("queryData") ChatQueryData queryData,
			@Parameter("page") PageData page) {
		Message message = roomChatLogService.queryRoomChatLog(head, roomId, queryData, page);
		return message;
	}
}
