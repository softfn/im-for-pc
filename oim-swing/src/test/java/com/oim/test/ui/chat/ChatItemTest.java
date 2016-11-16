package com.oim.test.ui.chat;

import java.awt.CardLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UnsupportedLookAndFeelException;

import com.oim.ui.chat.ChatItem;
import com.only.OnlyBorderFrame;
import com.only.OnlyScrollPane;
import com.only.layout.ListLayout;

/**
 * @author XiaHui
 * @date 2015年1月5日 上午11:52:00
 */
public class ChatItemTest extends OnlyBorderFrame {

	private static final long serialVersionUID = 1L;

	private JPanel chatPanel = new JPanel();
	private JPanel rootPanel = new JPanel();
	private OnlyScrollPane scrollPane = new OnlyScrollPane();

	/**
	 * Create the frame.
	 */
	public ChatItemTest() {
		initComponent();
		initData();
	}

	private void initComponent() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(100, 100, 450, 300);
		// this.setMinimumSize(new java.awt.Dimension(520, 500));
		this.setLocationRelativeTo(null);
		this.getContentPane().setLayout(null);
		chatPanel.setLayout(new CardLayout());
		chatPanel.add(scrollPane);

		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBorder(null);
		scrollPane.getViewport().setOpaque(false);
		scrollPane.setHeaderVisible(false);
		// scrollPane.setAlpha(0.0f);
		scrollPane.getVerticalScrollBar().setUnitIncrement(20);

		scrollPane.setViewportView(rootPanel);
		rootPanel.setLayout(new ListLayout(ListLayout.Y_AXIS, 30, 2));
		// rootPanel.setLayout(new BoxLayout(rootPanel, BoxLayout.Y_AXIS));
		add(chatPanel);
	}

	private void initData() {
		for (int i = 0; i < 10; i++) {
			rootPanel.add(new ChatItem());
		}
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
					ChatItemTest frame = new ChatItemTest();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
