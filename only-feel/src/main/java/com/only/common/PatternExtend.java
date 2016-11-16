package com.only.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternExtend {
	
	private static final String MAX_RANGE_REGEX = "Max-\\d+";
	private static final String MIN_RANGE_REGEX = "Min-\\d+";
	private static final String MAX_LENGTH_REGEX = "MaxLength-\\d+";
	private static final String DECIMAL_LENGTH_REGEX = "DecimalLength-\\d+";

	public static boolean matches(String input, Matcher matcher) {
		Pattern pattern = matcher.pattern();
		String regex = pattern.pattern();

		if (extendRegex(regex)) {
			return matchesExtend(regex, input);
		} else {
			return matcher.matches();
		}
	}

	private static boolean matchesExtend(String regex, String input) {
		boolean ret = true;
		Integer i = null;
		Double d = null;

		if ((d = parseMaxRange(regex)) != null) {
			try {
				ret = ret && d >= Double.parseDouble(input);
			} catch (Exception e) {
				ret = false;
			}
		}

		if (ret && ((d = parseMinRange(regex)) != null)) {
			try {
				ret = ret && d <= Double.parseDouble(input);
			} catch (Exception e) {
				ret = false;
			}
		}

		if (ret && ((i = parseMaxLength(regex)) != null)) {
			try {
				ret = ret && i >= input.length();
			} catch (Exception e) {
				ret = false;
			}
		}

		if (ret && ((i = parseDecimalLength(regex)) != null)) {
			try {
				int dotIndex = regex.lastIndexOf('.');
				int decimalLength = dotIndex < 0 ? 0 : input.length() - input.lastIndexOf('.') - 1;
				ret = ret && i >= decimalLength;
			} catch (Exception e) {
				ret = false;
			}
		}

		return ret;
	}

	public static Double parseMaxRange(String regex) {
		if (extendRegex(regex)) {
			Pattern pattern = Pattern.compile(MAX_RANGE_REGEX);
			Matcher matcher = pattern.matcher(regex);

			if (matcher.find()) {
				return Double.parseDouble(matcher.group().split("-")[1]);
			}
		}

		return null;
	}

	public static Double parseMinRange(String regex) {
		if (extendRegex(regex)) {
			Pattern pattern = Pattern.compile(MIN_RANGE_REGEX);
			Matcher matcher = pattern.matcher(regex);

			if (matcher.find()) {
				return Double.parseDouble(matcher.group().split("-")[1]);
			}
		}

		return null;
	}

	public static Integer parseMaxLength(String regex) {
		if (extendRegex(regex)) {
			Pattern pattern = Pattern.compile(MAX_LENGTH_REGEX);
			Matcher matcher = pattern.matcher(regex);

			if (matcher.find()) {
				return Integer.parseInt(matcher.group().split("-")[1]);
			}
		}

		return null;
	}

	public static Integer parseDecimalLength(String regex) {
		if (extendRegex(regex)) {
			Pattern pattern = Pattern.compile(DECIMAL_LENGTH_REGEX);
			Matcher matcher = pattern.matcher(regex);

			if (matcher.find()) {
				return Integer.parseInt(matcher.group().split("-")[1]);
			}
		}

		return null;
	}

	public static boolean extendRegex(String regex) {
		return regex.startsWith("[Extend]");
	}
}