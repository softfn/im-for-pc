package com.oim.ui.user;

import java.awt.CardLayout;
import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.oim.common.component.AlphaPanel;
import com.oim.swing.common.util.ImageUtil;
import com.oim.ui.component.BasePanel;
import com.only.OnlyLabel;
import com.only.OnlyPanel;
import com.only.layout.ListLayout;
import com.over.image.OverDecoratedImage;

/**
 * 描述：
 * 
 * @author XiaHui
 * @date 2016年1月29日 下午6:09:22
 * @version 0.0.1
 */
public class UserDataShowPanel extends BasePanel {

	private static final long serialVersionUID = 1L;

	private UserBaseDataPanel userDataPanel = new UserBaseDataPanel();
	private JLabel accountLabel = getLabel("账   号：");
	private JLabel nicknameLabel = getLabel("昵   称：");
	private JLabel remarkLabel = getLabel("备   注：");
	private JLabel personalLabel = getLabel("个   人：");
	private JLabel addressLabel = getLabel("所在地：");
	
	private JTextField accountField=getTextField();
	private JTextField nicknameField=getTextField();
	private JTextField remarkField=getTextField();
	private JTextField genderField=getTextField();
	private JTextField birthdateField=getTextField();
	private JTextField constellationField=getTextField();
	private JTextField bloodField=getTextField();
	private JTextField addressField=getTextField();
	
	public UserDataShowPanel() {
		initUI();
		initTestData();
	}

	private void initUI(){
		this.setLayout(new java.awt.BorderLayout());
		this.setOpaque(false);
		
		AlphaPanel centerPanel=new AlphaPanel();
		centerPanel.setBackgroundColor(new Color(255, 255, 255, 180));
		centerPanel.setOpaque(false);
		centerPanel.setLayout(new ListLayout(ListLayout.Y_AXIS, 30, 2, ListLayout.PREFER_SIZE));
		//centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
		
		JPanel accountPanel=new JPanel();
		accountPanel.setOpaque(false);
		accountPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
		accountPanel.add(accountLabel);
		accountPanel.add(accountField);
		centerPanel.add(accountPanel);
		
		
		JPanel nicknamePanel=new JPanel();
		nicknamePanel.setOpaque(false);
		nicknamePanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
		nicknamePanel.add(nicknameLabel);
		nicknamePanel.add(nicknameField);
		centerPanel.add(nicknamePanel);
		
		JPanel remarkPanel=new JPanel();
		remarkPanel.setOpaque(false);
		remarkPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
		remarkPanel.add(remarkLabel);
		remarkPanel.add(remarkField);
		centerPanel.add(remarkPanel);
		
		
		JPanel personalPanel=new JPanel();
		personalPanel.setOpaque(false);
		personalPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
		personalPanel.add(personalLabel);
		personalPanel.add(genderField);
		personalPanel.add(birthdateField);
		personalPanel.add(constellationField);
		personalPanel.add(bloodField);
		centerPanel.add(personalPanel);
		
		
		JPanel addressPanel=new JPanel();
		addressPanel.setOpaque(false);
		addressPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
		addressPanel.add(addressLabel);
		addressPanel.add(addressField);
		centerPanel.add(addressPanel);
		
		this.add(userDataPanel, java.awt.BorderLayout.PAGE_START);
		this.add(centerPanel, java.awt.BorderLayout.CENTER);
		//this.add(new JPanel(), java.awt.BorderLayout.PAGE_END);
	}
	
	private JLabel getLabel(String text) {
		JLabel label = new OnlyLabel(text);
		//label.setForeground(Color.white);
		return label;
	}

	private JTextField getTextField() {
		JTextField text = new JTextField();
		text.setOpaque(false);
		text.setEditable(false);
		text.setBorder(null);
		return text;
	}
	
	private void initTestData(){
		ImageIcon headIcon = ImageUtil.getRoundedCornerImageIcon("Resources/Images/Head/User/91_100.gif", 60, 60, 60, 60);
		userDataPanel.setHeadIcon(headIcon);
		userDataPanel.setName("wo擦哪件的fan华！77777777656！！");
		userDataPanel.setText("这是什么情况呀呀。。。。。。。。。");
		
		nicknameField.setText("jjjjjjjjjjs");
		genderField.setText("jjjjjjjjjjs");
		birthdateField.setText("jjjjjjjjjjs");
		constellationField.setText("jjjjjjjjjjs");
		bloodField.setText("jjjjjjjjjjs");
	}
	
	
	class UserBaseDataPanel extends OnlyPanel {

		private static final long serialVersionUID = 1L;

		private JPanel rootPanel = new JPanel();
		private JPanel baseHeadPanel = new JPanel();
		private JPanel baseTextPanel = new JPanel();

		private JPanel headPanel = new JPanel();
		private OverDecoratedImage headLabel=new OverDecoratedImage();
		private JPanel dataPanel = new JPanel();
		private JPanel textPanel = new JPanel();

		private JLabel nameLabel = new JLabel();
		private JLabel textLabel = new JLabel();

		

		public UserBaseDataPanel() {
			initComponent();
		}

		private void initComponent() {
			this.setOpaque(false);
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
			dataPanel.add(new JLabel(dataIcon));
			dataPanel.add(new JLabel(dataIcon));
			dataPanel.add(nameLabel);

			textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.X_AXIS));
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


			nameLabel.setFont(new java.awt.Font("微软雅黑", 1, 14));
			textLabel.setFont(new java.awt.Font("微软雅黑", 0, 12));
			
			headLabel.setRound(80);
			headLabel.setDrawGlassLayer(false);
			headLabel.setDrawBorder(true);
		}

		public void setHeadIcon(ImageIcon icon) {
			headLabel.setIcon(icon);
		}

		public void setName(String name) {
			nameLabel.setText(name);
		}

		public void setText(String text) {
			textLabel.setText(text);
		}
	}
}
