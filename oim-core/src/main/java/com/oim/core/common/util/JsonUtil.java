package com.oim.core.common.util;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @Author: XiaHui
 * @Date: 2015年12月17日
 * @ModifyUser: XiaHui
 * @ModifyDate: 2015年12月17日
 */
public class JsonUtil {
	
	private static final Gson gson = new Gson();
	/**
	 * 对象转json
	 * @param o
	 * @return
	 */
	public static String objectToJson(Object o) {
		if (null == o) {
			return "";
		}
		return JSONObject.fromObject(o).toString();
	}
	
	public static String toJson(Object o) {
		String json = "";
		if (null != o) {
			 json = gson.toJson(o);
		}
		return json;
	}
	/**
	 * 集合转json
	 * @param list
	 * @return
	 */
	public static String listToJson(List<?> list) {
		if (null == list) {
			return "";
		}
		return JSONArray.fromObject(list).toString();
	}
	
	
	@SuppressWarnings("unchecked")
	public static <T> T jsonToObject(String json, Class<?> classType) {
		Object o = null;
		if (json != o) {
			o = gson.fromJson(json, classType);
		}
		return (T) o;
	}

	@SuppressWarnings("unchecked")
	public static <T> T jsonToObject(String json, Type type) {
		Object o = null;
		if (json != o) {
			o = gson.fromJson(json, type);
		}
		return (T) o;
	}

}

