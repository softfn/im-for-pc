package com.oim.net.http;

import java.lang.annotation.Annotation;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.oim.common.config.ConfigManage;
import com.oim.common.config.data.ConnectConfigData;
import com.oim.common.util.HttpClientUtil;
import com.oim.net.server.Back;
import com.only.common.util.OnlyPropertyUtil;
import com.only.net.data.action.DataBackAction;

import net.sf.json.util.JSONUtils;

/**
 * @author XiaHui
 * @date 2015年3月4日 下午5:47:32
 */
public class HttpHandler {
	private Gson gson = new Gson();
	String url = "http://127.0.0.1:8080/";

	public void execute(Request data, DataBackAction dataBackAction) {
		ConnectConfigData ccd = (ConnectConfigData) ConfigManage.get(ConnectConfigData.path, ConnectConfigData.class);
		try {
			StringBuilder url = new StringBuilder();
			url.append("http://");
			url.append(ccd.getBusinessAddress());
			url.append(":");
			url.append(ccd.getHttpPort());
			url.append("/");
			url.append(data.getController());
			url.append(data.getMethod());

			String result = HttpClientUtil.post(url.toString(), data.getDataMap());

			if (StringUtils.isNotBlank(result) && null != dataBackAction) {
				if (JSONUtils.mayBeJSON(result)) {
					JsonObject jo = new JsonParser().parse(result).getAsJsonObject();

					ResultData rd = new ResultData();
					if (jo.has("code")) {
						String code = jo.get("code").getAsString();
						rd.setCode(code);
					}
					if (jo.has("message")) {
						String message = jo.get("message").getAsString();
						rd.setMessage(message);
					}
					JsonObject bodyObject =null;
					if (jo.has("data")) {
						bodyObject = jo.get("data").getAsJsonObject();
					}
					 

					Method[] methods = dataBackAction.getClass().getMethods();
					if (null != methods && methods.length > 0) {
						for (Method method : methods) {
							Annotation[] as = method.getAnnotations();
							for (Annotation a : as) {
								if (a instanceof Back) {

									Parameter[] parameters = method.getParameters();
									Object[] dataArray = getParameterValues(parameters, rd, bodyObject);
									try {
										method.invoke(dataBackAction, dataArray);
									} catch (IllegalAccessException e) {
									} catch (IllegalArgumentException e) {
									} catch (InvocationTargetException e) {
									}
									break;
								}
							}
						}
					}
				}
			}
		} catch (Exception ex) {
			// ex.printStackTrace();
			if (null != dataBackAction) {
				dataBackAction.lost();
			}
		}

	}

	private Object[] getParameterValues(Parameter[] parameters, ResultData rd, JsonObject bodyObject) {
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
						if (genericClass == ResultData.class || ResultData.class.isAssignableFrom(genericClass)) {
							value = rd;
						} else if (OnlyPropertyUtil.isString(genericClass)) {

						} else if (OnlyPropertyUtil.isPrimitive(genericClass)) {

						} else {
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

	public static void main(String main[]) {
	}
}
