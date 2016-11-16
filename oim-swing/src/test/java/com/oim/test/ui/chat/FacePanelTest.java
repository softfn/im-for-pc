package com.oim.test.ui.chat;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.UnsupportedLookAndFeelException;

import com.oim.ui.chat.FacePanel;
import com.only.OnlyBorderFrame;

/**
 * @author XiaHui
 * @date 2015年1月5日 上午11:52:00
 */
public class FacePanelTest extends OnlyBorderFrame {

	private static final long serialVersionUID = 1L;

	FacePanel facePanel=FacePanel.getFacePanel();
	private JButton grayButton = new JButton();
	
	public FacePanelTest() {
		initComponent();
		initEvent();
	}

	private void initComponent() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(100, 100, 450, 300);
		this.setLocationRelativeTo(null);
		this.getContentPane().setLayout(null);
		grayButton.setText("变灰");
		
		grayButton.setBounds(20, 50, 120, 25);
		
		grayButton.setIcon(new ImageIcon("Resources/Images/Default/Status/FLAG/Big/MobilePhoneQQOn.png"));
		grayButton.setSelectedIcon(new ImageIcon("Resources/Images/Face/1.gif "));
		
		add(grayButton);
	}

	private void initEvent() {
		grayButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				gray();
			}
		});
	}
	private void gray(){
		facePanel.show(grayButton, -240, -240);
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
					FacePanelTest frame = new FacePanelTest();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
