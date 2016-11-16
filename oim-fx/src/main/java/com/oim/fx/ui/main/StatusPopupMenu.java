package com.oim.fx.ui.main;

import com.oim.common.event.ExecuteAction;
import com.oim.core.bean.User;
import com.oim.fx.common.box.ImageBox;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * @author: XiaHui
 * @date: 2016年10月11日 下午3:43:16
 */
public class StatusPopupMenu extends ContextMenu {

	private MenuItem awayMenuItem = new MenuItem();
	private MenuItem busyMenuItem = new MenuItem();
	private MenuItem invisibleMenuItem = new MenuItem();

	private SeparatorMenuItem separator1 = new SeparatorMenuItem();
	private SeparatorMenuItem separator2 = new SeparatorMenuItem();
	private SeparatorMenuItem separator3 = new SeparatorMenuItem();
	private MenuItem muteMenuItem = new MenuItem();
	private MenuItem omeMenuItem = new MenuItem();
	private MenuItem onlineMenuItem = new MenuItem();
	private MenuItem offlineMenuItem = new MenuItem();

	Image onlineImage = ImageBox.getImageClassPath("/resources/common/images/status/imonline.png");
	Image callMeImage = ImageBox.getImageClassPath("/resources/common/images/status/Qme.png");
	Image awayImage = ImageBox.getImageClassPath("/resources/common/images/status/away.png");
	Image busyImage = ImageBox.getImageClassPath("/resources/common/images/status/busy.png");
	Image muteImage = ImageBox.getImageClassPath("/resources/common/images/status/mute.png");
	Image invisibleImage = ImageBox.getImageClassPath("/resources/common/images/status/invisible.png");
	Image offlineImage = ImageBox.getImageClassPath("/resources/common/images/status/imoffline.png");

	ImageView onlineImageView = new ImageView();
	ImageView callMeImageView = new ImageView();
	ImageView awayImageView = new ImageView();
	ImageView busyImageView = new ImageView();
	ImageView muteImageView = new ImageView();
	ImageView invisibleImageView = new ImageView();
	ImageView offlineImageView = new ImageView();

	private ExecuteAction statusAction;

	public StatusPopupMenu() {
		initMenu();
	}

	private void initMenu() {

		onlineImageView.setImage(onlineImage);
		callMeImageView.setImage(callMeImage);
		awayImageView.setImage(awayImage);
		busyImageView.setImage(busyImage);
		muteImageView.setImage(muteImage);
		invisibleImageView.setImage(invisibleImage);
		offlineImageView.setImage(offlineImage);

		onlineMenuItem.setText("我在线上");
		omeMenuItem.setText("Call我吧");
		awayMenuItem.setText("离开");
		busyMenuItem.setText("忙碌");
		muteMenuItem.setText("请勿打扰");
		invisibleMenuItem.setText("隐身");
		offlineMenuItem.setText("离线");

		onlineMenuItem.setGraphic(onlineImageView);
		omeMenuItem.setGraphic(callMeImageView);
		awayMenuItem.setGraphic(awayImageView);
		busyMenuItem.setGraphic(busyImageView);
		muteMenuItem.setGraphic(muteImageView);
		invisibleMenuItem.setGraphic(invisibleImageView);
		offlineMenuItem.setGraphic(offlineImageView);

		this.getItems().add(onlineMenuItem);
		this.getItems().add(omeMenuItem);
		this.getItems().add(separator1);
		this.getItems().add(awayMenuItem);
		this.getItems().add(busyMenuItem);
		this.getItems().add(muteMenuItem);
		this.getItems().add(separator2);
		this.getItems().add(invisibleMenuItem);
		this.getItems().add(separator3);
		this.getItems().add(offlineMenuItem);

		onlineMenuItem.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				onlineMenuItemActionPerformed();
			}
		});
		omeMenuItem.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				omeMenuItemActionPerformed();
			}
		});
		awayMenuItem.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				awayMenuItemActionPerformed();
			}
		});
		busyMenuItem.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				busyMenuItemActionPerformed();
			}
		});
		muteMenuItem.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				muteMenuItemActionPerformed();
			}
		});
		invisibleMenuItem.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				invisibleMenuItemActionPerformed();
			}
		});
		offlineMenuItem.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				offlineMenuItemActionPerformed();
			}
		});

	}

	private void onlineMenuItemActionPerformed() {
		updateStatus(User.status_online);
	}

	private void omeMenuItemActionPerformed() {
		updateStatus(User.status_call_me);
	}

	private void awayMenuItemActionPerformed() {
		updateStatus(User.status_away);
	}

	private void busyMenuItemActionPerformed() {
		updateStatus(User.status_busy);
	}

	private void muteMenuItemActionPerformed() {
		updateStatus(User.status_mute);
	}

	private void invisibleMenuItemActionPerformed() {
		updateStatus(User.status_invisible);
	}

	private void offlineMenuItemActionPerformed() {
		updateStatus(User.status_offline);
	}

	private void updateStatus(String status) {
		if (null != statusAction) {
			statusAction.execute(status);
		}
	}

	public void setStatusAction(ExecuteAction statusAction) {
		this.statusAction = statusAction;
	}
}
