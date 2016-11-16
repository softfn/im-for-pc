package com.only.common.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class OnlyMD5Util {
	/**
	 * 1.对文本进行32位小写MD5加密
	 * 
	 * @param plainText
	 *            要进行加密的文本
	 * @return 加密后的内容
	 */
	public static String textToMD5L32(String text) {
		String result = null;

		if (null != text && !"".equals(text)) {// 首先判断是否为空
			try {
				MessageDigest md = MessageDigest.getInstance("MD5");// 首先进行实例化和初始化
				byte[] btInput = text.getBytes();// 得到一个操作系统默认的字节编码格式的字节数组
				md.update(btInput);// 对得到的字节数组进行处理
				byte[] btResult = md.digest();// 进行哈希计算并返回结果
				StringBuffer sb = new StringBuffer();// 进行哈希计算后得到的数据的长度
				for (byte b : btResult) {
					int bt = b & 0xff;
					if (bt < 16) {
						sb.append(0);
					}
					sb.append(Integer.toHexString(bt));
				}
				result = sb.toString();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		} else {
			result = text;
		}
		return result;
	}

	public static String textToMD5U32(String text) {
		String result = textToMD5L32(text);
		if (null != result && !"".equals(result)) {
			result = result.toUpperCase();
		}
		return result;
	}
}
