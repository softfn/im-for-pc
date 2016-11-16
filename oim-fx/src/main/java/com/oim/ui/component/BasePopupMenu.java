package com.oim.ui.component;

import java.awt.Frame;

import javax.swing.JDialog;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import com.only.OnlyPopupMenu;

/**
 * @author: XiaHui
 * @date: 2016年10月12日 下午4:14:30
 */
public class BasePopupMenu extends OnlyPopupMenu {
	private static final long serialVersionUID = 1L;
	private JDialog parent;
	private PopupMenuListener popupMenuListener;

	public BasePopupMenu() {
		parent = new JDialog((Frame) null);
		parent.setUndecorated(true);
		parent.setAlwaysOnTop(true);
		popupMenuListener = new MyPopupMenuListener();
		addPopupMenuListener(popupMenuListener);
	}

	public void show(int x, int y) {
		parent.setLocation(x, y - this.getPreferredSize().height);
		parent.setVisible(true);
		this.show(parent, 0, 0);
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
