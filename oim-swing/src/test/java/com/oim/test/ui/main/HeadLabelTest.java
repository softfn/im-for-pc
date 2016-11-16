package com.oim.test.ui.main;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UnsupportedLookAndFeelException;

import com.oim.ui.component.list.HeadLabel;
import com.only.OnlyBorderFrame;

/**
 * @author XiaHui
 * @date 2015年1月5日 上午11:52:00
 */
public class HeadLabelTest extends OnlyBorderFrame {

	private static final long serialVersionUID = 1L;

	private JPanel chatPanel = new JPanel();
	ImageIcon icon = new ImageIcon("Resources/Images/List/big.png");
	/**
	 * Create the frame.
	 */
	public HeadLabelTest() {
		initComponent();
		initEvent();
	}

	private void initComponent() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setMinimumSize(new java.awt.Dimension(520, 500));
		this.setLocationRelativeTo(null);
		this.setLayout(null);
		
		chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
		chatPanel.setBackground(Color.blue);
		
		
		HeadLabel head=new HeadLabel();
		head.setIcon(icon);
		
		head.setStatusIcon(new ImageIcon("Resources/Images/Default/Status/FLAG/Big/MobilePhoneQQOn.png"));

		head.setHeadIcon(new ImageIcon("Resources/Images/Head/User/1.png"));
		
		head.setRemark("女神经");
		head.setNickname("(也的JJ刚刚)");
		head.setStatus("[2G]");
		head.setShowText("哈哈哈，又有新闻了");
		head.addBusinessAttribute("s",new JLabel(new ImageIcon("Resources/Images/Default/Status/FLAG/Big/imonline.png")));

		
		HeadLabel h2=new HeadLabel();
		h2.setIcon(icon);
		
		h2.setHeadIcon(new ImageIcon("Resources/Images/Head/User/12.png"));
		
		h2.setRemark("女神经");
		h2.setNickname("(也的JJ刚刚)");
		h2.setStatus("[2G]");
		h2.setShowText("哈哈哈，又有新闻了");
		//h2.addBusinessAttribute(new JLabel(new ImageIcon("Resources/Images/Default/Status/FLAG/Big/imonline.png")));

		
		chatPanel.add(head);
		chatPanel.add(h2);
		
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
					HeadLabelTest frame = new HeadLabelTest();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
