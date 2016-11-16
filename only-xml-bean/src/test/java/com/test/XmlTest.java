package com.test;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.only.xml.XmlNodeEncoder;
import com.only.xml.XmlTools;
import com.test.bean.Message;
import com.test.bean.User;

public class XmlTest {

	public static void main(String[] args) {
		Message m = new Message();
		m.setTeamArray(new int[]{1,2});
		m.setColor(new String[][][]{{{"3"},{},{},{}},{{"5","7"}},{{"hhhh","g","3","2"}},{{}}});
		m.setUserArray(new User[]{new User(),new User()});
		 XmlNodeEncoder xmlEncoder = new XmlNodeEncoder();
		String xml1 = xmlEncoder.objectToXmlWithoutHead(m);
		System.out.println(xml1);
		//Message user = tools.xmlNodeToObject(xml1, Message.class);
		String xml2 = xmlEncoder.objectToElementXml(m);
		System.out.println(xml2);
		Document document = null;
		try {
			document = DocumentHelper.parseText(xml2);
			Element e=document.getRootElement();
			System.out.println(e.asXML());
		} catch (DocumentException e) {
			e.printStackTrace();
		}

	}

}
