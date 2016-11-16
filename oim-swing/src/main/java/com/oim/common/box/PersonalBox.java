package com.oim.common.box;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 描述：这里是用于存放个人信息的集合
 * 
 * @author XiaHui
 * @date 2016年1月15日 下午7:15:17
 * @version 0.0.1
 */
public class PersonalBox {
	
	private static Map<Object, Object> objectMap = new ConcurrentHashMap<Object, Object>();

	public static void put(Object key, Object value) {
		objectMap.put(key, value);
	}

	@SuppressWarnings("unchecked")
	public static <T> T get(Object key) {
		return (T) objectMap.get(key);
	}
}
