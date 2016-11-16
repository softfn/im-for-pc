package com.im.business.server.action;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.im.business.server.message.Head;
import com.only.action.annotation.ActionMapping;
import com.only.action.annotation.MethodMapping;
import com.only.parameter.annotation.Parameter;

@Component
@ActionMapping(value = "300")
public class RoomAction {
	protected final Logger logger = LogManager.getLogger(this.getClass());
	

	/**
	 * 用户加入聊天室
	 * @author: XiaHui
	 * @createDate: 2016年8月26日 上午10:15:17
	 * @update: XiaHui
	 * @updateDate: 2016年8月26日 上午10:15:17
	 */
	@MethodMapping(value = "1-0001")
	public void joinRoom(Head head,
			@Parameter("userId") String userId,
			@Parameter("roomId") String roomId) {
		roomId = (null == roomId || "".equals(roomId)) ? "default" : roomId;
	}

	/**
	 * 获取聊天室用户列表
	 * @author: XiaHui
	 * @createDate: 2016年8月26日 上午10:15:35
	 * @update: XiaHui
	 * @updateDate: 2016年8月26日 上午10:15:35
	 */
	@MethodMapping(value = "1-0002")
	public void getRoomUserList(Head head, @Parameter("roomId") String roomId) {
	}
}
