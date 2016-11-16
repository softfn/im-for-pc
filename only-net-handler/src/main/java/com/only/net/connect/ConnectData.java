package com.only.net.connect;

/**
 * @Author: XiaHui
 * @Date: 2016-1-4
 * @ModifyUser: XiaHui
 * @ModifyDate: 2016-1-4
 */
public class ConnectData {

	String address = "127.0.0.1";
	int port = 12000;
	long timeOut = 30000;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public long getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(long timeOut) {
		this.timeOut = timeOut;
	}
}
