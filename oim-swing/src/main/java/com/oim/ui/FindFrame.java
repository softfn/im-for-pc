package com.oim.ui;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.UnsupportedLookAndFeelException;

import com.oim.common.box.HeadImageIconBox;
import com.oim.common.component.AlphaPanel;
import com.oim.common.util.DateUtil;
import com.oim.common.util.ImageUtil;
import com.oim.net.message.query.GroupQuery;
import com.oim.net.message.query.UserQuery;
import com.oim.ui.component.BaseFrame;
import com.oim.ui.component.WaitingPanel;
import com.oim.ui.component.WaitingPanel.WaitingType;
import com.oim.ui.component.event.ExecuteAction;
import com.oim.ui.find.ItemHead;
import com.oim.ui.main.TabPanel;
import com.only.OnlyButton;
import com.only.OnlyComboBox;
import com.only.OnlyPanel;
import com.only.OnlyTextField;
import com.only.layout.TileLayout;
import com.only.util.OnlyUIUtil;

/**
 * @author XiaHui
 * @date 2015年1月5日 上午11:52:00
 */
public class FindFrame extends BaseFrame {

	private static final long serialVersionUID = 1L;

	private OnlyPanel basePanel = new OnlyPanel();
	// private OnlyPanel topPanel = new OnlyPanel();
	private TabPanel tabPanel = new TabPanel();
	private OnlyPanel findUserPanel = new OnlyPanel();
	private JSeparator userSeparator = new JSeparator();

	private OnlyTextField nicknameTextField = new OnlyTextField();
	private OnlyComboBox<Object> sexComboBox = new OnlyComboBox<Object>();
	private OnlyComboBox<Object> ageComboBox = new OnlyComboBox<Object>();
	private OnlyComboBox<Object> bloodComboBox = new OnlyComboBox<Object>();
	private OnlyComboBox<Object> constellationComboBox = new OnlyComboBox<Object>();

	private JPanel userOptionsPanel = new JPanel();
	private OnlyButton userFindButton = new OnlyButton();
	private JButton userListDownButton = getPageButton();
	private JButton userListUpButton = getPageButton();
	private OnlyPanel userListPanel = new OnlyPanel();

	private OnlyPanel findGroupPanel = new OnlyPanel();
	private OnlyTextField groupKeywordsTextField = new OnlyTextField();
	private OnlyButton groupFindButton = new OnlyButton();
	private JButton groupListDownButton = getPageButton();
	private JButton groupListUpButton = getPageButton();
	private OnlyPanel groupListPanel = new OnlyPanel();

	private WaitingPanel userWaitingPanel = new WaitingPanel();
	private WaitingPanel groupWaitingPanel = new WaitingPanel();

	private List<ExecuteAction> userPageActionList = new ArrayList<ExecuteAction>();
	private List<ExecuteAction> queryUserActionList = new ArrayList<ExecuteAction>();
	private List<ExecuteAction> groupPageActionList = new ArrayList<ExecuteAction>();
	private List<ExecuteAction> queryGroupActionList = new ArrayList<ExecuteAction>();

	private int userPageNumber = 0;
	private int groupPageNumber = 0;

	public FindFrame() {
		initComponent();
		initEvent();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void initComponent() {
		this.setMinimumSize(new java.awt.Dimension(750, 520));
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setLayout(new CardLayout());
		this.setShowTitle(false);
		this.setShowIconImage(false);

		basePanel.setOpaque(false);
		basePanel.setLayout(null);
		basePanel.add(tabPanel);

		tabPanel.setBounds(0, 70, 750, 450);
		tabPanel.add(new ImageIcon("Resources/Images/Default/FindFrame/findfriend_normal.png"), new ImageIcon("Resources/Images/Default/FindFrame/findfriend_hover.png"), new ImageIcon("Resources/Images/Default/FindFrame/findfriend_hover.png"), findUserPanel);
		tabPanel.add(new ImageIcon("Resources/Images/Default/FindFrame/findgroup_normal.png"), new ImageIcon("Resources/Images/Default/FindFrame/findgroup_hover.png"), new ImageIcon("Resources/Images/Default/FindFrame/findgroup_hover.png"), findGroupPanel);

		findUserPanel.setBackground(Color.white);
		// findUserPanel.setBackground(new Color(213, 123, 44));
		findUserPanel.setLayout(null);
		findUserPanel.add(userOptionsPanel);
		findUserPanel.add(userListPanel);
		findUserPanel.add(userWaitingPanel);

		nicknameTextField.setLabelText("帐号/昵称");

		ageComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "年龄", "16-22", "23-30", "31-40", "40以上" }));
		sexComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "性别", "男", "女" }));
		constellationComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "星座", "水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "摩羯座" }));
		bloodComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "血型", "A", "B", "AB", "O", "其他" }));

		nicknameTextField.setBounds(100, 20, 360, 25);
		ageComboBox.setBounds(100, 55, 80, 22);
		sexComboBox.setBounds(195, 55, 80, 22);
		constellationComboBox.setBounds(285, 55, 80, 22);
		bloodComboBox.setBounds(380, 55, 80, 22);

		userOptionsPanel.add(bloodComboBox);
		userOptionsPanel.add(constellationComboBox);
		userOptionsPanel.add(nicknameTextField);
		userOptionsPanel.add(sexComboBox);
		userOptionsPanel.add(ageComboBox);

		userOptionsPanel.setLayout(null);
		userOptionsPanel.setBackground(new java.awt.Color(235, 242, 249));
		userOptionsPanel.setBounds(0, 0, 750, 100);
		userOptionsPanel.add(userFindButton);
		userOptionsPanel.add(userSeparator);

		userSeparator.setBounds(0, 99, 750, 1);

		userFindButton.setBounds(480, 23, 120, 50);
		userFindButton.setText("查找");
		userFindButton.setFont(OnlyUIUtil.getDefaultFont(16));
		userFindButton.setForeground(Color.white);
		userFindButton.setNormalImageInsets(6, 6, 6, 6);
		userFindButton.setFocusImageInsets(6, 6, 6, 6);
		userFindButton.setNormalImage(new ImageIcon("Resources/Images/Default/Button/blue_normal.png").getImage());
		userFindButton.setRolloverImage(new ImageIcon("Resources/Images/Default/Button/blue_hover.png").getImage());
		userFindButton.setPressedImage(new ImageIcon("Resources/Images/Default/Button/blue_down.png").getImage());
		userFindButton.setFocusImage(new ImageIcon("Resources/Images/Default/Button/blue_down.png").getImage());

		AlphaPanel userPagePanel = new AlphaPanel();
		userPagePanel.setLayout(null);
		userPagePanel.setBounds(0, 100, 750, 30);
		// userPagePanel.setBackground(new java.awt.Color(219, 239, 249));
		userPagePanel.setBackgroundColor(new Color(213, 123, 44, 20));
		userPagePanel.add(userListDownButton);
		userPagePanel.add(userListUpButton);

		findUserPanel.add(userPagePanel);

		Icon upNormalIcon = ImageUtil.getIcon("Resources/Images/Default/FindFrame/pre_normal.png");
		Icon upHoverIcon = ImageUtil.getIcon("Resources/Images/Default/FindFrame/pre_hover.png");
		Icon upDisabledIcon = ImageUtil.getIcon("Resources/Images/Default/FindFrame/pre_disabled.png");
		Icon downNormalIcon = ImageUtil.getIcon("Resources/Images/Default/FindFrame/next_normal.png");
		Icon downHoverIcon = ImageUtil.getIcon("Resources/Images/Default/FindFrame/next_hover.png");
		Icon downDisabledIcon = ImageUtil.getIcon("Resources/Images/Default/FindFrame/next_disabled.png");

		userListDownButton.setIcon(downNormalIcon);
		userListDownButton.setSelectedIcon(downHoverIcon);
		userListDownButton.setDisabledIcon(downDisabledIcon);
		userListDownButton.setBounds(657, 5, 27, 20);

		userListUpButton.setIcon(upNormalIcon);
		userListUpButton.setSelectedIcon(upHoverIcon);
		userListUpButton.setDisabledIcon(upDisabledIcon);
		userListUpButton.setBounds(630, 5, 27, 20);

		userListPanel.setBounds(3, 130, 744, 250);
		userListPanel.setLayout(new TileLayout(1, 1, 1, TileLayout.INDENTIC_SIZE));
		userListPanel.setOpaque(false);
		userListPanel.setBackground(Color.white);

		userWaitingPanel.setBounds(3, 130, 744, 250);
		userWaitingPanel.setVisible(false);

		findGroupPanel.setLayout(null);
		findGroupPanel.setBackground(Color.white);
		findGroupPanel.add(groupKeywordsTextField);
		findGroupPanel.add(groupFindButton);
		findGroupPanel.add(groupListPanel);
		findGroupPanel.add(groupWaitingPanel);

		groupKeywordsTextField.setBounds(100, 20, 360, 25);

		groupKeywordsTextField.setLabelText("群名称/群号");

		groupFindButton.setBounds(480, 20, 120, 25);
		groupFindButton.setText("查找");
		groupFindButton.setForeground(Color.white);
		groupFindButton.setNormalImageInsets(6, 6, 6, 6);
		groupFindButton.setNormalImage(new ImageIcon("Resources/Images/Default/Button/blue_normal.png").getImage());
		groupFindButton.setRolloverImage(new ImageIcon("Resources/Images/Default/Button/blue_hover.png").getImage());
		groupFindButton.setPressedImage(new ImageIcon("Resources/Images/Default/Button/blue_down.png").getImage());
		groupFindButton.setFocusImage(new ImageIcon("Resources/Images/Default/Button/blue_down.png").getImage());

		AlphaPanel groupPagePanel = new AlphaPanel();
		groupPagePanel.setLayout(null);
		groupPagePanel.setBounds(0, 60, 750, 30);
		// userPagePanel.setBackground(new java.awt.Color(219, 239, 249));
		groupPagePanel.setBackgroundColor(new Color(213, 123, 44, 20));
		groupPagePanel.add(groupListDownButton);
		groupPagePanel.add(groupListUpButton);

		findGroupPanel.add(groupPagePanel);

		groupListDownButton.setIcon(downNormalIcon);
		groupListDownButton.setSelectedIcon(downHoverIcon);
		groupListDownButton.setBounds(657, 5, 27, 20);

		groupListUpButton.setIcon(upNormalIcon);
		groupListUpButton.setSelectedIcon(upHoverIcon);
		groupListUpButton.setBounds(630, 5, 27, 20);

		groupListPanel.setBounds(3, 90, 744, 290);
		groupListPanel.setLayout(new TileLayout(1, 1, 1, TileLayout.INDENTIC_SIZE));
		groupListPanel.setOpaque(false);
		// userListPanel.setBackground(Color.white);

		groupWaitingPanel.setBounds(3, 90, 744, 290);
		groupWaitingPanel.setVisible(false);

		add(basePanel);
	}

	private JButton getPageButton() {

		JButton button = new JButton();
		button.setBorder(null);
		button.setContentAreaFilled(false);
		button.setFocusPainted(false);
		button.setDoubleBuffered(true);

		button.setOpaque(false);
		button.setFocusable(false);
		button.setActionCommand(null);
		button.setSelected(false);
		return button;
	}

	private void initEvent() {
		userFindButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				queryUser();
			}
		});

		groupFindButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				queryGroup();
			}
		});
		userListDownButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				userPage(userPageNumber + 1);
			}
		});
		userListUpButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				userPage(userPageNumber - 1);
			}
		});
		groupListDownButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				groupPage(groupPageNumber + 1);

			}
		});
		groupListUpButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				groupPage(groupPageNumber - 1);
			}
		});
	}

	public void addUserPageChangeAction(ExecuteAction action) {
		userPageActionList.add(action);
	}

	public void addQueryUserAction(ExecuteAction action) {
		queryUserActionList.add(action);
	}

	public void addGroupPageChangeAction(ExecuteAction action) {
		groupPageActionList.add(action);
	}

	public void addQueryGroupAction(ExecuteAction action) {
		queryGroupActionList.add(action);
	}

	public void initData() {

		userPageNumber = 0;
		groupPageNumber = 0;
		nicknameTextField.setText("");
		sexComboBox.setSelectedIndex(0);
		ageComboBox.setSelectedIndex(0);
		bloodComboBox.setSelectedIndex(0);
		constellationComboBox.setSelectedIndex(0);

		groupKeywordsTextField.setText("");

		groupListPanel.removeAll();
		userListPanel.removeAll();

	}

	public void setUserPage(int pageNumber, int totalPage) {
		this.userPageNumber = pageNumber;
		if (pageNumber <= 1) {
			userListUpButton.setEnabled(false);
		} else {
			userListUpButton.setEnabled(true);
		}
		if (totalPage <= pageNumber) {
			userListDownButton.setEnabled(false);
		} else {
			userListDownButton.setEnabled(true);
		}
	}

	public void setGroupPage(int pageNumber, int totalPage) {
		this.groupPageNumber = pageNumber;
		if (pageNumber <= 1) {
			groupListUpButton.setEnabled(false);
		} else {
			groupListUpButton.setEnabled(false);
		}
		if (totalPage <= pageNumber) {
			groupListDownButton.setEnabled(false);
		} else {
			groupListDownButton.setEnabled(true);
		}
	}

	public void setUserItemList(List<ItemHead> itemHeadList) {
		userListPanel.removeAll();
		for (ItemHead findHead : itemHeadList) {
			userListPanel.add(findHead);
		}
	}

	public void setGroupItemList(List<ItemHead> itemHeadList) {
		groupListPanel.removeAll();
		for (ItemHead findHead : itemHeadList) {
			groupListPanel.add(findHead);
		}
	}

	private void userPage(int number) {
		for (ExecuteAction userPageAction : userPageActionList) {
			userPageAction.execute(number);
		}
	}

	private void groupPage(int number) {
		for (ExecuteAction groupPageAction : groupPageActionList) {
			groupPageAction.execute(number);
		}
	}

	private void queryUser() {
		UserQuery user = new UserQuery();
		String nickname = this.nicknameTextField.getText();
		String age = (String) this.ageComboBox.getSelectedItem();
		String sex = (String) this.sexComboBox.getSelectedItem();
		String constellation = (String) this.constellationComboBox.getSelectedItem();
		String blood = (String) this.bloodComboBox.getSelectedItem();

		user.setQueryText(nickname);

		if (!"不限".equals(sex) && !"性别".equals(sex)) {
			user.setGender(sex);
		} else {
			user.setGender(null);
		}
		if (!"不限".equals(constellation) && !"星座".equals(constellation)) {
			user.setConstellation(constellation);
		} else {
			user.setConstellation(null);
		}
		if (!"不限".equals(blood) && !"血型".equals(blood)) {
			user.setBlood(blood);
		}

		if ("16-22".equals(age)) {
			long now = System.currentTimeMillis();
			String start = DateUtil.dateToDate(new Date((now - (22l * 31536000000l))));
			String end = DateUtil.dateToDate(new Date((now - (16l * 31536000000l))));
			user.setStartBirthdate(start);
			user.setEndBirthdate(end);
		} else if ("23-30".equals(age)) {
			long now = System.currentTimeMillis();
			String start = DateUtil.dateToDate(new Date((now - (30l * 31536000000l))));
			String end = DateUtil.dateToDate(new Date((now - (23l * 31536000000l))));
			user.setStartBirthdate(start);
			user.setEndBirthdate(end);
		} else if ("31-40".equals(age)) {
			long now = System.currentTimeMillis();
			String start = DateUtil.dateToDate(new Date((now - (40l * 31536000000l))));
			String end = DateUtil.dateToDate(new Date((now - (31l * 31536000000l))));
			user.setStartBirthdate(start);
			user.setEndBirthdate(end);
		} else if ("40以上".equals(age)) {
			long now = System.currentTimeMillis();
			String end = DateUtil.dateToDate(new Date((now - (40l * 31536000000l))));
			user.setStartBirthdate("");
			user.setEndBirthdate(end);
		} else {
			user.setStartBirthdate("");
			user.setEndBirthdate("");
		}
		for (ExecuteAction action : queryUserActionList) {
			action.execute(user);
		}
	}

	private void queryGroup() {
		GroupQuery group = new GroupQuery();
		String name = this.groupKeywordsTextField.getText();
		group.setName(name);
		for (ExecuteAction action : queryGroupActionList) {
			action.execute(group);
		}
	}

	public void showUserWaiting(boolean show, WaitingType waitingType) {
		userWaitingPanel.show(waitingType);
		userWaitingPanel.setVisible(show);
		userListPanel.setVisible(!show);
	}

	public void showGroupWaiting(boolean show, WaitingType waitingType) {
		groupWaitingPanel.show(waitingType);
		groupWaitingPanel.setVisible(show);
		groupListPanel.setVisible(!show);
	}

	public void showUserList() {
		int temp = 16;
		userListPanel.removeAll();
		for (int i = 0; i < temp; i++) {
			ItemHead findHead = new ItemHead();
			findHead.setName("hhhhahahhahh(819891823918)");
			findHead.setHeadIcon(HeadImageIconBox.getUserHeadImageIcon((i + 1) + "", 40, 40));
			findHead.setShowText("wo哈哈哇哇哇");
			findHead.setPreferredSize(new Dimension(185, 55));
			userListPanel.add(findHead);
		}

		int t = 20;
		groupListPanel.removeAll();
		for (int i = 0; i < t; i++) {
			ItemHead findHead = new ItemHead();
			findHead.setName("hhhhahahhahh(819891823918)");
			findHead.setHeadIcon(HeadImageIconBox.getUserHeadImageIcon40((i + 1) + ""));
			findHead.setShowText("wo哈哈哇哇哇");
			findHead.setPreferredSize(new Dimension(185, 55));
			groupListPanel.add(findHead);
		}
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FindFrame frame = new FindFrame();
					frame.showUserList();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
