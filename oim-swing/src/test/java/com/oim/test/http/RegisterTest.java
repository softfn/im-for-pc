package com.oim.test.http;

import java.util.Random;

import com.oim.bean.User;
import com.oim.common.util.DateUtil;

/**
 * 描述：
 * 
 * @author XiaHui
 * @date 2016年1月17日 下午3:08:27
 * @version 0.0.1
 */
public class RegisterTest {

	public static void main(String[] args) {
		for (int i = 0; i < 100; i++) {
			reg(i);
		}

	}

	private static void reg(int i) {

		int y = new Random().nextInt(40);
		int m = new Random().nextInt(11);
		int d = new Random().nextInt(27);
		y = y + 60;
		m++;
		d++;

		int ci = new Random().nextInt(11);
		int cb = new Random().nextInt(4);
		String[] c = (new String[] { "水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "摩羯座" });
		String[] b = (new String[] { "A", "B", "AB", "O", "其他" });

		String nickname = "我就是板车" + i + "号";
		String password = "123456";
		String gender = ((i % 2) > 0) ? "男" : "女";
		String birthdate = "19" + DateUtil.intMonthOrDayToString(y) + "-" + DateUtil.intMonthOrDayToString(m) + "-" + DateUtil.intMonthOrDayToString(d);
		String address = "天宫" + i + "号";
		String signature = "板车在，人在！";
		String blood = b[cb];
		String constellation = c[ci];


		User user = new User();

		user.setPassword(password);
		user.setNickname(nickname);
		user.setGender(gender);
		user.setBirthdate(birthdate);
		user.setHomeAddress(address);
		user.setSignature(signature);
		user.setBlood(blood);
		user.setConstellation(constellation);


	}
}
