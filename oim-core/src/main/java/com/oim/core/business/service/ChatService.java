package com.oim.core.business.service;

import com.oim.core.app.AppContext;
import com.oim.core.bean.Group;
import com.oim.core.business.manage.ChatManage;
import com.oim.core.business.manage.PromptManage;
import com.oim.core.business.sender.UserSender;
import com.oim.core.common.action.CallAction;
import com.oim.core.common.annotation.Parameter;
import com.oim.core.common.app.dto.PromptData.IconType;
import com.oim.core.common.app.service.Service;
import com.oim.core.common.box.GroupBox;
import com.oim.core.common.box.UserDataBox;
import com.oim.core.common.box.UserDataTempBox;
import com.oim.core.common.sound.SoundHandler;
import com.oim.core.net.message.data.UserData;
import com.oim.core.net.message.data.chat.Content;
import com.oim.core.net.server.Back;
import com.only.net.data.action.DataBackAction;
import com.only.net.data.action.DataBackActionAdapter;

/**
 * 描述：
 * 
 * @author 夏辉
 * @date 2014年3月31日 上午11:45:15 version 0.0.1
 */
public class ChatService extends Service {

	

	public ChatService(AppContext appContext) {
		super(appContext);
	}

	public void receiveUserChatMessage(String sendUserId, Content content) {
		UserData userData = UserDataBox.get(sendUserId);// 先从好友集合里面获取用户信息，如果用户不是好友，那么从服务器下载用户信息
		if (null == userData) {// 为null说明发送信息的不在好友列表，那么就要从服务器获取发送信息用户的信息了
			DataBackAction dataBackAction = new DataBackActionAdapter() {

				@Back
				public void back(@Parameter("userData") UserData userData) {
					UserDataTempBox.put(userData.getId(), userData);
					showChatData(userData, content);
				}
			};
			UserSender uh = this.appContext.getSender(UserSender.class);
			uh.getUserDataById(sendUserId, dataBackAction);
		} else {
			showChatData(userData, content);
		}
	}

	public void receiveShake(String sendUserId) {
		PromptManage pm = this.appContext.getManage(PromptManage.class);
		pm.playSound(SoundHandler.sound_type_shake);// 播放抖动声音
		ChatManage chatManage = appContext.getManage(ChatManage.class);
		chatManage.doShake(sendUserId);// 执行抖动效果
	}

	public void receiveGroupChatMessage(String userId, String groupId, Content content) {
		Group group = GroupBox.get(groupId);// 先从好友集合里面获取用户信息，如果用户不是好友，那么从服务器下载用户信息
		if (null != group) {
			UserData userData = UserDataTempBox.get(userId);
			if (null == userData) {
				DataBackAction dataBackAction = new DataBackActionAdapter() {
					@Back
					public void back(@Parameter("userData") UserData userData) {
						UserDataTempBox.put(userData.getId(), userData);
						showChatData(group, userData, content);
					}
				};
				UserSender uh = this.appContext.getSender(UserSender.class);
				uh.getUserDataById(userId, dataBackAction);
			} else {
				showChatData(group, userData, content);
			}
		}
	}

	/**
	 * 显示聊天信息
	 * 
	 * @param userData
	 * @param chatDataList
	 */
	private void showChatData(UserData userData, Content content) {
		ChatManage chatManage = this.appContext.getManage(ChatManage.class);
		if (!chatManage.isUserChatShowing(userData.getId())) {// 如果正显示的聊天窗口并不是发送信息的用户，那么就要跳动其头像、以及闪动系统托盘
			CallAction callAction = new CallAction() {
				@Override
				public void execute() {
					ChatManage chatManage = appContext.getManage(ChatManage.class);
					chatManage.showCahtFrame(userData);
				}
			};
			chatManage.putUserCaht(userData.getId(),content);
			PromptManage pm = this.appContext.getManage(PromptManage.class);
			pm.put(userData.getId(), IconType.userHead, userData.getHead(), callAction);
			pm.showUserHeadPulse(userData.getId(), true);// 跳动用户头像
			pm.playSound(SoundHandler.sound_type_message);// 播放消息声音
		} else {
			chatManage.userChat(userData, content);
		}
	}

	private void showChatData(Group group, UserData userData, Content content) {
		ChatManage chatManage = this.appContext.getManage(ChatManage.class);
		if (!chatManage.isGroupChatShowing(group.getId())) {

			CallAction callAction = new CallAction() {

				@Override
				public void execute() {
					ChatManage chatManage = appContext.getManage(ChatManage.class);
					chatManage.showCahtFrame(group);
				}
			};
			chatManage.putGroupCaht(group.getId(),userData,content);
			PromptManage pm = this.appContext.getManage(PromptManage.class);
			pm.put(group.getId(), IconType.groupHead, group.getHead(), callAction);
			pm.showGroupHeadPulse(group.getId(), true);
			pm.playSound(SoundHandler.sound_type_message);
		}else{
			chatManage.groupChat(group, userData, content);
		}
	}
}
