package com.only.laf;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.MenuElement;
import javax.swing.MenuSelectionManager;
import javax.swing.plaf.ComponentUI;

import com.only.OnlyCheckBoxMenuItem;
import com.only.box.UIBox;

public class OnlyCheckBoxMenuItemUI extends OnlyMenuItemUI {

	private static final Icon DEFAULT_ICON = UIBox.getIcon(UIBox.key_icon_menu_check_box_item_selected);

	public static ComponentUI createUI(JComponent c) {
		return new OnlyCheckBoxMenuItemUI();
	}

	protected String getPropertyPrefix() {
		return "CheckBoxMenuItem";
	}

	public void processMouseEvent(JMenuItem item, MouseEvent e, MenuElement path[], MenuSelectionManager manager) {
		Point p = e.getPoint();

		if (p.x >= 0 && p.x < item.getWidth() && p.y >= 0 && p.y < item.getHeight()) {
			if (e.getID() == MouseEvent.MOUSE_RELEASED) {
				manager.clearSelectedPath();
				item.doClick(0);
			} else {
				manager.setSelectedPath(path);
			}
		} else if (item.getModel().isArmed()) {
			MenuElement newPath[] = new MenuElement[path.length - 1];
			int i, c;

			for (i = 0, c = path.length - 1; i < c; i++) {
				newPath[i] = path[i];
			}

			manager.setSelectedPath(newPath);
		}
	}

	protected void paintIcon(Graphics g, JMenuItem menuItem, Rectangle iconRect) {
		if (menuItem.isSelected()) {
			Icon icon = menuItem.getSelectedIcon();
			icon = icon == null ? DEFAULT_ICON : icon;
			Graphics2D g2d = (Graphics2D) g;
			Composite oldComposite = g2d.getComposite();

			if (!menuItem.isEnabled()) {
				g2d.setComposite(AlphaComposite.SrcOver.derive(0.5f));
			}

			icon.paintIcon(menuItem, g2d, iconRect.x, iconRect.y);
			g2d.setComposite(oldComposite);
		}
	}

	protected void layoutIcon(Rectangle iconRect) {
		Icon icon = menuItem.getSelectedIcon();
		icon = icon == null ? DEFAULT_ICON : icon;
		int iconWidth = icon.getIconWidth();
		int iconHeight = icon.getIconHeight();
		iconRect.x = (int) Math.round((ICON_AREA_WIDTH - iconWidth) / 2.0);
		iconRect.y = (int) Math.round((menuItem.getHeight() - iconHeight) / 2.0);
		iconRect.width = iconWidth;
		iconRect.height = iconHeight;
	}

	protected Color getTextColor() {
		ButtonModel model = menuItem.getModel();
		Color color = null;

		if (menuItem instanceof OnlyCheckBoxMenuItem) {
			OnlyCheckBoxMenuItem cItem = (OnlyCheckBoxMenuItem) menuItem;

			if (!model.isEnabled()) {
				color = cItem.getDisabledTextColor();
			} else {
				if (model.isArmed()) {
					color = cItem.getSelectedForeground();
				} else {
					color = cItem.getForeground();
				}
			}
		}

		return color;
	}
}