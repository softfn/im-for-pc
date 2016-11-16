package com.im.business.common.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.im.common.box.RoomBox;
import com.im.common.util.KeyUtil;
import com.im.business.common.data.ChatItem;
import com.im.bean.RoomChatContent;
import com.im.bean.RoomChatItem;
import com.im.bean.UserChatContent;
import com.im.bean.UserChatItem;
import com.im.business.common.dao.RoomChatDAO;
import com.im.business.common.dao.UserChatDAO;
import com.im.business.server.message.Head;
import com.im.business.server.message.Message;
import com.im.business.server.message.data.UserData;
import com.im.business.server.message.data.chat.Content;
import com.im.business.server.message.data.chat.Item;
import com.im.business.server.message.data.chat.Section;
import com.im.business.server.mq.MessageQueueWriteHandler;
import com.im.business.common.service.api.UserBaseService;
import com.im.business.common.service.api.WordsFilterBaseService;

/**
 * @description:
 * @author: Only
 * @date: 2016年8月16日 上午9:38:51
 */
@Service
public class ChatService {
	protected final Logger logger = LogManager.getLogger(this.getClass());
	@Resource
	MessageQueueWriteHandler messageQueueWriteHandler;
	@Resource
	UserBaseService userBaseService;
	@Resource
	WordsFilterBaseService wordsFilterBaseService;
	@Resource
	RoomChatDAO roomChatDAO;
	@Resource
	UserChatDAO userChatDAO;

	/**
	 * 聊天室聊天信息推送，将用户所发的信息推送到其所在聊天室的其它用户
	 * 
	 * @author: XiaHui
	 * @createDate: 2016年8月19日 下午3:25:08
	 * @update: XiaHui
	 * @updateDate: 2016年8月19日 下午3:25:08
	 */
	@Transactional
	public void roomChat(Head head, String userId, String roomId, Content content) {

		List<ChatItem> chatItemList = wordsFilter(content);
		UserData userData = userBaseService.getUserData(userId);
		
		head.setTime(System.currentTimeMillis());
		Message m = new Message();
		m.setHead(head);
		m.put("userId", userId);
		m.put("roomId", roomId);
		m.put("content", content);

		roomChat(roomId, m);
		addRoomChatLog(userData, head.getKey(), roomId, chatItemList);
	}

	public void roomChat(String roomId, Message m) {

		List<String> userIdList = RoomBox.getUserKeyList(roomId);
		messageQueueWriteHandler.push(userIdList, m);
	}

	private List<ChatItem> wordsFilter(Content content) {
		List<ChatItem> chatItemList = new ArrayList<ChatItem>();

		List<Section> sections = content.getSections();
		int size = sections.size();
		for (int i = 0; i < size; i++) {
			Section cd = sections.get(i);
			List<Item> items = cd.getItems();
			int itemsSize = items.size();
			for (int j = 0; j < itemsSize; j++) {
				ChatItem chatItem = new ChatItem();

				Item item = items.get(j);
				String type = item.getType();
				String value = item.getValue();

				chatItem.setType(type);
				chatItem.setValue(value);
				chatItem.setSection(i);
				chatItem.setRank(j);

				if (Item.type_text.equals(type)) {
					value = wordsFilterBaseService.wordsFilter(value);
					item.setValue(value);
					chatItem.setFilterValue(value);
				}
				chatItemList.add(chatItem);
			}
		}
		return chatItemList;
	}

	/**
	 * 记录聊天室的聊天内容
	 * 
	 * @author: XiaHui
	 * @createDate: 2016年8月19日 下午3:26:10
	 * @update: XiaHui
	 * @updateDate: 2016年8月19日 下午3:26:10
	 */
	@Transactional
	private void addRoomChatLog(UserData userData, String messageId, String roomId, List<ChatItem> chatItemList) {
		messageId = (null == messageId || "".equals(messageId)) ? KeyUtil.getKey() : messageId;
		RoomChatContent content = createRoomChatContent(messageId, roomId, userData);
		content.setTimestamp(System.currentTimeMillis());
		roomChatDAO.save(content);
		List<RoomChatItem> list = new ArrayList<RoomChatItem>();
		for (ChatItem item : chatItemList) {
			RoomChatItem chatLog = new RoomChatItem();
			chatLog.setMessageId(messageId);
			chatLog.setRoomChatContentId(content.getId());
			chatLog.setRoomId(content.getRoomId());
			chatLog.setUserId(content.getUserId());
			chatLog.setRank(item.getRank());
			chatLog.setSection(item.getSection());
			chatLog.setType(item.getType());
			chatLog.setValue(item.getValue());
			chatLog.setFilterValue(item.getFilterValue());
			list.add(chatLog);
		}
		roomChatDAO.saveList(list);
	}

	private RoomChatContent createRoomChatContent(String messageId, String roomId, UserData userData) {
		RoomChatContent chatLog = new RoomChatContent();
		chatLog.setMessageId(messageId);
		chatLog.setRoomId(roomId);
		chatLog.setRoomName("");
		chatLog.setTime(new Timestamp(System.currentTimeMillis()));
		chatLog.setUserHead(userData.getHead());
		chatLog.setUserId(userData.getId());
		chatLog.setUserName(userData.getName());
		chatLog.setUserNickname(userData.getNickname());
		chatLog.setUserRemark("");
		return chatLog;
	}

	/**
	 * 用户对用户聊天
	 * 
	 * @author: XiaHui
	 * @createDate: 2016年8月19日 下午3:26:32
	 * @update: XiaHui
	 * @updateDate: 2016年8月19日 下午3:26:32
	 */
	@Transactional
	public void userChat(Head head, String sendUserId, String receiveUserId, Content content) {
		Message message = new Message();
		message.setHead(head);
		
		wordsFilter(content);
		content.setTime(System.currentTimeMillis());
		
		message.put("sendUserId", sendUserId);
		message.put("receiveUserId", receiveUserId);
		message.put("content", content);
		messageQueueWriteHandler.push(receiveUserId, message);
	}

	/**
	 * 记录用户聊天内容
	 * 
	 * @author: XiaHui
	 * @createDate: 2016年8月19日 下午3:26:46
	 * @update: XiaHui
	 * @updateDate: 2016年8月19日 下午3:26:46
	 */
	@Transactional
	private void addUserChatLog(UserData sendUserData, UserData receiveUserData, String messageId, List<ChatItem> chatItemList) {
		messageId = (null == messageId || "".equals(messageId)) ? KeyUtil.getKey() : messageId;
		UserChatContent content = createUserChatContent(messageId, sendUserData, receiveUserData);
		content.setTimestamp(System.currentTimeMillis());
		userChatDAO.save(content);
		List<UserChatItem> list = new ArrayList<UserChatItem>();
		for (ChatItem item : chatItemList) {
			UserChatItem chatLog = new UserChatItem();
			chatLog.setMessageId(messageId);
			chatLog.setUserChatContentId(content.getId());
			chatLog.setSendUserId(content.getSendUserId());
			chatLog.setReceiveUserId(content.getReceiveUserId());
			chatLog.setRank(item.getRank());
			chatLog.setSection(item.getSection());
			chatLog.setType(item.getType());
			chatLog.setValue(item.getValue());
			chatLog.setFilterValue(item.getFilterValue());
			list.add(chatLog);
		}
		userChatDAO.saveList(list);
	}

	private UserChatContent createUserChatContent(String messageId, UserData sendUserData, UserData receiveUserData) {
		UserChatContent chatLog = new UserChatContent();
		chatLog.setMessageId(messageId);
		// chatLog.setDeleted(deleted);
		// chatLog.setIsSend(isSend);
		chatLog.setReceiveUserHead(receiveUserData.getHead());
		chatLog.setReceiveUserId(receiveUserData.getId());
		chatLog.setReceiveUserName(receiveUserData.getName());
		chatLog.setReceiveUserNickname(receiveUserData.getNickname());
		chatLog.setTime(new Timestamp(System.currentTimeMillis()));
		chatLog.setSendUserHead(sendUserData.getHead());
		chatLog.setSendUserId(sendUserData.getId());
		chatLog.setSendUserName(sendUserData.getName());
		chatLog.setSendUserNickname(sendUserData.getNickname());
		chatLog.setSendUserRemark("");
		return chatLog;
	}
}
