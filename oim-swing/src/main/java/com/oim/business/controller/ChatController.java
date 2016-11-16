package com.oim.business.controller;

import com.oim.app.AppContext;
import com.oim.business.manage.ChatManage;
import com.oim.business.manage.PromptManage;
import com.oim.business.service.ChatService;
import com.oim.common.annotation.ActionMapping;
import com.oim.common.annotation.MethodMapping;
import com.oim.common.annotation.Parameter;
import com.oim.common.app.controller.AbstractController;
import com.oim.common.sound.SoundHandler;
import com.oim.net.message.data.chat.Content;

/**
 * 描述：负责接受聊天相关业务的控制层
 * 
 * @author 夏辉
 * @date 2014年6月14日 下午9:31:55
 * @version 0.0.1
 */

@ActionMapping(value = BaseAction.action_chat)
public class ChatController extends AbstractController {

	public ChatController(AppContext appContext) {
		super(appContext);
	}

	/**
	 * 收到用户聊天信息
	 * 
	 * @param chatMessage
	 */
	@MethodMapping(value = "1-1001")
	public void getUserChatMessage(
			@Parameter("sendUserId") String sendUserId,
			@Parameter("receiveUserId") String receiveUserId,
			@Parameter("content") Content content) {
		ChatService chatService = appContext.getService(ChatService.class);
		chatService.userChat(sendUserId, content);
	}

	/**
	 * 收到抖动窗口信息
	 * 
	 * @param chatMessage
	 */
	@MethodMapping(value = "1-1002")
	public void getShake(@Parameter("sendUserId") String sendUserId) {
		PromptManage pm = this.appContext.getManage(PromptManage.class);
		pm.playSound(SoundHandler.sound_type_shake);// 播放抖动声音
		ChatManage chatManage = appContext.getManage(ChatManage.class);
		chatManage.doShake(sendUserId);// 执行抖动效果
	}

	/**
	 * 收到群信息
	 * 
	 * @param chatMessage
	 */
	@MethodMapping(value = "1-2001")
	public void getGroupChatMessage(
			@Parameter("userId") String userId,
			@Parameter("groupId") String groupId,
			@Parameter("content") Content content) {
		ChatService chatService = appContext.getService(ChatService.class);
		chatService.groupChat(userId, groupId, content);
	}
}
