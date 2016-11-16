package com.only.spring.resolver;

import com.google.gson.Gson;
import com.only.parameter.annotation.Parameter;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;


import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class ParameterResolver implements HandlerMethodArgumentResolver {

    Gson gson = new Gson();

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(Parameter.class) != null;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest request, WebDataBinderFactory binderFactory) throws Exception {
        Object object = null;
        Parameter pd = parameter.getParameterAnnotation(Parameter.class);

        if (pd != null) {
            String parameterName = pd.value();
            String type = pd.type();
            if (null == parameterName || "".equals(parameterName)) {
                parameterName = parameter.getParameterName();
            }
            Type t = parameter.getGenericParameterType();
            Class<?> clazz = parameter.getParameterType();

            if (Parameter.TYPE_JSON.equals(type)) {
                String value = request.getParameter(parameterName);
                if (null != value && !"".equals(value)) {
                    object = gson.fromJson(value, t);
                } else {
                    object = clazz.newInstance();
                }
            } else {
                object = clazz.newInstance();
                HashMap<String, String[]> paramsMap = new HashMap<String, String[]>();
                Iterator<String> i = request.getParameterNames();
                while (i.hasNext()) {
                    String webParam = (String) i.next();
                    String[] webValue = request.getParameterValues(webParam);
                    if (webParam.startsWith(parameterName)) {
                        paramsMap.put(webParam, webValue);
                    }
                }
                BeanWrapper beanWrapper = new BeanWrapperImpl(object);
                beanWrapper.registerCustomEditor(Date.class, null, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
                for (String propName : paramsMap.keySet()) {
                    String[] value = paramsMap.get(propName);
                    String[] valueNameArray = propName.split("\\.");
                    if (valueNameArray.length == 2) {
                        beanWrapper.setPropertyValue(valueNameArray[1], value);
                    } else if (valueNameArray.length == 3) {
                        Object tempObject = beanWrapper.getPropertyValue(valueNameArray[1]);
                        if (tempObject == null) {
                            beanWrapper.setPropertyValue(valueNameArray[1], beanWrapper.getPropertyType(valueNameArray[1]).newInstance());
                        }
                        beanWrapper.setPropertyValue(valueNameArray[1] + "." + valueNameArray[2], value);
                    }
                }
            }
        }
        return (null == object) ? new Object() : object;
    }
}
