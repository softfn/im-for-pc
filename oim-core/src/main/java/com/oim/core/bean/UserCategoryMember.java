package com.oim.core.bean;


/**
 * 描述：好友分组成员
 * 
 * @author XiaHui
 * @date 2014年6月15日 下午2:32:18
 * @version 0.0.1
 */
public class UserCategoryMember {

	private int id = 0;
	private String ownUserId;
	private String userCategoryId;
	private String memberUserId;
	private String remark;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOwnUserId() {
		return ownUserId;
	}

	public void setOwnUserId(String ownUserId) {
		this.ownUserId = ownUserId;
	}

	public String getUserCategoryId() {
		return userCategoryId;
	}

	public void setUserCategoryId(String userCategoryId) {
		this.userCategoryId = userCategoryId;
	}

	public String getMemberUserId() {
		return memberUserId;
	}

	public void setMemberUserId(String memberUserId) {
		this.memberUserId = memberUserId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
