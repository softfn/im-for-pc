package com.im.bean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 描述： 因为用户账号与用户Id不是同一字段，当用户注册的时候用来生成账号
 * 
 * @author XiaHui
 * @date 2014年6月15日 下午2:17:07
 * @version 0.0.1
 */
@Entity(name = "im_user_number")
public class UserNumber {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String dateTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

}
