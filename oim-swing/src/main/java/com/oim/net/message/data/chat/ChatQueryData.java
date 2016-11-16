package com.oim.net.message.data.chat;

import java.util.Date;

/**
 * @author: XiaHui
 * @date: 2016年8月23日 上午11:02:39
 */
public class ChatQueryData {

	private String text;
	private Date startDate;
	private Date endDate;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
}
