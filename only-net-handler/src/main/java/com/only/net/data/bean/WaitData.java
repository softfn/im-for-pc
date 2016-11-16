/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.only.net.data.bean;

import com.only.net.data.action.DataBackAction;

/**
 * 2013-9-6 10:59:43<br>
 * 已发送等待处理的消息
 * 
 * @author XiaHui
 */
public class WaitData {

	private Object data;
	private DataBackAction dataBackAction;
	private long sendTime;
	private String key;

	public WaitData() {
	}

	public WaitData(String key, Object data) {
		this.key = key;
		this.data = data;
	}

	public WaitData(String key, Object data, DataBackAction dataBackAction) {
		this.key = key;
		this.data = data;
		this.dataBackAction = dataBackAction;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public DataBackAction getDataBackAction() {
		return dataBackAction;
	}

	public void setDataBackAction(DataBackAction dataBackAction) {
		this.dataBackAction = dataBackAction;
	}

	public long getSendTime() {
		return sendTime;
	}

	public void setSendTime(long sendTime) {
		this.sendTime = sendTime;
	}

}
