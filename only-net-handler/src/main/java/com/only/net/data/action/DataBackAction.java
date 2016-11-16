/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.only.net.data.action;

/**
 * 2013-8-29 16:46:32 信息发送后回调Action
 * 
 * @author XiaHui
 */
public interface DataBackAction {

	/**
	 * 发送失败执行
	 * 
	 * @param data
	 */
	void lost();

	/**
	 * 发送超时执行
	 * 
	 * @param data
	 */
	void timeOut();
}
