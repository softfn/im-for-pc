package com.im.business.common.service.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.im.common.box.RoomBox;
import com.im.common.box.UserDataBox;
import com.im.business.server.message.data.UserData;

/**
 * @author Only
 * @date 2016年5月24日 上午10:15:52
 */
@Service
public class UserBaseService {

	protected final Logger logger = LogManager.getLogger(this.getClass());

	private Map<String, String> userStatusMap = new ConcurrentSkipListMap<String, String>();

	public UserData getUserData(String userId) {
		UserData u = UserDataBox.get(userId);
		return u;
	}

	public List<UserData> getUserDataList(List<String> userIdList) {
		List<UserData> list = new ArrayList<UserData>();
		for (String userId : userIdList) {
			UserData u = UserDataBox.get(userId);
			list.add(u);
		}
		return list;
	}

	public List<UserData> getUserDataListByRoomId(String roomId) {
		List<String> userIdList = RoomBox.getUserKeyList(roomId);
		List<UserData> list = getUserDataList(userIdList);
		return list;
	}

	public void put(String userId, UserData userData) {
		UserDataBox.put(userId, userData);
	}

	public UserData remove(String userId) {
		UserData u = UserDataBox.remove(userId);
		userStatusMap.remove(userId);
		return u;
	}

	public void putUserStatus(String userId, String status) {
		userStatusMap.put(userId, status);
	}

	public String getUserStatus(String userId) {
		return userStatusMap.get(userId);
	}
}
