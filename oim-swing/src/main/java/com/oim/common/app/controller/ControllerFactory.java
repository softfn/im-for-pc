package com.oim.common.app.controller;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.oim.app.AppContext;
import com.oim.common.annotation.ActionMapping;
import com.oim.common.annotation.MethodMapping;
import com.oim.common.util.ClassScaner;

/**
 * 类描述：xxx XiaHui 2014年2月16日
 */
public class ControllerFactory {

	private Map<String, Class<?>> classMap = new HashMap<String, Class<?>>();// 缓存所有的action的方法
	private Map<String, Map<String, Method>> methodMap = new HashMap<String, Map<String, Method>>();// 缓存所有的action的方法
	private Map<Class<?>, Object> controllerMap = new HashMap<Class<?>, Object>();// 缓存所有的action的方法

	AppContext appContext;

	public ControllerFactory(AppContext appContext) {
		this.appContext = appContext;
		initFilterFactory();
	}

	@SuppressWarnings("unchecked")
	private void initFilterFactory() {
		Set<Class<?>> classSet = ClassScaner.scan("com.oim", ActionMapping.class);// 扫描com.hk包下面的所有被注解ActionMapping的类
		for (Class<?> classType : classSet) {
			add(classType);
		}
	}

	/**
	 * 为了更快获取要执行的action，将要执行的action加入Map进行缓存
	 * 
	 * @param classType
	 */
	private void add(Class<?> classType) {
		Annotation[] as = classType.getAnnotations();
		for (Annotation annotation : as) {
			if (annotation instanceof ActionMapping) {
				methodMap.put(((ActionMapping) annotation).value(), getMethodMap(classType));
				classMap.put(((ActionMapping) annotation).value(), classType);
				break;
			}
		}
	}

	/**
	 * 获取要执行的action的方法，并且缓存
	 * 
	 * @param classType
	 * @return
	 */
	private Map<String, Method> getMethodMap(Class<?> classType) {
		Map<String, Method> map = new HashMap<String, Method>();
		Method[] methods = classType.getMethods();
		if (null != methods && methods.length > 0) {
			for (Method method : methods) {
				Annotation[] as = method.getAnnotations();
				for (Annotation a : as) {
					if (a instanceof MethodMapping) {
						map.put(((MethodMapping) a).value(), method);
						break;
					}
				}
			}
		}
		return map;
	}

	public String getKey(Class<?> clazz) {
		StringBuilder key = new StringBuilder();
		if (null != clazz) {
			key.append(clazz.getSimpleName().substring(0, 1).toLowerCase());
			key.append(clazz.getSimpleName().substring(1));
		}
		return key.toString();
	}

	public boolean has(String classCode) {
		return classMap.containsKey(classCode);
	}

	public Object getController(String classCode) {
		Object object = null;
		Class<?> classType = classMap.get(classCode);
		if (null != classType) {
			object = controllerMap.get(classType);
			if (null == object) {
				try {
					Class<?>[] types = { AppContext.class };
					Constructor<?> constructor = classType.getConstructor(types);
					Object[] objects = { appContext };
					object = constructor.newInstance(objects);
					controllerMap.put(classType, object);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return object;
	}

	public Method getMethod(String classCode, String methodCode) {
		Method method = null;
		Map<String, Method> map = methodMap.get(classCode);
		if (null != map) {
			method = map.get(methodCode);
		}
		return method;
	}
}
