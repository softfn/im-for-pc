package com.oim.business.handler;

import com.oim.app.AppContext;
import com.oim.bean.User;
import com.oim.common.app.handler.SendHandler;
import com.oim.common.box.PersonalBox;
import com.oim.common.config.ConfigManage;
import com.oim.common.config.data.ConnectConfigData;
import com.oim.net.message.Head;
import com.oim.net.message.Message;
import com.oim.net.message.data.LoginData;
import com.only.net.action.ConnectBackAction;
import com.only.net.connect.ConnectData;
import com.only.net.data.action.DataBackAction;
import com.only.net.data.action.DataBackActionAdapter;

/**
 * 描述：
 * 
 * @author XiaHui
 * @date 2016年1月6日 下午8:26:31
 * @version 0.0.1
 */
public class PersonalHandler extends SendHandler {

	public PersonalHandler(AppContext appContext) {
		super(appContext);
	}

	/**
	 * 登录,为了层次更分明，所以绕了这么远，最终在这里把消息发送出去。而不是直接在ui层就发送了
	 * 
	 * @param loginData
	 * @param action
	 */
	public void login(LoginData loginData, final DataBackActionAdapter action) {

		final Message message = new Message();
		message.put("loginData", loginData);

		Head head = new Head();
		head.setAction("100");
		head.setMethod("0001");
		head.setTime(System.currentTimeMillis());
		message.setHead(head);

		ConnectConfigData ccd = (ConnectConfigData) ConfigManage.get(ConnectConfigData.path, ConnectConfigData.class);
		ConnectData connectData = new ConnectData();
		connectData.setAddress(ccd.getBusinessAddress());
		connectData.setPort(ccd.getBusinessPort());
		ConnectBackAction cba = new ConnectBackAction() {

			@Override
			public void connectBack(boolean success) {
				PersonalHandler.this.appContext.write(message, action);
			}
		};

		if (this.appContext.getConnectThread().isConnected()) {
			this.appContext.write(message, action);
		} else {
			// 因为负责连接服务器的和负责发送消息的线程不同，在执行登录之前是没有连接的，所以在这里先添加个连接后回掉的action
			// 当连接成功后再把登陆消息发出去，不然先把消息发了，再连接就没有执行登陆操作了
			this.appContext.getConnectThread().addConnectBackAction(cba);
			this.appContext.getConnectThread().setConnectData(connectData);
			this.appContext.getConnectThread().setAutoConnect(true);
		}
		PersonalBox.put(LoginData.class, loginData);
	}

	/**
	 * 重连接，当断网后又恢复网络时
	 * 
	 * @Author: XiaHui
	 * @Date: 2016年2月16日
	 * @ModifyUser: XiaHui
	 * @ModifyDate: 2016年2月16日
	 */
	public void reconnect() {
		LoginData loginData = PersonalBox.get(LoginData.class);
		Message message = new Message();
		message.put("loginData", loginData);
		Head head = new Head();
		head.setAction("100");
		head.setMethod("0002");
		head.setTime(System.currentTimeMillis());
		message.setHead(head);
		this.appContext.getConnectThread().setAutoConnect(true);
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
