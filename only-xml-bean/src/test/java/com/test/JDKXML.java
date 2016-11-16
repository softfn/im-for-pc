package com.test;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.test.bean.Message;
import com.test.bean.User;

/**
 * @author XiaHui
 * @date 2015年3月2日 下午12:15:54
 */
public class JDKXML {

	public static void main(String[] args) {
		Message m = new Message();
		m.setTeamArray(new int[] { 1, 2 });
		m.setColor(new String[][][] { { { "3" }, {}, {}, {} }, { { "5", "7" } }, { { "hhhh", "g", "3", "2" } }, { {} } });
		m.setUserArray(new User[] { new User(), new User() });

		XMLEncoder encoder;
		try {
			encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream("Beanarchive.xml")));

			encoder.writeObject(m);
			encoder.close();
			
			
			XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream("Beanarchive.xml")));

			Object object = decoder.readObject();
			decoder.close();
			System.out.println(object);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		
	}

}
