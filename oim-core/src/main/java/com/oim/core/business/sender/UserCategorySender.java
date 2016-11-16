package com.oim.core.business.sender;

import com.oim.core.app.AppContext;
import com.oim.core.bean.User;
import com.oim.core.bean.UserCategory;
import com.oim.core.bean.UserCategoryMember;
import com.oim.core.common.app.sender.Sender;
import com.oim.core.common.box.PersonalBox;
import com.oim.core.net.message.Head;
import com.oim.core.net.message.Message;
import com.only.net.data.action.DataBackAction;

/**
 * 描述：
 * 
 * @author XiaHui
 * @date 2016年1月8日 下午8:22:48
 * @version 0.0.1
 */
public class UserCategorySender extends Sender {

	public UserCategorySender(AppContext appContext) {
		super(appContext);
	}

	/***
	 * 新增好友分组
	 * 
	 * @Author: XiaHui
	 * @Date: 2016年2月16日
	 * @ModifyUser: XiaHui
	 * @ModifyDate: 2016年2月16日
	 * @param name
	 * @param action
	 */
	public void addUserCategory(String name, DataBackAction action) {
		User user = PersonalBox.get(User.class);
		
		UserCategory userCategory = new UserCategory();
		userCategory.setName(name);
		userCategory.setUserId(user.getId());
		

		Message message = new Message();
		message.put("userCategory", userCategory );

		Head head = new Head();
		
		head.setAction("102");
		head.setMethod("0001");
		head.setTime(System.currentTimeMillis());
		message.setHead(head);
		
		this.write(message, action);
	}

	/**
	 * 添加好友
	 * 
	 * @Author: XiaHui
	 * @Date: 2016年2月16日
	 * @ModifyUser: XiaHui
	 * @ModifyDate: 2016年2月16日
	 * @param userCategoryId
	 * @param memberUserId
	 * @param remark
	 * @param action
	 */
	public void addUserCategoryMember(String userCategoryId, String memberUserId, String remark, DataBackAction action) {
		
		User user = PersonalBox.get(User.class);
		
		UserCategoryMember userCategoryMember = new UserCategoryMember();

		userCategoryMember.setUserCategoryId(userCategoryId);
		userCategoryMember.setMemberUserId(memberUserId);
		userCategoryMember.setRemark(remark);
		userCategoryMember.setOwnUserId(user.getId());
		

		Message message = new Message();
		message.put("userCategoryMember", userCategoryMember );

		Head head = new Head();
		head.setAction("102");
		head.setMethod("0002");
		head.setTime(System.currentTimeMillis());
		message.setHead(head);
		this.write(message, action);
	}
}
