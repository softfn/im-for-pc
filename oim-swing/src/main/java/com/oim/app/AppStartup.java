/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oim.app;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.UIManager;

import com.oim.business.manage.ChatManage;
import com.oim.business.manage.PromptManage;
import com.oim.common.config.ConfigManage;
import com.oim.common.config.data.Theme;
import com.oim.ui.view.AddView;
import com.oim.ui.view.FindView;
import com.oim.ui.view.LoginView;
import com.oim.ui.view.MainView;
import com.oim.ui.view.ThemeView;
import com.oim.ui.view.TrayView;
import com.only.OnlyBorderDialog;
import com.only.OnlyBorderFrame;
import com.only.util.OnlyImageUtil;

/**
 * 2013-9-4 15:36:45
 * 
 * @author XiaHui
 */
public class AppStartup implements Runnable {
	
	AppContext appContext = new AppContext();

	public static void main(String[] args) {
		try {
			//			UIManager.setLookAndFeel("com.seaglasslookandfeel.SeaGlassLookAndFeel");
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ex) {
			java.util.logging.Logger.getLogger(AppStartup.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		java.awt.EventQueue.invokeLater(new AppStartup());
	}

	@Override
	public void run() {
		long time = System.currentTimeMillis();
		initTheme();
		//为了能够有更好的体验，先将登录界面显示出来，再异步初始化其他模块对象
		LoginView loginView = appContext.getSingleView(LoginView.class);
		System.out.println(System.currentTimeMillis() - time);
		loginView.setVisible(true);
		System.out.println(System.currentTimeMillis() - time);
		appContext.start();
		new InitApp().start();
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
			bi = OnlyImageUtil.applyGaussianFilter(bi, null, theme.getGaussian());
			UIBox.put("key_window_background_image", bi);
		}
	}

	class InitApp extends Thread {
		@Override
		public void run() {
			/** 因为有比较多的图片，所以io操作比较多，为了在后面体验更好，所以异步把界面对象都实例化 **/
			long time = System.currentTimeMillis();
			appContext.getManage(ChatManage.class);
			
			appContext.getManage(PromptManage.class);
			//initTheme();
			appContext.getSingleView(MainView.class);
			appContext.getSingleView(TrayView.class);
			appContext.getSingleView(FindView.class);
			appContext.getSingleView(AddView.class);
			appContext.getSingleView(ThemeView.class);

			System.out.println(System.currentTimeMillis() - time);
		}

		void initTheme() {
			Theme theme = (Theme) ConfigManage.get(Theme.config_file_path, Theme.class);

			Image imaeg = new ImageIcon(theme.getWindowBackgroundImage()).getImage();
			BufferedImage bi = new BufferedImage(imaeg.getWidth(null), imaeg.getHeight(null), BufferedImage.TYPE_INT_RGB);

			Graphics2D biContext = bi.createGraphics();
			biContext.drawImage(imaeg, 0, 0, null);
			bi = OnlyImageUtil.applyGaussianFilter(bi, null, theme.getGaussian());
			UIBox.put("key_window_background_image", bi);
			//			
			for (OnlyBorderFrame ourFrame : UIBox.frameSet) {
				ourFrame.setBackgroundImage(bi);
			}
			for (OnlyBorderDialog ourFrame : UIBox.dialogSet) {
				ourFrame.setBackgroundImage(bi);
			}
		}
	}
}
