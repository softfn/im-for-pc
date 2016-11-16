package com.oim.core.business.sender;

import com.oim.core.app.AppContext;
import com.oim.core.bean.User;
import com.oim.core.common.app.sender.Sender;
import com.oim.core.net.message.Head;
import com.oim.core.net.message.Message;
import com.oim.core.net.message.data.LoginData;
import com.only.net.data.action.DataBackAction;
import com.only.net.data.action.DataBackActionAdapter;

/**
 * 描述：
 * 
 * @author XiaHui
 * @date 2016年1月6日 下午8:26:31
 * @version 0.0.1
 */
public class PersonalSender extends Sender {

	public PersonalSender(AppContext appContext) {
		super(appContext);
	}

	/**
	 * 登录,为了层次更分明，所以绕了这么远，最终在这里把消息发送出去。而不是直接在ui层就发送了
	 * 
	 * @param loginData
	 * @param action
	 */
	public void login(LoginData loginData, DataBackActionAdapter action) {

		Message message = new Message();
		message.put("loginData", loginData);

		Head head = new Head();
		head.setAction("100");
		head.setMethod("0001");
		head.setTime(System.currentTimeMillis());
		message.setHead(head);
		this.appContext.write(message,action);
	}

	/**
	 * 重连接，当断网后又恢复网络时
	 * 
	 * @Author: XiaHui
	 * @Date: 2016年2月16日
	 * @ModifyUser: XiaHui
	 * @ModifyDate: 2016年2月16日
	 */
	public void reconnect(LoginData loginData) {
		Message message = new Message();
		message.put("loginData", loginData);
		Head head = new Head();
		head.setAction("100");
		head.setMethod("0002");
		head.setTime(System.currentTimeMillis());
		message.setHead(head);
		this.appContext.write(message);
	}

	/**
	 * 发送修改密码请求
	 * 
	 * @Author: XiaHui
	 * @Date: 2016年2月16日
	 * @ModifyUser: XiaHui
	 * @ModifyDate: 2016年2月16日
	 * @param user
	 * @param action
	 */
	public void upadtePassword(String password, DataBackActionAdapter action) {
		Message message = new Message();
		message.put("password", password);
		Head head = new Head();
		head.setAction("100");
		head.setMethod("0005");
		head.setTime(System.currentTimeMillis());
		message.setHead(head);
		this.appContext.write(message, action);
	}

	public void updateUser(User user, DataBackAction action) {
		Message message = new Message();
		message.put("user", user);

		Head head = new Head();
		head.setAction("100");
		head.setMethod("0004");
		head.setTime(System.currentTimeMillis());
		message.setHead(head);
		this.appContext.write(message, action);
	}

	public void getUserData(DataBackAction action) {
		Message message = new Message();
		Head head = new Head();
		head.setAction("100");
		head.setMethod("0003");
		head.setTime(System.currentTimeMillis());
		message.setHead(head);
		this.appContext.write(message, action);
	}
}
