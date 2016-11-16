package com.im.bean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 描述：群成员
 * 
 * @author XiaHui
 * @date 2015年3月13日 下午8:30:06
 * @version 0.0.1
 */
@Entity(name = "im_group_member")
public class GroupMember {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id = 0;
	private String groupId;// 帐号
	private String userId;//群成员id
	private int position;//权限

	//
	public static final int position_owner = 1;//群主
	public static final int position_admin = 2;//管理员
	public static final int position_normal = 3;//普通成员

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

}
