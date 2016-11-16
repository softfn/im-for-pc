package com.oim.common.box;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.oim.net.message.data.UserData;

/**
 * 描述：这里存放一些非好友的用户：如陌生人
 * 
 * @author XiaHui
 * @date 2016年1月17日 下午1:49:49
 * @version 0.0.1
 */
public class UserDataTempBox {
	
	private static Map<String, UserData> userMap = new ConcurrentHashMap<String, UserData>();

	public static void put(String id, UserData userData) {
		userMap.put(id, userData);
	}

	public static UserData get(String id) {
		return userMap.get(id);
	}
}
