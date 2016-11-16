package com.oim.ui;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JLayeredPane;
import javax.swing.UnsupportedLookAndFeelException;

import com.oim.common.box.HeadImageIconBox;
import com.oim.common.component.AlphaPanel;
import com.oim.ui.chat.ChatItem;
import com.oim.ui.chat.ChatPanel;
import com.oim.ui.chat.GroupUserList;
import com.oim.ui.component.BaseFrame;
import com.oim.ui.component.list.ItemPanel;
import com.only.OnlyPanel;
import com.only.OnlyScrollPane;
import com.only.OnlySplitPane;
import com.only.layout.ListLayout;

/**
 * @author XiaHui
 * @date 2015年1月5日 上午11:52:00
 */
public class ListChatFrame extends BaseFrame {

	private static final long serialVersionUID = 1L;
	private int mX;
	private int mY;
	private OnlyPanel rootPanel = new OnlyPanel();
	private JLayeredPane headLayeredPane = new JLayeredPane();

	private OnlySplitPane splitPane = new OnlySplitPane();

	private AlphaPanel listBackgroundPanel = new AlphaPanel();
	private OnlyPanel baseListPanel = new OnlyPanel();
	private OnlyScrollPane listScrollPane = new OnlyScrollPane();
	private OnlyPanel listPanel = new OnlyPanel();

	private AlphaPanel chatBackgroundPanel = new AlphaPanel();
	private OnlyPanel baseChatPanel = new OnlyPanel();

	/**
	 * Create the frame.
	 */
	public ListChatFrame() {
		initComponent();
		initEvent();
		//showUserList();
	}

	private void initComponent() {
		this.setMinimumSize(new java.awt.Dimension(650, 500));
		this.setLocationRelativeTo(null);
		this.setLayout(new CardLayout());
		this.setBorderPainted(false);
		this.setShowIconImage(false);
		this.setShowTitle(false);

		baseListPanel.setOpaque(false);
		baseListPanel.setLayout(new CardLayout());
		baseListPanel.add(listScrollPane);

		listScrollPane.getVerticalScrollBar().setUnitIncrement(20);
		listScrollPane.setHeaderVisible(false);
		listScrollPane.setAlpha(0.0f);
		listScrollPane.setViewportView(listPanel);

		listPanel.setLayout(new ListLayout(ListLayout.Y_AXIS, 30, 0, ListLayout.PREFER_SIZE));
		listPanel.setOpaque(false);

		baseChatPanel.setOpaque(false);
		baseChatPanel.setLayout(new CardLayout());

		//baseListPanel.setBackground(Color.blue);
		//splitPane.setBackground(Color.red);

		splitPane.setLeftComponent(baseListPanel);
		splitPane.setRightComponent(baseChatPanel);
		splitPane.setDividerSize(2);
		splitPane.setDividerLocation(120);

		//rootPanel.setBackground(new Color(60,69,76));
		rootPanel.setOpaque(false);
		rootPanel.setLayout(new CardLayout());
		rootPanel.add(headLayeredPane);

		listBackgroundPanel.setBackgroundColor(new Color(95, 161, 196));
		chatBackgroundPanel.setBackgroundColor(new Color(255, 255, 255, 180));
		chatBackgroundPanel.setOpaque(false);

		headLayeredPane.add(chatBackgroundPanel, javax.swing.JLayeredPane.DEFAULT_LAYER);
		headLayeredPane.add(listBackgroundPanel, javax.swing.JLayeredPane.PALETTE_LAYER);
		headLayeredPane.add(splitPane, javax.swing.JLayeredPane.MODAL_LAYER);

		add(rootPanel);
	}

	private void initEvent() {
		baseChatPanel.addMouseListener(new java.awt.event.MouseAdapter() {

			@Override
			public void mousePressed(java.awt.event.MouseEvent evt) {
				splitPaneMousePressed(evt);
			}

			@Override
			public void mouseReleased(java.awt.event.MouseEvent evt) {
				splitPaneMouseReleased(evt);
			}
		});
		baseChatPanel.addMouseMotionListener(new MouseMotionAdapter() {

			@Override
			public void mouseDragged(MouseEvent e) {
				splitPaneMouseDragged(e);

			}
		});
		listPanel.addMouseListener(new java.awt.event.MouseAdapter() {

			@Override
			public void mousePressed(java.awt.event.MouseEvent evt) {
				splitPaneMousePressed(evt);
			}

			@Override
			public void mouseReleased(java.awt.event.MouseEvent evt) {
				splitPaneMouseReleased(evt);
			}
		});
		listPanel.addMouseMotionListener(new MouseMotionAdapter() {

			@Override
			public void mouseDragged(MouseEvent e) {
				splitPaneMouseDragged(e);

			}
		});

		baseListPanel.addComponentListener(new java.awt.event.ComponentAdapter() {

			@Override
			public void componentResized(java.awt.event.ComponentEvent evt) {
				baseListPanelResized();

			}
		});
	}

	private void splitPaneMouseReleased(MouseEvent evt) {
	}

	private void splitPaneMousePressed(MouseEvent evt) {
		mX = evt.getX();
		mY = evt.getY();
	}

	private void splitPaneMouseDragged(MouseEvent evt) {
		int x = this.getX() + evt.getX() - mX;
		int y = this.getY() + evt.getY() - mY;
		this.setBounds(x, y, this.getWidth(), this.getHeight());
	}

	private void baseListPanelResized() {
		listBackgroundPanel.setBounds(0, 0, baseListPanel.getWidth() + 2, this.getHeight());
	}

	public void formComponentResized() {
		chatBackgroundPanel.setBounds(0, 60, this.getWidth(), this.getHeight() - 60);
		splitPane.setBounds(2, 2, this.getWidth() - 4, this.getHeight() - 4);
	}

	

	public void addChatItem(ChatItem item) {
		listPanel.add(item);
	}

	public void removeChatItem(ChatItem item) {
		listPanel.remove(item);
		listScrollPane.setViewportView(listPanel);
	}

	public void setChatPanel(ChatPanel chatPanel) {
		baseChatPanel.removeAll();
		baseChatPanel.add(chatPanel);
		splitPane.revalidate();
	}

	public void showUserList() {
		ChatPanel chatPanel = new ChatPanel();
		chatPanel.setIcon(HeadImageIconBox.getUserHeadImageIcon("1", 40, 40));
		chatPanel.setName("fssssssssss");
		chatPanel.setText("hhhhhhhhhhhh");
		GroupUserList groupUserList = new GroupUserList();
		for (int i = 0; i < 13; i++) {
			ItemPanel item = new ItemPanel();
			item.setText("jjjjjjjjkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkjas");
			item.setIcon(HeadImageIconBox.getUserHeadImageIcon((i+1)+"", 20, 20));
			groupUserList.add(item);
		}

		chatPanel.setUserListPanel(groupUserList);

		setChatPanel(chatPanel);
		for (int i = 0; i < 13; i++) {
			ChatItem item = new ChatItem();
			item.setText("jjjjjjjjkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkjas");
			item.setIcon(HeadImageIconBox.getUserHeadImageIcon((i+1)+"", 20, 20));
			listPanel.add(item);
		}
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
					ListChatFrame frame = new ListChatFrame();
					frame.showUserList();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
