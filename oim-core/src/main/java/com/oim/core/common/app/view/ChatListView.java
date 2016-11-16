package com.oim.core.common.app.view;

import com.oim.core.bean.Group;
import com.oim.core.net.message.data.UserData;
import com.oim.core.net.message.data.chat.Content;

/**
 * 描述：
 * 
 * @author XiaHui
 * @date 2015年3月14日 上午11:14:38
 * @version 0.0.1
 */
public interface ChatListView extends View {

	public boolean isGroupChatShowing(String groupId);

	public boolean isUserChatShowing(String userId);
	
	public boolean hasGroupChat(String groupId);

	public boolean hasUserChat(String userId);

	public void show(UserData userData);

	public void show(Group group);
	
	public void userChat(UserData userData,Content content);
	
	public void groupChat(Group group,UserData userData,Content content);
	
	public void updateGroupUserList(String groupId);

	public void doShake(UserData userData);
}
