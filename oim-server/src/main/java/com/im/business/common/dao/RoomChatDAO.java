package com.im.business.common.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.im.base.dao.BaseDAO;
import com.im.bean.RoomChatContent;
import com.im.bean.RoomChatItem;
import com.im.business.common.data.RoomChatQuery;
import com.only.query.QueryWrapper;
import com.only.query.hibernate.QueryOption;
import com.only.query.hibernate.QueryOptionType;
import com.only.query.page.QueryPage;
import com.only.query.util.QueryUtil;

/**
 * @author: XiaHui
 * @date: 2016年8月19日 下午1:56:03
 */
@Repository
public class RoomChatDAO extends BaseDAO {

	String space = "roomChat";

	public List<RoomChatContent> queryRoomChatContentList(RoomChatQuery roomChatQuery, QueryPage queryPage) {
		QueryWrapper queryWrapper = new QueryWrapper();
		queryWrapper.setPage(queryPage);
		List<QueryOption> queryOption = new ArrayList<QueryOption>();
		queryOption.add(new QueryOption("text", QueryOptionType.likeAll));
		QueryUtil.getQueryWrapperType(roomChatQuery, queryWrapper, queryOption);

		List<RoomChatContent> list = this.queryPageListByName(space + ".queryRoomChatContentList", queryWrapper, RoomChatContent.class);
		return list;
	}

	public List<RoomChatItem> queryRoomChatItemList(RoomChatQuery roomChatQuery, QueryPage queryPage) {
		QueryWrapper queryWrapper = new QueryWrapper();
		queryWrapper.setPage(queryPage);
		List<QueryOption> queryOption = new ArrayList<QueryOption>();
		queryOption.add(new QueryOption("text", QueryOptionType.likeAll));
		QueryUtil.getQueryWrapperType(roomChatQuery, queryWrapper, queryOption);

		List<RoomChatItem> list = this.queryPageListByName(space + ".queryRoomChatItemList", queryWrapper, RoomChatItem.class);
		return list;
	}

	public List<RoomChatItem> getRoomChatItemList(List<String> messageIds) {
		QueryWrapper queryWrapper = new QueryWrapper();
		queryWrapper.addParameter("messageIds", messageIds);
		List<RoomChatItem> list = this.queryPageListByName(space + ".queryRoomChatItemList", queryWrapper, RoomChatItem.class);
		return list;
	}
}
