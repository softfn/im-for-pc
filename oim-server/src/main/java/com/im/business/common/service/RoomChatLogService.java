package com.im.business.common.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.im.business.common.data.RoomChatQuery;
import com.im.bean.RoomChatContent;
import com.im.bean.RoomChatItem;
import com.im.business.common.dao.RoomChatDAO;
import com.im.business.server.message.Head;
import com.im.business.server.message.Message;
import com.im.business.server.message.data.PageData;
import com.im.business.server.message.data.UserData;
import com.im.business.server.message.data.chat.ChatQueryData;
import com.im.business.server.message.data.chat.Content;
import com.im.business.server.message.data.chat.Item;
import com.im.business.server.message.data.chat.Section;
import com.only.query.page.DefaultPage;

/**
 * @author: XiaHui
 * @date: 2016年8月23日 上午11:20:14
 */
@Service
public class RoomChatLogService {
	protected final Logger logger = LogManager.getLogger(this.getClass());
	@Resource
	RoomChatDAO roomChatDAO;

	public Message queryRoomChatLog(Head head, String roomId, ChatQueryData queryData, PageData page) {
		
		List<Map<String, Object>> contents = queryRoomChatLog( roomId,  queryData,  page) ;
		
		Map<String,Object> p=new HashMap<String,Object>();
		
		p.put("totalPage", page.getTotalPage());
		p.put("pageNumber", page.getPageNumber());
		
		head.setTime(System.currentTimeMillis());

		Message m = new Message();
		m.setHead(head);
		m.put("roomId", roomId);
		m.put("page", p);
		m.put("contents", contents);
		
		return m;
	}
	
	public List<Map<String, Object>> queryRoomChatLog(String roomId, ChatQueryData queryData, PageData page) {

		List<Map<String, Object>> contents = new ArrayList<Map<String, Object>>();

		if (null != roomId && !"".equals(roomId)) {
			RoomChatQuery roomChatQuery = new RoomChatQuery();
			roomChatQuery.setRoomId(roomId);
			roomChatQuery.setText(queryData.getText());
			roomChatQuery.setStartDate(queryData.getStartDate());
			roomChatQuery.setEndDate(queryData.getEndDate());
			DefaultPage defaultPage=new DefaultPage();
			List<RoomChatContent> chatContentList = roomChatDAO.queryRoomChatContentList(roomChatQuery, defaultPage);
			List<String> messageIds = new ArrayList<String>();
			for (RoomChatContent rcc : chatContentList) {
				messageIds.add(rcc.getMessageId());
			}
			Map<String, List<RoomChatItem>> chatItemMap = new HashMap<String, List<RoomChatItem>>();
			if (!messageIds.isEmpty()) {
				List<RoomChatItem> chatItemList = roomChatDAO.getRoomChatItemList(messageIds);
				for (RoomChatItem rci : chatItemList) {
					List<RoomChatItem> list = chatItemMap.get(rci.getMessageId());
					if (null == list) {
						list = new ArrayList<RoomChatItem>();
						chatItemMap.put(rci.getMessageId(), list);
					}
					list.add(rci);
				}
			}

			for (RoomChatContent rcc : chatContentList) {

				Map<String, Object> map = new HashMap<String, Object>();
				Content content = new Content();
				UserData userData = new UserData();

				userData.setHead(rcc.getUserHead());
				userData.setId(rcc.getUserId());
				userData.setName(rcc.getUserName());
				userData.setNickname(rcc.getUserNickname());
				long timestamp =rcc.getTimestamp();
				List<RoomChatItem> list = chatItemMap.get(rcc.getMessageId());
				List<Section> sections = new ArrayList<Section>();
				if (null != list&&!list.isEmpty()) {
					int index = -1;
					Section section;
					List<Item> items = null;
					for (RoomChatItem rci : list) {
						if (index != rci.getSection()) {
							index = rci.getSection();

							items = new ArrayList<Item>();
							section = new Section();
							section.setItems(items);

							sections.add(section);
						}
						
						Item item = new Item();
						item.setType(rci.getType());
						item.setValue(rci.getFilterValue());
						if (null != items) {
							items.add(item);
						}
					}
					content.setSections(sections);
					map.put("userData", userData);
					map.put("content", content);
					map.put("timestamp", timestamp);
					contents.add(0, map);
				}
			}
		}
		return contents;
	}
}
