package com.test;

import org.dom4j.Element;

import com.only.xml.XmlNodeEncoder;

public class ArrayToXmlTest {

	public static void main(String[] args) {
		String t[][][] = { { { "1", "2", "3", "4" }, { "1", "2" }, { "1", "2" }, {} }, { { "1", "2" }, { "1", "2" }, {} }, {} };
		XmlNodeEncoder en = new XmlNodeEncoder();
		Element element = en.arrayToElementNode(t, "color");
		System.out.println(element.asXML());
	}

}
