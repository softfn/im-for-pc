package com.test;

import com.only.xml.XmlTools;
import com.test.bean.Message;
import com.test.bean.User;

public class ObjectTest {

	public static void main(String[] args) {
		Message m = new Message();
		m.setTeamArray(new int[]{1,2});
		m.setColor(new String[][][]{{{"3"},{},{},{}},{{"5","7"}},{{"hhhh","g","3","2"}},{{}}});
		m.setUserArray(new User[]{new User(),new User()});
		XmlTools tools = new XmlTools();
		String xml1 = tools.objectToXmlNode(m);
		System.out.println(xml1);
		Message user = tools.xmlNodeToObject(xml1, Message.class);
		String xml2 = tools.objectToXmlNode(user);
		System.out.println(xml2);
	}

}
