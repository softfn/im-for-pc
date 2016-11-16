package com.im.business.server.handler;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import javax.annotation.Resource;
import javax.websocket.Session;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.im.business.server.message.AbstractMessage;
import com.im.business.server.message.Head;
import com.im.business.server.message.Message;
import com.im.business.server.message.PushMessage;
import com.im.business.common.portal.dispatch.ControllerDispatch;
import com.only.action.annotation.MethodMapping;
import com.only.common.util.OnlyPropertyUtil;
import com.only.net.session.SocketSession;

import net.sf.json.util.JSONUtils;

@Component
public class ServerHandler {

	protected final Logger logger = LogManager.getLogger(this.getClass());
	@Resource
	private ApplicationContext applicationContext = null;
	@Resource
	private SessionHandler sessionHandler;
	private final Gson gson = new Gson();
	private final ControllerDispatch cd = new ControllerDispatch();

	public ServerHandler() {
		init();
	}

	private void init() {
	}

	public void onMessage(String message, SocketSession socketSession) {
		Object result = null;
		if (JSONUtils.mayBeJSON(message)) {

			JsonObject jo = new JsonParser().parse(message).getAsJsonObject();

			JsonElement headElement = jo.get("head");
			JsonObject bodyObject = jo.get("body").getAsJsonObject();

			Head head = gson.fromJson(headElement, Head.class);

			String classCode = head.getAction();
			String methodCode = head.getMethod();

			Class<?> classType = cd.getClass(classCode);
			Method method = cd.getMethod(classCode, methodCode);
			if (null != classType && null != method) {
				Object filter = applicationContext.getBean(classType);
				if ((null != filter)) {
					boolean isAuth = interceptor(filter, method, socketSession, head);
					Parameter[] parameters = method.getParameters();
					Object[] data = getParameterValues(parameters, head, bodyObject, socketSession);

					try {
						if (isAuth) {
							result = method.invoke(filter, data);
							if (null == result) {
								Head h=new Head();
								h.setKey(head.getKey());
								h.setAction("");
								h.setMethod("");
								h.setTime(System.currentTimeMillis());
								Message m = new Message();
								m.setHead(h);
								m.setInfoCode(Message.code_success);
								write(socketSession, m);
							} else {
								if (result instanceof AbstractMessage) {
									AbstractMessage m = (AbstractMessage) result;
									if (null == m.getHead()) {
										head.setTime(System.currentTimeMillis());
										m.setHead(head);
									}
									write(socketSession, m);
								} else {
									head.setTime(System.currentTimeMillis());
									PushMessage m = new PushMessage();
									m.setHead(head);
									m.setBody(result);
									write(socketSession, m);
								}
							}
						} else {
							
							Head h=new Head();
							h.setKey(head.getKey());
							h.setAction("");
							h.setMethod("");
							h.setResultCode(Head.code_fail);
							h.setResultMessage("您没有权限访问！");
							h.setTime(System.currentTimeMillis());
							
							Message m = new Message();
							m.setHead(h);
							write(socketSession, m);
						}
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

	private boolean interceptor(Object filter, Method method, SocketSession socketSession, Head head) {
		boolean isAuth = false;
		if (!isIntercept(method)) {
			isAuth = true;
		} else {
			isAuth = socketSession.isAuth();
		}
		return isAuth;
	}

	private boolean isIntercept(Method method) {
		boolean isIntercept = true;
		MethodMapping mm = method.getAnnotation(MethodMapping.class);
		if (null != mm) {
			isIntercept = mm.isIntercept();
		}
		return isIntercept;
	}

	private Object[] getParameterValues(Parameter[] parameters, Head head, JsonObject bodyObject, SocketSession socketSession) {
		Object[] data = new Object[parameters.length];
		if (null != parameters) {
			int length = parameters.length;
			for (int i = 0; i < length; i++) {
				Object value = null;
				Parameter p = parameters[i];
				Type type = p.getParameterizedType();
				com.only.parameter.annotation.Parameter pd = p
						.getAnnotation(com.only.parameter.annotation.Parameter.class);
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
						} else if (genericClass == SocketSession.class
								|| Session.class.isAssignableFrom(genericClass)) {
							value = socketSession;
						} else if (OnlyPropertyUtil.isString(genericClass)) {

						} else if (OnlyPropertyUtil.isPrimitive(genericClass)) {

						} else {
							// applicationContext.getBeanNamesForType(genericClass);
							value = applicationContext.getBean(genericClass);
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

	private void write(SocketSession socketSession, Object object) {
		if (null != object) {
			socketSession.write(object);
		}
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

	public void onClose(SocketSession socketSession) {
		sessionHandler.removeSession(socketSession);
	}
}
