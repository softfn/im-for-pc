package com.oim.test.ui.chat;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.UnsupportedLookAndFeelException;

import com.oim.ui.chat.ScreenShotAction;
import com.oim.ui.chat.ScreenShotWindow;
import com.only.OnlyBorderFrame;

/**
 * 描述：
 * @author XiaHui 
 * @date 2016年1月24日 下午4:00:12
 * @version 0.0.1
 */
public class ScreenShotWindowTest extends OnlyBorderFrame {

	private static final long serialVersionUID = 1L;

	private JButton grayButton = new JButton();
	ScreenShotWindow ssw=ScreenShotWindow.getScreenShotWindow();
	
	public ScreenShotWindowTest() {
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
		ssw.setAction(new ScreenShotAction() {
			
			@Override
			public void saveImage(BufferedImage image) {
				if(null!=image){
					ScreenShotWindowTest.this.setBackgroundImage(image);
				}
			}
		});
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
		ssw.setVisible(true);
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
					ScreenShotWindowTest frame = new ScreenShotWindowTest();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
