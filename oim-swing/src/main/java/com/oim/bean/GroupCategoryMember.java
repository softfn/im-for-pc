package com.oim.bean;



/**
 * 描述：
 * 
 * @author XiaHui
 * @date 2014年6月15日 下午2:32:18
 * @version 0.0.1
 */

public class GroupCategoryMember {

	private int id = 0;
	private String userId;// 帐号
	private String groupCategoryId;
	private String groupId;
	private String remark;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getGroupCategoryId() {
		return groupCategoryId;
	}

	public void setGroupCategoryId(String groupCategoryId) {
		this.groupCategoryId = groupCategoryId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
