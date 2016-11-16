package com.oim.ui.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;

import com.oim.app.AppContext;
import com.oim.business.handler.PersonalHandler;
import com.oim.common.app.view.AbstractView;
import com.oim.common.box.HeadImageIconBox;
import com.oim.common.config.ConfigManage;
import com.oim.common.data.UserSaveData;
import com.oim.common.data.UserSaveDataBox;
import com.oim.net.message.data.LoginData;
import com.oim.ui.LoginFrame;
import com.oim.ui.LoginSettingDialog;
import com.oim.ui.RegisterDialog;
import com.oim.ui.login.ComboBoxAction;
import com.only.net.data.action.DataBackActionAdapter;

/**
 * 描述：
 * 
 * @author XiaHui
 * @date 2015年3月8日 下午4:10:51
 * @version 0.0.1
 */
public class LoginView extends AbstractView {

	LoginFrame loginFrame = new LoginFrame();
	RegisterDialog registerDialog;
	LoginSettingDialog loginSettingDialog;

	public LoginView(AppContext appContext) {
		super(appContext);
		initUI();
		initEvent();
	}

	private void initUI() {
		loginSettingDialog = new LoginSettingDialog(loginFrame, "登录设置", true);
		registerDialog = new RegisterDialog(loginFrame, "用户注册", true);
		final UserSaveDataBox usdb = (UserSaveDataBox) ConfigManage.get(UserSaveDataBox.path, UserSaveDataBox.class);
		if (null != usdb && null != usdb.getMap() && !usdb.getMap().isEmpty()) {
			Map<String, UserSaveData> map = usdb.getMap();
			List<UserSaveData> list = new ArrayList<UserSaveData>(map.values());
			UserSaveData ud = list.get(0);
			for (UserSaveData u : list) {
				loginFrame.addAccount(u.getAccount());
			}
			ImageIcon imageIcon =HeadImageIconBox.getUserHeadImageIcon100(ud.getHead()); 
			loginFrame.setHeadIcon(imageIcon);
		}
		loginFrame.add(new ComboBoxAction() {
			ImageIcon imageIcon = new ImageIcon("Resources/Images/login/1.png");

			@Override
			public void itemChange(String text) {
				if (null != text) {
					UserSaveData ud = usdb.get(text);
					if (null != ud) {
						ImageIcon i =HeadImageIconBox.getUserHeadImageIcon100(ud.getHead());
						loginFrame.setHeadIcon(i);
					} else {
						loginFrame.setHeadIcon(imageIcon);
					}
				}
			}

			@Override
			public void delete(Object o) {
				if (null != o) {
					usdb.remove(o.toString());
					ConfigManage.addOrUpdate(UserSaveDataBox.path, usdb);
				}
			}

			@Override
			public void select(Object o) {
				if (null != o) {
					UserSaveData ud = usdb.get(o.toString());
					if (null != ud) {
						ImageIcon i =HeadImageIconBox.getUserHeadImageIcon100(ud.getHead());
						loginFrame.setHeadIcon(i);
					} else {
						loginFrame.setHeadIcon(imageIcon);
					}
				}
			}
		});
	}

	private void initEvent() {
		loginFrame.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent evt) {
				closing();
			}
		});
		loginFrame.addLoginKeyListener(new java.awt.event.KeyAdapter() {
			@Override
			public void keyPressed(java.awt.event.KeyEvent evt) {
				formKeyPressed(evt);
			}
		});
		loginFrame.addLoginAction(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				loginActionPerformed(evt);
			}
		});
		loginFrame.addRegisterListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				openRegisterDialog();
			}
		});
		loginFrame.addSettingAction(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				openSettingDialog();

			}
		});
	}

	private void formKeyPressed(java.awt.event.KeyEvent evt) {
		if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
			login();
		}
	}

	private void loginActionPerformed(java.awt.event.ActionEvent evt) {
		login();
	}

	public void setVisible(boolean visible) {
		loginFrame.setVisible(visible);
	}

	public void login() {

		String account = loginFrame.getAccount();
		String password = loginFrame.getPassword();
		String status = loginFrame.getStatus();

		if (loginFrame.isCheck()) {
			LoginData loginData = new LoginData();
			loginData.setStatus(status);
			loginData.setAccount(account);
			loginData.setPassword(password);
			loginFrame.showWaiting(true);
			final DataBackActionAdapter action = new DataBackActionAdapter() {// 这是消息发送后回掉
				@Override
				public void lost() {
					showWaiting(false);
					showPromptMessage("登录失败，请检查网络是否正常。");
				}

				@Override
				public void timeOut() {
					showWaiting(false);
					showPromptMessage("登录超时，请检查网络是否正常。");
				}
			};
			PersonalHandler ph = this.appContext.getHandler(PersonalHandler.class);
			ph.login(loginData, action);
		}
	}

	public void showWaiting(boolean show) {
		loginFrame.showWaiting(show);
	}

	public void showPromptMessage(String text) {
		loginFrame.showPromptMessage(text);
	}

	public void closing() {
		this.appContext.exit();
	}

	public void openRegisterDialog() {
		registerDialog.clear();
		registerDialog.setLocationRelativeTo(loginFrame);
		registerDialog.setVisible(true);
	}

	public void openSettingDialog() {
		loginSettingDialog.setLocationRelativeTo(loginFrame);
		loginSettingDialog.setVisible(true);
	}
}
