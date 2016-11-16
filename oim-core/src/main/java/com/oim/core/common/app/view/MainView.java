package com.oim.core.common.app.view;

import com.oim.core.bean.Group;
import com.oim.core.bean.GroupCategory;
import com.oim.core.bean.User;
import com.oim.core.bean.UserCategory;
import com.oim.core.net.message.data.UserData;

/**
 * 描述：
 * 
 * @author XiaHui
 * @date 2015年3月10日 下午10:20:49
 * @version 0.0.1
 */

public interface MainView extends View {

	public void setStatus(String status);

	public void setUser(User user);

	public void addOrUpdateGroupCategory(GroupCategory groupCategory);

	public void addOrUpdateGroup(String groupCategoryId, Group group);

	public void addOrUpdateUserCategory(UserCategory userCategory);

	public void addOrUpdateUserData(String userCategoryId, UserData userData);

	public void updateUserCategoryMemberCount(String userCategoryId, int totalCount, int onlineCount);

	public void updateGroupCategoryMemberCount(String groupCategoryId, int totalCount);

	public void showUserHeadPulse(String userId, boolean pulse);

	public void showGroupHeadPulse(String groupId, boolean pulse);
	
	public void addLastGroup(Group group);

	public void addLastUserData(UserData userData);

	public void updateLastGroup(Group group) ;

	public void updateLastUserData(UserData userData);

}
