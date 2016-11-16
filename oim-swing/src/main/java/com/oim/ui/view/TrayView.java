package com.oim.ui.view;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;

import com.oim.app.AppContext;
import com.oim.business.manage.PersonalManage;
import com.oim.business.manage.PromptManage;
import com.oim.common.app.view.AbstractView;
import com.oim.ui.tray.Tray;
import com.oim.ui.tray.TrayPopupMenu;
import com.only.OnlyTrayIcon;

/**
 * 描述：
 * 
 * @author XiaHui
 * @date 2015年3月16日 下午9:16:16
 * @version 0.0.1
 */
public class TrayView extends AbstractView {

	private Image image = new ImageIcon("Resources/Images/Logo/logo_16.png").getImage();
	private OnlyTrayIcon trayIcon;
	private TrayPopupMenu trayPopupMenu = new TrayPopupMenu(this);
	private Image tempImage = new ImageIcon("Resources/Images/Tray/01.png").getImage();
	private ConcurrentLinkedQueue<String> keyQueue = new ConcurrentLinkedQueue<String>();// 消息队列
	private boolean isShowTempImage = false;
	private boolean isShowTrayImage = true;
	private String key = null;

	public TrayView(AppContext appContext) {
		super(appContext);
		initTrayIcon();
		initTray();
		initEvent();
	}

	private void initTrayIcon() {
		trayIcon = new OnlyTrayIcon(image, "OIM");
		trayIcon.setMenu(trayPopupMenu);
		this.showAllMenu(false);
	}

	private void initEvent() {
		Timer timer = new Timer();
		timer.schedule(new PromptTask(), 1000, 600);
		trayIcon.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				trayMouseClicked(evt);
			}

			@Override
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				trayMouseEntered(evt);
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent evt) {
				trayMouseExited(evt);
			}

			@Override
			public void mousePressed(java.awt.event.MouseEvent evt) {
				trayMousePressed(evt);
			}
		});
	}

	private void initTray() {
		try {
			if (SystemTray.isSupported()) {
				SystemTray tray = SystemTray.getSystemTray();
				tray.add(trayIcon);
			}
		} catch (AWTException ex) {
			Logger.getLogger(Tray.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private void trayMouseClicked(java.awt.event.MouseEvent evt) {
		if (evt.getClickCount() == 2 && evt.getButton() == MouseEvent.BUTTON1) {
			doAction();
		}
	}

	private void trayMouseEntered(java.awt.event.MouseEvent evt) {
		// TODO add your handling code here:
	}

	private void trayMouseExited(java.awt.event.MouseEvent evt) {
		// TODO add your handling code here:
	}

	private void trayMousePressed(java.awt.event.MouseEvent evt) {
		// TODO add your handling code here:
	}

	public void setImage(Image image) {
		trayIcon.setImage(image);
	}

	public void showAllMenu(boolean show) {
		trayPopupMenu.showAll(show);
	}

	public void exit() {
		this.appContext.exit();
	}

	public void changeStatus(String status) {
		PersonalManage pm = this.appContext.getManage(PersonalManage.class);
		pm.updateStatus(status);
	}

	public void doAction() {
		if (null != key) {
			PromptManage pm=appContext.getManage(PromptManage.class);
			pm.execute(key);
			key=null;
		} else {
			showMainFrame();
		}
	}

	public void showMainFrame() {
		if (appContext.isLogin()) {
			MainView mainView = appContext.getSingleView(MainView.class);
			mainView.setVisible(true);
		} else {
			LoginView loginView = appContext.getSingleView(LoginView.class);
			loginView.setVisible(true);
		}
	}

	public void put(String key) {
		keyQueue.add(key);
	}

	class PromptTask extends TimerTask {

		@Override
		public void run() {
			Image showImage = null;
			if (key == null && keyQueue.peek() != null) {
				key = keyQueue.poll();
			}
			if (key != null) {
				PromptManage pm=appContext.getManage(PromptManage.class);
				showImage =pm.getTrayImage(key);
				if(null==showImage){
					key=null;
				}
			}
			if (showImage != null) {
				if (isShowTempImage) {
					isShowTempImage = false;
					trayIcon.setImage(showImage);
				} else {
					isShowTempImage = true;
					trayIcon.setImage(tempImage);
				}
				isShowTrayImage=false;
			} else {
				if (!isShowTrayImage) {
					isShowTrayImage = true;
					trayIcon.setImage(image);
				}
			}
		}
	}

	public static void main(String[] args) {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				System.out.println(1111);
			}

		}, 1000, 800);
	}
}
