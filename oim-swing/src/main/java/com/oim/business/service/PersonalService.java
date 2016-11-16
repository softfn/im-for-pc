package com.oim.business.service;

import java.awt.EventQueue;

import com.oim.app.AppContext;
import com.oim.bean.User;
import com.oim.business.handler.UserHandler;
import com.oim.business.handler.VideoHandler;
import com.oim.business.manage.AppManage;
import com.oim.business.manage.PersonalManage;
import com.oim.business.manage.VideoManage;
import com.oim.common.annotation.Parameter;
//import com.oim.business.manage.VideoManage;
import com.oim.common.app.service.Service;
import com.oim.common.box.PersonalBox;
import com.oim.common.config.ConfigManage;
import com.oim.common.config.data.ConnectConfigData;
import com.oim.common.task.ExecuteTask;
import com.oim.net.message.Head;
import com.oim.net.message.Info;
import com.oim.net.message.Message;
import com.oim.net.message.data.LoginData;
import com.oim.net.message.data.AddressData;
import com.oim.net.server.Back;
//import com.oim.net.message.data.VideoAddress;
//import com.oim.net.server.Back;
import com.oim.ui.view.LoginView;
import com.only.net.data.action.DataBackAction;
import com.only.net.data.action.DataBackActionAdapter;
//import com.only.net.data.action.DataBackActionAdapter;

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

	/**
	 * 登录后回掉
	 * 
	 * @param userMessage
	 */
	public void loginBack(final Info info, final User user) {

		boolean isLogin = Message.code_success.equals(info.getCode());
		final LoginView loginView = this.getSingleView(LoginView.class);
		if (isLogin) {
			loginView.setVisible(false);

			// DataBackActionAdapter dataBackAction=new DataBackActionAdapter(){
			// @Back
			// public void back(){
			//
			// }
			// };

			ExecuteTask et = new ExecuteTask() {

				@Override
				public void execute() {
					AppManage am = appContext.getManage(AppManage.class);
					am.initApp(user);
					PersonalManage pm = appContext.getManage(PersonalManage.class);
					pm.initPersonal(user);
					initBeatMessage();
					initVideoService();
				}
			};
			appContext.add(et);
		} else {
			this.appContext.getConnectThread().setAutoCloseConnectTime(1);
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					loginView.showPromptMessage(info.getValue());
				}
			});
		}
		LoginData loginData = PersonalBox.get(LoginData.class);
		UserHandler uh = appContext.getHandler(UserHandler.class);
		uh.sendUpdateStatus(loginData.getStatus());
		loginView.showWaiting(false);
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
		VideoHandler vh = this.appContext.getHandler(VideoHandler.class);
		vh.getVideoServerPort(dataBackAction);
	}

	private void setVideoAddress(AddressData videoAddress) {
		VideoManage vm = this.appContext.getManage(VideoManage.class);
		vm.setVideoServerAddress(videoAddress);
	}
}
