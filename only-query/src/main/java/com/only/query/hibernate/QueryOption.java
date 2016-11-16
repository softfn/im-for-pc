package com.only.query.hibernate;

/**
 * @Author: XiaHui
 * @Date: 2015年12月31日
 * @ModifyUser: XiaHui
 * @ModifyDate: 2015年12月31日
 */
public class QueryOption {

	private String name;
	private QueryOptionType optionType;

	public QueryOption(String name, QueryOptionType optionType) {
		super();
		this.name = name;
		this.optionType = optionType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public QueryOptionType getOptionType() {
		return optionType;
	}

	public void setOptionType(QueryOptionType optionType) {
		this.optionType = optionType;
	}

}
