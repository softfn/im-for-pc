package com.oim.test.ui.chat;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.oim.common.util.ImageUtil;
import com.oim.ui.chat.ChatPanel;
import com.oim.ui.chat.GroupUserList;
import com.oim.ui.component.list.ItemPanel;
import com.only.OnlyBorderFrame;

/**
 * @author XiaHui
 * @date 2015年1月5日 上午11:52:00
 */
public class GroupChatPanelTest extends OnlyBorderFrame {

	private static final long serialVersionUID = 1L;

	private ChatPanel baseChatPanel = new ChatPanel();

	/**
	 * Create the frame.
	 */
	public GroupChatPanelTest() {
		initComponent();
	}

	private void initComponent() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// this.setBounds(100, 100, 450, 300);
		this.setMinimumSize(new java.awt.Dimension(520, 500));
		this.setLocationRelativeTo(null);
		getContentPane().setLayout(null);

		add(baseChatPanel);
		

		GroupUserList groupUserList=new GroupUserList();
		for (int i = 0; i < 13; i++) {
			ItemPanel item = new ItemPanel();
			item.setText("jjjjjjjjkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkjas");
			item.setIcon(ImageUtil.getRoundedCornerImageIcon("Resources/Images/Head/User/" + (i + 1) + ".png", 20, 20, 20, 20));
			groupUserList.add(item);
		}

		baseChatPanel.setUserListPanel(groupUserList);
	}



	public void formComponentResized() {
		baseChatPanel.setBounds(3, 50, this.getWidth() - 6, this.getHeight() - 100);
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
			UIManager.setLookAndFeel("com.seaglasslookandfeel.SeaGlassLookAndFeel");
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
					GroupChatPanelTest frame = new GroupChatPanelTest();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
