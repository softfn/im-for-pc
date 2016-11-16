package com.only.common.util;

public class OnlyPropertyUtil {

	public static boolean isEmpty(Object o) {
		return (null == o || "".equals(o));
	}

	public static boolean isNotEmpty(Object o) {
		return (null != o && !"".equals(o));
	}

	public static boolean isPrimitive(Class<?> o) {
		return (o.isPrimitive()) || (o == Integer.class) || (o == Long.class) || (o == Float.class)
				|| (o == Double.class) || (o == Byte.class) || (o == Character.class) || (o == Boolean.class)
				|| (o == Short.class);
	}

	public static boolean isInteger(Class<?> o) {
		return (o == Integer.class);
	}

	public static boolean isLong(Class<?> o) {
		return (o == Long.class);
	}

	public static boolean isFloat(Class<?> o) {
		return (o == Float.class);
	}

	public static boolean isDouble(Class<?> o) {
		return (o == Double.class);
	}

	public static boolean isByte(Class<?> o) {
		return (o == Byte.class);
	}

	public static boolean isCharacter(Class<?> o) {
		return (o == Character.class);
	}

	public static boolean isBoolean(Class<?> o) {
		return (o == Boolean.class);
	}

	public static boolean isShort(Class<?> o) {
		return (o == Short.class);
	}

	/**
	 * 判断对象是否为8种基本类型的对象。
	 * 
	 * @param o
	 * @return
	 */
	public static boolean isPrimitive(Object o) {
		return (o instanceof Integer) || (o instanceof Long) || (o instanceof Float) || (o instanceof Double)
				|| (o instanceof Byte) || (o instanceof Character) || (o instanceof Boolean) || (o instanceof Short)
				|| o.getClass().isPrimitive();
	}

	/**
	 * 是否为字符串类型
	 * 
	 * @param o
	 * @return
	 */
	public static boolean isString(Object o) {
		return (o instanceof String);
	}

	/**
	 * 是否为字符串类型
	 * 
	 * @param o
	 * @return
	 */
	public static boolean isString(Class<?> o) {
		return (o == String.class);
	}
}
