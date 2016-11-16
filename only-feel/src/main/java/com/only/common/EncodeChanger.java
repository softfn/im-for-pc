package com.only.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
/**
 * 
 * @date 2013年12月28日 上午11:22:43
 * version 0.0.1
 */
public class EncodeChanger {
	
	private static final char[] HEX_DIGIT = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
	private static final String COMMENT_START = "\\";
	private static final char UNICODE_SIGN_CHAR = '\\';
	private static final String UNICODE_START = "\\u";
	private static final char LINE_SEP = '\n';
	private static final char CODE_U = 'U';
	private static final char[] EXCEPTED_CHART = { '\r', '\n' };

	static {
		Arrays.sort(EXCEPTED_CHART);
	}

	public static String unicode2UnicodeEsc(String uniStr) {
		return unicode2UnicodeEsc(uniStr, true);
	}

	public static String unicode2UnicodeEsc(String uniStr, boolean uppercase) {
		if (uniStr == null || uniStr.isEmpty() || uniStr.trim().isEmpty()) {
			return uniStr;
		}

		int length = uniStr.length();
		StringBuilder ret = new StringBuilder();
		char ch;

		for (int index = 0; index < length; index++) {
			ch = uniStr.charAt(index);

			if ((ch >= 0x0020 && ch <= 0x007e) || Arrays.binarySearch(EXCEPTED_CHART, ch) >= 0) {
				ret.append(ch);
			} else {
				ret.append(UNICODE_START);
				ret.append(toHex((ch >> 12) & 0xF, uppercase));
				ret.append(toHex((ch >> 8) & 0xF, uppercase));
				ret.append(toHex((ch >> 4) & 0xF, uppercase));
				ret.append(toHex(ch & 0xF, uppercase));
			}
		}

		return ret.toString();
	}

	public static String unicodeEsc2Unicode(String unicodeStr) {
		if (unicodeStr == null || unicodeStr.isEmpty() || unicodeStr.trim().isEmpty()) {
			return unicodeStr;
		}

		int length = unicodeStr.length();
		StringBuffer ret = new StringBuffer();
		char ch;
		boolean needChange;

		for (int index = 0; index < length; index++) {
			ch = unicodeStr.charAt(index);
			needChange = false;

			if (ch == UNICODE_SIGN_CHAR && index < length - 5 && Character.toUpperCase(unicodeStr.charAt(index + 1)) == CODE_U) {
				try {
					ret.append((char) Integer.parseInt(unicodeStr.substring(index + 2, index + 6), 16));
					index += 5;
					needChange = true;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			if (!needChange) {
				ret.append(ch);
			}
		}

		return ret.toString();
	}

	public static String unicode2UnicodeEscWithoutComment(String uniStr) throws IOException {
		return unicode2UnicodeEscWithoutComment(uniStr, true);
	}

	public static String unicode2UnicodeEscWithoutComment(String uniStr, boolean uppercase) throws IOException {
		StringBuffer ret = new StringBuffer();
		BufferedReader reader = new BufferedReader(new StringReader(uniStr));
		boolean continueFlag = false;
		String lineWithoutSpace;
		String line;

		while ((line = reader.readLine()) != null) {
			lineWithoutSpace = line.trim();

			if ((lineWithoutSpace.startsWith("#") || lineWithoutSpace.startsWith("!")) && !continueFlag) {
				ret.append(line);
			} else {
				continueFlag = line.endsWith(COMMENT_START);
				ret.append(unicode2UnicodeEsc(line, uppercase));
			}

			ret.append(LINE_SEP);
		}

		return ret.toString();
	}

	private static char toHex(int nibble, boolean uppercase) {
		char hex = HEX_DIGIT[nibble & 0xF];
		return uppercase ? hex : Character.toLowerCase(hex);
	}
}