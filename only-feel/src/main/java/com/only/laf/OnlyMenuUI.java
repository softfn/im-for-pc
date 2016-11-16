package com.only.laf;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.plaf.basic.BasicMenuUI;
import javax.swing.text.View;

import sun.swing.SwingUtilities2;

import com.only.OnlyMenu;
import com.only.box.UIBox;
import com.only.util.OnlyUIUtil;

public class OnlyMenuUI extends BasicMenuUI {
	
	protected static final Icon DEFAULT_ARROW_ICON =UIBox.getIcon(UIBox.key_icon_menu_arrow);
	protected static final int DEFAULT_GAP = 5;
	protected static final int DEFAULT_LEFT_GAP = 35;
	protected static final int DEFAULT_ARROW_LEFT_GAP = 28;
	protected static final int DEFAULT_ARROW_RIGHT_GAP = 8;
	protected static final int ICON_AREA_WIDTH = 25;
	private static final String MAX_TEXT_WIDTH = "maxTextWidth";
	private static Rectangle zeroRect = new Rectangle(0, 0, 0, 0);
	private static Rectangle iconRect = new Rectangle();
	private static Rectangle arrowIconRect = new Rectangle();
	private static Rectangle textRect = new Rectangle();
	private static Rectangle viewRect = new Rectangle(Short.MAX_VALUE, Short.MAX_VALUE);
	private static Rectangle rect = new Rectangle();

	public static ComponentUI createUI(JComponent c) {
		return new OnlyMenuUI();
	}

	public void paint(Graphics g, JComponent c) {
		JMenu menu = (JMenu) c;
		Font font = c.getFont();
		FontMetrics fm = SwingUtilities2.getFontMetrics(c, g, font);
		resetRects();
		viewRect.setBounds(0, 0, menu.getWidth(), menu.getHeight());
		String text = layoutMenu(fm, menu.getText(), menu.getVerticalAlignment(), menu.getHorizontalAlignment(), menu.getVerticalTextPosition(), menu.getHorizontalTextPosition(), viewRect, iconRect, arrowIconRect, textRect);
		paintBackground(g, menu);

		if (!menu.isTopLevelMenu()) {
			paintIcon(g, menu, iconRect, arrowIconRect);
		}

		if (text != null) {
			View view = (View) c.getClientProperty(BasicHTML.propertyKey);
			g.setFont(font);

			if (view != null) {
				view.paint(g, textRect);
			} else {
				paintText(g, menu, textRect, text);
			}
		}
	}

	protected void paintBackground(Graphics g, JMenu menu) {
		ButtonModel model = menu.getModel();

		if (menu.isEnabled() && (model.isSelected() || (menu.isTopLevelMenu() && model.isRollover()))) {
			OnlyUIUtil.paintImage(g, UIBox.getImage(UIBox.key_image_menu_background), new Insets(1, 1, 1, 1), new Rectangle(0, 0, menu.getWidth(), menu.getHeight()), menu);
		}
	}

	protected void paintIcon(Graphics g, JMenu menu, Rectangle iconRect, Rectangle arrowIconRect) {
		ButtonModel model = menu.getModel();
		Icon icon = menu.getIcon();
		boolean existDisabledIcon = true;
		Graphics2D g2d = (Graphics2D) g;
		Composite oldComposite = g2d.getComposite();
		Composite composite = AlphaComposite.SrcOver.derive(0.5f);

		if (icon != null) {
			if (!model.isEnabled()) {
				icon = menu.getDisabledIcon();

				if (icon == null) {
					icon = menu.getIcon();
					existDisabledIcon = false;
				}
			} else if (model.isPressed() && model.isArmed()) {
				icon = menu.getPressedIcon();

				if (icon == null) {
					icon = menu.getIcon();
				}
			}

			if (icon != null) {
				if (!existDisabledIcon) {
					g2d.setComposite(composite);
				}

				icon.paintIcon(menu, g2d, iconRect.x, iconRect.y);
				g2d.setComposite(oldComposite);
			}
		}

		if (!menu.isEnabled()) {
			g2d.setComposite(composite);
		}

		DEFAULT_ARROW_ICON.paintIcon(menu, g2d, arrowIconRect.x, arrowIconRect.y);
		g2d.setComposite(oldComposite);
	}

	protected void paintText(Graphics g, JMenu menu, Rectangle textRect, String text) {
		ButtonModel model = menu.getModel();
		FontMetrics fm = SwingUtilities2.getFontMetrics(menu, g);
		int mnemIndex = menu.getDisplayedMnemonicIndex();
		Color color = null;

		if (menu instanceof OnlyMenu) {
			OnlyMenu cMenu = (OnlyMenu) menu;

			if (!model.isEnabled()) {
				color = cMenu.getDisabledTextColor();
			} else {
				if (model.isSelected() || (menu.isTopLevelMenu() && model.isRollover())) {
					color = cMenu.getSelectedForeground();
				} else {
					color = cMenu.getForeground();
				}
			}
		}

		if (color != null) {
			g.setColor(color);
			SwingUtilities2.drawStringUnderlineCharAt(menu, g, text, mnemIndex, textRect.x, textRect.y + fm.getAscent());
		} else {
			super.paintText(g, menu, textRect, text);
		}
	}

	public Dimension getPreferredSize(JComponent c) {
		JMenu menu = (JMenu) c;
		String text = menu.getText();
		Font font = menu.getFont();
		FontMetrics fm = menu.getFontMetrics(font);
		JComponent parent = getMenuParent(menu);
		Icon icon = menu.getIcon();
		int iconHeight = icon == null ? 0 : icon.getIconHeight();

		resetRects();
		layoutMenu(fm, text, menu.getVerticalAlignment(), menu.getHorizontalAlignment(), menu.getVerticalTextPosition(), menu.getHorizontalTextPosition(), viewRect, iconRect, arrowIconRect, textRect);
		addMaxWidth(parent, OnlyMenuUI.MAX_TEXT_WIDTH, textRect.width);
		rect.height = max(textRect.height, iconHeight, DEFAULT_ARROW_ICON.getIconHeight());

		if (menu.isTopLevelMenu()) {
			rect.width += DEFAULT_GAP * 2;
		} else {
			rect.width += DEFAULT_LEFT_GAP + DEFAULT_ARROW_LEFT_GAP + DEFAULT_ARROW_RIGHT_GAP + DEFAULT_ARROW_ICON.getIconWidth();
		}

		return rect.getSize();
	}

	private String layoutMenu(FontMetrics fm, String text, int verticalAlignment, int horizontalAlignment, int verticalTextPosition, int horizontalTextPosition, Rectangle viewRect, Rectangle iconRect, Rectangle arrowIconRect, Rectangle textRect) {
		SwingUtilities.layoutCompoundLabel(menuItem, fm, text, null, verticalAlignment, horizontalAlignment, verticalTextPosition, horizontalTextPosition, viewRect, iconRect, textRect, 0);
		textRect.x += ((JMenu) menuItem).isTopLevelMenu() ? DEFAULT_GAP : DEFAULT_LEFT_GAP;
		layoutIcon(iconRect, arrowIconRect);
		return text;
	}

	protected void layoutIcon(Rectangle iconRect, Rectangle arrowIconRect) {
		JMenu menu = (JMenu) menuItem;

		if (!menu.isTopLevelMenu()) {
			Icon icon = menuItem.getIcon();

			if (icon != null) {
				int iconWidth = icon.getIconWidth();
				int iconHeight = icon.getIconHeight();
				iconRect.x = (int) Math.round((ICON_AREA_WIDTH - iconWidth) / 2.0);
				iconRect.y = (int) Math.round((menuItem.getHeight() - iconHeight) / 2.0);
				iconRect.width = iconWidth;
				iconRect.height = iconHeight;
			}

			icon = DEFAULT_ARROW_ICON;
			int iconWidth = icon.getIconWidth();
			int iconHeight = icon.getIconHeight();
			arrowIconRect.x = menuItem.getWidth() - iconWidth - DEFAULT_ARROW_RIGHT_GAP;
			arrowIconRect.y = (int) Math.round((menuItem.getHeight() - iconHeight) / 2.0);
			arrowIconRect.width = iconWidth;
			arrowIconRect.height = iconHeight;
		}
	}

	private void addMaxWidth(JComponent parent, String propertyName, int curWidth) {
		Integer maxWidth = null;

		if (parent != null) {
			maxWidth = (Integer) parent.getClientProperty(propertyName);
		}

		if (maxWidth == null) {
			maxWidth = 0;
		}

		if (curWidth > maxWidth) {
			maxWidth = curWidth;

			if (parent != null) {
				parent.putClientProperty(propertyName, maxWidth);
			}
		}

		if (maxWidth > 0) {
			rect.width += maxWidth;
		}
	}

	private void resetRects() {
		iconRect.setBounds(zeroRect);
		arrowIconRect.setBounds(zeroRect);
		textRect.setBounds(zeroRect);
		viewRect.setBounds(0, 0, Short.MAX_VALUE, Short.MAX_VALUE);
		rect.setBounds(zeroRect);
	}

	private JComponent getMenuParent(JMenu menu) {
		Container parent = menu.getParent();

		if (parent instanceof JComponent && !menu.isTopLevelMenu()) {
			return (JComponent) parent;
		} else {
			return null;
		}
	}

	private int max(int... values) {
		int maxValue = Integer.MIN_VALUE;

		for (int value : values) {
			if (value > maxValue) {
				maxValue = value;
			}
		}

		return maxValue;
	}

	protected void installDefaults() {
	}

	protected void uninstallDefaults() {
		menuItem.setArmed(false);
		menuItem.setSelected(false);
		menuItem.resetKeyboardActions();
	}

	protected MouseInputListener createMouseInputListener(JComponent c) {
		return new CMouseInputHandler();
	}

	protected class CMouseInputHandler extends BasicMenuUI.MouseInputHandler {
		public void mouseEntered(MouseEvent evt) {
			JMenu menu = (JMenu) evt.getSource();

			if (!(menu instanceof OnlyMenu) || ((OnlyMenu) menu).isShowWhenRollover()) {
				super.mouseEntered(evt);
			}

			if (menu.isTopLevelMenu() && menu.isRolloverEnabled()) {
				menu.getModel().setRollover(true);
				menuItem.repaint();
			}
		}

		public void mouseExited(MouseEvent evt) {
			JMenu menu = (JMenu) evt.getSource();

			if (!(menu instanceof OnlyMenu) || ((OnlyMenu) menu).isShowWhenRollover()) {
				super.mouseExited(evt);
			}

			if (menu.isRolloverEnabled()) {
				menu.getModel().setRollover(false);
				menuItem.repaint();
			}
		}
	}
}