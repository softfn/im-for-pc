package com.oim.core.business.action;

import com.oim.core.app.AppContext;
import com.oim.core.business.service.ChatService;
import com.oim.core.common.annotation.ActionMapping;
import com.oim.core.common.annotation.MethodMapping;
import com.oim.core.common.annotation.Parameter;
import com.oim.core.common.app.action.AbstractAction;
import com.oim.core.net.message.data.chat.Content;

/**
 * 描述：负责接受聊天相关业务的控制层
 * 
 * @author 夏辉
 * @date 2014年6月14日 下午9:31:55
 * @version 0.0.1
 */

@ActionMapping(value = BaseAction.action_chat)
public class ChatAction extends AbstractAction {

	public ChatAction(AppContext appContext) {
		super(appContext);
	}

	/**
	 * 收到用户聊天信息
	 */
	@MethodMapping(value = "1-1001")
	public void receiveUserChatMessage(
			@Parameter("sendUserId") String sendUserId,
			@Parameter("receiveUserId") String receiveUserId,
			@Parameter("content") Content content) {
		ChatService chatService=this.getService(ChatService.class);
		chatService.receiveUserChatMessage(sendUserId,content);
	}

	/**
	 * 收到抖动窗口信息
	 */
	@MethodMapping(value = "1-1002")
	public void receiveShake(@Parameter("sendUserId") String sendUserId) {
		ChatService chatService=this.getService(ChatService.class);
		chatService.receiveShake(sendUserId);
	}

	/**
	 * 收到群信息
	 */
	@MethodMapping(value = "1-2001")
	public void receiveGroupChatMessage(
			@Parameter("userId") String userId,
			@Parameter("groupId") String groupId,
			@Parameter("content") Content content) {
		ChatService chatService=this.getService(ChatService.class);
		chatService.receiveGroupChatMessage(userId,groupId,content);
	}
}
