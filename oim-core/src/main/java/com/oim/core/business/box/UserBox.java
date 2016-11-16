package com.oim.core.business.box;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.oim.core.bean.UserCategory;
import com.oim.core.bean.UserCategoryMember;

/**
 * @author: XiaHui
 * @date: 2016年9月28日 下午3:18:16
 */
public class UserBox {

	private Map<String, UserCategory> userCategoryMap = new ConcurrentHashMap<String, UserCategory>();
	private Map<String, List<UserCategoryMember>> userCategoryMemberListMap = new ConcurrentHashMap<String, List<UserCategoryMember>>();
	private Map<String, UserCategoryMember> userCategoryMemberMap = new ConcurrentHashMap<String, UserCategoryMember>();

	public void put(UserCategoryMember userCategoryMember) {
		List<UserCategoryMember> userCategoryMemberList = getUserCategoryMemberList(userCategoryMember.getUserCategoryId());
		if (null == userCategoryMemberList) {
			userCategoryMemberList = new ArrayList<UserCategoryMember>();
			userCategoryMemberListMap.put(userCategoryMember.getUserCategoryId(), userCategoryMemberList);
		}
		userCategoryMemberList.add(userCategoryMember);
		userCategoryMemberMap.put(userCategoryMember.getMemberUserId(), userCategoryMember);
	}

	public List<UserCategoryMember> getUserCategoryMemberList(String userCategoryId) {
		List<UserCategoryMember> userCategoryMemberList = userCategoryMemberListMap.get(userCategoryId);
		if (null == userCategoryMemberList) {
			userCategoryMemberList = new ArrayList<UserCategoryMember>();
			userCategoryMemberListMap.put(userCategoryId, userCategoryMemberList);
		}
		return userCategoryMemberList;
	}

	public void putUserCategory(String id, UserCategory userCategory) {
		userCategoryMap.put(id, userCategory);
	}

	public UserCategory getUserCategory(String id) {
		return userCategoryMap.get(id);
	}

	public List<UserCategory> getUserCategoryList() {
		return new ArrayList<UserCategory>(userCategoryMap.values());
	}

	public int getUserCategoryMemberSize(String userCategoryId) {
		List<UserCategoryMember> userCategoryMemberList = userCategoryMemberListMap.get(userCategoryId);
		return null == userCategoryMemberList ? 0 : userCategoryMemberList.size();
	}
}
