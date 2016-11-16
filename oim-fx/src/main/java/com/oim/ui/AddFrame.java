/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * AddFriends.java
 *
 * Created on 2010-12-29, 22:24:15
 */
package com.oim.ui;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.MouseListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;

import com.oim.common.component.OurLabel;
import com.oim.swing.common.box.HeadImageIconBox;
import com.oim.swing.common.box.ImageBox;
import com.oim.swing.common.util.ImageUtil;
import com.oim.ui.component.BaseFrame;
import com.only.OnlyBorderButton;
import com.only.OnlyButton;
import com.only.OnlyComboBox;
import com.only.OnlyLabel;
import com.only.OnlyTextField;

/**
 * 
 * @author XiaHui
 * @date 2015年3月8日 上午12:02:51
 * @version 0.0.1
 */
public class AddFrame extends BaseFrame {

	private static final long serialVersionUID = 1L;

	private OnlyLabel titleLabel = new OnlyLabel();
	private JPanel topPanel = new JPanel();
	private JPanel basePanel = new JPanel();

	private OnlyLabel headLabel = new OnlyLabel();

	private OnlyLabel categoryLabel = new OnlyLabel();
	private OnlyLabel remarksLabel = new OnlyLabel();
	private OnlyTextField remarksTextField = new OnlyTextField();
	private OnlyLabel nameLabel = new OnlyLabel();
	private OnlyLabel textLabel = new OnlyLabel();
	private OnlyComboBox<Object> categoryComboBox = new OnlyComboBox<Object>();
	private OnlyBorderButton addCategoryButton = new OnlyBorderButton();

	private OurLabel iconLabel = new OurLabel();

	private OnlyButton agreeButton = new OnlyButton();
	private OnlyButton cancelButton = new OnlyButton();

	public AddFrame() {
		initComponents();
		initEvent();
	}

	private void initComponents() {
		this.setSize(320, 250);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setLayout(null);
		this.setBorderPainted(false);
		this.setShowTitle(false);
		this.setShowIconImage(false);

		topPanel.setLayout(null);
		topPanel.setBackground(new Color(70, 190, 200));

		titleLabel.setFont(new java.awt.Font("微软雅黑", 1, 14)); // NOI18N

		basePanel.setLayout(null);
		basePanel.setBackground(new Color(255, 255, 255));

		//addCategoryButton.setText("新建分组");

		addCategoryButton.setNormalImage(new ImageIcon("Resources/Images/Default/Common/panel.png").getImage());

		//		addCategoryButton.setBorder(null);
		//		addCategoryButton.setContentAreaFilled(false);
		addCategoryButton.setFocusPainted(false);
		//		addCategoryButton.setDoubleBuffered(true);
		//
		//		addCategoryButton.setOpaque(false);
		//		addCategoryButton.setFocusable(false);
		//		addCategoryButton.setActionCommand(null);

		titleLabel.setText("添加好友");
		remarksLabel.setText("备注：");
		categoryLabel.setText("分组：");

		agreeButton.setText("确定");
		cancelButton.setText("取消");

		addCategoryButton.setIcon(ImageUtil.getIcon("Resources/Images/Default/FindFrame/add_normal_25.png"));
		addCategoryButton.setSelectedIcon(ImageUtil.getIcon("Resources/Images/Default/FindFrame/add_hover_25.png"));

		iconLabel.setVisible(false);
		iconLabel.setIcon(ImageBox.getImageIcon("Resources/Images/Default/Loading/loading_140_9.gif"));
		iconLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

		topPanel.setBounds(0, 0, 320, 50);
		titleLabel.setBounds(20, 5, 120, 25);
		basePanel.setBounds(0, 50, 320, 200);

		headLabel.setBounds(60, 10, 60, 60);
		nameLabel.setBounds(130, 15, 120, 25);
		textLabel.setBounds(130, 35, 120, 25);
		remarksLabel.setBounds(50, 80, 40, 25);
		remarksTextField.setBounds(90, 80, 150, 25);
		categoryLabel.setBounds(50, 115, 40, 25);
		categoryComboBox.setBounds(90, 115, 150, 25);
		addCategoryButton.setBounds(250, 115, 25, 25);
		agreeButton.setBounds(20, 155, 100, 30);
		cancelButton.setBounds(200, 155, 100, 30);

		iconLabel.setBounds(20, 155, 280, 30);

		topPanel.add(titleLabel);

		basePanel.add(headLabel);
		basePanel.add(nameLabel);
		basePanel.add(textLabel);
		basePanel.add(remarksLabel);
		basePanel.add(remarksTextField);
		basePanel.add(categoryLabel);
		basePanel.add(categoryComboBox);
		basePanel.add(addCategoryButton);
		basePanel.add(agreeButton);
		basePanel.add(cancelButton);
		basePanel.add(iconLabel);

		add(topPanel);
		add(basePanel);

	}

	private void initEvent() {
		cancelButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				cancelActionPerformed(evt);
			}
		});
	}

	public void addCancelAction(ActionListener l) {
		cancelButton.addActionListener(l);
	}

	public void addDoneAction(ActionListener l) {
		agreeButton.addActionListener(l);
	}

	public void addComboBoxItemListener(ItemListener i) {
		categoryComboBox.addItemListener(i);
	}

	public void addCategoryMouseListener(MouseListener l) {
		addCategoryButton.addMouseListener(l);
	}

	public void setTitleText(String text) {
		titleLabel.setText(text);
	}

	@SuppressWarnings("unchecked")
	public <E> void setComboBoxRenderer(ListCellRenderer<? super E> renderer) {
		categoryComboBox.setRenderer(renderer);
	}

	private void cancelActionPerformed(java.awt.event.ActionEvent evt) {
		this.setVisible(false);
	}

	@SuppressWarnings("unchecked")
	public void addItem(Object o) {
		categoryComboBox.addItem(o);
		categoryComboBox.setSelectedItem(o);
	}

	public void showWaiting(boolean show) {
		agreeButton.setVisible(!show);
		cancelButton.setVisible(!show);
		iconLabel.setVisible(show);
	}

	public void removeAllItems() {
		categoryComboBox.removeAllItems();
	}

	public void setShowIcon(Icon icon) {
		headLabel.setIcon(icon);

	}

	public void setShowName(String name) {
		nameLabel.setText(name);
	}

	public void setShowText(String text) {
		textLabel.setText(text);
	}

	public Object getSelectedItem() {
		return this.categoryComboBox.getSelectedItem();
	}

	public String getRemark() {
		return this.remarksTextField.getText();
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
		try {
			UIManager.setLookAndFeel("com.seaglasslookandfeel.SeaGlassLookAndFeel");
			//			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		java.awt.EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				AddFrame frame = new AddFrame();
				frame.setVisible(true);
				frame.setShowIcon(HeadImageIconBox.getUserHeadImageIcon("1", 60, 60));
				frame.setShowText("hhhhhdddddddddddddddddddddddddddddddddddah");
				frame.setShowName("hhhhhddddddddddddddddddddddddddddddddah");
			}
		});
	}
}
