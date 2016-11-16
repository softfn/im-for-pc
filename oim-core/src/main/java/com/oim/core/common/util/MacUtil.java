package com.oim.core.common.util;

import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * 
 * 2013-9-2 下午5:24:59
 * 
 * @author XiaHui 获取MAC地址工具类
 */
public class MacUtil {
	private MacUtil() {
	}

	/**
	 * 按照"XX-XX-XX-XX-XX-XX"格式，获取本机MAC地址
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String getMacAddress()   {
		try {
			Enumeration<NetworkInterface> ni = NetworkInterface.getNetworkInterfaces();

			while (ni.hasMoreElements()) {
				NetworkInterface netI = ni.nextElement();

				byte[] bytes = netI.getHardwareAddress();
				if (netI.isUp() && netI != null && bytes != null && bytes.length == 6) {
					StringBuffer sb = new StringBuffer();
					for (byte b : bytes) {
						// 与11110000作按位与运算以便读取当前字节高4位
						sb.append(Integer.toHexString((b & 240) >> 4));
						// 与00001111作按位与运算以便读取当前字节低4位
						sb.append(Integer.toHexString(b & 15));
						sb.append("-");
					}
					sb.deleteCharAt(sb.length() - 1);
					return sb.toString().toUpperCase();
				}
			}
		} catch (Exception e) {
		}
		return null;
	}

	public static void main(String[] args) throws Exception {
		System.out.println(MacUtil.getMacAddress());
	}

}