package com.oim.ui.component;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.KeyEvent;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.oim.common.box.ImageBox;
import com.oim.common.component.OurScrollPane;
import com.only.OnlyButton;
import com.only.OnlyPopupMenu;
import com.only.util.OnlyImageUtil;
import com.only.util.OnlyUIUtil;

/**
 * 消息提示窗口
 * 
 * @author 夏辉
 * @date 2014年5月26日 上午1:45:48
 * @version 0.0.1
 */
public class PromptPopup extends OnlyPopupMenu {

	private static final long serialVersionUID = 1L;
	

	private JPanel basePanel = new JPanel();
	private JPanel titlePanel = new JPanel();
	
	private JLabel titleLabel = new JLabel();
	private OnlyButton closeButton = new OnlyButton();
	
	private JPanel mainPanel = new JPanel();
	private JLabel iconLabel = new JLabel();
	private OurScrollPane scrollPane = new OurScrollPane();
	private JTextArea textArea = new JTextArea();
	private JPanel bottomPanel = new JPanel();
	private OnlyButton confirmButton = new OnlyButton();

	public PromptPopup() {
		initComponents();
		initEvent();
	}

	private void initComponents() {
		this.setLayout(new java.awt.CardLayout());
		this.setSize(320,200);
		this.setPreferredSize(new Dimension(320,200));
		
		titlePanel.setOpaque(false);
		mainPanel.setOpaque(false);
		bottomPanel.setOpaque(false);
		
		titleLabel.setFont(OnlyUIUtil.getDefaultFont(0, 14)); // NOI18N
		
		closeButton.setPreferredSize(new Dimension(30, 22));
		closeButton.setNormalImage(null);
		closeButton.setRolloverImage(null);
		closeButton.setPressedImage(null);
		
		closeButton.setIcon(OnlyImageUtil.getImageIcon("Resources/Images/Default/Window/window_close_down.png"));
		closeButton.setPressedIcon(OnlyImageUtil.getImageIcon("Resources/Images/Default/Window/window_close_down.png"));
		closeButton.setSelectedIcon(OnlyImageUtil.getImageIcon("Resources/Images/Default/Window/window_close_hover.png"));
		
		closeButton.setToolTipText("关闭");
		closeButton.setText("");
		closeButton.setFocusable(false);
		closeButton.setFocusPainted(false);
		
		JPanel closePanel=new JPanel();
		closePanel.setOpaque(false);
		closePanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 0, 0));
		closePanel.add(closeButton);
		
		//titlePanel.setLayout(new java.awt.BorderLayout());
		titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));
		
		titlePanel.add(titleLabel, java.awt.BorderLayout.WEST);
		titlePanel.add(closePanel, java.awt.BorderLayout.EAST);
		
		basePanel.setBackground(new java.awt.Color(255, 255, 255));
		basePanel.setLayout(new java.awt.BorderLayout());
		
		basePanel.add(titlePanel, java.awt.BorderLayout.PAGE_START);
		basePanel.add(mainPanel, java.awt.BorderLayout.CENTER);
		basePanel.add(bottomPanel, java.awt.BorderLayout.PAGE_END);

		textArea.setEditable(false);
		textArea.setFont(OnlyUIUtil.getDefaultFont()); // NOI18N

		scrollPane.setHeaderVisible(false);
		scrollPane.setAlpha(0);
		scrollPane.setViewportView(textArea);

		ImageIcon imageIcon = ImageBox.getImageIcon("Resources/Images/Default/MessageBox/message_box_info.png");
		iconLabel.setIcon(imageIcon);

		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
		mainPanel.add(iconLabel);
		mainPanel.add(scrollPane);

		confirmButton.setText("确定");
		confirmButton.setPreferredSize(new Dimension(120,25));

		bottomPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 10, 5));
		bottomPanel.add(confirmButton);

		add(basePanel);
		
	}

	private void initEvent() {
		closeButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				setVisible(false);
			}
		});
		confirmButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				setVisible(false);
			}
		});
		confirmButton.addKeyListener(new java.awt.event.KeyAdapter() {
			@Override
			public void keyPressed(java.awt.event.KeyEvent evt) {
				if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
					setVisible(false);
				}
			}
		});
	}

	@Override
	public void show(Component invoker, int x, int y) {
		super.show(invoker, x, y);
	}


	public void setText(String text) {
		textArea.setText(text);
	}

	public void setTitle(String title) {
		titleLabel.setText(title);
	}
}
