/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oim.fx.app;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.UIManager;

import com.oim.core.app.AppContext;
import com.oim.core.common.action.CallAction;
import com.oim.core.common.app.view.*;
import com.oim.core.common.config.ConfigManage;
import com.oim.core.common.config.data.Theme;
import com.oim.fx.view.*;
import com.oim.fx.view.MainViewImpl;
import com.oim.swing.UIBox;
import com.oim.swing.view.*;
import com.oim.swing.view.AddUserViewImpl;
import com.oim.swing.view.FindViewImpl;
import com.oim.swing.view.NetSettingViewImpl;
import com.oim.swing.view.RegisterViewImpl;
import com.oim.swing.view.TrayViewImpl;
import com.oim.swing.view.ThemeViewImpl;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

/**
 * 2013-9-4 15:36:45
 * 
 * @author XiaHui
 */
public class AppStartup extends Application {

	AppContext appContext = new AppContext();

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ex) {
			java.util.logging.Logger.getLogger(AppStartup.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		appContext.setExitAction(new CallAction(){
			@Override
			public void execute() {
				Platform.exit();// TODO Auto-generated method stub
				System.exit(0);
			}
		});
		Platform.setImplicitExit(false);
		initTheme();
		appContext.register(LoginView.class, LoginViewImpl.class);
		LoginView loginView = appContext.getSingleView(LoginView.class);
		loginView.setVisible(true);
		appContext.start();
		initView();
	}

	private void initView() {
		appContext.register(ChatListView.class, ChatListViewImpl.class);
		appContext.register(NetSettingView.class, NetSettingViewImpl.class);
		appContext.register(RegisterView.class, RegisterViewImpl.class);
		appContext.register(TrayView.class, TrayViewImpl.class);
		appContext.register(AddGroupView.class, AddGroupViewImpl.class);
		appContext.register(AddUserView.class, AddUserViewImpl.class);
		appContext.register(FindView.class, FindViewImpl.class);
		appContext.register(MainView.class, MainViewImpl.class);
		appContext.register(ThemeView.class, ThemeViewImpl.class);
		appContext.register(UpdatePasswordView.class, UpdatePasswordViewImpl.class);
		appContext.register(GroupDataView.class, GroupDataViewImpl.class);
		appContext.register(VideoView.class, VideoViewImpl.class);
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				
				appContext.getSingleView(MainView.class);
				appContext.getSingleView(ChatListView.class);
				new Thread(){
					public void run() {
						appContext.getSingleView(TrayView.class);
						appContext.getSingleView(NetSettingView.class);
						appContext.getSingleView(RegisterView.class);
						appContext.getSingleView(ThemeView.class);
						appContext.getSingleView(AddGroupView.class);
						appContext.getSingleView(AddUserView.class);
						appContext.getSingleView(FindView.class);
						appContext.getSingleView(MainView.class);
						appContext.setViewReady(true);
					}
				}.start();
			}
		});
	}
	
	/**
	 * 初始化主题图片
	 */
	private void initTheme() {
		Theme theme = (Theme) ConfigManage.get(Theme.config_file_path, Theme.class);

		Image imaeg = new ImageIcon(theme.getWindowBackgroundImage()).getImage();
		if(imaeg.getWidth(null)>0&&imaeg.getHeight(null)>0){
			BufferedImage bi = new BufferedImage(imaeg.getWidth(null), imaeg.getHeight(null), BufferedImage.TYPE_INT_RGB);
			Graphics2D biContext = bi.createGraphics();
			biContext.drawImage(imaeg, 0, 0, null);
			//bi = OnlyImageUtil.applyGaussianFilter(bi, null, theme.getGaussian());
			UIBox.put("key_window_background_image", bi);
			UIBox.put("key_window_background_image_path", theme.getWindowBackgroundImage());
			//UIBox.update();
		}
	}
}
