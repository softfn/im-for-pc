package com.oim.core.business.manage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.oim.core.app.AppContext;
import com.oim.core.bean.Group;
import com.oim.core.bean.GroupCategory;
import com.oim.core.bean.GroupCategoryMember;
import com.oim.core.bean.User;
import com.oim.core.bean.UserCategory;
import com.oim.core.bean.UserCategoryMember;
import com.oim.core.common.app.manage.Manage;
import com.oim.core.common.app.view.MainView;
import com.oim.core.common.box.GroupBox;
import com.oim.core.common.box.UserDataBox;
import com.oim.core.net.message.data.UserData;

/**
 * 描述： 主界面好友列表、群列表的管理
 * 
 * @author XiaHui
 * @date 2015年4月12日 上午10:18:18
 * @version 0.0.1
 */
public class ListManage extends Manage {

	private Map<String, UserCategory> userCategoryMap = new ConcurrentHashMap<String, UserCategory>();
	private Map<String, List<UserCategoryMember>> userCategoryMemberListMap = new ConcurrentHashMap<String, List<UserCategoryMember>>();
	private Map<String, UserCategoryMember> userCategoryMemberMap = new ConcurrentHashMap<String, UserCategoryMember>();

	private Map<String, GroupCategory> groupCategoryMap = new ConcurrentHashMap<String, GroupCategory>();
	private Map<String, List<GroupCategoryMember>> groupCategoryMemberListMap = new ConcurrentHashMap<String, List<GroupCategoryMember>>();
	private Map<String, GroupCategoryMember> groupCategoryMemberMap = new ConcurrentHashMap<String, GroupCategoryMember>();

	public ListManage(AppContext appContext) {
		super(appContext);
		initEvent();
	}

	private void initEvent() {
	}

	public void put(UserCategoryMember userCategoryMember) {
		List<UserCategoryMember> userCategoryMemberList = getUserCategoryMemberList(userCategoryMember.getUserCategoryId());
		if (null == userCategoryMemberList) {
			userCategoryMemberList = new ArrayList<UserCategoryMember>();
			userCategoryMemberListMap.put(userCategoryMember.getUserCategoryId(), userCategoryMemberList);
		}
		userCategoryMemberList.add(userCategoryMember);
		userCategoryMemberMap.put(userCategoryMember.getMemberUserId(), userCategoryMember);
	}

	public void put(GroupCategoryMember groupCategoryMember) {
		List<GroupCategoryMember> list = getGroupCategoryMemberList(groupCategoryMember.getGroupCategoryId());
		if (null == list) {
			list = new ArrayList<GroupCategoryMember>();
			groupCategoryMemberListMap.put(groupCategoryMember.getGroupCategoryId(), list);
		}
		list.add(groupCategoryMember);
		groupCategoryMemberMap.put(groupCategoryMember.getGroupId(), groupCategoryMember);
	}

	public List<UserCategoryMember> getUserCategoryMemberList(String userCategoryId) {
		List<UserCategoryMember> userCategoryMemberList = userCategoryMemberListMap.get(userCategoryId);
		if (null == userCategoryMemberList) {
			userCategoryMemberList = new ArrayList<UserCategoryMember>();
			userCategoryMemberListMap.put(userCategoryId, userCategoryMemberList);
		}
		return userCategoryMemberList;
	}

	public List<GroupCategoryMember> getGroupCategoryMemberList(String groupCategoryId) {
		List<GroupCategoryMember> list = groupCategoryMemberListMap.get(groupCategoryId);
		if (null == list) {
			list = new ArrayList<GroupCategoryMember>();
			groupCategoryMemberListMap.put(groupCategoryId, list);
		}
		return list;
	}

	public int getUserCategoryMemberSize(String userCategoryId) {
		List<UserCategoryMember> userCategoryMemberList = userCategoryMemberListMap.get(userCategoryId);
		return null == userCategoryMemberList ? 0 : userCategoryMemberList.size();
	}

	public int getGroupCategoryMemberSize(String groupCategoryId) {
		List<GroupCategoryMember> list = groupCategoryMemberListMap.get(groupCategoryId);
		return null == list ? 0 : list.size();
	}

	public void putUserCategory(String id, UserCategory userCategory) {
		userCategoryMap.put(id, userCategory);
	}

	public void putGroupCategory(String id, GroupCategory groupCategory) {
		groupCategoryMap.put(id, groupCategory);
	}

	public UserCategory getUserCategory(String id) {
		return userCategoryMap.get(id);
	}

	public GroupCategory getGroupCategory(String id) {
		return groupCategoryMap.get(id);
	}

	public List<UserCategory> getUserCategoryList() {
		return new ArrayList<UserCategory>(userCategoryMap.values());
	}

	public List<GroupCategory> getGroupCategoryList() {
		return new ArrayList<GroupCategory>(groupCategoryMap.values());
	}

	public void setUserCategoryWithUserList(List<UserCategory> userCategoryList, List<UserData> userDataList, List<UserCategoryMember> userCategoryMemberList) {
		setUserCategoryList(userCategoryList);
		setUserDataList(userDataList);
		setUserCategoryMemberList(userCategoryMemberList);
	}

	public void setGroupCategoryWithGroupList(List<GroupCategory> groupCategoryList, List<Group> groupList, List<GroupCategoryMember> groupCategoryMemberList) {
		setGroupCategoryList(groupCategoryList);
		setGroupList(groupList);
		setGroupCategoryMemberList(groupCategoryMemberList);
	}

	public void setUserCategoryList(List<UserCategory> userCategoryList) {
		if (null != userCategoryList) {
			for (UserCategory userCategory : userCategoryList) {
				addUserCategory(userCategory);
			}
		}
	}

	public void setGroupCategoryList(List<GroupCategory> groupCategoryList) {
		if (null != groupCategoryList) {
			for (GroupCategory groupCategory : groupCategoryList) {
				addGroupCategory(groupCategory);
			}
		}
	}

	public void setUserDataList(List<UserData> userDataList) {
		if (null != userDataList) {
			for (UserData userData : userDataList) {
				addUserData(userData);
			}
		}
	}

	public void setGroupList(List<Group> groupList) {
		if (null != groupList) {
			for (Group group : groupList) {
				addGroup(group);
			}
		}
	}

	public void setUserCategoryMemberList(List<UserCategoryMember> userCategoryMemberList) {
		if (null != userCategoryMemberList) {
			for (UserCategoryMember userCategoryMember : userCategoryMemberList) {
				addUserCategoryMember(userCategoryMember);
			}
			updateCategoryMemberCount();
		}
	}

	public void setGroupCategoryMemberList(List<GroupCategoryMember> groupCategoryMemberList) {
		if (null != groupCategoryMemberList) {
			for (GroupCategoryMember groupCategoryMember : groupCategoryMemberList) {
				addGroupCategoryMember(groupCategoryMember);
			}
			updateGroupCategoryMemberCount();
		}
	}

	public void addUserCategory(UserCategory userCategory) {
		putUserCategory(userCategory.getId(), userCategory);
		MainView mainView = this.appContext.getSingleView(MainView.class);
		mainView.addOrUpdateUserCategory(userCategory);
	}

	public void addGroupCategory(GroupCategory groupCategory) {
		putGroupCategory(groupCategory.getId(), groupCategory);
		MainView mainView = this.appContext.getSingleView(MainView.class);
		mainView.addOrUpdateGroupCategory(groupCategory);
	}

	/**
	 * 插入用户信息
	 * 
	 * @Author: XiaHui
	 * @Date: 2016年2月16日
	 * @ModifyUser: XiaHui
	 * @ModifyDate: 2016年2月16日
	 * @param userData
	 */
	public void addUserData(UserData userData) {
		UserDataBox.put(userData.getId(), userData);
	}

	/**
	 * 插入群信息
	 * 
	 * @Author: XiaHui
	 * @Date: 2016年2月16日
	 * @ModifyUser: XiaHui
	 * @ModifyDate: 2016年2月16日
	 * @param group
	 */
	public void addGroup(Group group) {
		GroupBox.put(group.getId(), group);
	}

	/***
	 * 插入好友分组信息
	 * 
	 * @Author: XiaHui
	 * @Date: 2016年2月16日
	 * @ModifyUser: XiaHui
	 * @ModifyDate: 2016年2月16日
	 * @param userCategoryMember
	 */
	public void addUserCategoryMember(UserCategoryMember userCategoryMember) {

		this.put(userCategoryMember);
		MainView mainView = this.appContext.getSingleView(MainView.class);
		String userId = userCategoryMember.getMemberUserId();
		String userCategoryId = userCategoryMember.getUserCategoryId();

		UserData userData = UserDataBox.get(userId);
		if (null != userData) {
			userData.setRemark(userCategoryMember.getRemark());
			mainView.addOrUpdateUserData(userCategoryId, userData);
		}
	}

	/**
	 * 插入群分组信息
	 * 
	 * @Author: XiaHui
	 * @Date: 2016年2月16日
	 * @ModifyUser: XiaHui
	 * @ModifyDate: 2016年2月16日
	 * @param groupCategoryMember
	 */
	public void addGroupCategoryMember(GroupCategoryMember groupCategoryMember) {
		this.put(groupCategoryMember);
		MainView mainView = this.appContext.getSingleView(MainView.class);
		String groupId = groupCategoryMember.getGroupId();
		String groupCategoryId = groupCategoryMember.getGroupCategoryId();
		Group group = GroupBox.get(groupId);
		if (null != group) {
			mainView.addOrUpdateGroup(groupCategoryId, group);
		}
	}

	/**
	 * 插入一个用户到好友分组列表
	 * 
	 * @Author: XiaHui
	 * @Date: 2016年2月16日
	 * @ModifyUser: XiaHui
	 * @ModifyDate: 2016年2月16日
	 * @param userData
	 * @param userCategoryMember
	 */
	public void add(UserData userData, UserCategoryMember userCategoryMember) {
		addUserData(userData);
		addUserCategoryMember(userCategoryMember);
		updateCategoryMemberCount(userCategoryMember.getUserCategoryId());
	}

	/**
	 * 插入一个群到群分组列表
	 * 
	 * @Author: XiaHui
	 * @Date: 2016年2月16日
	 * @ModifyUser: XiaHui
	 * @ModifyDate: 2016年2月16日
	 * @param group
	 * @param groupCategoryMember
	 */
	public void add(Group group, GroupCategoryMember groupCategoryMember) {
		addGroup(group);
		addGroupCategoryMember(groupCategoryMember);
		updateGroupCategoryMemberCount(groupCategoryMember.getGroupCategoryId());
	}

	/**
	 * 更新所有的好友分组的在线人数和总人数
	 * 
	 * @Author: XiaHui
	 * @Date: 2016年2月16日
	 * @ModifyUser: XiaHui
	 * @ModifyDate: 2016年2月16日
	 */
	public void updateCategoryMemberCount() {
		List<UserCategory> userCategoryList = getUserCategoryList();
		for (UserCategory userCategory : userCategoryList) {
			updateCategoryMemberCount(userCategory.getId());
		}
	}

	/**
	 * 更新所有的群分组的数量信息
	 * 
	 * @Author: XiaHui
	 * @Date: 2016年2月16日
	 * @ModifyUser: XiaHui
	 * @ModifyDate: 2016年2月16日
	 */
	public void updateGroupCategoryMemberCount() {
		List<GroupCategory> list = getGroupCategoryList();
		for (GroupCategory gc : list) {
			updateGroupCategoryMemberCount(gc.getId());
		}
	}

	/**
	 * 更新好友分组的总数量和在线人数
	 * 
	 * @Author: XiaHui
	 * @Date: 2016年2月16日
	 * @ModifyUser: XiaHui
	 * @ModifyDate: 2016年2月16日
	 * @param userCategoryId
	 */
	public void updateCategoryMemberCount(String userCategoryId) {
		List<UserCategoryMember> userCategoryMemberList = getUserCategoryMemberList(userCategoryId);
		int totalCount = userCategoryMemberList.size();
		int onlineCount = 0;
		for (UserCategoryMember userCategoryMember : userCategoryMemberList) {
			UserData userData = UserDataBox.get(userCategoryMember.getMemberUserId());
			if (!isGray(userData.getStatus())) {
				onlineCount++;
			}
		}
		MainView mainView = this.appContext.getSingleView(MainView.class);
		mainView.updateUserCategoryMemberCount(userCategoryId, totalCount, onlineCount);
	}

	/**
	 * 更新群的分组的数量
	 * 
	 * @Author: XiaHui
	 * @Date: 2016年2月16日
	 * @ModifyUser: XiaHui
	 * @ModifyDate: 2016年2月16日
	 * @param groupCategoryId
	 */
	public void updateGroupCategoryMemberCount(String groupCategoryId) {
		List<GroupCategoryMember> userCategoryMemberList = getGroupCategoryMemberList(groupCategoryId);
		int totalCount = userCategoryMemberList.size();
		MainView mainView = this.appContext.getSingleView(MainView.class);
		mainView.updateGroupCategoryMemberCount(groupCategoryId, totalCount);
	}

	/**
	 * 执行用户信息更新操作
	 * 
	 * @Author: XiaHui
	 * @Date: 2016年2月16日
	 * @ModifyUser: XiaHui
	 * @ModifyDate: 2016年2月16日
	 * @param userData
	 */
	public void updateUserData(UserData userData) {
		if (null != userData) {
			MainView mainView = this.appContext.getSingleView(MainView.class);
			UserDataBox.put(userData.getId(), userData);
			mainView.addOrUpdateUserData("", userData);
		}
	}

	/**
	 * 执行群信息更新操作
	 * 
	 * @Author: XiaHui
	 * @Date: 2016年2月16日
	 * @ModifyUser: XiaHui
	 * @ModifyDate: 2016年2月16日
	 * @param group
	 */
	public void updateGroup(Group group) {
		if (null != group) {
			MainView mainView = this.appContext.getSingleView(MainView.class);
			GroupBox.put(group.getId(), group);
			mainView.addOrUpdateGroup("", group);
		}
	}

	private boolean isGray(String status) {
		boolean gray = true;
		switch (status) {
		case User.status_online:
			gray = false;
			break;
		case User.status_call_me:
			gray = false;
			break;
		case User.status_away:
			gray = false;
			break;
		case User.status_busy:
			gray = false;
			break;
		case User.status_mute:
			gray = false;
			break;
		case User.status_invisible:
			gray = true;
			break;
		case User.status_offline:
			gray = true;
			break;
		default:
			gray = true;
			break;
		}
		return gray;
	}
}
