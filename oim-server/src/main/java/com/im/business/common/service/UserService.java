package com.im.business.common.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.im.bean.User;
import com.im.bean.UserCategory;
import com.im.bean.UserCategoryMember;
import com.im.business.common.dao.NumberDAO;
import com.im.business.common.dao.UserCategoryDAO;
import com.im.business.common.dao.UserCategoryMemberDAO;
import com.im.business.common.dao.UserDAO;
import com.im.business.common.service.api.CacheBaseService;
import com.im.business.common.service.api.UserBaseService;
import com.im.business.server.message.Head;
import com.im.business.server.message.data.UserData;
import com.im.business.server.message.data.query.UserQuery;
import com.im.business.server.push.UserPush;
import com.only.query.page.QueryPage;

/**
 * @author Only
 * @date 2016年5月20日 上午11:45:04
 */
@Service
@Transactional
public class UserService {
	protected final Logger logger = LogManager.getLogger(this.getClass());
	@Resource
	private UserDAO userDAO;
	@Resource
	private NumberDAO numberDAO;
	@Resource
	private UserCategoryDAO userCategoryDAO;
	@Resource
	private UserCategoryMemberDAO userCategoryMemberDAO;
	@Resource
	private CacheBaseService cacheBox;
	@Resource
	private UserBaseService userBaseService;
	@Resource
	private UserPush userPush;

	public User getUser(String userId) {
		return userDAO.get(userId);
	}

	/**
	 * 获取好友分组信息
	 * 
	 * @param userId
	 * @return
	 */
	public List<UserCategory> getUserCategoryList(String userId) {
		return userCategoryDAO.getListByUserId(userId);
	}

	/**
	 * 获取自己的好友列表集合
	 * 
	 * @param userId
	 * @return
	 */
	public List<UserCategoryMember> getUserCategoryMemberList(String userId) {
		return userCategoryMemberDAO.getListByUserId(userId);
	}

	/**
	 * 获取在自己在别人的好友列表中的用户集合
	 * 
	 * @param userId
	 * @return
	 */
	public List<UserCategoryMember> getInUserCategoryMemberList(String userId) {
		return userCategoryMemberDAO.getInListByUserId(userId);
	}

	public List<UserData> getUserCategoryMemberUserDataList(String userId) {
		List<UserData> userDataList = userDAO.getUserCategoryMemberUserDataList(userId);
		return userDataList;
	}

	public UserData getUserDataById(String id) {
		UserData userData = userDAO.getUserDataById(id);
		return userData;
	}

	public List<UserData> queryUserDataList(UserQuery userQuery, QueryPage queryPage) {
		List<UserData> userDataList = userDAO.queryUserList(userQuery, queryPage);
		return userDataList;
	}

	/**
	 * 根据群id，获取所有成员
	 * 
	 * @param groupId
	 * @return
	 */
	public List<UserData> getGroupMemberUserDataListByGroupId(String groupId) {
		List<UserData> userDataList = userDAO.getGroupMemberUserDataListByGroupId(groupId);
		return userDataList;
	}

	/**
	 * 根用户id，获取他所在群的所有成员
	 * 
	 * @param userId
	 * @return
	 */
	public List<UserData> getGroupMemberUserDataListByUserId(String userId) {
		List<UserData> userDataList = userDAO.getGroupMemberUserDataListByUserId(userId);
		return userDataList;
	}

	public void removeUser(String userId) {
		userBaseService.remove(userId);
		sendUserStatus(userId, UserData.status_offline);
	}

	public void setUserStatus(List<UserData> userDataList) {
		if (null != userDataList) {
			for (UserData userData : userDataList) {
				setUserStatus(userData);
			}
		}
	}

	public void setUserStatus(UserData userData) {
		if (null != userData) {
			String status = userBaseService.getUserStatus(userData.getId());
			status = (null == status) ? UserData.status_offline : status;
			userData.setStatus(status);
		}
	}

	public void sendUserStatus(String userId, String status) {
		Head head = new Head();
		head.setAction("101");
		head.setMethod("0008");
		List<UserCategoryMember> memberList = getInUserCategoryMemberList(userId);
		List<UserData> userDataList = getGroupMemberUserDataListByUserId(userId);

		Map<String, String> map = new HashMap<String, String>();
		for (UserCategoryMember ucm : memberList) {
			map.put(ucm.getOwnUserId(), ucm.getOwnUserId());
		}
		for (UserData ud : userDataList) {
			map.put(ud.getId(), ud.getId());
		}
		List<String> keyList = new ArrayList<String>(map.values());
		
		userPush.pushUserStatus(head, userId, status, keyList);
	}
}
