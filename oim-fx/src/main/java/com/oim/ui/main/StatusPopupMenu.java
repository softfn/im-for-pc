package com.oim.ui.main;

import javax.swing.ImageIcon;

import com.oim.common.event.ExecuteAction;
import com.oim.core.bean.User;
import com.oim.swing.common.util.ImageUtil;
import com.oim.ui.component.BasePopupMenu;
import com.only.OnlyMenuItem;
import com.only.OnlyPopupMenu;

/**
 * @author: XiaHui
 * @date: 2016年10月11日 下午3:43:16
 */
public class StatusPopupMenu extends BasePopupMenu {

	private static final long serialVersionUID = 1L;
	
	private OnlyMenuItem awayMenuItem = new OnlyMenuItem();
	private OnlyMenuItem busyMenuItem = new OnlyMenuItem();
	private OnlyMenuItem invisibleMenuItem = new OnlyMenuItem();

	private OnlyPopupMenu.Separator separator1 = new OnlyPopupMenu.Separator();
	private OnlyPopupMenu.Separator separator2 = new OnlyPopupMenu.Separator();
	private OnlyPopupMenu.Separator separator3 = new OnlyPopupMenu.Separator();
	private OnlyMenuItem muteMenuItem = new OnlyMenuItem();
	private OnlyMenuItem omeMenuItem = new OnlyMenuItem();
	private OnlyMenuItem onlineMenuItem = new OnlyMenuItem();
	private OnlyMenuItem offlineMenuItem = new OnlyMenuItem();

	private ImageIcon offlineImageIcon = ImageUtil.getImageIcon("Resources/Images/Default/Status/FLAG/Big/imoffline.png");
	private ImageIcon onlineImageIcon = ImageUtil.getImageIcon("Resources/Images/Default/Status/FLAG/Big/imonline.png");
	private ImageIcon omeImageIcon = ImageUtil.getImageIcon("Resources/Images/Default/Status/FLAG/Big/Qme.png");
	private ImageIcon awayImageIcon = ImageUtil.getImageIcon("Resources/Images/Default/Status/FLAG/Big/away.png");
	private ImageIcon busyImageIcon = ImageUtil.getImageIcon("Resources/Images/Default/Status/FLAG/Big/busy.png");
	private ImageIcon muteImageIcon = ImageUtil.getImageIcon("Resources/Images/Default/Status/FLAG/Big/mute.png");
	private ImageIcon invisibleImageIcon = ImageUtil.getImageIcon("Resources/Images/Default/Status/FLAG/Big/invisible.png");
	private ExecuteAction statusAction;

	public StatusPopupMenu() {
		initMenu();
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

		onlineMenuItem.setIcon(onlineImageIcon);
		omeMenuItem.setIcon(omeImageIcon);
		awayMenuItem.setIcon(awayImageIcon);
		busyMenuItem.setIcon(busyImageIcon);
		muteMenuItem.setIcon(muteImageIcon);
		invisibleMenuItem.setIcon(invisibleImageIcon);
		offlineMenuItem.setIcon(offlineImageIcon);
		

		this.add(onlineMenuItem);
		this.add(omeMenuItem);
		this.add(separator1);
		this.add(awayMenuItem);
		this.add(busyMenuItem);
		this.add(muteMenuItem);
		this.add(separator2);
		this.add(invisibleMenuItem);
		this.add(separator3);
		this.add(offlineMenuItem);

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
	private void updateStatus(String status){
		if(null!=statusAction){
			statusAction.execute(status);
		}
	}


	public void setStatusAction(ExecuteAction statusAction) {
		this.statusAction = statusAction;
	}
	
}
