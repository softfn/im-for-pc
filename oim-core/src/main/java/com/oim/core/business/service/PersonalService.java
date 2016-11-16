package com.oim.core.business.service;

import com.oim.core.app.AppContext;
import com.oim.core.bean.User;
import com.oim.core.business.manage.SystemManage;
import com.oim.core.business.manage.VideoManage;
import com.oim.core.business.sender.UserSender;
import com.oim.core.business.sender.VideoSender;
import com.oim.core.common.annotation.Parameter;
import com.oim.core.common.app.service.Service;
import com.oim.core.common.app.view.LoginView;
import com.oim.core.common.app.view.MainView;
import com.oim.core.common.app.view.TrayView;
import com.oim.core.common.box.PersonalBox;
import com.oim.core.common.config.ConfigManage;
import com.oim.core.common.config.data.ConnectConfigData;
import com.oim.core.common.task.ExecuteTask;
import com.oim.core.net.message.Head;
import com.oim.core.net.message.Info;
import com.oim.core.net.message.Message;
import com.oim.core.net.message.data.AddressData;
import com.oim.core.net.message.data.LoginData;
import com.oim.core.net.server.Back;
import com.only.net.data.action.DataBackAction;
import com.only.net.data.action.DataBackActionAdapter;

/**
 * 描述：
 * 
 * @author XiaHui
 * @date 2016年1月7日 下午8:37:18
 * @version 0.0.1
 */
public class PersonalService extends Service {

	public PersonalService(AppContext appContext) {
		super(appContext);
	}

	public void login() {

	}

	/**
	 * 登录后回掉
	 * 
	 * @param userMessage
	 */
	public void loginBack(final Info info, final User user) {
		boolean isLogin = Message.code_success.equals(info.getCode());
		LoginView loginView = this.appContext.getSingleView(LoginView.class);
		if (isLogin) {
			loginView.setVisible(false);
			MainView mainView = this.appContext.getSingleView(MainView.class);
			mainView.setVisible(true);
			mainView.setUser(user);
			TrayView trayView = this.appContext.getSingleView(TrayView.class);
			trayView.showAllMenu(true);

			ExecuteTask et = new ExecuteTask() {

				@Override
				public void execute() {
					SystemManage sm = appContext.getManage(SystemManage.class);
					sm.initApp(user);
					LoginData loginData = PersonalBox.get(LoginData.class);
					UserSender us = appContext.getSender(UserSender.class);
					us.sendUpdateStatus(loginData.getStatus());
					initVideoService();
					initBeatMessage();
				}
			};
			appContext.add(et);

		} else {
			this.appContext.getConnectThread().setAutoCloseConnectTime(1);
			loginView.showPrompt(info.getValue());
		}
	}

	/**
	 * 设置发送心跳包(当tcp太久没发送消息的时候，可能已经断开连接了，这个用来保持连接)(不熟mina的心跳机制，就这样了，懒得去研究了)
	 */
	private void initBeatMessage() {
		Message beatData = new Message();
		Head head = new Head();
		head.setAction("000");
		head.setMethod("0001");
		beatData.setHead(head);

		this.appContext.getDataWriteThread().setIntervalBeatTime(1000 * 60 * 5);
		this.appContext.getDataWriteThread().setBeatData("heartbeat", beatData);
		this.appContext.getDataWriteThread().setSendBeatData(true);
	}

	private void initVideoService() {
		DataBackAction dataBackAction = new DataBackActionAdapter() {
			@Back
			public void back(@Parameter("videoAddress") AddressData videoAddress) {
				ConnectConfigData ccd = (ConnectConfigData) ConfigManage.get(ConnectConfigData.path, ConnectConfigData.class);
				videoAddress.setAddress(ccd.getBusinessAddress());
				setVideoAddress(videoAddress);
			}
		};
		VideoSender vh = this.appContext.getSender(VideoSender.class);
		vh.getVideoServerPort(dataBackAction);
	}

	private void setVideoAddress(AddressData videoAddress) {
		VideoManage vm = this.appContext.getManage(VideoManage.class);
		vm.setVideoServerAddress(videoAddress);
	}
}
