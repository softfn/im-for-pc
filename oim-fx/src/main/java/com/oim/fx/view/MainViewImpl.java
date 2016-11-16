package com.oim.fx.view;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.oim.common.event.ExecuteAction;
import com.oim.core.app.AppContext;
import com.oim.core.bean.Group;
import com.oim.core.bean.GroupCategory;
import com.oim.core.bean.User;
import com.oim.core.bean.UserCategory;
import com.oim.core.business.manage.ChatManage;
import com.oim.core.business.manage.PersonalManage;
import com.oim.core.common.app.view.AbstractView;
import com.oim.core.common.app.view.FindView;
import com.oim.core.common.app.view.GroupDataView;
import com.oim.core.common.app.view.MainView;
import com.oim.core.common.app.view.ThemeView;
import com.oim.core.common.app.view.UpdatePasswordView;
import com.oim.core.common.box.PersonalBox;
import com.oim.core.net.message.data.LoginData;
import com.oim.core.net.message.data.UserData;
import com.oim.fx.common.box.ImageBox;
import com.oim.fx.common.util.HeadUtil;
import com.oim.fx.ui.MainFrame;
import com.oim.fx.ui.component.IconButton;
import com.oim.fx.ui.list.HeadItem;
import com.oim.fx.ui.list.ListNodePanel;
import com.oim.fx.ui.list.ListRootPanel;
import com.oim.fx.ui.main.GroupPopupMenu;
import com.oim.fx.ui.main.MainPopupMenu;
import com.oim.fx.ui.main.StatusPopupMenu;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/**
 * @author: XiaHui
 * @date: 2016年10月11日 上午9:17:32
 */
public class MainViewImpl extends AbstractView implements MainView {

	MainFrame mainFrame = new MainFrame();
	StatusPopupMenu statusPopupMenu = new StatusPopupMenu();
	MainPopupMenu mainPopupMenu = new MainPopupMenu();
	GroupPopupMenu groupPopupMenu = new GroupPopupMenu();
	/***** 存放好友分组列表组件 *****/
	private Map<String, ListNodePanel> userListNodeMap = new ConcurrentHashMap<String, ListNodePanel>();
	/*** 存放群分组组件 **/
	private Map<String, ListNodePanel> groupListNodeMap = new ConcurrentHashMap<String, ListNodePanel>();
	/** 存放单个好友组件 ***/
	private Map<String, HeadItem> userHeadLabelMap = new ConcurrentHashMap<String, HeadItem>();
	/** 存放单个群 **/
	private Map<String, HeadItem> groupHeadLabelMap = new ConcurrentHashMap<String, HeadItem>();

	private Map<String, HeadItem> lastMap = new ConcurrentHashMap<String, HeadItem>();

	ListRootPanel userRoot = new ListRootPanel();
	ListRootPanel groupRoot = new ListRootPanel();
	ListRootPanel lastRoot = new ListRootPanel();

	public MainViewImpl(AppContext appContext) {
		super(appContext);
		initIocn();
		initComponent();
		initEvent();
	}

	private void initIocn() {
		Image businessImage = ImageBox.getImageClassPath("/resources/main/images/top/1.png");

		IconButton iconButton = new IconButton(businessImage);
		mainFrame.addBusinessIcon(iconButton);

		businessImage = ImageBox.getImageClassPath("/resources/main/images/top/2.png");

		iconButton = new IconButton(businessImage);
		mainFrame.addBusinessIcon(iconButton);

		businessImage = ImageBox.getImageClassPath("/resources/main/images/top/3.png");

		iconButton = new IconButton(businessImage);
		mainFrame.addBusinessIcon(iconButton);

		businessImage = ImageBox.getImageClassPath("/resources/main/images/top/4.png");

		iconButton = new IconButton(businessImage);
		mainFrame.addBusinessIcon(iconButton);

		businessImage = ImageBox.getImageClassPath("/resources/main/images/top/5.png");

		iconButton = new IconButton(businessImage);
		mainFrame.addBusinessIcon(iconButton);

		businessImage = ImageBox.getImageClassPath("/resources/main/images/top/skin.png");

		iconButton = new IconButton(businessImage);
		mainFrame.addBusinessIcon(iconButton);
		iconButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				ThemeView findView = appContext.getSingleView(ThemeView.class);
				findView.setVisible(true);
			}
		});

		///////////////////////////////////////////// function
		Image normalImage = ImageBox.getImageClassPath("/resources/main/images/bottom/menu_btn_normal.png");
		Image hoverImage = ImageBox.getImageClassPath("/resources/main/images/bottom/menu_btn2_down.png");
		Image pressedImage = ImageBox.getImageClassPath("/resources/main/images/bottom/menu_btn_highlight.png");

		iconButton = new IconButton(normalImage, hoverImage, pressedImage);
		mainFrame.addFunctionIcon(iconButton);
		iconButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				Object source = event.getSource();
				if (source instanceof Node) {
					Node node = (Node) source;
					mainPopupMenu.show(node, Side.TOP, node.getLayoutX(), node.getLayoutY());
				}
			}
		});

		normalImage = ImageBox.getImageClassPath("/resources/main/images/bottom/tools.png");
		hoverImage = ImageBox.getImageClassPath("/resources/main/images/bottom/tools_hover.png");
		pressedImage = ImageBox.getImageClassPath("/resources/main/images/bottom/tools_down.png");

		iconButton = new IconButton(normalImage, hoverImage, pressedImage);
		mainFrame.addFunctionIcon(iconButton);

		normalImage = ImageBox.getImageClassPath("/resources/main/images/bottom/message.png");
		hoverImage = ImageBox.getImageClassPath("/resources/main/images/bottom/message_highlight.png");
		pressedImage = ImageBox.getImageClassPath("/resources/main/images/bottom/message_down.png");

		iconButton = new IconButton(normalImage, hoverImage, pressedImage);
		mainFrame.addFunctionIcon(iconButton);

		normalImage = ImageBox.getImageClassPath("/resources/main/images/bottom/filemanager.png");
		hoverImage = ImageBox.getImageClassPath("/resources/main/images/bottom/filemanager_hover.png");
		pressedImage = ImageBox.getImageClassPath("/resources/main/images/bottom/filemanager_down.png");

		iconButton = new IconButton(normalImage, hoverImage, pressedImage);
		mainFrame.addFunctionIcon(iconButton);

		normalImage = ImageBox.getImageClassPath("/resources/main/images/bottom/mycollection_mainpanel.png");
		hoverImage = ImageBox.getImageClassPath("/resources/main/images/bottom/myCollection_mainpanel_hover.png");
		pressedImage = ImageBox.getImageClassPath("/resources/main/images/bottom/myCollection_mainpanel_down.png");

		iconButton = new IconButton(normalImage, hoverImage, pressedImage);
		mainFrame.addFunctionIcon(iconButton);

		normalImage = ImageBox.getImageClassPath("/resources/main/images/bottom/find.png");
		hoverImage = ImageBox.getImageClassPath("/resources/main/images/bottom/find_hover.png");
		pressedImage = ImageBox.getImageClassPath("/resources/main/images/bottom/find_down.png");

		iconButton = new IconButton("查找", normalImage, hoverImage, pressedImage);
		mainFrame.addFunctionIcon(iconButton);
		iconButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				FindView findView = appContext.getSingleView(FindView.class);
				findView.setVisible(true);
			}
		});

		normalImage = ImageBox.getImageClassPath("/resources/main/images/bottom/store.png");

		iconButton = new IconButton("应用宝", normalImage);
		mainFrame.addRightFunctionIcon(iconButton);

		///////////////////////////////////////////////////////////////////// app
		normalImage = ImageBox.getImageClassPath("/resources/main/images/bottom/appbox_mgr_btn.png");
		hoverImage = ImageBox.getImageClassPath("/resources/main/images/bottom/appbox_mgr_btn_hover.png");
		pressedImage = ImageBox.getImageClassPath("/resources/main/images/bottom/appbox_mgr_btn_down.png");

		iconButton = new IconButton(normalImage, hoverImage, pressedImage);
		mainFrame.addRightAppIcon(iconButton);

		Image appImage = ImageBox.getImageClassPath("/resources/main/images/app/1.png");

		iconButton = new IconButton(appImage);
		mainFrame.addAppIcon(iconButton);

		appImage = ImageBox.getImageClassPath("/resources/main/images/app/2.png");

		iconButton = new IconButton(appImage);
		mainFrame.addAppIcon(iconButton);

		appImage = ImageBox.getImageClassPath("/resources/main/images/app/3.png");

		iconButton = new IconButton(appImage);
		mainFrame.addAppIcon(iconButton);

		appImage = ImageBox.getImageClassPath("/resources/main/images/app/7.png");
		iconButton = new IconButton(appImage);
		mainFrame.addAppIcon(iconButton);

		appImage = ImageBox.getImageClassPath("/resources/main/images/app/8.png");
		iconButton = new IconButton(appImage);
		mainFrame.addAppIcon(iconButton);

		appImage = ImageBox.getImageClassPath("/resources/main/images/app/9.png");
		iconButton = new IconButton(appImage);
		mainFrame.addAppIcon(iconButton);

		appImage = ImageBox.getImageClassPath("/resources/main/images/app/10.png");
		iconButton = new IconButton(appImage);
		mainFrame.addAppIcon(iconButton);

		appImage = ImageBox.getImageClassPath("/resources/main/images/app/11.png");
		iconButton = new IconButton(appImage);
		mainFrame.addAppIcon(iconButton);
	}

	private void initComponent() {

		groupRoot.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {

			@Override
			public void handle(ContextMenuEvent event) {
				groupPopupMenu.show(groupRoot, event.getScreenX(), event.getScreenY());
			}
		});

		Image normalImage = ImageBox.getImageClassPath("/resources/main/images/panel/icon_contacts_normal.png");
		Image hoverImage = ImageBox.getImageClassPath("/resources/main/images/panel/icon_contacts_hover.png");
		Image selectedImage = ImageBox.getImageClassPath("/resources/main/images/panel/icon_contacts_selected.png");

		mainFrame.addTab(normalImage, hoverImage, selectedImage, userRoot);

		normalImage = ImageBox.getImageClassPath("/resources/main/images/panel/icon_group_normal.png");
		hoverImage = ImageBox.getImageClassPath("/resources/main/images/panel/icon_group_hover.png");
		selectedImage = ImageBox.getImageClassPath("/resources/main/images/panel/icon_group_selected.png");

		mainFrame.addTab(normalImage, hoverImage, selectedImage, groupRoot);

		normalImage = ImageBox.getImageClassPath("/resources/main/images/panel/icon_last_normal.png");
		hoverImage = ImageBox.getImageClassPath("/resources/main/images/panel/icon_last_hover.png");
		selectedImage = ImageBox.getImageClassPath("/resources/main/images/panel/icon_last_selected.png");

		mainFrame.addTab(normalImage, hoverImage, selectedImage, lastRoot);

		normalImage = ImageBox.getImageClassPath("/resources/main/images/panel/qzone_normal.png");
		hoverImage = ImageBox.getImageClassPath("/resources/main/images/panel/qzone_hover.png");
		selectedImage = ImageBox.getImageClassPath("/resources/main/images/panel/qzone_selected.png");

		VBox box1 = new VBox();
		box1.getChildren().add(new Button("我的空间"));
		box1.setStyle("-fx-background-color:rgba(44, 123, 245, 1)");
		mainFrame.addTab(normalImage, hoverImage, selectedImage, box1);

		normalImage = ImageBox.getImageClassPath("/resources/main/images/panel/main_panel_tab_inco_normal.png");
		hoverImage = ImageBox.getImageClassPath("/resources/main/images/panel/main_panel_tab_inco_hover.png");
		selectedImage = ImageBox.getImageClassPath("/resources/main/images/panel/main_panel_tab_inco_selected.png");

		VBox box2 = new VBox();
		WebView webView = new WebView();
		WebEngine webEngine = webView.getEngine();
		webEngine.load("http://www.oschina.net/code/snippet_935786_52805");
		box2.getChildren().add(webView);
		box2.setStyle("-fx-background-color:rgba(215, 165, 230, 1)");
		mainFrame.addTab(normalImage, hoverImage, selectedImage, box2);

		normalImage = ImageBox.getImageClassPath("/resources/main/images/panel/main_panel_phone_inco_normal.png");
		hoverImage = ImageBox.getImageClassPath("/resources/main/images/panel/main_panel_phone_inco_hover.png");
		selectedImage = ImageBox.getImageClassPath("/resources/main/images/panel/main_panel_phone_inco_selected.png");

		VBox box3 = new VBox();
		box3.getChildren().add(new Button("我的手机"));
		box3.setStyle("-fx-background-color:rgba(112, 245, 86, 1);");
		mainFrame.addTab(normalImage, hoverImage, selectedImage, box3);
	}

	private void initEvent() {
		statusPopupMenu.setStatusAction(new ExecuteAction() {

			@Override
			public <T, E> E execute(T value) {
				if (value instanceof String) {
					PersonalManage pm = appContext.getManage(PersonalManage.class);
					pm.updateStatus((String) value);
				}
				return null;
			}

		});
		mainPopupMenu.setStatusAction(new ExecuteAction() {

			@Override
			public <T, E> E execute(T value) {
				if (value instanceof String) {
					PersonalManage pm = appContext.getManage(PersonalManage.class);
					pm.updateStatus((String) value);
				}
				return null;
			}

		});
		mainFrame.setStatusOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				Object source = event.getSource();
				if (source instanceof Node) {
					Node node = (Node) source;
					statusPopupMenu.show(node, Side.BOTTOM, node.getLayoutX(), node.getLayoutY());
				}
			}
		});

		mainPopupMenu.setQuitAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				appContext.exit();
			}
		});
		mainPopupMenu.setUpdatePasswordAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				UpdatePasswordView upv = appContext.getSingleView(UpdatePasswordView.class);
				upv.setVisible(true);
			}
		});

		groupPopupMenu.setAddAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				GroupDataView view = appContext.getSingleView(GroupDataView.class);
				view.setGroup(null);
				view.setVisible(true);
			}
		});

		groupPopupMenu.setFindAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				FindView findView = appContext.getSingleView(FindView.class);
				findView.setVisible(true);
			}
		});
	}

	@Override
	public void setVisible(boolean visible) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				if (visible) {
					mainFrame.show();
					mainFrame.toFront();
				} else {
					mainFrame.hide();
				}
			}
		});
	}

	@Override
	public boolean isShowing() {
		return mainFrame.isShowing();
	}

	@Override
	public void showPrompt(String text) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				mainFrame.showPrompt(text);
			}
		});
	}

	@Override
	public void setStatus(String status) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Image statusImage = ImageBox.getStatusImageIcon(status);
				mainFrame.setStatusImage(statusImage);
			}
		});
	}

	public void addOrUpdateUserCategory(UserCategory userCategory) {

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				ListNodePanel node = userListNodeMap.get(userCategory.getId());
				if (null == node) {
					node = new ListNodePanel();
					userListNodeMap.put(userCategory.getId(), node);
				}

				node.setText(userCategory.getName());
				node.setNumberText("[0/0]");
				userRoot.addNode(node);
			}
		});

	}

	public void addOrUpdateUserData(String userCategoryId, UserData userData) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				HeadItem head = userHeadLabelMap.get(userData.getId());
				if (null == head) {
					head = new HeadItem();
					userHeadLabelMap.put(userData.getId(), head);
				}

				setUserDataHead(userData, head);

				ListNodePanel node = userListNodeMap.get(userCategoryId);
				if (null != node) {
					node.addItem(head);
				}
			}
		});
	}

	public void addOrUpdateGroupCategory(GroupCategory groupCategory) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				ListNodePanel node = groupListNodeMap.get(groupCategory.getId());
				if (null == node) {
					node = new ListNodePanel();
					groupListNodeMap.put(groupCategory.getId(), node);
				}

				node.setText(groupCategory.getName());
				node.setNumberText("[0/0]");
				groupRoot.addNode(node);
			}
		});

	}

	public void addOrUpdateGroup(String groupCategoryId, Group group) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				HeadItem head = groupHeadLabelMap.get(group.getId());
				if (null == head) {
					head = new HeadItem();
					groupHeadLabelMap.put(group.getId(), head);
				}
				setGroupHead(group, head);
				ListNodePanel node = groupListNodeMap.get(groupCategoryId);
				if (null != node) {
					node.addItem(head);
				}
			}
		});
	}

	@Override
	public void setUser(User user) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				LoginData loginData = PersonalBox.get(LoginData.class);
				Image statusImage = ImageBox.getStatusImageIcon(loginData.getStatus());
				Image headImage = ImageBox.getImagePath("Resources/Images/Head/User/" + user.getHead() + "_100.gif", 60, 60);
				mainFrame.setHeadImage(headImage);
				mainFrame.setStatusImage(statusImage);
				mainFrame.setNickname(user.getNickname());
				mainFrame.setText(user.getSignature());
			}
		});
	}

	@Override
	public void updateUserCategoryMemberCount(String userCategoryId, int totalCount, int onlineCount) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				ListNodePanel node = userListNodeMap.get(userCategoryId);
				if (null != node) {
					node.setNumberText("[" + onlineCount + "/" + totalCount + "]");
				}
			}
		});
	}

	@Override
	public void updateGroupCategoryMemberCount(String groupCategoryId, int totalCount) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				ListNodePanel node = groupListNodeMap.get(groupCategoryId);
				if (null != node) {
					node.setNumberText("[" + totalCount + "]");
				}
			}
		});
	}

	@Override
	public void showUserHeadPulse(String userId, boolean pulse) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				HeadItem node = userHeadLabelMap.get(userId);
				if (null != node) {
					node.setPulse(pulse);
				}
			}
		});

	}

	@Override
	public void showGroupHeadPulse(String groupId, boolean pulse) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				HeadItem node = groupHeadLabelMap.get(groupId);
				if (null != node) {
					node.setPulse(pulse);
				}
			}
		});
	}

	private void setUserDataHead(UserData userData, HeadItem head) {
		String status = UserData.status_offline;
		if (null != userData.getStatus() && !(UserData.status_invisible.equals(userData.getStatus()))) {
			status = userData.getStatus();
		}

		Image image = ImageBox.getImagePath("Resources/Images/Head/User/" + userData.getHead() + ".png", 40, 40);

		String remark = (null == userData.getRemark() || "".equals(userData.getRemark())) ? userData.getNickname() : userData.getRemark();
		String nickname = (null == userData.getRemark() || "".equals(userData.getRemark())) ? userData.getAccount() : userData.getNickname();

		head.setHeadImage(image);
		head.setRemark(remark);// 备注名
		head.setNickname("(" + nickname + ")");// 昵称
		head.setShowText(userData.getSignature());// 个性签名
		head.setStatus("[4G]");

		IconButton iconButton = head.getAttribute("statusLabel");
		Image iconImage = ImageBox.getStatusImageIcon(status);

		if (null == iconButton) {// 状态图标显示组件
			iconButton = new IconButton(iconImage);
			head.addAttribute("statusLabel", iconButton);
			head.addBusinessIcon(iconButton);
		}

		iconButton.setNormalImage(iconImage);

		// 如果用户不是在线状态，则使其头像变灰
		if (HeadUtil.isGray(userData.getStatus())) {
			head.setGray(true);
		} else {
			head.setGray(false);
		}

		head.setOnMouseClicked((MouseEvent me) -> {
			if (me.getClickCount() == 2) {
				ChatManage chatManage = appContext.getManage(ChatManage.class);
				chatManage.showCahtFrame(userData);
			}
		});

	}

	private void setGroupHead(Group group, HeadItem head) {
		Image image = ImageBox.getImagePath("Resources/Images/Head/Group/" + group.getHead() + ".png", 40, 40);

		head.setHeadImage(image);
		head.setRemark(group.getName());
		head.setNickname("(" + group.getNumber() + ")");
		head.setShowText(group.getIntroduce());

		head.setOnMouseClicked((MouseEvent me) -> {
			if (me.getClickCount() == 2) {
				ChatManage chatManage = appContext.getManage(ChatManage.class);
				chatManage.showCahtFrame(group);
			}
		});

	}

	public void addLastGroup(Group group) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				HeadItem head = lastMap.get(group.getId());
				if (null == head) {
					head = new HeadItem();
					addLastItem(head);
					lastMap.put(group.getId(), head);
				}
				setGroupHead(group, head);
			}
		});
	}

	public void addLastUserData(UserData userData) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {

				HeadItem head = lastMap.get(userData.getId());
				if (null == head) {
					head = new HeadItem();
					addLastItem(head);
					lastMap.put(userData.getId(), head);
				}
				setUserDataHead(userData, head);
			}
		});
	}

	public void updateLastGroup(Group group) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				HeadItem head = lastMap.get(group.getId());
				if (null != head) {
					setGroupHead(group, head);
				}
			}
		});
	}

	public void updateLastUserData(UserData userData) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				HeadItem head = lastMap.get(userData.getId());
				if (null != head) {
					setUserDataHead(userData, head);
				}
			}
		});
	}

	private void addLastItem(HeadItem head) {
		int count = lastRoot.nodeSize();
		if (count > 50) {
			lastRoot.removeNode((count - 1));
		}
		lastRoot.addNode(0, head);
	}
}
