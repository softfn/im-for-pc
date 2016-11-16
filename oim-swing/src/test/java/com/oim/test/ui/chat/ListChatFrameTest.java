package com.oim.test.ui.chat;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UnsupportedLookAndFeelException;

import com.oim.ui.chat.ChatItem;
import com.oim.ui.chat.ChatPanel;
import com.only.OnlyBorderFrame;
import com.only.OnlyButton;
import com.only.OnlyPanel;
import com.only.OnlyScrollPane;
import com.only.layout.ListLayout;

/**
 * @author XiaHui
 * @date 2015年1月5日 上午11:52:00
 */
public class ListChatFrameTest extends OnlyBorderFrame {

	private static final long serialVersionUID = 1L;
	private int mX;
	private int sX;
	private int x = 120;
	private int width;

	private int minimum = 120;
	private int maximum = 200;

	private JPanel splitPane = new JPanel();

	private OnlyPanel baseListPanel = new OnlyPanel();
	private OnlyScrollPane listScrollPane = new OnlyScrollPane();
	private OnlyPanel listPanel = new OnlyPanel();

	private OnlyPanel basePanel = new OnlyPanel();
	private OnlyPanel baseChatPanel = new OnlyPanel();
	private OnlyPanel bottomPanel = new OnlyPanel();

	private OnlyButton sendButton = new OnlyButton();
	private OnlyButton closeButton = new OnlyButton();

	/**
	 * Create the frame.
	 */
	public ListChatFrameTest() {
		initComponent();
		initEvent();
		resized();
		showUserList();
	}

	private void initComponent() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// this.setBounds(100, 100, 450, 300);
		this.setMinimumSize(new java.awt.Dimension(550, 500));
		//this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setLayout(null);
		this.setBorderPainted(false);

		baseListPanel.setLayout(new CardLayout());
		baseListPanel.add(listScrollPane);

		listScrollPane.getVerticalScrollBar().setUnitIncrement(20);
		listScrollPane.setHeaderVisible(false);
		listScrollPane.setAlpha(0.0f);
		listScrollPane.setViewportView(listPanel);

		listPanel.setLayout(new ListLayout(ListLayout.Y_AXIS, 30, 2, ListLayout.PREFER_SIZE));

		//basePanel.setLayout(new CardLayout());
		//basePanel.setOpaque(false);
		basePanel.setLayout(new java.awt.BorderLayout());
		//basePanel.add(topPanel, java.awt.BorderLayout.PAGE_START);
		basePanel.add(baseChatPanel, java.awt.BorderLayout.CENTER);
		basePanel.add(bottomPanel, java.awt.BorderLayout.PAGE_END);

		bottomPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 10, 10));

		bottomPanel.add(closeButton);
		bottomPanel.add(sendButton);

		closeButton.setText("关闭");
		sendButton.setText("发送");

		closeButton.setPreferredSize(new Dimension(100, 25));
		sendButton.setPreferredSize(new Dimension(100, 25));

		baseChatPanel.setLayout(new CardLayout());

		baseListPanel.setBackground(Color.blue);
		splitPane.setBackground(Color.red);
		basePanel.setBackground(Color.green);

		//splitPane.setBounds(120, 0, 3, this.getHeight());
		splitPane.setCursor(new java.awt.Cursor(java.awt.Cursor.W_RESIZE_CURSOR));

		add(baseListPanel);
		add(splitPane);
		add(basePanel);

	}

	private void initEvent() {
		splitPane.addMouseListener(new java.awt.event.MouseAdapter() {

			@Override
			public void mousePressed(java.awt.event.MouseEvent evt) {
				splitPaneMousePressed(evt);
			}

			@Override
			public void mouseReleased(java.awt.event.MouseEvent evt) {
				splitPaneMouseReleased(evt);
			}
		});
		splitPane.addMouseMotionListener(new MouseMotionAdapter() {

			@Override
			public void mouseDragged(MouseEvent e) {
				splitPaneMouseDragged(e);

			}
		});
	}

	private void splitPaneMouseReleased(MouseEvent evt) {
		//formComponentResized() ;
		//baseListPanel.setBounds(0, 0, splitPane.getX(), this.getHeight());
		
	}

	private void splitPaneMousePressed(MouseEvent evt) {
		mX = evt.getX();
		width = this.getWidth();
		sX = splitPane.getX();
	}

	private void splitPaneMouseDragged(MouseEvent evt) {
		x = splitPane.getX() + evt.getX() - mX;

		if (0 < minimum) {
			if (x < minimum) {
				x = minimum;
			}
		}
		if (0 < maximum) {
			if (x > maximum) {
				x = maximum;
			}
		}
		if (x < 0) {
			x = 0;
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				
				ListChatFrameTest.this.setBounds(ListChatFrameTest.this.getX(), ListChatFrameTest.this.getY(), width + (x - sX), ListChatFrameTest.this.getHeight());
				//resized();
			}
		});
		

	}

	public void resized() {
		//ListChatFrame.this.setBounds(ListChatFrame.this.getX(), ListChatFrame.this.getY(), width + (x - sX), ListChatFrame.this.getHeight());
		baseListPanel.setBounds(0, 0, x, this.getHeight());
		splitPane.setBounds(x, splitPane.getY(), 3, this.getHeight());
		basePanel.setBounds(splitPane.getX() + 3, 0, this.getWidth() - (splitPane.getX() + 6), this.getHeight());
	}

	public void formComponentResized() {
//		resized();
		baseListPanel.setBounds(0, 0, x, this.getHeight());
		splitPane.setBounds(x, splitPane.getY(), 3, this.getHeight());
		basePanel.setBounds(splitPane.getX() + 3, 0, this.getWidth() - (splitPane.getX() + 6), this.getHeight());
	}

	public void showUserList() {
		ChatPanel chatPanel = new ChatPanel();
		chatPanel.setIcon(new ImageIcon());
		chatPanel.setName("fssssssssss");
		chatPanel.setText("hhhhhhhhhhhh");

		addChatPanel(chatPanel);
		for (int i = 0; i < 30; i++) {
			ChatItem item = new ChatItem();
			item.setText("jjjjjjjjkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkjas");
			listPanel.add(item);
		}
	}

	public void addChatPanel(ChatPanel chatPanel) {
		baseChatPanel.removeAll();
		baseChatPanel.add(chatPanel);
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
					ListChatFrameTest frame = new ListChatFrameTest();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
