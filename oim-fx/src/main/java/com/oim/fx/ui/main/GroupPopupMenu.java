package com.oim.fx.ui.main;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

/**
 * @author XiaHui
 * @date 2015年3月13日 上午10:03:25
 */
public class GroupPopupMenu extends ContextMenu {

	private MenuItem addMenuItem = new MenuItem();
	private MenuItem findMenuItem = new MenuItem();

	public GroupPopupMenu() {
		initMenu();
		initEvent();
	}

	private void initMenu() {

		addMenuItem.setText("创建一个群");
		findMenuItem.setText("查找群");

		this.getItems().add(addMenuItem);
		this.getItems().add(findMenuItem);

	}

	private void initEvent() {

	}

	public void setAddAction(EventHandler<ActionEvent> value) {
		addMenuItem.setOnAction(value);
	}

	public void setFindAction(EventHandler<ActionEvent> value) {
		this.findMenuItem.setOnAction(value);
	}
}
