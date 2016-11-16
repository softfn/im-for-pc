package com.oim.ui.chat;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import com.oim.common.event.ExecuteAction;
import com.oim.ui.component.BasePanel;
import com.only.OnlyBorderButton;
import com.only.OnlyButton;
import com.only.OnlyScrollPane;

/**
 * 表情选择组件
 * 
 * @Author: XiaHui
 * @Date: 2016年1月23日
 * @ModifyUser: XiaHui
 * @ModifyDate: 2016年1月23日
 */
public class FacePanel extends BasePanel {
	private static final long serialVersionUID = 1L;
	private GridLayout gridLayout = new GridLayout();
	private JPopupMenu popup = new JPopupMenu();
	private JPanel basePanel = new JPanel();
	private OnlyScrollPane scrollPane = new OnlyScrollPane();
	private Map<JButton, String> faceMap = new HashMap<JButton, String>();
	private ExecuteAction selectAction = null;

	ActionListener action;

	private JDialog parent;
	private PopupMenuListener popupMenuListener;

	private FacePanel() {
		initUI();
		initEvent();
		initData();
	}

	private void initUI() {

		this.setPreferredSize(new Dimension(480, 240));
		this.setLayout(new CardLayout());
		gridLayout.setColumns(15);
		gridLayout.setHgap(1);
		gridLayout.setRows(0);
		gridLayout.setVgap(1);

		basePanel.setLayout(gridLayout);

		scrollPane.setViewportView(basePanel);
		// scrollPane.setBorder(null);
		scrollPane.setHeaderVisible(false);
		scrollPane.getVerticalScrollBar().setUnitIncrement(20);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		this.add(scrollPane);

		popup.setLayout(new CardLayout());
		popup.add(this);
	}

	private void initEvent() {
		action = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				action(e);

			}
		};

		parent = new JDialog((Frame) null);
		parent.setUndecorated(true);
		parent.setAlwaysOnTop(true);
		popupMenuListener = new MyPopupMenuListener();
		popup.addPopupMenuListener(popupMenuListener);
	}

	public void show(Component invoker, int x, int y) {
		popup.show(invoker, x, y);
	}

	private void initData() {
		JButton button = null;
		String normal = " ";
		String selected = " ";
		for (int i = 0; i < 171; i++) {
			normal = "Resources/Images/Face/png/" + i + ".png ";
			selected = "Resources/Images/Face/" + i + ".gif ";
			button = createIconButton(new ImageIcon(normal), new ImageIcon(selected));
			basePanel.add(button);
			faceMap.put(button, i + "");
		}
	}

	private JButton createIconButton(Icon normalIcon, Icon selectedIcon) {
		OnlyButton button = new OnlyBorderButton();
		button.setIcon(normalIcon);
		button.setRolloverIcon(selectedIcon);
		button.setNormalImage(new ImageIcon("Resources/Images/Default/Common/white_background_0.png").getImage());
		button.setFocusImage(button.getNormalImage());
		button.addActionListener(action);
		return button;
	}

	private void action(ActionEvent e) {
		if (null != selectAction && e.getSource() instanceof JButton) {
			String value = faceMap.get(e.getSource());
			if (null != value) {
				selectAction.execute(value);
			}
		}
		popup.setVisible(false);
	}

	public ExecuteAction getSelectAction() {
		return selectAction;
	}

	public void setSelectAction(ExecuteAction selectAction) {
		this.selectAction = selectAction;
	}

	public void show(int x, int y) {
		parent.setLocation(x, y - popup.getPreferredSize().height);
		parent.setVisible(true);
		popup.show(parent, 0, 0);
	}

	static FacePanel facePanel = new FacePanel();

	public static FacePanel getFacePanel() {
		return facePanel;
	}

	private class MyPopupMenuListener implements PopupMenuListener {

		@Override
		public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
		}

		@Override
		public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
			parent.setVisible(false);
		}

		@Override
		public void popupMenuCanceled(PopupMenuEvent e) {
			parent.setVisible(false);
		}
	}
}
