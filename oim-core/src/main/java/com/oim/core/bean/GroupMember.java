package com.oim.core.bean;



/**
 * 描述：群成员
 * 
 * @author XiaHui
 * @date 2015年3月13日 下午8:30:06
 * @version 0.0.1
 */
public class GroupMember {


	private int id = 0;
	private String groupId;// 帐号
	private String userId;
	private int position;

	//
	public static final int position_owner = 1;
	public static final int position_admin = 2;
	public static final int position_normal = 3;

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
