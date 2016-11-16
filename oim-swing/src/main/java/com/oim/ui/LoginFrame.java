package com.oim.ui;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.oim.bean.User;
import com.oim.common.box.ImageBox;
import com.oim.common.component.AlphaPanel;
import com.oim.ui.component.BaseFrame;
import com.oim.ui.component.StatusLabel;
import com.oim.ui.component.WaitingPanel;
import com.oim.ui.login.ComboBox;
import com.oim.ui.login.ComboBoxAction;
import com.only.OnlyButton;
import com.only.OnlyCheckBox;
import com.only.OnlyLabel;
import com.only.OnlyMenuItem;
import com.only.OnlyPanel;
import com.only.OnlyPasswordField;
import com.only.OnlyPopupMenu;
import com.only.util.OnlyUIUtil;
import com.over.image.OverDecoratedImage;

/**
 * 登录界面
 * 
 * @author XiaHui
 * @date 2015-02-04 11:10:12
 */
public class LoginFrame extends BaseFrame {

	private static final long serialVersionUID = 1L;
	private OnlyButton settingButton = new OnlyButton();
	private OverDecoratedImage logoLabel = new OverDecoratedImage();
	// private OnlyLabel logoLabel = new OnlyLabel();//
	private OnlyPanel topPanel = new OnlyPanel();

	private OnlyPanel rootPanel = new OnlyPanel();
	private AlphaPanel basePanel = new AlphaPanel();
	private AlphaPanel userNamePanel = new AlphaPanel();
	private AlphaPanel passwordPanel = new AlphaPanel();
	private OnlyLabel userNameLabel = new OnlyLabel();
	private ComboBox textField = new ComboBox();

	private OnlyLabel passwordLabel = new OnlyLabel();
	private OnlyPasswordField passwordField = new OnlyPasswordField();
	private OnlyCheckBox rememberCheckBox = new OnlyCheckBox();
	private OnlyButton loginButton = new OnlyButton();
	private OnlyLabel forgetPasswordLabel = new OnlyLabel();
	private OnlyLabel registerLabel = new OnlyLabel();
	private StatusLabel statusButton = new StatusLabel();

	private OnlyPopupMenu statusPopupMenu = new OnlyPopupMenu();

	private WaitingPanel waitingPanel = new WaitingPanel();

	private ImageIcon onlineImageIcon = ImageBox.getImageIcon("Resources/Images/Default/Status/FLAG/Big/imonline.png");
	private ImageIcon omeImageIcon = ImageBox.getImageIcon("Resources/Images/Default/Status/FLAG/Big/Qme.png");
	private ImageIcon awayImageIcon = ImageBox.getImageIcon("Resources/Images/Default/Status/FLAG/Big/away.png");
	private ImageIcon busyImageIcon = ImageBox.getImageIcon("Resources/Images/Default/Status/FLAG/Big/busy.png");
	private ImageIcon muteImageIcon = ImageBox.getImageIcon("Resources/Images/Default/Status/FLAG/Big/mute.png");
	private ImageIcon invisibleImageIcon = ImageBox.getImageIcon("Resources/Images/Default/Status/FLAG/Big/invisible.png");



	private String status = "1";

	public LoginFrame() {
		initComponents();
		initMenu();
		initEvent();
	}

	private void initComponents() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(280, 330);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setLayout(null);
		this.setShowTitle(false);
		this.setShowIconImage(false);
		this.setTitle("OIM登录");

		settingButton.setToolTipText("设置");
		settingButton.setText(null);
		settingButton.setFocusable(false);
		settingButton.setFocusPainted(false);
		settingButton.setNormalImage(ImageBox.getImageIcon("Resources/Images/Default/Login/setting_normal.png").getImage());
		settingButton.setRolloverImage(ImageBox.getImageIcon("Resources/Images/Default/Login/setting_hover.png").getImage());
		settingButton.setPressedImage(ImageBox.getImageIcon("Resources/Images/Default/Login/setting_down.png").getImage());
		settingButton.setPreferredSize(new Dimension(settingButton.getNormalImage().getWidth(null), settingButton.getNormalImage().getHeight(null)));
		settingButton.setBounds(190, 0, 30, 27);

		logoLabel.setIcon(ImageBox.getImageIcon("Resources/Images/login/1.png"));
		logoLabel.setBounds(80, 40, 110, 110);
		logoLabel.setRound(60);
		logoLabel.setDrawGlassLayer(false);
		logoLabel.setDrawBorder(true);

		topPanel.setOpaque(false);
		topPanel.setLayout(null);
		topPanel.setBounds(0, 0, 280, 100);
		topPanel.setBackground(new Color(0, 128, 157));

		rootPanel.setBounds(0, 100, 280, 230);
		// basePanel.setBackgroundColor(new Color(10, 10, 10));
		// basePanel.setBackground(new Color(69, 159, 134));
		basePanel.setBackgroundColorAlpha(0);
		basePanel.setLayout(null);
		basePanel.setOpaque(false);

		userNamePanel.setLayout(new BoxLayout(userNamePanel, BoxLayout.X_AXIS));
		passwordPanel.setLayout(new BoxLayout(passwordPanel, BoxLayout.X_AXIS));

		userNamePanel.setBounds(30, 55, 220, 32);
		passwordPanel.setBounds(30, 97, 220, 32);
		statusButton.setBounds(30, 137, 18, 18);
		rememberCheckBox.setBounds(55, 134, 80, 25);
		loginButton.setBounds(30, 160, 220, 32);
		forgetPasswordLabel.setBounds(30, 200, 70, 25);
		registerLabel.setBounds(220, 200, 30, 25);

		// userNamePanel.setBackground(new Color(81, 197, 174));
		// passwordPanel.setBackground(new Color(81, 197, 174));
		userNamePanel.setBackground(new Color(255, 255, 255));
		passwordPanel.setBackground(new Color(255, 255, 255));

		userNameLabel.setIcon(ImageBox.getImageIcon("Resources/Images/Default/Login/user_name_16.png"));
		passwordLabel.setIcon(ImageBox.getImageIcon("Resources/Images/Default/Login/password_16.png"));

		textField.setBorder(null);
		passwordField.setBorder(null);

		rememberCheckBox.setText("记住密码");
		statusButton.setIcon(onlineImageIcon);
		statusButton.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

		loginButton.setFont(OnlyUIUtil.getDefaultFont(15));

		rememberCheckBox.setForeground(new Color(255, 255, 255));
		loginButton.setForeground(new Color(255, 255, 255));
		forgetPasswordLabel.setForeground(new Color(255, 255, 255));
		registerLabel.setForeground(new Color(255, 255, 255));

		loginButton.setText("登  录");
		forgetPasswordLabel.setText("<html><u>忘记密码？</u><html>");
		registerLabel.setText("<html><u>注册</u><html>");

		forgetPasswordLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		registerLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

		textField.setLabelText("帐号/邮箱/手机号");
		passwordField.setLabelText("密码");

		textField.setImageAlpha(0.0f);
		passwordField.setAlpha(0.0f);
		textField.setOpaque(false);

		userNamePanel.setBackgroundColorAlpha(200);
		userNamePanel.setOpaque(false);

		passwordPanel.setBackgroundColorAlpha(200);
		passwordPanel.setOpaque(false);

		JLabel nameLabel = new JLabel();
		nameLabel.setPreferredSize(new Dimension(6, 12));
		userNamePanel.add(nameLabel);
		userNamePanel.add(userNameLabel);
		userNamePanel.add(textField);

		JLabel password = new JLabel();
		password.setPreferredSize(new Dimension(6, 12));
		passwordPanel.add(password);
		passwordPanel.add(passwordLabel);
		passwordPanel.add(passwordField);

		// topPanel.add(logoLabel);

		basePanel.add(userNamePanel);
		basePanel.add(passwordPanel);
		// basePanel.add(statusButton);

		// basePanel.add(rememberCheckBox);
		basePanel.add(loginButton);
		basePanel.add(forgetPasswordLabel);
		basePanel.add(registerLabel);

		rootPanel.setLayout(new CardLayout());
		rootPanel.setOpaque(false);

		rootPanel.add(basePanel);
		rootPanel.add(waitingPanel);

		waitingPanel.setVisible(false);
		waitingPanel.setWaitingText("正在登录中。");

		add(settingButton);
		add(logoLabel);
		add(rootPanel);

//		Image imaeg = new ImageIcon("Resources/Images/Login/002.jpg").getImage();
//		BufferedImage bi = new BufferedImage(imaeg.getWidth(null), imaeg.getHeight(null), BufferedImage.TYPE_INT_RGB);
//
//		Graphics2D biContext = bi.createGraphics();
//		biContext.drawImage(imaeg, 0, 0, null);
//		bi = OnlyImageUtil.applyGaussianFilter(bi, null, 30);
		//this.setBackgroundImage(imaeg);
	}

	private void initMenu() {
		OnlyMenuItem awayMenuItem = new OnlyMenuItem();
		OnlyMenuItem busyMenuItem = new OnlyMenuItem();
		OnlyMenuItem invisibleMenuItem = new OnlyMenuItem();

		OnlyPopupMenu.Separator separator1 = new OnlyPopupMenu.Separator();
		OnlyPopupMenu.Separator separator2 = new OnlyPopupMenu.Separator();
		OnlyMenuItem muteMenuItem = new OnlyMenuItem();
		OnlyMenuItem omeMenuItem = new OnlyMenuItem();
		OnlyMenuItem onlineMenuItem = new OnlyMenuItem();

		onlineMenuItem.setText("我在线上");
		onlineMenuItem.setToolTipText("");
		omeMenuItem.setText("o我吧");
		awayMenuItem.setText("离开");
		busyMenuItem.setText("忙碌");
		muteMenuItem.setText("请勿打扰");
		invisibleMenuItem.setText("隐身");

		onlineMenuItem.setIcon(onlineImageIcon);
		omeMenuItem.setIcon(omeImageIcon);
		awayMenuItem.setIcon(awayImageIcon);
		busyMenuItem.setIcon(busyImageIcon);
		muteMenuItem.setIcon(muteImageIcon);
		invisibleMenuItem.setIcon(invisibleImageIcon);

		statusPopupMenu.add(onlineMenuItem);
		statusPopupMenu.add(omeMenuItem);
		statusPopupMenu.add(separator1);
		statusPopupMenu.add(awayMenuItem);
		statusPopupMenu.add(busyMenuItem);
		statusPopupMenu.add(muteMenuItem);
		statusPopupMenu.add(separator2);
		statusPopupMenu.add(invisibleMenuItem);

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
	}

	private void onlineMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
		statusButton.setIcon(onlineImageIcon);
		status = User.status_online;
	}

	private void omeMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
		statusButton.setIcon(omeImageIcon);
		status = User.status_call_me;
	}

	private void awayMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
		statusButton.setIcon(awayImageIcon);
		status = User.status_away;
	}

	private void busyMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
		statusButton.setIcon(busyImageIcon);
		status = User.status_busy;
	}

	private void muteMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
		statusButton.setIcon(muteImageIcon);
		status = User.status_mute;
	}

	private void invisibleMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
		statusButton.setIcon(invisibleImageIcon);
		status = User.status_invisible;
	}

	private void initEvent() {

		statusButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				statusPopupMenu.show(statusButton, 0, statusButton.getHeight());
			}
		});

	}

	public void addLoginAction(ActionListener l) {
		loginButton.addActionListener(l);
	}

	public void addLoginKeyListener(KeyListener l) {
		addKeyListener(l);
		loginButton.addKeyListener(l);
		passwordField.addKeyListener(l);
	}

	public void addAccountAction(ComboBoxAction a) {
		textField.addComboBoxAction(a);
	}

	public void addSettingAction(ActionListener l) {
		settingButton.addActionListener(l);
	}

	public void addRegisterListener(MouseListener l) {
		registerLabel.addMouseListener(l);
	}

	public void setStatusIcon(Icon icon) {
		statusButton.setIcon(icon);
	}

	public String getAccount() {
		return textField.getText();
	}

	public String getPassword() {
		return new String(passwordField.getPassword());
	}

	public String getStatus() {
		return status;
	}

	public boolean isCheck() {
		String account = (String) textField.getText();
		String password = new String(passwordField.getPassword());
		boolean mark = true;
		if (null == account || "".equals(account)) {
			showMessage(userNamePanel, userNamePanel.getWidth(), 0, "请输入账号！");
			return mark = false;
		}
		if ("".equals(password)) {
			showMessage(passwordPanel, passwordPanel.getWidth(), 0, "请输入密码！");
			return mark = false;
		}
		return mark;
	}

	public void showWaiting(boolean show) {
		waitingPanel.setVisible(show);
		basePanel.setVisible(!show);
	}

	public void addAccount(String account) {
		textField.addItem(account);
	}

	public void setHeadIcon(ImageIcon imageIcon) {
		logoLabel.setIcon(imageIcon);
	}

	public void add(ComboBoxAction comboBoxAction) {
		textField.addComboBoxAction(comboBoxAction);
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				long time = System.currentTimeMillis();
				try {
					LoginFrame frame = new LoginFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
				System.out.println(System.currentTimeMillis() - time);
			}
		});
	}

}
