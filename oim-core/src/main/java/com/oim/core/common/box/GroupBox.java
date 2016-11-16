package com.oim.core.common.box;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.oim.core.bean.Group;

/**
 * 
 * @author XiaHui
 * @date 2016年1月17日 下午1:49:49
 * @version 0.0.1
 */
public class GroupBox {

	private static Map<String, Group> groupMap = new ConcurrentHashMap<String, Group>();

	public static void put(String id, Group group) {
		groupMap.put(id, group);
	}

	public static Group get(String id) {
		return groupMap.get(id);
	}
}
