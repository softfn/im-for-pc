package com.oim.test;

import java.util.Calendar;
import java.util.Date;

import com.oim.common.util.DateUtil;

/**
 * 描述：
 * @author XiaHui 
 * @date 2015年4月26日 上午12:43:42
 * @version 0.0.1
 */
public class YearTest {

	public static void main(String[] args) {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH )+1;

		System.out.println(year + " 年 " + month + " 月");
		long now=System.currentTimeMillis();
		
		System.out.println(DateUtil.dateToDate(new Date((now-(22l*31536000000l)))));
	}

}
