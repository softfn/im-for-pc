package com.only.common.util;

/**
 * @author: XiaHui
 * @date: 2016年9月19日 下午12:03:55
 */
public class OnlyStringUtil {

	/**
	 * 替换字符串中的一节，从beginIndex开始到endIndex结尾，原字符串中的这一部分被替换成replace
	 *
	 * @author: XiaHui
	 * @createDate: 2016年9月19日 上午11:43:49
	 * @update: XiaHui
	 * @updateDate: 2016年9月19日 上午11:43:49
	 */
	public static String replace(String text, String replace, int beginIndex, int endIndex) {
		String value = null;
		if (null != text) {
			if (text.length() > endIndex && (beginIndex < endIndex)) {
				String oldChar = text.substring(beginIndex, endIndex);
				value = text.replace(oldChar, replace);
			} else {
				value = text;
			}
		}
		return value;
	}

	/**
	 * 替换前面部分字符为replace，保证后面endLength长度不被替换
	 * 
	 * @author: XiaHui
	 * @createDate: 2016年9月19日 上午11:54:47
	 * @update: XiaHui
	 * @updateDate: 2016年9月19日 上午11:54:47
	 */
	public static String replaceBefore(String text, String replace, int replaceLength, int endLength) {
		String value = null;
		if (null != text) {
			StringBuilder sb = new StringBuilder();
			char[] chars = text.toCharArray();
			int length = chars.length;
			int endIndex = (length - endLength);
			int beginIndex = (endIndex - replaceLength);
			beginIndex = (beginIndex < 0) ? 0 : beginIndex;
			for (int i = 0; i < length; i++) {
				if (beginIndex <= i && i < endIndex) {
					sb.append(replace);
				} else {
					sb.append(chars[i]);
				}
			}
			value = sb.toString();
		}
		return value;
	}

	/**
	 * 替换中间部分的字符串；保留前部分长度beginLength和后部分长度endLength；<br>
	 * 中间的每一个字符都被替换成replace
	 *
	 * @author: XiaHui
	 * @createDate: 2016年9月19日 上午11:46:27
	 * @update: XiaHui
	 * @updateDate: 2016年9月19日 上午11:46:27
	 */
	public static String replaceMiddle(String text, String replace, int beginLength, int endLength) {
		String value = null;
		if (null != text) {
			StringBuilder sb = new StringBuilder();
			char[] chars = text.toCharArray();
			int length = chars.length;
			int endIndex = (length - endLength);
			for (int i = 0; i < length; i++) {
				if (beginLength <= i && i < endIndex) {
					sb.append(replace);
				} else {
					sb.append(chars[i]);
				}
			}
			value = sb.toString();
		}
		return value;
	}

	/**
	 * 替换一节字符，不管后面够不够，保留前面部分
	 *
	 * @author: XiaHui
	 * @createDate: 2016年9月19日 上午11:49:24
	 * @update: XiaHui
	 * @updateDate: 2016年9月19日 上午11:49:24
	 */
	public static String replaceAfter(String text, String replace, int beginIndex, int endIndex) {
		String value = null;
		if (null != text) {
			StringBuilder sb = new StringBuilder();
			char[] chars = text.toCharArray();
			int length = chars.length;
			for (int i = 0; i < length; i++) {
				if (beginIndex <= i && i <= endIndex) {
					sb.append(replace);
				} else {
					sb.append(chars[i]);
				}
			}
			value = sb.toString();
		}
		return value;
	}
}
