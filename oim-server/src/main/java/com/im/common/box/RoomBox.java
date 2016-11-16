package com.im.common.box;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author Only
 * @date 2016年5月24日 上午9:28:08
 */
public class RoomBox {
	// 存放用户加入聊天室的key
	private static Map<String, CopyOnWriteArraySet<String>> userJoinRoomMap = new ConcurrentSkipListMap<String, CopyOnWriteArraySet<String>>();
	// 存放聊天室中的用户key
	private static Map<String, ConcurrentSkipListSet<String>> roomUserKeyMap = new ConcurrentSkipListMap<String, ConcurrentSkipListSet<String>>();

	public static void put(String roomKey, String userKey) {
		ConcurrentSkipListSet<String> set = roomUserKeyMap.get(roomKey);
		if (null == set) {
			set = new ConcurrentSkipListSet<String>();
			roomUserKeyMap.put(roomKey, set);
		}
		set.add(userKey);
		putUserJoinRoom(userKey, roomKey);
	}

	public static List<String> getUserKeyList(String roomKey) {
		List<String> list = new ArrayList<String>();
		ConcurrentSkipListSet<String> set = roomUserKeyMap.get(roomKey);
		if (null != set) {
			list.addAll(set);
		}
		return list;
	}

	public static ConcurrentSkipListSet<String> getUserKeySet(String roomKey) {
		ConcurrentSkipListSet<String> set = roomUserKeyMap.get(roomKey);
		return set;
	}

	private static void putUserJoinRoom(String userKey, String roomKey) {
		CopyOnWriteArraySet<String> set = userJoinRoomMap.get(userKey);
		if (null == set) {
			set = new CopyOnWriteArraySet<String>();
			userJoinRoomMap.put(userKey, set);
		}
		set.add(roomKey);
	}

	public static List<String> removeUser(String userKey) {
		List<String> list = new ArrayList<String>();
		CopyOnWriteArraySet<String> roomKeySet = userJoinRoomMap.remove(userKey);
		if (null != roomKeySet) {
			for (String roomKey : roomKeySet) {
				ConcurrentSkipListSet<String> userKeySet = roomUserKeyMap.get(roomKey);
				if (null != userKeySet) {
					userKeySet.remove(userKey);
				}
				list.add(roomKey);
			}
		}
		return list;
	}
	
	public static List<String> getRoomIdList(String userKey) {
		List<String> list = new ArrayList<String>();
		CopyOnWriteArraySet<String> roomKeySet = userJoinRoomMap.get(userKey);
		if (null != roomKeySet) {
			for (String roomKey : roomKeySet) {
				list.add(roomKey);
			}
		}
		return list;
	}
}
