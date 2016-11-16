package com.oim.core.business.controller;

import com.oim.core.app.AppContext;
import com.oim.core.bean.User;
import com.oim.core.business.sender.PersonalSender;
import com.oim.core.business.service.PersonalService;
import com.oim.core.common.annotation.Parameter;
import com.oim.core.common.app.controller.BaseController;
import com.oim.core.common.app.view.LoginView;
import com.oim.core.common.box.PersonalBox;
import com.oim.core.common.config.ConfigManage;
import com.oim.core.common.config.data.ConnectConfigData;
import com.oim.core.net.message.Info;
import com.oim.core.net.message.data.LoginData;
import com.oim.core.net.server.Back;
import com.only.net.action.ConnectBackAction;
import com.only.net.connect.ConnectData;
import com.only.net.data.action.DataBackActionAdapter;

public class PersonalController extends BaseController {

	public PersonalController(AppContext appContext) {
		super(appContext);
	}

	public void login(LoginData loginData) {

		LoginView loginView = appContext.getSingleView(LoginView.class);
		boolean mark = true;
		StringBuilder sb = new StringBuilder();
		if (null == loginData.getAccount() || "".equals(loginData.getAccount())) {
			sb.append("账号不能为空!");
			sb.append("\n");
			mark = false;
		}
		if (null == loginData.getPassword() || "".equals(loginData.getPassword())) {
			sb.append("密码不能为空!");
			mark = false;
		}

		if (!mark) {
			loginView.showPrompt(sb.toString());
		} else {
			loginView.showWaiting(true);
			new Thread() {
				public void run() {
					while (!appContext.isViewReady()) {
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					doLogin(loginData);
				}
			}.start();
		}
	}

	private void doLogin(LoginData loginData) {
		PersonalBox.put(LoginData.class, loginData);
		LoginView loginView = appContext.getSingleView(LoginView.class);
		DataBackActionAdapter action = new DataBackActionAdapter() {// 这是消息发送后回掉
			@Override
			public void lost() {
				loginView.showWaiting(false);
				loginView.showPrompt("登录失败，请检查网络是否正常。");
			}

			@Override
			public void timeOut() {
				loginView.showWaiting(false);
				loginView.showPrompt("登录超时，请检查网络是否正常。");
			}

			@Back
			public void back(Info info, @Parameter("user") User user) {
				loginView.showWaiting(false);
				PersonalService ps = appContext.getService(PersonalService.class);
				ps.loginBack(info, user);
			}
		};

		ConnectConfigData ccd = (ConnectConfigData) ConfigManage.get(ConnectConfigData.path, ConnectConfigData.class);
		ConnectData connectData = new ConnectData();
		connectData.setAddress(ccd.getBusinessAddress());
		connectData.setPort(ccd.getBusinessPort());

		PersonalSender ps = this.appContext.getSender(PersonalSender.class);
		ConnectBackAction cba = new ConnectBackAction() {

			@Override
			public void connectBack(boolean success) {
				ps.login(loginData, action);
			}
		};

		if (this.appContext.getConnectThread().isConnected()) {
			ps.login(loginData, action);
		} else {
			// 因为负责连接服务器的和负责发送消息的线程不同，在执行登录之前是没有连接的，所以在这里先添加个连接后回掉的action
			// 当连接成功后再把登陆消息发出去，不然先把消息发了，再连接就没有执行登陆操作了
			this.appContext.getConnectThread().addConnectBackAction(cba);
			this.appContext.getConnectThread().setConnectData(connectData);
			this.appContext.getConnectThread().setAutoConnect(true);
		}
	}
}
