package com.oim.test.common.util;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.oim.ui.chat.ChatPanel;
import com.only.OnlyBorderFrame;
import com.only.OnlyButton;

/**
 * @author XiaHui
 * @date 2015年1月5日 上午11:52:00
 */
public class ChatPanelTest extends OnlyBorderFrame {

	private static final long serialVersionUID = 1L;

	private ChatPanel chatPanel = new ChatPanel();

	private OnlyButton button = new OnlyButton();

	/**
	 * Create the frame.
	 */
	public ChatPanelTest() {
		initComponent();
	}

	private void initComponent() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// this.setBounds(100, 100, 450, 300);
		this.setMinimumSize(new java.awt.Dimension(520, 500));
		this.setLocationRelativeTo(null);
		getContentPane().setLayout(null);

		button.setText("发送");

		add(chatPanel);
		add(button);

	}



	public void formComponentResized() {
		chatPanel.setBounds(3, 50, this.getWidth() - 6, this.getHeight() - 100);
		button.setBounds(this.getWidth() - 150, this.getHeight() - 40, 120, 25);
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
//			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//				if ("Nimbus".equals(info.getName())) {
//					javax.swing.UIManager.setLookAndFeel(info.getClassName());
//					break;
//				}
//			}
//			UIManager.setLookAndFeel("com.seaglasslookandfeel.SeaGlassLookAndFeel");
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
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
					ChatPanelTest frame = new ChatPanelTest();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
