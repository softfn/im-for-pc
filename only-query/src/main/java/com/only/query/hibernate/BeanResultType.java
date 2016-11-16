package com.only.query.hibernate;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.hibernate.type.Type;
import org.hibernate.type.TypeResolver;

public class BeanResultType {

	private final TypeResolver typeResolver = new TypeResolver();
	private final Map<Class<?>, List<ResultType>> classMap = new ConcurrentHashMap<Class<?>, List<ResultType>>();

	public List<ResultType> getResultTypeList(Class<?> classType) {
		List<ResultType> list = classMap.get(classType);
		if (null == list) {
			list = getTypeList(classType);
			classMap.put(classType, list);
		}
		return list;
	}

	private List<ResultType> getTypeList(Class<?> classType) {
		List<ResultType> list = new ArrayList<ResultType>();
		if(!isPrimitive(classType)){
			List<PropertyDescriptor> propertyList = getWriteMethodPropertyDescriptorList(classType);
			for (PropertyDescriptor pd : propertyList) {
				Type t = typeResolver.basic(pd.getPropertyType().getName());
				if (null != t) {
					list.add(new ResultType(pd.getName(), t));
				}
			}
		}
		return list;
	}

	public List<PropertyDescriptor> getWriteMethodPropertyDescriptorList(Class<?> classType) {
		List<PropertyDescriptor> propertyDescriptorList = new ArrayList<PropertyDescriptor>();
		try {
			BeanInfo bi = Introspector.getBeanInfo(classType, Object.class);
			bi.getPropertyDescriptors();
			PropertyDescriptor[] pds = bi.getPropertyDescriptors();
			for (PropertyDescriptor pd : pds) {
				if (null != pd.getWriteMethod()) {
					propertyDescriptorList.add(pd);
				}
			}
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}
		return propertyDescriptorList;
	}

	public boolean isPrimitive(Class<?> o) {
		return (o.isPrimitive()) || (o == Integer.class) || (o == Long.class) || (o == Float.class)
				|| (o == Double.class) || (o == Byte.class) || (o == Character.class) || (o == Boolean.class)
				|| (o == Short.class);
	}
}
