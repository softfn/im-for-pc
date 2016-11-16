package com.test;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Set;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.test.bean.Message;

public class ATest {

	public static void main(String[] args) {
		Field[] fields = Message.class.getDeclaredFields();
		for (Field field : fields) {
			String propertyName = field.getName();
			Class<?> classType = field.getType(); // 得到field的class及类型全路径
			Type type = field.getGenericType(); // 关键的地方，如果是List类型，得到其Generic的类型
			// array(classType);
			if (classType.isArray()) {
				Integer i=1;
				System.out.println(i+""+getArrayType(i,classType));
			}

		}
	}

	public static Class<?> getArrayType(int dimension ,Class<?> classType) {
		dimension++;
		Class<?> componentType = classType.getComponentType();
		if (null == componentType) {
			return classType;
		} else {
			return getArrayType(dimension,componentType);
		}

	}

	public static void array(Class<?> c) {
		try {
			Object o = c.newInstance();
			test(o);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void test(Object o) {
		if (o.getClass().isArray()) {
			int b = Array.getLength(o);
			for (int i = 0; i < b; i++) {
				Object v = Array.get(o, i);
				if (v.getClass().isArray()) {
					test(v);
				} else {
					System.out.println(v);
				}
			}
		}
	}
	
	
}
