package com.oim.business.handler;

import com.oim.app.AppContext;
import com.oim.common.app.handler.SendHandler;
import com.oim.net.message.Head;
import com.oim.net.message.Message;
import com.oim.net.message.different.PageImpl;
import com.oim.net.message.query.UserQuery;
import com.only.net.data.action.DataBackAction;

/**
 * @author XiaHui
 * @date 2015年3月16日 下午3:23:23
 */
public class UserHandler extends SendHandler {

	public UserHandler(AppContext appContext) {
		super(appContext);
	}

	/**
	 * 发送获取好友信息
	 */
	public void getUserCategoryWithUserList() {
		Message message = new Message();
		Head head = new Head();
		head.setAction("101");
		head.setMethod("0004");
		head.setTime(System.currentTimeMillis());
		message.setHead(head);
		this.write(message);
	}

	/**
	 * 查询用户
	 * 
	 * @Author: XiaHui
	 * @Date: 2016年2月16日
	 * @ModifyUser: XiaHui
	 * @ModifyDate: 2016年2月16日
	 * @param userData
	 * @param page
	 * @param dataBackAction
	 */
	public void queryUserDataList(UserQuery userQuery, PageImpl page, DataBackAction dataBackAction) {
		Message message = new Message();

		message.put("userQuery", userQuery);
		message.put("page", page == null ? (new PageImpl()) : page);

		Head head = new Head();
		head.setAction("101");
		head.setMethod("0006");
		head.setTime(System.currentTimeMillis());
		message.setHead(head);
		this.write(message, dataBackAction);
	}

	/**
	 * 用户状态发生变化时，给好友发送状态变化信息
	 * 
	 * @Author: XiaHui
	 * @Date: 2016年2月16日
	 * @ModifyUser: XiaHui
	 * @ModifyDate: 2016年2月16日
	 * @param status
	 */
	public void sendUpdateStatus(String status) {

		Message message = new Message();
		message.put("userId", "");
		message.put("status", status);

		Head head = new Head();
		head.setAction("101");
		head.setMethod("0008");
		head.setTime(System.currentTimeMillis());
		message.setHead(head);
		this.write(message);
	}

	/***
	 * 获取用户详细信息
	 * 
	 * @Author: XiaHui
	 * @Date: 2016年2月16日
	 * @ModifyUser: XiaHui
	 * @ModifyDate: 2016年2月16日
	 * @param userId
	 * @param dataBackAction
	 */
	public void getUserDataById(String userId, DataBackAction dataBackAction) {
		Message message = new Message();
		message.put("userId", userId);

		Head head = new Head();
		head.setAction("101");
		head.setMethod("0005");
		head.setTime(System.currentTimeMillis());
		message.setHead(head);
		this.write(message, dataBackAction);
	}
}
