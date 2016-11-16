package com.only.query.hibernate;

/**
 * @Author: XiaHui
 * @Date: 2015年12月31日
 * @ModifyUser: XiaHui
 * @ModifyDate: 2015年12月31日
 */
public enum QueryOptionType {

	likeAll {
		public String getText(Object value) {
			StringBuilder sb = new StringBuilder();
			sb.append("%");
			sb.append(value);
			sb.append("%");
			return sb.toString();
		}
	},
	likeLeft {
		public String getText(Object value) {
			StringBuilder sb = new StringBuilder();
			sb.append("%");
			sb.append(value);
			return sb.toString();
		}
	},
	likeRight {
		public String getText(Object value) {
			StringBuilder sb = new StringBuilder();
			sb.append(value);
			sb.append("%");
			return sb.toString();
		}
	};
	public String getText(Object value) {
		return "";
	}
}
