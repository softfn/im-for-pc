package com.only.test.reflect;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;

import com.only.xml.XmlNodeDecoder;
import com.only.xml.XmlNodeEncoder;
import com.test.bean.User;

/**
 * @Author: XiaHui
 * @Date: 2016年1月26日
 * @ModifyUser: XiaHui
 * @ModifyDate: 2016年1月26日
 */
public class MethodTypeTest {

	public static void main(String k[]){
		XmlNodeDecoder xd=new XmlNodeDecoder();
		XmlNodeEncoder xe=new XmlNodeEncoder();
		
		User u=new User();
		u.setId(1);
		u.setName("user_1");
		String xml=xe.objectToXml(u);
		System.out.println(xml);
		
		List<User> list=new ArrayList<User>();
		list.add(u);
		xml=xe.objectToXml(list);
		System.out.println(xml);
		Method[] ms=UserBean.class.getMethods();
		for(Method m:ms){
			if("doWhat".equals(m.getName())){
				System.out.println("name:"+m.getName());
//				TypeVariable<Method>[] tvs=m.getTypeParameters();
//				for(TypeVariable t:tvs){
//					System.out.println(t.getClass());
//				}
				
				Type[] gps=m.getGenericParameterTypes();
				for(Type t:gps){
//					System.out.println(t instanceof Class<?>);
//					Object o=xd.xmlToObject(xml, t);
//					System.out.println(o);
//					if(  User.class.isAssignableFrom(t.getClass())){
//						System.out.println(t);
//					}
				}
				
//				Type[] ps=m.getParameterTypes();
//				for(Type t:ps){
//					System.out.println(t);
//				}
			}
		}
	}
}
