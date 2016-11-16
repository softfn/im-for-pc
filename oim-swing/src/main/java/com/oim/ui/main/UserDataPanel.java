package com.oim.ui.main;

import java.awt.CardLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.oim.bean.User;
import com.oim.common.util.ImageUtil;
import com.oim.net.message.data.UserData;
import com.oim.ui.component.StatusLabel;
import com.oim.ui.component.event.ExecuteAction;
import com.only.OnlyMenuItem;
import com.only.OnlyPanel;
import com.only.OnlyPopupMenu;
import com.over.image.OverDecoratedImage;

/**
 * @author XiaHui
 * @date 2015年3月2日 下午2:57:41
 */
public class UserDataPanel extends OnlyPanel {

	private static final long serialVersionUID = 1L;

	private JPanel rootPanel = new JPanel();
	private JPanel baseHeadPanel = new JPanel();
	private JPanel baseTextPanel = new JPanel();

	private JPanel headPanel = new JPanel();
	//private JLabel headLabel = new JLabel();
	private OverDecoratedImage headLabel=new OverDecoratedImage();
	private JPanel dataPanel = new JPanel();
	private JPanel textPanel = new JPanel();
	// private JPanel baseTextPanel = new JPanel();

	private JLabel nameLabel = new JLabel();
	private JLabel textLabel = new JLabel();

	private StatusLabel statusButton = new StatusLabel();

	private OnlyPopupMenu statusPopupMenu = new OnlyPopupMenu();

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
	
	public UserDataPanel() {
		initComponent();
		initMenu();
		initEvent();
	}

	private void initComponent() {
		this.setOpaque(false);
		// this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setLayout(new java.awt.BorderLayout());

		rootPanel.setLayout(new java.awt.BorderLayout());
		rootPanel.add(baseHeadPanel, java.awt.BorderLayout.WEST);
		rootPanel.add(baseTextPanel, java.awt.BorderLayout.CENTER);

		baseHeadPanel.setLayout(new java.awt.GridBagLayout());
		baseHeadPanel.add(headPanel);

		headPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 2));
		headPanel.add(headLabel);

		Icon dataIcon = ImageUtil.getEmptyIcon(5, 25);

		dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.X_AXIS));
		// dataPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT,
		// 2, 2));
		dataPanel.add(new JLabel(dataIcon));
		dataPanel.add(statusButton);
		dataPanel.add(new JLabel(dataIcon));
		dataPanel.add(nameLabel);

		textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.X_AXIS));
		// textPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT,
		// 0, 0));
		textPanel.add(new JLabel(dataIcon));
		textPanel.add(textLabel);

		JPanel textTempPanel = new JPanel();
		textTempPanel.setOpaque(false);
		textTempPanel.setLayout(new CardLayout());
		textTempPanel.add(textPanel);

		JPanel dataTempPanel = new JPanel();
		dataTempPanel.setOpaque(false);
		dataTempPanel.setLayout(new CardLayout());
		dataTempPanel.add(dataPanel);

		JPanel dataTopPanel = new JPanel();
		dataTopPanel.setOpaque(false);
		dataTopPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 5));
		//dataTopPanel.add(new JLabel(textIcon));

		JPanel baseDataPanel = new JPanel();
		baseDataPanel.setOpaque(false);
		baseDataPanel.setLayout(new java.awt.BorderLayout());
		baseDataPanel.add(dataTopPanel, java.awt.BorderLayout.CENTER);
		baseDataPanel.add(dataTempPanel, java.awt.BorderLayout.PAGE_END);

		baseTextPanel.setLayout(new BoxLayout(baseTextPanel, BoxLayout.Y_AXIS));
		baseTextPanel.add(baseDataPanel);
		baseTextPanel.add(textTempPanel);

		Icon icon = ImageUtil.getEmptyIcon(10, 60);
		this.add(new JLabel(icon), java.awt.BorderLayout.WEST);
		this.add(rootPanel, java.awt.BorderLayout.CENTER);
		this.add(new JLabel(icon), java.awt.BorderLayout.EAST);

		rootPanel.setOpaque(false);
		baseHeadPanel.setOpaque(false);

		baseTextPanel.setOpaque(false);
		headPanel.setOpaque(false);
		dataPanel.setOpaque(false);
		textPanel.setOpaque(false);

		// statusButton.setText("fff");
		statusButton.setIcon(onlineImageIcon);
		// statusButton.setIcon(ImageUtil.getImageIcon("Resources/Images/Default/Status/MainPanel_FolderNode_expandTexture.png"));

		nameLabel.setFont(new java.awt.Font("微软雅黑", 1, 14));
		textLabel.setFont(new java.awt.Font("微软雅黑", 0, 12));
		
		headLabel.setRound(80);
		headLabel.setDrawGlassLayer(false);
		headLabel.setDrawBorder(true);
	}

	private void initEvent() {
		statusButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				statusPopupMenu.show(statusButton, 0, statusButton.getHeight());
			}
		});
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
		

		statusPopupMenu.add(onlineMenuItem);
		statusPopupMenu.add(omeMenuItem);
		statusPopupMenu.add(separator1);
		statusPopupMenu.add(awayMenuItem);
		statusPopupMenu.add(busyMenuItem);
		statusPopupMenu.add(muteMenuItem);
		statusPopupMenu.add(separator2);
		statusPopupMenu.add(invisibleMenuItem);
		statusPopupMenu.add(separator3);
		statusPopupMenu.add(offlineMenuItem);

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
		updateStatus(UserData.status_online);
	}

	private void omeMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
		updateStatus(UserData.status_call_me);
	}

	private void awayMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
		updateStatus(UserData.status_away);
	}

	private void busyMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
		updateStatus(UserData.status_busy);
	}

	private void muteMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
		updateStatus(UserData.status_mute);
	}

	private void invisibleMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
		updateStatus(UserData.status_invisible);
	}

	private void offlineMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
		updateStatus(UserData.status_offline);
	}
	private void updateStatus(String status){
		if(null!=statusAction){
			statusAction.execute(status);
		}
	}

	public void changeStatus(String status) {
		switch (status) {
		case User.status_online:
			statusButton.setIcon(onlineImageIcon);
			break;
		case User.status_call_me:
			statusButton.setIcon(omeImageIcon);
			break;
		case User.status_away:
			statusButton.setIcon(awayImageIcon);
			break;
		case User.status_busy:
			statusButton.setIcon(busyImageIcon);
			break;
		case User.status_mute:
			statusButton.setIcon(muteImageIcon);
			break;
		case User.status_invisible:
			statusButton.setIcon(invisibleImageIcon);
			break;
		case User.status_offline:
			statusButton.setIcon(offlineImageIcon);
			break;
		default:
			statusButton.setIcon(offlineImageIcon);
			break;
		}
	}

	public void setStatusIcon(Icon icon) {
		statusButton.setIcon(icon);
	}

	public void setHeadIcon(ImageIcon icon) {
		headLabel.setIcon(icon);
		//headLabel.setIcon(icon);
	}

	public void setName(String name) {
		nameLabel.setText(name);
	}

	public void setText(String text) {
		textLabel.setText(text);
	}

	public ExecuteAction getStatusAction() {
		return statusAction;
	}

	public void setStatusAction(ExecuteAction statusAction) {
		this.statusAction = statusAction;
	}
	
	
}
