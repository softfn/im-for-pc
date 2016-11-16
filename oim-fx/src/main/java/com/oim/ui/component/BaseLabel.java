package com.oim.ui.component;

import java.util.HashMap;
import java.util.Map;

import com.oim.common.component.OurLabel;

/**
 * @author XiaHui
 * @date 2015年3月16日 下午3:54:30
 */
public class BaseLabel extends OurLabel {

	private static final long serialVersionUID = 1L;

	private Map<Object, Object> attributeMap = new HashMap<Object, Object>();

	public void addAttribute(Object key, Object value) {
		attributeMap.put(key, value);
	}

	@SuppressWarnings("unchecked")
	public <T> T getAttribute(Object key) {
		return (T) attributeMap.get(key);
	}

}
