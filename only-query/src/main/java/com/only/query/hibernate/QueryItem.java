package com.only.query.hibernate;

import java.util.ArrayList;
import java.util.List;

/**
 * sql执行对象
 * 
 * @Author: XiaHui
 * @Date: 2015年12月18日
 * @ModifyUser: XiaHui
 * @ModifyDate: 2015年12月18日
 */
public class QueryItem {

	private String spaceName;
	private String name;
	private String type;
	private String content;
	private List<ResultType> resultTypeList = new ArrayList<ResultType>();

	public String getSpaceName() {
		return spaceName;
	}

	public void setSpaceName(String spaceName) {
		this.spaceName = spaceName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<ResultType> getResultTypeList() {
		return resultTypeList;
	}

	public void add(ResultType resultType) {
		resultTypeList.add(resultType);
	}
}
