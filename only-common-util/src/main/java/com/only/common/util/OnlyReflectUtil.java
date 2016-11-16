package com.only.common.util;

import java.lang.reflect.Field;

public class OnlyReflectUtil {
	/**
	 * 获取data对象fieldName的Field
	 * 
	 * @param data
	 * @param fieldName
	 * @return
	 */
	public static Field getFieldByFieldName(Object data, String fieldName) {
		for (Class<?> superClass = data.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
			try {
				return superClass.getDeclaredField(fieldName);
			} catch (NoSuchFieldException e) {
			}
		}
		return null;
	}

	/**
	 * 获取data对象fieldName的属性值
	 * 
	 * @param data
	 * @param fieldName
	 * @return
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static Object getValueByFieldName(Object data, String fieldName) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		Field field = getFieldByFieldName(data, fieldName);
		Object value = null;
		if (field != null) {
			if (field.isAccessible()) {
				value = field.get(data);
			} else {
				field.setAccessible(true);
				value = field.get(data);
				field.setAccessible(false);
			}
		}
		return value;
	}

	/**
	 * 设置data对象fieldName的属性值
	 * 
	 * @param data
	 * @param fieldName
	 * @param value
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static void setValueByFieldName(Object data, String fieldName, Object value) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		Field field = data.getClass().getDeclaredField(fieldName);
		if (field.isAccessible()) {
			field.set(data, value);
		} else {
			field.setAccessible(true);
			field.set(data, value);
			field.setAccessible(false);
		}
	}
}
