package com.only.query;

import java.util.HashMap;
import java.util.Map;

import com.only.query.page.QueryPage;

/**
 * @Author: XiaHui
 * @Date: 2015年12月15日
 * @ModifyUser: XiaHui
 * @ModifyDate: 2015年12月15日
 */
public class QueryWrapper {

	private Map<String, Object> parameterMap = new HashMap<String, Object>();
	private QueryPage page;

	public Map<String, Object> getParameterMap() {
		return parameterMap;
	}

	public void addParameter(String key, Object value) {
		parameterMap.put(key, value);
	}

	public Object getParameter(String key) {
		return parameterMap.get(key);
	}
	
	public void put(String key, Object value) {
		parameterMap.put(key, value);
	}

	public Object get(String key) {
		return parameterMap.get(key);
	}

	public QueryPage getPage() {
		return page;
	}

	public void setPage(QueryPage page) {
		this.page = page;
	}

}
