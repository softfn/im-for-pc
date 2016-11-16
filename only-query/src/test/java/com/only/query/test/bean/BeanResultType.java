package com.only.query.test.bean;

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

import com.only.query.test.bean.User;

public class BeanResultType {
	
	private final Map<Class<?>, List<Type>> classMap = new ConcurrentHashMap<Class<?>, List<Type>>();

	private List<Type> getResultTypeList(Class<?> type) {
		List<Type> list = classMap.get(type);
		if (null == list) {

		}
		// typeResolver.basic(name)
		return list;
	}

	public static List<PropertyDescriptor> getWriteMethodPropertyDescriptorList(Class<?> classType) {
		List<PropertyDescriptor> propertyDescriptorList = new ArrayList<PropertyDescriptor>();
		try {
			 TypeResolver typeResolver = new TypeResolver();
			BeanInfo bi = Introspector.getBeanInfo(classType, Object.class);
			bi.getPropertyDescriptors();
			PropertyDescriptor[] pds = bi.getPropertyDescriptors();
			for (PropertyDescriptor pd : pds) {
				if (null != pd.getWriteMethod()) {
					propertyDescriptorList.add(pd);
					System.out.println(pd.getName());
					System.out.println(typeResolver.basic(pd.getPropertyType().getName()));
				}
			}
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}
		return propertyDescriptorList;
	}

	public static void main(String[] arg) {
		List<PropertyDescriptor> list = getWriteMethodPropertyDescriptorList(String.class);
	}
}
