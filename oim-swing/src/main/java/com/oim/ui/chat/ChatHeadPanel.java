package com.oim.ui.chat;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.only.util.OnlyUIUtil;

/**
 * 描述：
 * 
 * @author XiaHui
 * @date 2015年3月13日 下午11:15:31
 * @version 0.0.1
 */
public class ChatHeadPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel headLabel = new JLabel();

	private JLabel nameLabel = new JLabel();
	private JLabel textLabel = new JLabel();

	public ChatHeadPanel() {
		initComponents();
	}

	private void initComponents() {
		this.setOpaque(false);
		this.setLayout(new java.awt.BorderLayout());

		JPanel headPanel = new JPanel();
		headPanel.setOpaque(false);
		headPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 8, 8));
		headPanel.add(headLabel);

		JPanel headRootPanel = new JPanel();
		headRootPanel.setOpaque(false);
		headRootPanel.setLayout(new java.awt.GridBagLayout());
		headRootPanel.add(headPanel);

		nameLabel.setFont(OnlyUIUtil.getDefaultFont());
		textLabel.setFont(OnlyUIUtil.getDefaultFont());
		
		JPanel nameTempPanel = new JPanel();
		nameTempPanel.setOpaque(false);
		nameTempPanel.setLayout(new java.awt.BorderLayout());
		nameTempPanel.add(nameLabel, java.awt.BorderLayout.PAGE_END);
		
		JPanel textTempPanel = new JPanel();
		textTempPanel.setOpaque(false);
		textTempPanel.setLayout(new java.awt.BorderLayout());
		textTempPanel.add(textLabel, java.awt.BorderLayout.PAGE_START);
		
		JPanel textPanel = new JPanel();
		textPanel.setOpaque(false);
		textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
//		JLabel tempLabel=	new JLabel(" ");
//		tempLabel.setPreferredSize(new Dimension(120,40));
//		textPanel.add(tempLabel);
		textPanel.add(nameTempPanel);
		textPanel.add(textTempPanel);

		
		add(headRootPanel, java.awt.BorderLayout.WEST);
		add(textPanel, java.awt.BorderLayout.CENTER);
	}

	public void setText(String text) {
		textLabel.setText(text);
	}

	public void setName(String text) {
		nameLabel.setText(text);
	}

	public void setIcon(Icon icon) {
		headLabel.setIcon(icon);
	}

}
