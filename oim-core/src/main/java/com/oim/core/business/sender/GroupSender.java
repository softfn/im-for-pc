package com.oim.core.business.sender;

import com.oim.core.app.AppContext;
import com.oim.core.bean.Group;
import com.oim.core.common.app.sender.Sender;
import com.oim.core.net.message.Head;
import com.oim.core.net.message.Message;
import com.oim.core.net.message.different.PageImpl;
import com.oim.core.net.message.query.GroupQuery;
import com.only.net.data.action.DataBackAction;
import com.only.net.data.action.DataBackActionAdapter;

/**
 * @author XiaHui
 * @date 2015年3月16日 下午3:23:23
 */
public class GroupSender extends Sender {

	public GroupSender(AppContext appContext) {
		super(appContext);
	}

	/**
	 * 发送获取群列表请求
	 * 
	 * @Author: XiaHui
	 * @Date: 2016年2月16日
	 * @ModifyUser: XiaHui
	 * @ModifyDate: 2016年2月16日
	 */
	public void getGroupCategoryWithGroupList() {
		Message message = new Message();
		Head head = new Head();
		head.setAction("200");
		head.setMethod("0004");
		head.setTime(System.currentTimeMillis());
		message.setHead(head);
		
		this.appContext.write(message);
	}

	/**
	 * 发送新建群信息
	 * 
	 * @Author: XiaHui
	 * @Date: 2016年2月16日
	 * @ModifyUser: XiaHui
	 * @ModifyDate: 2016年2月16日
	 * @param group
	 * @param action
	 */
	public void addGroup(Group group, DataBackAction action) {
		Message message = new Message();
		message.put("group", group);
		Head head = new Head();
		head.setAction("200");
		head.setMethod("0008");
		head.setTime(System.currentTimeMillis());
		message.setHead(head);
		this.appContext.write(message, action);
	}

	/***
	 * 发送修改群信息
	 * 
	 * @Author: XiaHui
	 * @Date: 2016年2月16日
	 * @ModifyUser: XiaHui
	 * @ModifyDate: 2016年2月16日
	 * @param group
	 * @param action
	 */
	public void updateGroup(Group group, DataBackAction action) {
		Message message = new Message();
		message.put("group", group);
		Head head = new Head();
		head.setAction("200");
		head.setMethod("0009");
		head.setTime(System.currentTimeMillis());
		message.setHead(head);
		this.appContext.write(message, action);
	}

	/**
	 * 发送查询群消息
	 * 
	 * @Author: XiaHui
	 * @Date: 2016年2月16日
	 * @ModifyUser: XiaHui
	 * @ModifyDate: 2016年2月16日
	 * @param group
	 * @param page
	 * @param dataBackAction
	 */
	public void queryGroupList(GroupQuery groupQuery, PageImpl page, DataBackActionAdapter dataBackAction) {
		Message message = new Message();
		message.put("groupQuery", groupQuery);
		message.put("page", page == null ? (new PageImpl()) : page);
		Head head = new Head();
		head.setAction("200");
		head.setMethod("0006");
		head.setTime(System.currentTimeMillis());
		message.setHead(head);
		this.appContext.write(message, dataBackAction);
	}

	/**
	 * 发送获取群成员信息
	 * 
	 * @Author: XiaHui
	 * @Date: 2016年2月16日
	 * @ModifyUser: XiaHui
	 * @ModifyDate: 2016年2月16日
	 * @param groupId
	 * @param action
	 */
	public void getGroupMemberListWithUserDataList(String groupId, DataBackAction action) {
		Message message = new Message();
		message.put("groupId", groupId);
		Head head = new Head();
		head.setAction("200");
		head.setMethod("0010");
		head.setTime(System.currentTimeMillis());
		message.setHead(head);
		this.appContext.write(message, action);
	}
}
