package com.oim.test.ui.main;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UnsupportedLookAndFeelException;

import com.oim.ui.main.TabPanel;
import com.only.OnlyBorderFrame;

/**
 * @author XiaHui
 * @date 2015年1月5日 上午11:52:00
 */
public class TabPanelTest extends OnlyBorderFrame {

	private static final long serialVersionUID = 1L;

	private TabPanel chatPanel = new TabPanel();

	/**
	 * Create the frame.
	 */
	public TabPanelTest() {
		initComponent();
		initEvent();
	}

	private void initComponent() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setMinimumSize(new java.awt.Dimension(520, 500));
		this.setLocationRelativeTo(null);
		this.setLayout(null);
		JPanel b = new JPanel();
		JPanel g = new JPanel();
		JPanel r = new JPanel();

		b.setBackground(Color.blue);
		g.setBackground(Color.green);
		r.setBackground(Color.red);

		chatPanel.add(new ImageIcon("Resources/Images/Default/MainPanel/icon_contacts_normal.png"), new ImageIcon("Resources/Images/Default/MainPanel/icon_contacts_hover.png"), new ImageIcon("Resources/Images/Default/MainPanel/icon_contacts_selected.png"), b);
		chatPanel.add(new ImageIcon("Resources/Images/Default/MainPanel/icon_contacts_normal.png"), new ImageIcon("Resources/Images/Default/MainPanel/icon_contacts_hover.png"), new ImageIcon("Resources/Images/Default/MainPanel/icon_contacts_selected.png"), g);
		chatPanel.add(new ImageIcon("Resources/Images/Default/MainPanel/icon_contacts_normal.png"), new ImageIcon("Resources/Images/Default/MainPanel/icon_contacts_hover.png"), new ImageIcon("Resources/Images/Default/MainPanel/icon_contacts_selected.png"), r);

		add(chatPanel);

	}

	private void initEvent() {
		
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
					TabPanelTest frame = new TabPanelTest();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
