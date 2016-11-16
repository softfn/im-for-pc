package com.oim.ui.view;

import java.awt.Component;
import java.awt.event.ActionEvent;

import com.oim.app.AppContext;
import com.oim.common.app.view.AbstractView;
import com.only.OnlyMenuItem;
import com.only.OnlyPopupMenu;

/**
 * @Author: XiaHui
 * @Date: 2016年1月28日
 * @ModifyUser: XiaHui
 * @ModifyDate: 2016年1月28日
 */
public class GroupPanelMenuView extends AbstractView {

	private OnlyPopupMenu popupMenu = new OnlyPopupMenu();

	private OnlyMenuItem addMenuItem = new OnlyMenuItem();
	private OnlyMenuItem fndMenuItem = new OnlyMenuItem();

	public GroupPanelMenuView(AppContext appContext) {
		super(appContext);
		initUI();
		initEvent();
	}

	private void initUI() {
		addMenuItem.setText("创建一个群");
		fndMenuItem.setText("查找群");
		popupMenu.add(addMenuItem);
		popupMenu.add(fndMenuItem);
	}

	private void initEvent() {
		addMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				addMenuItemActionPerformed(evt);
			}
		});
		fndMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				findMenuItemActionPerformed(evt);
			}
		});
	}

	private void addMenuItemActionPerformed(ActionEvent evt) {
		GroupDataView view = this.appContext.getSingleView(GroupDataView.class);
		view.setGroup(null);
		view.setVisible(true);
	}

	private void findMenuItemActionPerformed(ActionEvent evt) {
		FindView findView = this.appContext.getSingleView(FindView.class);
		if (!findView.isShowing()) {
			findView.initData();
		}
		findView.setVisible(true);
	}

	public void show(Component invoker, int x, int y) {
		popupMenu.show(invoker, x, y);
	}
}
