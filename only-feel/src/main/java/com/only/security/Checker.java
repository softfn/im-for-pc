package com.only.security;

import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.Adler32;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

public class Checker {

	public static final String[] SUPPORTED_TYPES = { "CRC-32", "CRC-16", "CRC-16/CCITT", "Adler32" };
	public static final Map<String, String> map = new HashMap<String, String>();

	static {
		map.put("CRC-32", CRC32.class.getCanonicalName());
		map.put("CRC-16", CRC16.class.getCanonicalName());
		map.put("CRC-16/CCITT", CRC16_CCITT.class.getCanonicalName());
		map.put("Adler32", Adler32.class.getCanonicalName());
	}

	public static long compute(File file, String type) {
		FileInputStream fis = null;

		try {
			Checksum check = getChecksum(type);

			if (check != null) {
				fis = new FileInputStream(file);
				byte[] buffer = new byte[1024 * 1024];

				for (int bytesRead = 0; (bytesRead = fis.read(buffer)) != -1;) {
					check.update(buffer, 0, bytesRead);
				}

				return check.getValue();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return 0;
	}

	public static long compute(String str, String type) {
		return compute(str.getBytes(), type);
	}

	public static long compute(String str, String type, String charsetName) {
		try {
			return compute(str.getBytes(charsetName), type);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return 0;
	}

	public static long compute(byte[] bytes, String type) {
		Checksum check = getChecksum(type);
		check.update(bytes, 0, bytes.length);
		return check.getValue();
	}

	public static String computeToHex(File file, String type) {
		long value = compute(file, type);
		return value == 0 ? null : longToHex(value);
	}

	public static String computeToHex(String str, String type) {
		long value = compute(str, type);
		return value == 0 ? null : longToHex(value);
	}

	public static String computeToHex(String str, String type, String charsetName) {
		long value = compute(str, type, charsetName);
		return value == 0 ? null : longToHex(value);
	}

	public static String computeToHex(byte[] bytes, String type) {
		long value = compute(bytes, type);
		return value == 0 ? null : longToHex(value);
	}

	public static String[] getSupportedTypes() {
		return SUPPORTED_TYPES;
	}

	private static String longToHex(long value) {
		String hex = Long.toHexString(value);
		return hex.length() % 2 == 0 ? hex : '0' + hex;
	}

	private static Checksum getChecksum(String type) {
		String name = map.get(type);

		if (name == null) {
			try {
				throw new NoSuchAlgorithmException("Unsupported this type:" + type);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		} else {
			try {
				Class<?> clazz = Class.forName(name);
				return (Checksum) clazz.newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return null;
	}
}