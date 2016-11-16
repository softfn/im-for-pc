package com.oim.fx.view;

import com.oim.core.app.AppContext;
import com.oim.core.business.controller.PersonalController;
import com.oim.core.common.app.view.AbstractView;
import com.oim.core.common.app.view.LoginView;
import com.oim.core.common.app.view.NetSettingView;
import com.oim.core.common.app.view.RegisterView;
import com.oim.core.common.config.ConfigManage;
import com.oim.core.common.data.UserSaveData;
import com.oim.core.common.data.UserSaveDataBox;
import com.oim.core.net.message.data.LoginData;
import com.oim.fx.common.box.ImageBox;
import com.oim.fx.ui.LoginFrame;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

/**
 * @author: XiaHui
 * @date: 2016年10月10日 上午11:04:46
 */
public class LoginViewImpl extends AbstractView implements LoginView {

	LoginFrame loginFrame = new LoginFrame();

	public LoginViewImpl(AppContext appContext) {
		super(appContext);
		initComponent();
		initEvent();
	}

	private void initComponent() {

	}

	private void initEvent() {
		loginFrame.getTitlePane().addOnCloseAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				appContext.exit();
			}
		});
		loginFrame.setSettingAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				NetSettingView netSettingView = appContext.getSingleView(NetSettingView.class);
				netSettingView.setVisible(true);
			}
		});

		loginFrame.setRegisterOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				RegisterView registerView = appContext.getSingleView(RegisterView.class);
				registerView.setVisible(true);
			}
		});

		loginFrame.setLoginAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				login();
			}
		});
		
		 UserSaveDataBox usdb = (UserSaveDataBox) ConfigManage.get(UserSaveDataBox.path, UserSaveDataBox.class);
		 
		loginFrame.addAccountChangeListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (null != newValue) {
					UserSaveData ud = usdb.get(newValue);
					if (null != ud) {
						Image image = ImageBox.getImagePath("Resources/Images/Head/User/"+ud.getHead()+"_100.gif", 80, 80);
						loginFrame.setHeadImage(image);
					} else {
						Image image = ImageBox.getImagePath("Resources/Images/Head/User/1_100.gif", 80, 80);
						loginFrame.setHeadImage(image);
					}
				}
			}
		});
	}

	private void login() {

		String account = loginFrame.getAccount();
		String password = loginFrame.getPassword();
		String status = loginFrame.getStatus();

		LoginData loginData = new LoginData();
		loginData.setStatus(status);
		loginData.setAccount(account);
		loginData.setPassword(password);

		PersonalController pc = appContext.getController(PersonalController.class);
		pc.login(loginData);

	}

	@Override
	public void setVisible(boolean visible) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				if (visible) {
					loginFrame.show();
				} else {
					loginFrame.hide();
				}
			}
		});
	}

	@Override
	public boolean isShowing() {
		return loginFrame.isShowing();
	}

	@Override
	public boolean isSavePassword() {
		return false;
	}

	@Override
	public void showWaiting(boolean show) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				loginFrame.showWaiting(show);
			}
		});
	}

	@Override
	public void showPrompt(String text) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				loginFrame.showPrompt(text);
			}
		});
	}
}
