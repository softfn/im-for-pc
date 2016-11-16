package com.oim.ui.tray;

import java.awt.event.ActionListener;

import javax.swing.ImageIcon;

import com.oim.common.event.ExecuteAction;
import com.oim.core.bean.User;
import com.oim.swing.common.util.ImageUtil;
import com.only.OnlyMenuItem;
import com.only.OnlyPopupMenu;

/**
 * @author XiaHui
 * @date 2015年3月13日 上午10:03:25
 */
public class TrayPopupMenu extends OnlyPopupMenu {

	private static final long serialVersionUID = 1L;

	private OnlyMenuItem awayMenuItem = new OnlyMenuItem();
	private OnlyMenuItem busyMenuItem = new OnlyMenuItem();
	private OnlyMenuItem invisibleMenuItem = new OnlyMenuItem();

	private OnlyPopupMenu.Separator separator1 = new OnlyPopupMenu.Separator();
	private OnlyPopupMenu.Separator separator2 = new OnlyPopupMenu.Separator();
	private OnlyPopupMenu.Separator separator3 = new OnlyPopupMenu.Separator();
	private OnlyPopupMenu.Separator separator4 = new OnlyPopupMenu.Separator();
	private OnlyMenuItem muteMenuItem = new OnlyMenuItem();
	private OnlyMenuItem omeMenuItem = new OnlyMenuItem();
	private OnlyMenuItem onlineMenuItem = new OnlyMenuItem();
	private OnlyMenuItem offlineMenuItem = new OnlyMenuItem();
	private OnlyMenuItem openMainMenuItem = new OnlyMenuItem();
	private OnlyMenuItem quitMenuItem = new OnlyMenuItem();

	private ImageIcon offlineImageIcon = ImageUtil.getImageIcon("Resources/Images/Default/Status/FLAG/Big/imoffline.png");
	private ImageIcon onlineImageIcon = ImageUtil.getImageIcon("Resources/Images/Default/Status/FLAG/Big/imonline.png");
	private ImageIcon omeImageIcon = ImageUtil.getImageIcon("Resources/Images/Default/Status/FLAG/Big/Qme.png");
	private ImageIcon awayImageIcon = ImageUtil.getImageIcon("Resources/Images/Default/Status/FLAG/Big/away.png");
	private ImageIcon busyImageIcon = ImageUtil.getImageIcon("Resources/Images/Default/Status/FLAG/Big/busy.png");
	private ImageIcon muteImageIcon = ImageUtil.getImageIcon("Resources/Images/Default/Status/FLAG/Big/mute.png");
	private ImageIcon invisibleImageIcon = ImageUtil.getImageIcon("Resources/Images/Default/Status/FLAG/Big/invisible.png");
	private ExecuteAction statusAction;

	public TrayPopupMenu() {
		initMenu();
		initEvent();
	}

	private void initMenu() {

		onlineMenuItem.setText("我在线上");
		onlineMenuItem.setToolTipText("");
		omeMenuItem.setText("Call我吧");
		awayMenuItem.setText("离开");
		busyMenuItem.setText("忙碌");
		muteMenuItem.setText("请勿打扰");
		invisibleMenuItem.setText("隐身");
		offlineMenuItem.setText("离线");
		openMainMenuItem.setText("打开主面板");
		quitMenuItem.setText("退出");

		onlineMenuItem.setIcon(onlineImageIcon);
		omeMenuItem.setIcon(omeImageIcon);
		awayMenuItem.setIcon(awayImageIcon);
		busyMenuItem.setIcon(busyImageIcon);
		muteMenuItem.setIcon(muteImageIcon);
		invisibleMenuItem.setIcon(invisibleImageIcon);
		offlineMenuItem.setIcon(offlineImageIcon);

		add(onlineMenuItem);
		add(omeMenuItem);
		add(separator1);
		add(awayMenuItem);
		add(busyMenuItem);
		add(muteMenuItem);
		add(separator2);
		add(invisibleMenuItem);
		add(separator3);
		add(offlineMenuItem);
		add(separator4);
		add(openMainMenuItem);
		add(quitMenuItem);

	}

	private void initEvent() {
		onlineMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				onlineMenuItemActionPerformed(evt);
			}
		});
		omeMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				omeMenuItemActionPerformed(evt);
			}
		});
		awayMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				awayMenuItemActionPerformed(evt);
			}
		});
		busyMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				busyMenuItemActionPerformed(evt);
			}
		});
		muteMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				muteMenuItemActionPerformed(evt);
			}
		});
		invisibleMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				invisibleMenuItemActionPerformed(evt);
			}
		});
		offlineMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				offlineMenuItemActionPerformed(evt);
			}
		});
	}

	private void onlineMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
		updateStatus(User.status_online);
	}

	private void omeMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
		updateStatus(User.status_call_me);
	}

	private void awayMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
		updateStatus(User.status_away);
	}

	private void busyMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
		updateStatus(User.status_busy);
	}

	private void muteMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
		updateStatus(User.status_mute);
	}

	private void invisibleMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
		updateStatus(User.status_invisible);
	}

	private void offlineMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
		updateStatus(User.status_offline);
	}

	private void updateStatus(String status) {
		if (null != statusAction) {
			statusAction.execute(status);
		}
	}

	public ExecuteAction getStatusAction() {
		return statusAction;
	}

	public void setStatusAction(ExecuteAction statusAction) {
		this.statusAction = statusAction;
	}

	public void showAll(boolean show) {
		onlineMenuItem.setVisible(show);
		onlineMenuItem.setVisible(show);
		omeMenuItem.setVisible(show);
		awayMenuItem.setVisible(show);
		busyMenuItem.setVisible(show);
		muteMenuItem.setVisible(show);
		invisibleMenuItem.setVisible(show);
		offlineMenuItem.setVisible(show);
		separator1.setVisible(show);
		separator2.setVisible(show);
		separator3.setVisible(show);
		separator4.setVisible(show);
	}

	public void addOpenMainActionListener(ActionListener l) {
		openMainMenuItem.addActionListener(l);
	}

	public void addExitActionListener(ActionListener l) {
		quitMenuItem.addActionListener(l);
	}
}
