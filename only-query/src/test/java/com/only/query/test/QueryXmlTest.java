package com.only.query.test;

import com.only.query.QueryWrapper;
import com.only.query.hibernate.QueryContext;
import com.only.query.hibernate.QueryItemException;

/**
 * @Author: XiaHui
 * @Date: 2015年12月18日
 * @ModifyUser: XiaHui
 * @ModifyDate: 2015年12月18日
 */
public class QueryXmlTest {

	public static void main(String[] arg) {
		QueryContext qc = new QueryContext();
		qc.setConfigLocation("classpath:query/*.xml");
		try {
			qc.load();
		} catch (QueryItemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// QueryItem queryItem=qc.getQueryItem("user.queryUserList");

		QueryWrapper queryWrapper = new QueryWrapper();
		queryWrapper.addParameter("name", "1000");
		queryWrapper.addParameter("introduce", "1000");
		queryWrapper.addParameter("super", "1000");

		// String
		// sql=qc.getQueryContent("user.queryUserList",queryWrapper.getParameterMap());
		// System.out.println(sql);

		qc.getQueryContent("demo.queryList", queryWrapper.getParameterMap());
		// System.out.println(sql);
	}
}
