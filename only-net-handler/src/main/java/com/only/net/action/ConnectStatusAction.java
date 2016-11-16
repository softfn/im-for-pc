/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.only.net.action;

/**
 * 2013-9-6 10:11:31 <br>
 * 连接Action
 * 
 * @author XiaHui
 */
public interface ConnectStatusAction {

	/**
	 * 连接状态改变调用
	 * 
	 * @param isConnected
	 */
	void connectStatusChange(boolean isConnected);
}
