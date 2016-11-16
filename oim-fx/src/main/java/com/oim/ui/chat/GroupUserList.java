package com.oim.ui.chat;

import java.awt.CardLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.oim.common.component.OurPanel;
import com.oim.ui.component.list.ItemPanel;
import com.only.OnlyScrollPane;
import com.only.layout.ListLayout;

/**
 * 描述：群聊天窗口的成员列表
 * 
 * @author XiaHui
 * @date 2015年4月25日 下午12:46:35
 * @version 0.0.1
 */
public class GroupUserList extends OurPanel {

	private static final long serialVersionUID = 1L;

	private JPanel rootPanel = new JPanel();
	private OnlyScrollPane scrollPane = new OnlyScrollPane();

	public GroupUserList() {
		initComponent();
	}

	private void initComponent() {
		this.setOpaque(false);
		this.setLayout(new CardLayout());
		this.add(scrollPane);

		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBorder(null);
		scrollPane.getViewport().setOpaque(false);
		scrollPane.setHeaderVisible(false);
		scrollPane.setAlpha(0.0f);
		scrollPane.getVerticalScrollBar().setUnitIncrement(20);

		scrollPane.setViewportView(rootPanel);
		rootPanel.setLayout(new ListLayout(ListLayout.Y_AXIS, 30, 1,ListLayout.PREFER_SIZE));
		rootPanel.setOpaque(false);
	}

	public void add(ItemPanel item) {
		rootPanel.add(item);
	}

	public void rootPanel(ItemPanel item) {
		rootPanel.remove(item);
	}
}
