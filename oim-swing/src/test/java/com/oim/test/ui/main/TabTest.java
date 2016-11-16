package com.oim.test.ui.main;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UnsupportedLookAndFeelException;

import com.oim.ui.main.Tab;
import com.only.OnlyBorderFrame;

/**
 * @author XiaHui
 * @date 2015年1月5日 上午11:52:00
 */
public class TabTest extends OnlyBorderFrame {

	private static final long serialVersionUID = 1L;

	private JPanel chatPanel = new JPanel();
	private JButton grayButton = new JButton();
	private JButton drawBorderButton = new JButton();
//	private OnlyLabel head=new OnlyLabel();
	private Tab head=new Tab();
	/**
	 * Create the frame.
	 */
	public TabTest() {
		initComponent();
		initEvent();
	}

	private void initComponent() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setMinimumSize(new java.awt.Dimension(520, 500));
		this.setLocationRelativeTo(null);
		this.setLayout(null);

		chatPanel.setLayout(null);
		
		grayButton.setText("变灰");
		drawBorderButton.setText("边框");
		
		grayButton.setBounds(20, 50, 120, 25);
		drawBorderButton.setBounds(20, 85, 120, 25);
		
		head.setBounds(20, 120, 160, 50);
		
		
		head.setNormalIcon(new ImageIcon("Resources/Images/Default/MainPanel/icon_contacts_normal.png"));
		head.setSelectedIcon(new ImageIcon("Resources/Images/Default/MainPanel/icon_contacts_selected.png"));
		head.setRolloverIcon(new ImageIcon("Resources/Images/Default/MainPanel/icon_contacts_hover.png"));
		
		chatPanel.add(grayButton);
		chatPanel.add(drawBorderButton);
		chatPanel.add(head);
		
		add(chatPanel);

	}

	private void initEvent() {
		grayButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				gray();
			}
		});
		drawBorderButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				drawBorder();
			}
		});
	}
	
	private void gray(){
//		head.setEnabled(!head.isEnabled());
		head.setSelected(!head.isSelected());;
	}
	private void drawBorder(){
		//head.setb
		//head.setDrawBorder(!head.isDrawBorder());
	}
	

	public void formComponentResized() {
		chatPanel.setBounds(3, 50, this.getWidth() - 6, this.getHeight() - 100);
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
					TabTest frame = new TabTest();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
