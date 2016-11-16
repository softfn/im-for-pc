package com.only.query.hibernate;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.convert.support.DefaultConversionService;

/**
 * @author: XiaHui
 * @date: 2016年9月23日 上午11:13:50
 */
public class BeanResolver {

	static final BeanResolver beanResolver = new BeanResolver();
	protected final Logger logger = LogManager.getLogger(this.getClass());
	protected final DefaultConversionService conversionService = new DefaultConversionService();
	protected final Map<Object, List<PropertyDescriptor>> writeMethodPropertyDescriptorListMap = new HashMap<Object, List<PropertyDescriptor>>();

	public Object resolver(Class<?> classType, Object[] values, String[] names) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		int namesLength = names.length;
		int valuesLength = values.length;
		int length = 0;
		
		if (namesLength == valuesLength) {
			length = namesLength;
		} else if (namesLength > valuesLength) {
			length = valuesLength;
		} else {
			length = namesLength;
		}
		
		for (int i = 0; i < length; i++) {
			String name = names[i];
			Object value = values[i];

			if (null != name && !"".equals(name)) {
				String key = name.replace("-", "").toLowerCase();
				map.put(key, value);
			}
		}
		

		Object object = null;
		try {
			object = classType.newInstance();// Class.forName(classType.getName()).newInstance();
			List<PropertyDescriptor> propertyDescriptorList = this.getWriteMethodPropertyDescriptorList(classType);
			for (PropertyDescriptor pd : propertyDescriptorList) {
				String propertyName = pd.getName();
				Method writeMethod = pd.getWriteMethod();
				String key = propertyName.replace("-", "").toLowerCase();
				Object value = map.get(key);
				if (null != value) {
					if (conversionService.canConvert(value.getClass(), pd.getPropertyType())) {
						value = conversionService.convert(value, pd.getPropertyType());
						writeMethod.invoke(object, value);
					}
				}
			}
		} catch (IllegalAccessException e) {
			logger.error(e.getMessage(), e);
		} catch (IllegalArgumentException e) {
			logger.error(e.getMessage(), e);
		} catch (InvocationTargetException e) {
			logger.error(e.getMessage(), e);
		} catch (InstantiationException e) {
			logger.error(e.getMessage(), e);
		}
		return object;
	}

	/**
	 * 获取类中有set方法是属性
	 * 
	 * @param classType
	 * @return
	 */
	public List<PropertyDescriptor> getWriteMethodPropertyDescriptorList(Class<?> classType) {
		List<PropertyDescriptor> propertyDescriptorList = writeMethodPropertyDescriptorListMap.get(classType);
		try {
			if (null == propertyDescriptorList) {
				propertyDescriptorList = new ArrayList<PropertyDescriptor>();
				BeanInfo bi = Introspector.getBeanInfo(classType, Object.class);
				PropertyDescriptor[] pds = bi.getPropertyDescriptors();
				for (PropertyDescriptor pd : pds) {
					if (null != pd.getWriteMethod()) {
						propertyDescriptorList.add(pd);
					}
				}
				writeMethodPropertyDescriptorListMap.put(classType, propertyDescriptorList);
			}
		} catch (IntrospectionException e) {
			logger.error(e.getMessage(), e);
		}
		return propertyDescriptorList;
	}

	public static Object beanResolver(Class<?> classType, Object[] values, String[] names) {
		return beanResolver.resolver(classType, values, names);
	}
}
