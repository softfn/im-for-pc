package com.oim.ui;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;

import com.oim.common.util.ImageUtil;
import com.oim.ui.component.BaseDialog;
import com.oim.ui.component.event.ExecuteAction;
import com.oim.ui.component.list.HeadLabel;
import com.oim.ui.component.list.Node;
import com.oim.ui.component.list.Root;
import com.oim.ui.main.Tab;
import com.oim.ui.main.TabPanel;
import com.oim.ui.main.UserDataPanel;
import com.only.OnlyPanel;
import com.only.common.WindowAutoHide;
import com.only.util.OnlyImageUtil;

/**
 * 描述：
 * 
 * @author 夏辉
 * @date 2015年2月27日 下午9:41:12
 * @version 0.0.1
 */
public class MainDialog extends BaseDialog {

	private static final long serialVersionUID = 1L;

	private OnlyPanel topPanel = new OnlyPanel();
	private OnlyPanel titlePanel = new OnlyPanel();
	private OnlyPanel mainPanel = new OnlyPanel();
	private OnlyPanel bottomPanel = new OnlyPanel();

	private UserDataPanel userDataPanel = new UserDataPanel();
	private TabPanel tabPanel = new TabPanel();

	private Root userRoot = new Root();
	private Root groupRoot = new Root();
	private Root lastRoot = new Root();

	public MainDialog() {
		super(new javax.swing.JFrame(), false);
		initComponents();
	}

	/**
	 * Creates new form MainDialog
	 */
	public MainDialog(java.awt.Frame parent, boolean modal) {
		super(parent, modal);
		initComponents();
		initUserList();
	}

	private void initComponents() {
		this.setSize(280, 630);
		this.setMinimumSize(new java.awt.Dimension(280, 530));
		this.setMaximumSize(new java.awt.Dimension(520, 760));
		this.setBorderPainted(false);
		this.setLocationRelativeTo(null);
		this.setShowIconImage(false);
		this.setLayout(new CardLayout());
		this.setTitle("OIM");
		this.getRootPane().setWindowDecorationStyle(JRootPane.INFORMATION_DIALOG);

		topPanel.setOpaque(false);
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

		mainPanel.setLayout(new java.awt.BorderLayout());
		mainPanel.setOpaque(false);

		new WindowAutoHide(this);

		tabPanel.add(new ImageIcon("Resources/Images/Default/MainPanel/icon_contacts_normal.png"), new ImageIcon("Resources/Images/Default/MainPanel/icon_contacts_hover.png"), new ImageIcon("Resources/Images/Default/MainPanel/icon_contacts_selected.png"), userRoot);
		tabPanel.add(new ImageIcon("Resources/Images/Default/MainPanel/icon_group_normal.png"), new ImageIcon("Resources/Images/Default/MainPanel/icon_group_hover.png"), new ImageIcon("Resources/Images/Default/MainPanel/icon_group_selected.png"), groupRoot);
		tabPanel.add(new ImageIcon("Resources/Images/Default/MainPanel/icon_last_normal.png"), new ImageIcon("Resources/Images/Default/MainPanel/icon_last_hover.png"), new ImageIcon("Resources/Images/Default/MainPanel/icon_last_selected.png"), lastRoot);

		JPanel panel = new JPanel();
		panel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 8, 3));
		panel.setOpaque(false);
		panel.add(new JLabel(""));

		topPanel.add(titlePanel, java.awt.BorderLayout.PAGE_START);
		topPanel.add(userDataPanel, java.awt.BorderLayout.CENTER);
		topPanel.add(panel);

		titlePanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 8, 15));
		titlePanel.setOpaque(false);
		titlePanel.add(new JLabel(""));

		mainPanel.add(topPanel, java.awt.BorderLayout.PAGE_START);
		mainPanel.add(tabPanel, java.awt.BorderLayout.CENTER);
		mainPanel.add(bottomPanel, java.awt.BorderLayout.PAGE_END);

		add(mainPanel);
		bottomPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 8, 2));
		bottomPanel.setBackground(new Color(40, 63, 85));
		//bottomPanel.setOpaque(false);
	}

	public void setHeadIcon(ImageIcon headIcon) {
		userDataPanel.setHeadIcon(headIcon);
	}

	public void setNickname(String nickname) {
		userDataPanel.setName(nickname);
	}

	public void setSignature(String signature) {
		userDataPanel.setText(signature);
	}

	public void setStatus(String status) {
		userDataPanel.changeStatus(status);
	}

	public Root getUserRoot() {
		return userRoot;
	}

	public Root getGroupRoot() {
		return groupRoot;
	}

	public Root getLastRoot() {
		return lastRoot;
	}

	public void addUserCategoryNode(Node node) {
		userRoot.addNode(node);
	}

	public void addGroupCategoryNode(Node node) {
		groupRoot.addNode(node);
	}

	public void addLastNode(HeadLabel node) {
		int count = lastRoot.getComponentCount();
		if (count > 50) {
			lastRoot.remove((count - 1));
		}
		lastRoot.addNode(node, 0);

	}

	public void removeUserCategoryNode(Node node) {
		userRoot.removeNode(node);
	}

	public void removeGroupCategoryNode(Node node) {
		groupRoot.removeNode(node);
	}

	public void removeLastNode(HeadLabel node) {
		lastRoot.removeNode(node);
	}

	public void addBottom(Component c) {
		bottomPanel.add(c);
	}

	public void updateStatus(String status) {
		userDataPanel.changeStatus(status);
	}

	public void setStatusAction(ExecuteAction statusAction){
		userDataPanel.setStatusAction(statusAction);
	}
	
	private void initUserList() {
		Node[] teamNode = new Node[5];
		for (int j = 0; j < 5; j++) {
			teamNode[j] = new Node();
			teamNode[j].setTitleText("我的好友" + j);

			for (int i = 0; i < 5; i++) {

				HeadLabel head = new HeadLabel();

				head.setStatusIcon(new ImageIcon("Resources/Images/Default/Status/FLAG/Big/MobilePhoneQQOn.png"));

				head.setHeadIcon(ImageUtil.getRoundedCornerImageIcon("Resources/Images/Head/User/" + (j + 1) + ".png", 40, 40, 40, 40));
				head.setRoundedCorner(40, 40);
				head.setRemark("女神经");
				head.setNickname("(也的JJ刚刚)");
				head.setStatus("[2G]");
				head.setShowText("哈哈哈，又有新闻了");
				head.addBusinessAttribute("s",new JLabel(new ImageIcon("Resources/Images/Default/Status/FLAG/Big/imonline.png")));

				teamNode[j].addNode(head);
			}
			userRoot.addNode(teamNode[j]);
		}

		Node[] groupNode = new Node[5];
		for (int j = 0; j < 5; j++) {
			groupNode[j] = new Node();
			groupNode[j].setTitleText("我的群" + j);
			groupNode[j].setCountText("[5]");
			for (int i = 0; i < 5; i++) {

				HeadLabel head = new HeadLabel();

				head.setStatusIcon(new ImageIcon("Resources/Images/Default/Status/FLAG/Big/MobilePhoneQQOn.png"));

				head.setHeadIcon(ImageUtil.getRoundedCornerImageIcon("Resources/Images/Head/User/" + (j + 1) + ".png", 40, 40, 6, 6));

				head.setRemark("女神经");
				head.setNickname("(也的JJ刚刚)");
				head.setStatus("[2G]");
				head.setShowText("哈哈哈，又有新闻了");
				head.addBusinessAttribute("s",new JLabel(new ImageIcon("Resources/Images/Default/Status/FLAG/Big/imonline.png")));

				groupNode[j].addNode(head);
			}
			groupRoot.addNode(groupNode[j]);
		}

		for (int j = 0; j < 15; j++) {

			HeadLabel head = new HeadLabel();

			head.setStatusIcon(new ImageIcon("Resources/Images/Default/Status/FLAG/Big/MobilePhoneQQOn.png"));

			head.setHeadIcon(new ImageIcon("Resources/Images/Default/UserHead/" + (j + 1) + ".png"));

			head.setRemark("女神经");
			head.setNickname("(也的JJ刚刚)");
			head.setStatus("[2G]");
			head.setShowText("哈哈哈，又有新闻了");
			head.addBusinessAttribute("s",new JLabel(new ImageIcon("Resources/Images/Default/Status/FLAG/Big/imonline.png")));
			head.setShowSeparator(true);

			lastRoot.addNode(head);
		}

		ImageIcon headIcon = ImageUtil.getRoundedCornerImageIcon("Resources/Images/Head/User/91_100.gif", 60, 60, 60, 60);
		userDataPanel.setHeadIcon(headIcon);
		userDataPanel.setName("wo擦哪件的fan华！77777777656！！");
		userDataPanel.setText("这是什么情况呀呀。。。。。。。。。");
		// topPanel.setBackgroundImage(new
		// ImageIcon("Resources/Images/Default/MainPanel/head-b.png").getImage());

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

		bottomPanel.add(menuTab);
		bottomPanel.add(settingTab);
		bottomPanel.add(skinTab);
		bottomPanel.add(messageTab);
		bottomPanel.add(findTab);

	}

	public static void main(String args[]) {

		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(MainDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(MainDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(MainDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(MainDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}

		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				MainDialog dialog = new MainDialog(new javax.swing.JFrame(), true);
				dialog.addWindowListener(new java.awt.event.WindowAdapter() {
					@Override
					public void windowClosing(java.awt.event.WindowEvent e) {
						System.exit(0);
					}
				});

				Image imaeg = new ImageIcon("Resources/Images/Login/002.jpg").getImage();
				BufferedImage bi = new BufferedImage(imaeg.getWidth(null), imaeg.getHeight(null), BufferedImage.TYPE_INT_RGB);

				Graphics2D biContext = bi.createGraphics();
				biContext.drawImage(imaeg, 0, 0, null);
				bi = OnlyImageUtil.applyGaussianFilter(bi, null, 50);
				dialog.setBackgroundImage(bi);
				dialog.setVisible(true);
			}
		});
	}
}
