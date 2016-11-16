package com.oim.core.business.manage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.oim.core.app.AppContext;
import com.oim.core.bean.Group;
import com.oim.core.common.app.manage.Manage;
import com.oim.core.common.app.view.ChatListView;
import com.oim.core.common.box.UserDataBox;
import com.oim.core.net.message.data.UserData;
import com.oim.core.net.message.data.chat.Content;

/**
 * 对聊天相关的一些管理，如不同用户聊天界面
 * 
 * @author XiaHui
 * @date 2015年3月16日 下午1:37:57
 */
public class ChatManage extends Manage {

	
	Map<String, List<Content>> userChatMap = new HashMap<String, List<Content>>();
	Map<String, List<GroupChatData>> groupChatMap = new HashMap<String, List<GroupChatData>>();
	
	public ChatManage(AppContext appContext) {
		super(appContext);
		initEvent();
	}

	private void initEvent() {
	}

	public void putUserCaht(String userId,Content content) {
		List<Content> list = userChatMap.get(userId);
		if (list==null) {
			list=new ArrayList<Content>();
			userChatMap.put(userId, list);
		}
		list.add(content);
	}

	public void putGroupCaht(String groupId,UserData userData,Content content) {
		List<GroupChatData> list = groupChatMap.get(groupId);
		if (list==null) {
			list=new ArrayList<GroupChatData>();
			groupChatMap.put(groupId, list);
		}
		list.add(new GroupChatData(userData,content));
	}
	
	public boolean isGroupChatShowing(String groupId) {
		ChatListView chatListView = appContext.getSingleView(ChatListView.class);
		return chatListView.isGroupChatShowing(groupId);
	}

	public boolean isUserChatShowing(String userId) {
		ChatListView chatListView = appContext.getSingleView(ChatListView.class);
		return  chatListView.isUserChatShowing(userId);
	}

	public void userChat(UserData userData, Content content) {
		ChatListView chatListView = appContext.getSingleView(ChatListView.class);
		chatListView.userChat(userData, content);
	}

	public void groupChat(Group group, UserData userData, Content content) {
		ChatListView chatListView = appContext.getSingleView(ChatListView.class);
		chatListView.groupChat(group, userData, content);
	}

	public void showCahtFrame(UserData userData) {
		ChatListView chatListView = appContext.getSingleView(ChatListView.class);
		chatListView.show(userData);
		chatListView.setVisible(true);
		
		PromptManage pm = this.appContext.getManage(PromptManage.class);
		pm.showUserHeadPulse(userData.getId(), false);// 停止头像跳动
		pm.remove(userData.getId());// 系统托盘停止跳动
		
	}

	public void showCahtFrame(Group group) {
		ChatListView chatListView = appContext.getSingleView(ChatListView.class);
		chatListView.show(group);
		chatListView.setVisible(true);
		
		PromptManage pm = this.appContext.getManage(PromptManage.class);
		pm.showGroupHeadPulse(group.getId(), false);// 停止头像跳动
		pm.remove(group.getId());// 系统托盘停止跳动
	}
	
	public void showUserCaht(UserData userData) {
		List<Content> list = userChatMap.remove(userData.getId());
		ChatManage chatManage = this.appContext.getManage(ChatManage.class);
		if (null != userData && list != null && !list.isEmpty()) {
			for (Content content : list) {
				chatManage.userChat(userData, content);
			}
		}
	}

	public void showGroupCaht(Group group) {
		List<GroupChatData> list = groupChatMap.remove(group.getId());
		ChatManage chatManage = this.appContext.getManage(ChatManage.class);
		if (null != group && list != null && !list.isEmpty()) {
			for (GroupChatData ucd : list) {
				chatManage.groupChat(group, ucd.userData, ucd.content);
			}
		}
	}
	public void showShake(String sendUserId) {
		
	}
	class GroupChatData{
		
		private UserData userData;
		private Content content;
		
		public GroupChatData(UserData userData,Content content){
			this.userData = userData;
			this.content = content;
		}
		public UserData getUserData() {
			return userData;
		}
		public void setUserData(UserData userData) {
			this.userData = userData;
		}
		public Content getContent() {
			return content;
		}
		public void setContent(Content content) {
			this.content = content;
		}
	}

	public void updateGroupUserList(String groupId) {
		ChatListView chatListView = appContext.getSingleView(ChatListView.class);
		chatListView.updateGroupUserList(groupId);
	}

	public void doShake(String sendUserId) {
		UserData userData = UserDataBox.get(sendUserId);
		if (null == userData) {// 如果发送抖动的不是好友，暂时不支持抖动
			return;
		}
		ChatListView chatListView = appContext.getSingleView(ChatListView.class);
		chatListView.doShake(userData);
	}
}
