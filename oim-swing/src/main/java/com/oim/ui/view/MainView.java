package com.oim.ui.view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;

import com.oim.app.AppContext;
import com.oim.bean.User;
import com.oim.business.handler.PersonalHandler;
import com.oim.business.manage.PersonalManage;
import com.oim.common.app.view.AbstractView;
import com.oim.common.box.HeadImageIconBox;
import com.oim.common.box.PersonalBox;
import com.oim.net.message.data.LoginData;
import com.oim.ui.MainDialog;
import com.oim.ui.component.event.ExecuteAction;
import com.oim.ui.component.list.HeadLabel;
import com.oim.ui.component.list.Node;
import com.oim.ui.main.MainPopupMenu;
import com.oim.ui.main.Tab;
import com.only.OnlyMessageBox;

/**
 * 描述：
 * 
 * @author XiaHui
 * @date 2015年3月10日 下午10:20:49
 * @version 0.0.1
 */

public class MainView extends AbstractView {

	MainDialog mainDialog = new MainDialog();
	MainPopupMenu menu = new MainPopupMenu();

	public MainView(AppContext appContext) {
		super(appContext);
		initBottomPanel();
		initEvent();
	}

	private void initBottomPanel() {

		ImageIcon menuNormalIcon = new ImageIcon("Resources/Images/Default/MainFrame/menu_btn_normal.png");
		ImageIcon menuRolloverIcon = new ImageIcon("Resources/Images/Default/MainFrame/menu_btn_highlight.png");
		ImageIcon menuSelectedIcon = new ImageIcon("Resources/Images/Default/MainFrame/menu_btn_down.png");

		ImageIcon settingNormalIcon = new ImageIcon("Resources/Images/Default/MainFrame/Tools.png");
		ImageIcon settingRolloverIcon = new ImageIcon("Resources/Images/Default/MainFrame/tools_hover.png");
		ImageIcon settingSelectedIcon = new ImageIcon("Resources/Images/Default/MainFrame/tools_down.png");

		ImageIcon skinNormalIcon = new ImageIcon("Resources/Images/Default/MainFrame/skin_manage_normal.png");
		ImageIcon skinRolloverIcon = new ImageIcon("Resources/Images/Default/MainFrame/skin_manage_hover.png");
		ImageIcon skinSelectedIcon = new ImageIcon("Resources/Images/Default/MainFrame/skin_manage_down.png");

		ImageIcon messageNormalIcon = new ImageIcon("Resources/Images/Default/MainFrame/message.png");
		ImageIcon messageRolloverIcon = new ImageIcon("Resources/Images/Default/MainFrame/message_highlight.png");
		ImageIcon messageSelectedIcon = new ImageIcon("Resources/Images/Default/MainFrame/message_down.png");

		ImageIcon findNormalIcon = new ImageIcon("Resources/Images/Default/MainFrame/find.png");
		ImageIcon findRolloverIcon = new ImageIcon("Resources/Images/Default/MainFrame/find_hover.png");
		ImageIcon findSelectedIcon = new ImageIcon("Resources/Images/Default/MainFrame/find_down.png");

		Tab menuTab = new Tab(menuNormalIcon, menuRolloverIcon, menuSelectedIcon);
		Tab settingTab = new Tab(settingNormalIcon, settingRolloverIcon, settingSelectedIcon);
		Tab skinTab = new Tab(skinNormalIcon, skinRolloverIcon, skinSelectedIcon);

		Tab messageTab = new Tab(messageNormalIcon, messageRolloverIcon, messageSelectedIcon);
		Tab findTab = new Tab(findNormalIcon, findRolloverIcon, findSelectedIcon);
		findTab.setText("查找");
		findTab.setTextForeground(new Color(255, 255, 255));

		mainDialog.addBottom(menuTab);
		mainDialog.addBottom(settingTab);
		mainDialog.addBottom(skinTab);
		mainDialog.addBottom(messageTab);
		mainDialog.addBottom(findTab);

		menuTab.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				showMenu();
			}
		});

		findTab.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				openFindView();
			}
		});

		skinTab.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				openThemeView();
			}
		});

		ExecuteAction statusAction = new ExecuteAction() {

			@Override
			public <T, E> E execute(T value) {
				if (value instanceof String) {
					PersonalManage pm = MainView.this.appContext.getManage(PersonalManage.class);
					pm.updateStatus((String) value);
				}
				return null;
			}

		};
		mainDialog.setStatusAction(statusAction);
		menu.setStatusAction(statusAction);

		menu.addQuitAction(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				MainView.this.appContext.exit();
			}
		});
		menu.addUpdatePasswordAction(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				UpdatePasswordView upw = MainView.this.appContext.getSingleView(UpdatePasswordView.class);
				upw.setVisible(true);
			}
		});

	}

	private void initEvent() {
		mainDialog.getGroupRoot().addListMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3) {
					GroupPanelMenuView gpm = appContext.getSingleView(GroupPanelMenuView.class);
					gpm.show(mainDialog.getGroupRoot(), e.getX(), e.getY());
				}
			}
		});
	}

	public void setVisible(boolean visible) {
		mainDialog.setVisible(visible);
	}

	public void setUser(User user) {
		LoginData loginData = PersonalBox.get(LoginData.class);
		ImageIcon headIcon = HeadImageIconBox.getUserHeadImageIcon60(user.getHead());
		mainDialog.setHeadIcon(headIcon);
		mainDialog.setNickname(user.getNickname());
		mainDialog.setSignature(user.getSignature());
		mainDialog.setStatus(loginData.getStatus());
	}

	public void addUserCategoryNode(Node node) {
		mainDialog.addUserCategoryNode(node);
	}

	public void addGroupCategoryNode(Node node) {
		mainDialog.addGroupCategoryNode(node);
	}

	public void addLastNode(HeadLabel node) {
		mainDialog.addLastNode(node);
	}

	public void removeUserCategoryNode(Node node) {
		mainDialog.removeUserCategoryNode(node);
	}

	public void removeGroupCategoryNode(Node node) {
		mainDialog.removeGroupCategoryNode(node);
	}

	public void removeLastNode(HeadLabel node) {
		mainDialog.removeLastNode(node);
	}

	public void shwoDifferentLogin() {
		int button = OnlyMessageBox.createQuestionMessageBox(mainDialog, "连接", "你的帐号在其他的地方登录！\n是否重新连接？").open();
		if (OnlyMessageBox.YES_OPTION == button) {
			PersonalHandler userHandle = appContext.getHandler(PersonalHandler.class);
			userHandle.reconnect();
		}
	}

	private void showMenu() {
		menu.show(mainDialog, 0, mainDialog.getHeight() - 260);
	}

	public void openFindView() {
		FindView findView = this.appContext.getSingleView(FindView.class);
		if (!findView.isShowing()) {
			findView.initData();
		}
		findView.setVisible(true);
	}

	public void openThemeView() {
		ThemeView findView = this.appContext.getSingleView(ThemeView.class);
		findView.setVisible(true);
	}

	public void updateStatus(String status) {
		mainDialog.updateStatus(status);
	}
	
	public void showPromptMessage(String text) {
		mainDialog.showPromptMessage(text);
	}

}
