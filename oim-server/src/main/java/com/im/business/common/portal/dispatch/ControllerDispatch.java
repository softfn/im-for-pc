package com.im.business.common.portal.dispatch;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.im.common.util.ClassScaner;
import com.only.action.annotation.ActionMapping;
import com.only.action.annotation.MethodMapping;

/**
 * action分配调度模块
 * 
 * @author XiaHui
 * 
 */

public class ControllerDispatch {
	protected final Logger logger = LogManager.getLogger(this.getClass());
	private Map<String, Class<?>> actionMap = new HashMap<String, Class<?>>();// 缓存所有action的简单类名
	private Map<String, Map<String, Method>> methodMap = new HashMap<String, Map<String, Method>>();// 缓存所有的action的方法

	public ControllerDispatch() {
		initActionDispatch();
	}

	/**
	 * 初始化action
	 */

	@SuppressWarnings("unchecked")
	private void initActionDispatch() {
		Set<Class<?>> classSet = ClassScaner.scan("com.im", ActionMapping.class);// 扫描com.hk包下面的所有被注解ActionMapping的类
		try {
			for (Class<?> classType : classSet) {
				add(classType);
			}
		} catch (MappingException e) {
			logger.error("", e);
		}
	}

	/**
	 * 为了更快获取要执行的action，将要执行的action加入Map进行缓存
	 * 
	 * @param classType
	 * @throws MappingException
	 */
	private void add(Class<?> classType) throws MappingException {
		Annotation[] as = classType.getAnnotations();
		for (Annotation annotation : as) {
			if (annotation instanceof ActionMapping) {
				ActionMapping am = ((ActionMapping) annotation);
				String value = am.value();
				if (actionMap.containsKey(value)) {
					StringBuilder sb=new StringBuilder();
					sb.append(classType.getName());
					sb.append("存在重复的ActionMapping配置：");
					sb.append(value);
					throw new MappingException(sb.toString());
				} else {
					actionMap.put(value, classType);
					methodMap.put(value, getMethodMap(classType));
				}
				break;
			}
		}
	}

	/**
	 * 获取要执行的action的方法，并且缓存
	 * 
	 * @param classType
	 * @return
	 * @throws MappingException 
	 */
	private Map<String, Method> getMethodMap(Class<?> classType) throws MappingException {
		Map<String, Method> map = new HashMap<String, Method>();
		Method[] methods = classType.getMethods();
		if (null != methods && methods.length > 0) {
			for (Method method : methods) {
				Annotation[] as = method.getAnnotations();
				for (Annotation a : as) {
					if (a instanceof MethodMapping) {
						String value = ((MethodMapping) a).value();
						if (map.containsKey(value)) {
							StringBuilder sb=new StringBuilder();
							sb.append(classType.getName());
							sb.append("中存在重复的MethodMapping：");
							sb.append(value);
							throw new MappingException(sb.toString());
						} else {
							map.put(value, method);
						}
						break;
					}
				}
			}
		}
		return map;
	}

	/**
	 * 截取不包含包名，和小写首字母的类名
	 * 
	 * @param classType
	 * @return
	 */
	public String getSimpleNameAsProperty(Class<?> classType) {
		String valueName = classType.getSimpleName();
		return valueName = valueName.substring(0, 1).toLowerCase() + valueName.substring(1);
	}

	// public String getClassName(String classCode) {
	// return actionMap.get(classCode);
	// }

	public Class<?> getClass(String classCode) {
		return actionMap.get(classCode);
	}

	public Method getMethod(String classCode, String methodCode) {
		Method method = null;
		Map<String, Method> map = methodMap.get(classCode);
		if (null != map) {
			method = map.get(methodCode);
		}
		return method;
	}

	/**
	 * 判断对象是否为8种基本类型的对象。
	 * 
	 * @param o
	 * @return
	 */
	public boolean isPrimitive(Object o) {
		return (o instanceof Integer)
				|| (o instanceof Long)
				|| (o instanceof Float)
				|| (o instanceof Double)
				|| (o instanceof Byte)
				|| (o instanceof Character)
				|| (o instanceof Boolean)
				|| (o instanceof Short)
				|| o.getClass().isPrimitive();
	}

	/**
	 * 判断对象是否为8种基本类型的对象。
	 * 
	 * @param o
	 * @return
	 */

	public boolean isPrimitive(Class<?> o) {
		return (o.isPrimitive())
				|| (o == Integer.class)
				|| (o == Long.class)
				|| (o == Float.class)
				|| (o == Double.class)
				|| (o == Byte.class)
				|| (o == Character.class)
				|| (o == Boolean.class)
				|| (o == Short.class);
	}

	/**
	 * 是否为字符串类型
	 * 
	 * @param o
	 * @return
	 */
	public boolean isString(Object o) {
		return (o instanceof String);
	}

	/**
	 * 是否为字符串类型
	 * 
	 * @param o
	 * @return
	 */
	public boolean isString(Class<?> o) {
		return (o == String.class);
	}

}
