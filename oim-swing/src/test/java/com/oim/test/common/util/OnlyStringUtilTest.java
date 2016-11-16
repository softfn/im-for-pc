package com.oim.test.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import com.oim.common.util.OnlyStringUtil;

/**
 * @author XiaHui
 * @date 2015年3月13日 下午2:17:43
 */
public class OnlyStringUtilTest {

	public static void main(String[] args) {

		File file = new File("Resources/Images/Default/Button/blue_down.png");
		try {
			FileInputStream in = new FileInputStream(file);
			long size = file.length();
			byte[] bytes = new byte[(int)size];
			StringBuilder b = new StringBuilder();
			while (in.read(bytes) != -1) {
				b.append(OnlyStringUtil.bytesToStringValue(bytes));
			}
			byte[] byted=OnlyStringUtil.stringValueToBytes(b.toString());
			File f = new File("temp/blue_down.png");
			FileOutputStream out = new FileOutputStream(f);
			out.write(byted);
			System.out.println(b);
			in.close();
			out.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
