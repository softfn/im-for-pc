package com.oim.test.ui;

import java.awt.CardLayout;
import java.awt.EventQueue;

import javax.swing.UnsupportedLookAndFeelException;

import com.oim.ui.component.BaseFrame;
import com.oim.ui.user.UserDataShowPanel;

/**
 * @author XiaHui
 * @date 2015年1月5日 上午11:52:00
 */
public class UserDataShowFrameTest extends BaseFrame {

	private static final long serialVersionUID = 1L;

	private UserDataShowPanel basePanel = new UserDataShowPanel();

	public UserDataShowFrameTest() {
		initComponent();
	}

	private void initComponent() {
		this.setMinimumSize(new java.awt.Dimension(400, 520));
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setLayout(new CardLayout());
		this.setShowTitle(false);
		this.setShowIconImage(false);

		add(basePanel);
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
					UserDataShowFrameTest frame = new UserDataShowFrameTest();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
