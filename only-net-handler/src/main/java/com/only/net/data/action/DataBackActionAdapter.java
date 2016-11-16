package com.only.net.data.action;

import java.util.HashMap;
import java.util.Map;

public abstract class DataBackActionAdapter implements DataBackAction {

	private Map<Object, Object> attributeMap = new HashMap<Object, Object>();

	public void addAttribute(Object key, Object value) {
		attributeMap.put(key, value);
	}

	@SuppressWarnings("unchecked")
	public <T> T getAttribute(Object key) {
		return (T) attributeMap.get(key);
	}

	
	@Override
	public void lost() {
	}

	@Override
	public void timeOut() {
	}
}
