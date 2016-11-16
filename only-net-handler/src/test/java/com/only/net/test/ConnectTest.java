package com.only.net.test;

import com.only.net.connect.ConnectThread;

/**
 * @Author: XiaHui
 * @Date: 2016年1月7日
 * @ModifyUser: XiaHui
 * @ModifyDate: 2016年1月7日
 */
public class ConnectTest {

	public static void main(String[] args) {
		ConnectThread ct=new ConnectThread();
		ct.start();
		ct.setAutoConnect(true);
	}

}
