package com.oim.net.server;

import java.lang.annotation.Annotation;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.oim.common.app.controller.ControllerFactory;
import com.oim.net.message.Head;
import com.oim.net.message.Info;
import com.only.common.util.OnlyPropertyUtil;
import com.only.net.connect.ReadHandler;
import com.only.net.data.action.DataBackAction;
import com.only.net.data.bean.HandlerData;

import net.sf.json.util.JSONUtils;

public class MessageReadHandler implements ReadHandler {

	protected final Logger logger = LogManager.getLogger(this.getClass());
	ControllerFactory controllerFactory;
	private Gson gson = new Gson();

	public void setControllerFactory(ControllerFactory controllerFactory) {
		this.controllerFactory = controllerFactory;
	}

	@Override
	public void read(Object data, Map<String, HandlerData> handlerDataMap) {
		String text = (data instanceof String) ? (String) data : "";
		if (JSONUtils.mayBeJSON(text)) {
			JsonObject jo = new JsonParser().parse(text).getAsJsonObject();

			JsonElement headElement = jo.get("head");
			JsonElement infoElement = jo.get("info");
			JsonObject bodyObject = jo.get("body").getAsJsonObject();

			Head head = gson.fromJson(headElement, Head.class);
			Info info = gson.fromJson(infoElement, Info.class);

			String key = head.getKey();
			String classCode = head.getAction();
			String methodCode = head.getMethod();

			HandlerData hd = handlerDataMap.remove(null==key?"":key);
			if (null != hd && null != hd.getDataBackAction()) {
				DataBackAction dataBackAction = hd.getDataBackAction();
				Method[] methods = dataBackAction.getClass().getMethods();
				if (null != methods && methods.length > 0) {
					for (Method method : methods) {
						Annotation[] as = method.getAnnotations();
						for (Annotation a : as) {
							if (a instanceof Back) {

								Parameter[] parameters = method.getParameters();
								Object[] dataArray = getParameterValues(parameters, head, info, bodyObject);
								try {
									method.setAccessible(true);
									method.invoke(dataBackAction, dataArray);
								} catch (IllegalAccessException e) {
									e.printStackTrace();
								} catch (IllegalArgumentException e) {
									e.printStackTrace();
								} catch (InvocationTargetException e) {
									e.printStackTrace();
								}
								break;
							}
						}
					}
				}
			}

			if (null != controllerFactory && StringUtils.isNotBlank(classCode) && StringUtils.isNotBlank(methodCode)) {
				Object filter = controllerFactory.getController(classCode);
				Method method = controllerFactory.getMethod(classCode, methodCode);
				if ((null != filter && null != method)) {
					Parameter[] parameters = method.getParameters();
					Object[] dataArray = getParameterValues(parameters, head, info, bodyObject);
					try {
						method.invoke(filter, dataArray);
					} catch (IllegalAccessException e) {
						logger.error("", e);
					} catch (IllegalArgumentException e) {
						logger.error("", e);
					} catch (InvocationTargetException e) {
						logger.error("", e);
					}
				}
			}
		}
	}

	private Object[] getParameterValues(Parameter[] parameters, Head head, Info info, JsonObject bodyObject) {
		Object[] data = new Object[parameters.length];
		if (null != parameters) {
			int length = parameters.length;
			for (int i = 0; i < length; i++) {
				Object value = null;
				Parameter p = parameters[i];
				Type type = p.getParameterizedType();
				com.oim.common.annotation.Parameter pd = p
						.getAnnotation(com.oim.common.annotation.Parameter.class);
				if (null != pd) {
					String name = pd.value();
					value = getParameterValue(name, bodyObject, type);
				} else {
					if (type instanceof ParameterizedType) {
						// ParameterizedType pt =
						// (ParameterizedType) type;
						// Type rawType = pt.getRawType();

					} else if (type instanceof GenericArrayType) {
						// GenericArrayType gap = (GenericArrayType)
						// type;
						// Type tp = gap.getGenericComponentType();

					} else if (type instanceof Class) {
						Class<?> genericClass = (Class<?>) type;
						if (genericClass == Head.class || Head.class.isAssignableFrom(genericClass)) {
							value = head;
						} else if (genericClass == Info.class || Info.class.isAssignableFrom(genericClass)) {
							value = info;
						} else if (OnlyPropertyUtil.isString(genericClass)) {

						} else if (OnlyPropertyUtil.isPrimitive(genericClass)) {

						} else {
							// applicationContext.getBeanNamesForType(genericClass);
							// value = applicationContext.getBean(genericClass);
						}
						if (null == value) {
							if (isCanInstance(genericClass)) {
								try {
									value = genericClass.newInstance();
								} catch (InstantiationException e) {
									e.printStackTrace();
								} catch (IllegalAccessException e) {
									e.printStackTrace();
								}
							}
						}
					}
				}
				data[i] = value;
			}
		}
		return data;
	}

	private Object getParameterValue(String name, JsonObject bodyObject, Type type) {
		Object o = null;
		if (null != bodyObject) {
			JsonElement je = bodyObject.get(name);
			o = gson.fromJson(je, type);
		}
		return o;
	}

	private boolean isCanInstance(Class<?> classType) {
		boolean can = true;
		if (classType.isAnnotation()) {
			can = false;
		}
		if (classType.isArray()) {
			can = false;
		}
		if (classType.isEnum()) {
			can = false;
		}
		if (classType.isInterface()) {
			can = false;
		}
		return can;
	}
}
