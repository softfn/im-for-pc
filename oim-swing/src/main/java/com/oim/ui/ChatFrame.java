package com.oim.ui;

import java.awt.EventQueue;

import javax.swing.UnsupportedLookAndFeelException;

import com.oim.ui.component.BaseFrame;

/**
 * @author XiaHui
 * @date 2015年1月5日 上午11:52:00
 */
public class ChatFrame extends BaseFrame {

	private static final long serialVersionUID = 1L;

	public ChatFrame() {
		initComponent();
	}

	private void initComponent() {

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
					ChatFrame frame = new ChatFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
