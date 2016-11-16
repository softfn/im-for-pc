package com.oim.test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * 描述：
 * @author XiaHui 
 * @date 2016年1月13日 下午8:32:40
 * @version 0.0.1
 */
public class ByteTest {

	public static void main(String[] args) {
		String sb="1213fdfffsasaas哈哈哈";
		byte[] stringBytes=sb.getBytes();
		int size=stringBytes.length;
		byte[] intBytes=intToBytes(size);
		byte[] allBytes= byteMerger(intBytes, stringBytes);
		ByteArrayInputStream in=new ByteArrayInputStream(allBytes);
		
		
		byte[] nub=new byte[4];
		
		
		try {
			in.read(nub);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int length=bytesToInt(nub);
		byte[] data=new byte[length];
		try {
			in.read(data);
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(new String(data));

	}
	public static int bytesToInt(byte[] bytes) {
		int value = 0;
		for (int i = 0; i < bytes.length; i++) {
			value += (bytes[i] & 0XFF) << (8 * (3 - i));
		}
		return value;
	}

	public static byte[] intToBytes(int value) {
		byte[] bytes = new byte[4];
		for (int i = 0; i < 4; i++) {
			bytes[i] = (byte) (value >> (24 - i * 8));
		}
		return bytes;
	}
	
	public static byte[] byteMerger(byte[] byte_1, byte[] byte_2) {
		byte[] byte_3 = new byte[byte_1.length + byte_2.length];
		System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
		System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
		return byte_3;
	}
}
