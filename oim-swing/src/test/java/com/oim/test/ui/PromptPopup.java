package com.oim.test.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.KeyEvent;

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
	private int width = 350;
	private int height = 180;
	private ImageIcon icon = ImageBox.getEmptyImageIcon(width, height);
	private JLabel baseLabel = new JLabel();
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
	private ImageIcon closeNormal;
	private ImageIcon closeHighlight;
	private ImageIcon closeDown;

	public PromptPopup() {
		initComponents();
		initEvent();
	}

	private void initComponents() {
		this.setLayout(new java.awt.CardLayout());
		this.setSize(width, height);

		titlePanel.setOpaque(false);
		mainPanel.setOpaque(false);
		bottomPanel.setOpaque(false);

		basePanel.setLayout(null);
		titlePanel.setLayout(null);
		mainPanel.setLayout(null);
		bottomPanel.setLayout(null);

		titleLabel.setFont(OnlyUIUtil.getDefaultFont(1, 14)); // NOI18N

		this.closeNormal = OnlyImageUtil.getImageIcon("Resources\\Images\\Default\\Window\\window_close_down.png");
		this.closeHighlight = OnlyImageUtil.getImageIcon("Resources\\Images\\Default\\Window\\window_close_hover.png");
		this.closeDown = OnlyImageUtil.getImageIcon("Resources\\Images\\Default\\Window\\window_close_down.png");

		closeButton.setPreferredSize(new Dimension(30, 22));
		closeButton.setNormalImage(closeNormal.getImage());
		closeButton.setRolloverImage(closeHighlight.getImage());
		closeButton.setPressedImage(closeDown.getImage());
		closeButton.setToolTipText("关闭");
		closeButton.setText("");
		closeButton.setFocusable(false);
		closeButton.setFocusPainted(false);

		titlePanel.add(titleLabel);
		titlePanel.add(closeButton);

		textArea.setEditable(false);
		textArea.setFont(OnlyUIUtil.getDefaultFont()); // NOI18N

		scrollPane.setHeaderVisible(false);
		scrollPane.setAlpha(0);
		scrollPane.setViewportView(textArea);

		ImageIcon imageIcon = ImageBox.getImageIcon("Resources\\Images\\Default\\MessageBox\\message_box_info.png");
		iconLabel.setIcon(imageIcon);

		mainPanel.add(iconLabel);
		mainPanel.add(scrollPane);

		confirmButton.setText("确定");

		bottomPanel.add(confirmButton);

		basePanel.setBackground(new java.awt.Color(255, 255, 255));

		basePanel.add(titlePanel);
		basePanel.add(mainPanel);
		basePanel.add(bottomPanel);

		baseLabel.setIcon(icon);
		baseLabel.setLayout(new java.awt.CardLayout());
		baseLabel.add(basePanel);

		add(baseLabel);
		formComponentResized();
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
		confirmButton.requestFocus();

	}

	public void formComponentResized() {
		titlePanel.setBounds(0, 0, 350, 30);
		mainPanel.setBounds(0, 25, 350, 120);
		bottomPanel.setBounds(0, 150, 350, 30);

		titleLabel.setBounds(10, 0, 300, 25);
		closeButton.setBounds(320, -2, 35, 20);
		iconLabel.setBounds(20, 30, 60, 60);
		scrollPane.setBounds(80, 0, 250, 120);
		confirmButton.setBounds(250, 0, 80, 25);
	}

	public void setText(String text) {
		textArea.setText(text);
	}

	public void setTitle(String title) {
		titleLabel.setText(title);
	}
}
