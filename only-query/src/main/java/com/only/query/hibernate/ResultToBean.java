package com.only.query.hibernate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.property.access.internal.PropertyAccessStrategyBasicImpl;
import org.hibernate.property.access.internal.PropertyAccessStrategyChainedImpl;
import org.hibernate.property.access.internal.PropertyAccessStrategyFieldImpl;
import org.hibernate.property.access.internal.PropertyAccessStrategyMapImpl;
import org.hibernate.property.access.spi.Setter;
import org.hibernate.transform.BasicTransformerAdapter;

/**
 * @author: XiaHui
 * @date: 2016年9月23日 上午9:13:11
 */
public class ResultToBean extends BasicTransformerAdapter {

	private static final long serialVersionUID = 1L;
	private Class<?> resultClass;
	private boolean isInitialized;
	private String[] names;
	private Setter[] setters;

	PropertyAccessStrategyChainedImpl propertyAccessStrategy = new PropertyAccessStrategyChainedImpl(
			PropertyAccessStrategyBasicImpl.INSTANCE,
			PropertyAccessStrategyFieldImpl.INSTANCE,
			PropertyAccessStrategyMapImpl.INSTANCE);

	public ResultToBean(Class<?> resultClass) {
		this.isInitialized = false;
		this.resultClass = resultClass;
	}

	@Override
	public Object transformTuple(Object[] values, String[] names) {
		Object result = null;
		try {
			if (null == resultClass) {
				resultClass = HashMap.class;
			}

			if (Map.class.isAssignableFrom(resultClass)) {
				result = new HashMap<String, Object>();
				if (!isInitialized) {
					initialize(names);
				} else {
					check(names);
				}
				int length = names.length;
				result = new HashMap<String, Object>();
				for (int i = 0; i < length; i++) {
					if (setters[i] != null) {
						setters[i].set(result, values[i], null);
					}
				}
			} else {
				result = BeanResolver.beanResolver(resultClass, values, names);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private void initialize(String[] names) {
		int length = names.length;
		this.names = new String[length];
		this.setters = new Setter[length];
		for (int i = 0; i < length; i++) {
			String name = names[i];
			if (name != null) {
				this.names[i] = name;
				this.setters[i] = propertyAccessStrategy.buildPropertyAccess(resultClass, name).getSetter();
			}
		}
		isInitialized = true;
	}

	private void check(String[] names) {
		if (!Arrays.equals(names, this.names)) {
			throw new IllegalStateException(
					"aliases are different from what is cached; aliases=" + Arrays.asList(names) +
							" cached=" + Arrays.asList(this.names));
		}
	}

}
