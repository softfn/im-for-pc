package com.oim.business.manage;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import com.oim.app.AppContext;
import com.oim.app.UIBox;
import com.oim.bean.Group;
import com.oim.bean.GroupCategory;
import com.oim.bean.GroupCategoryMember;
import com.oim.bean.User;
import com.oim.bean.UserCategory;
import com.oim.bean.UserCategoryMember;
import com.oim.common.app.manage.Manage;
import com.oim.common.box.GroupBox;
import com.oim.common.box.HeadImageIconBox;
import com.oim.common.box.UserDataBox;
import com.oim.net.message.data.UserData;
import com.oim.ui.component.list.HeadLabel;
import com.oim.ui.component.list.HeadLabelAction;
import com.oim.ui.component.list.Node;
import com.oim.ui.view.MainView;

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

	private HeadLabelAction userLabelAction;
	private HeadLabelAction groupLabelAction;

	public ListManage(AppContext appContext) {
		super(appContext);
		initEvent();
	}

	private void initEvent() {
		userLabelAction = new HeadLabelAction() {// 好友头像点击事件

			@Override
			public void action(MouseEvent e, HeadLabel headLabel) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					if (e.getClickCount() == 2) {
						UserData userData = headLabel.getAttribute(UserData.class.getName());
						ChatManage chatManage = appContext.getManage(ChatManage.class);
						chatManage.showCahtFrame(userData);
					}
				}
			}
		};

		groupLabelAction = new HeadLabelAction() {// 群头像点击事件

			@Override
			public void action(MouseEvent e, HeadLabel headLabel) {
				if (e.getClickCount() == 2) {
					Group group = headLabel.getAttribute(Group.class);
					ChatManage chatManage = appContext.getManage(ChatManage.class);
					chatManage.showCahtFrame(group);
				}
			}
		};
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
		userCategoryMap.put(userCategory.getId(), userCategory);
		MainView mainView = this.appContext.getSingleView(MainView.class);
		Node node = UIBox.getUserListNode(userCategory.getId());
		if (null == node) {
			node = new Node();
			UIBox.putUserListNode(userCategory.getId(), node);
		}
		mainView.addUserCategoryNode(node);
		node.setTitleText(userCategory.getName());
		node.setCountText("[0/0]");
	}

	public void addGroupCategory(GroupCategory groupCategory) {
		groupCategoryMap.put(groupCategory.getId(), groupCategory);
		MainView mainView = this.appContext.getSingleView(MainView.class);
		Node node = UIBox.getGroupListNode(groupCategory.getId());
		if (null == node) {
			node = new Node();
			UIBox.putGroupListNode(groupCategory.getId(), node);
		}
		mainView.addGroupCategoryNode(node);
		node.setTitleText(groupCategory.getName());
		node.setCountText("[0]");
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

		HeadLabel head = UIBox.getUserHeadLabel(userData.getId());
		if (null == head) {
			head = new HeadLabel();
			UIBox.putUserHeadLabel(userData.getId(), head);
		}
		head.addAction(userLabelAction);// 添加头像点击事件
		head.addAttribute(UserData.class.getName(), userData);
		head.setRoundedCorner(40, 40);
		head.setHeadIcon(HeadImageIconBox.getUserHeadImageIcon(userData.getHead(), 40, 40));

		head.setRemark(userData.getNickname());// 备注名
		head.setNickname("(" + userData.getAccount() + ")");// 昵称
		head.setShowText(userData.getSignature());// 个性签名
		// head.setStatusIcon(new
		// ImageIcon("Resources/Images/Default/Status/FLAG/Big/MobilePhoneQQOn.png"));
		head.setStatus("[4G]");
		String status = UserData.status_offline;
		if (null != userData.getStatus() && !(UserData.status_invisible.equals(userData.getStatus()))) {
			status = userData.getStatus();
		}
		ImageIcon icon = UserDataBox.getStatusImageIcon(status);
		JLabel statusLabel = head.getAttribute("statusLabel");
		if (null == statusLabel) {// 状态图标显示组件
			statusLabel = new JLabel(icon);
			head.addBusinessAttribute("statusLabel", statusLabel);
		} else {
			statusLabel.setIcon(icon);
		}
		// 如果用户不是在线状态，则使其头像变灰
		if (isGray(userData.getStatus())) {
			head.setGray(true);
		}
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

		HeadLabel head = UIBox.getGroupHeadLabel(group.getId());
		if (null == head) {
			head = new HeadLabel();
			UIBox.putGroupHeadLabel(group.getId(), head);
		}
		head.addAction(groupLabelAction);
		head.addAttribute(Group.class, group);
		head.setRoundedCorner(40, 40);
		head.setHeadIcon(HeadImageIconBox.getGroupHeadImageIcon(group.getHead(), 40, 40));

		head.setRemark(group.getName());
		head.setNickname("(" + group.getNumber() + ")");
		head.setShowText(group.getIntroduce());

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
		HeadLabel head = UIBox.getUserHeadLabel(userCategoryMember.getMemberUserId());
		Node node = UIBox.getUserListNode(userCategoryMember.getUserCategoryId());
		if (null != head && null != node) {
			if (null != userCategoryMember.getRemark() && !"".equals(userCategoryMember.getRemark())) {
				UserData userData = head.getAttribute(UserData.class.getName());
				userData.setRemark(userCategoryMember.getRemark());
				head.setRemark(userCategoryMember.getRemark());
				head.setNickname("(" + userData.getNickname() + ")");
				head.setShowText(userData.getSignature());
			}
			node.addNode(head);
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
		HeadLabel head = UIBox.getGroupHeadLabel(groupCategoryMember.getGroupId());
		Node node = UIBox.getGroupListNode(groupCategoryMember.getGroupCategoryId());
		if (null != head && null != node) {
			if (null != groupCategoryMember.getRemark() && !"".equals(groupCategoryMember.getRemark())) {
				Group group = head.getAttribute(Group.class);
				head.setRemark(groupCategoryMember.getRemark());
				head.setNickname("(" + group.getNumber() + ")");
				head.setShowText(group.getIntroduce());
			}
			node.addNode(head);
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
		int count = userCategoryMemberList.size();
		int onlineCount = 0;
		for (UserCategoryMember userCategoryMember : userCategoryMemberList) {
			UserData userData = UserDataBox.get(userCategoryMember.getMemberUserId());
			if (!isGray(userData.getStatus())) {
				onlineCount++;
			}
		}
		Node node = UIBox.getUserListNode(userCategoryId);
		if (null != node) {
			node.setCountText("[" + onlineCount + "/" + count + "]");
		}
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
		int count = userCategoryMemberList.size();
		Node node = UIBox.getGroupListNode(groupCategoryId);
		if (null != node) {
			node.setCountText("[" + count + "]");
		}
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
		if (null != UserDataBox.get(userData.getId())) {
			UserDataBox.put(userData.getId(), userData);
		}
		UserCategoryMember userCategoryMember = userCategoryMemberMap.get(userData.getId());
		HeadLabel head = UIBox.getUserHeadLabel(userData.getId());
		if (null != head) {

			if (null != userCategoryMember && null != userCategoryMember.getRemark() && !"".equals(userCategoryMember.getRemark())) {
				userData.setRemark(userCategoryMember.getRemark());
				head.setRemark(userCategoryMember.getRemark());
				head.setNickname("(" + userData.getNickname() + ")");
				head.setShowText(userData.getSignature());
			} else {
				head.setRemark(userData.getNickname());
				head.setNickname("(" + userData.getAccount() + ")");
				head.setShowText(userData.getSignature());
			}
			if (isGray(userData.getStatus())) {
				head.setGray(true);
			} else {
				head.setGray(false);
			}
			head.addAttribute(UserData.class.getName(), userData);
			head.setRoundedCorner(40, 40);
			head.setHeadIcon(HeadImageIconBox.getUserHeadImageIcon(userData.getHead(), 40, 40));
			head.setStatus("[2G]");
			ImageIcon icon = UserDataBox.getStatusImageIcon((UserData.status_invisible.equals(userData.getStatus())) ? UserData.status_offline : userData.getStatus());
			JLabel statusLabel = head.getAttribute("statusLabel");
			if (null == statusLabel) {
				statusLabel = new JLabel(icon);
				head.addBusinessAttribute("statusLabel", statusLabel);
			} else {
				statusLabel.setIcon(icon);
			}
			head.revalidate();
		}
		if (null != userCategoryMember) {
			updateCategoryMemberCount(userCategoryMember.getUserCategoryId());
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
		GroupCategoryMember groupCategoryMember = groupCategoryMemberMap.get(group.getId());
		HeadLabel head = UIBox.getGroupHeadLabel(group.getId());
		if (null != head) {
			if (null != groupCategoryMember.getRemark() && !"".equals(groupCategoryMember.getRemark())) {
				head.setRemark(groupCategoryMember.getRemark());
				head.setNickname("(" + group.getNumber() + ")");
				head.setShowText(group.getIntroduce());
			} else {
				head.setRemark(group.getName());
				head.setNickname("(" + group.getNumber() + ")");
				head.setShowText(group.getIntroduce());
			}
			head.addAttribute(Group.class, group);
			head.setRoundedCorner(40, 40);
			head.setHeadIcon(HeadImageIconBox.getGroupHeadImageIcon(group.getHead(), 40, 40));
			head.setStatus("[2G]");
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
