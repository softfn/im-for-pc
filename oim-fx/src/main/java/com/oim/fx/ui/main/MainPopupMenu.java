package com.oim.fx.ui.main;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;

/**
 * @author XiaHui
 * @date 2015年3月13日 上午10:03:25
 */
public class MainPopupMenu extends StatusPopupMenu {


	private SeparatorMenuItem separator4 = new SeparatorMenuItem();

	private MenuItem updatePasswordMenuItem = new MenuItem();
	private MenuItem quitMenuItem = new MenuItem();

	public MainPopupMenu() {
		initMenu();
		initEvent();
	}

	private void initMenu() {

		updatePasswordMenuItem.setText("修改密码");
		quitMenuItem.setText("退出");
		
		this.getItems().add(separator4);
		this.getItems().add(updatePasswordMenuItem);
		this.getItems().add(quitMenuItem);

	}

	private void initEvent() {
		
	}

	public void setQuitAction(EventHandler<ActionEvent> value) {
		quitMenuItem.setOnAction(value);;
	}

	public void setUpdatePasswordAction(EventHandler<ActionEvent> value) {
		this.updatePasswordMenuItem.setOnAction(value);;
	}
}
