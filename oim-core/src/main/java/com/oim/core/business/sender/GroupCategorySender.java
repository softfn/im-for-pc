package com.oim.core.business.sender;

import com.oim.core.app.AppContext;
import com.oim.core.bean.GroupCategory;
import com.oim.core.bean.GroupCategoryMember;
import com.oim.core.common.app.sender.Sender;
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
public class GroupCategorySender extends Sender {

	public GroupCategorySender(AppContext appContext) {
		super(appContext);
	}

	/**
	 * 向服务器发送添加群分组信息
	 * 
	 * @Author: XiaHui
	 * @Date: 2016年2月16日
	 * @ModifyUser: XiaHui
	 * @ModifyDate: 2016年2月16日
	 * @param name
	 * @param action
	 */
	public void addGroupCategory(String name, DataBackAction action) {

		GroupCategory groupCategory = new GroupCategory();
		groupCategory.setName(name);

		Message message = new Message();
		message.put("groupCategory", groupCategory);
		
		Head head = new Head();
		head.setAction("201");
		head.setMethod("0001");
		head.setTime(System.currentTimeMillis());
		message.setHead(head);
		
		this.appContext.write(message, action);
	}

	/**
	 * 向服务器发送添加加入群信息
	 * 
	 * @Author: XiaHui
	 * @Date: 2016年2月16日
	 * @ModifyUser: XiaHui
	 * @ModifyDate: 2016年2月16日
	 * @param groupCategoryId
	 * @param groupId
	 * @param remark
	 * @param action
	 */
	public void addGroupCategoryMember(String groupCategoryId, String groupId, String remark, DataBackAction action) {
		GroupCategoryMember groupCategoryMember = new GroupCategoryMember();

		groupCategoryMember.setGroupCategoryId(groupCategoryId);// 要加入的群分配的分组。
		groupCategoryMember.setGroupId(groupId);// 群id
		groupCategoryMember.setRemark(remark);// 群的备注名

		Message message = new Message();
		message.put("groupCategoryMember", groupCategoryMember);
		Head head = new Head();
		head.setAction("201");
		head.setMethod("0002");
		head.setTime(System.currentTimeMillis());
		message.setHead(head);
		
		this.appContext.write(message, action);
	}
}
