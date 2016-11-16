package com.only.laf;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.text.View;

import sun.swing.SwingUtilities2;

import com.only.OnlyButton;
import com.only.OnlyTabbedPane;
import com.only.box.UIBox;
import com.only.util.OnlyUIUtil;

public class OnlyTabbedPaneUI extends BasicTabbedPaneUI {

	public static ComponentUI createUI(JComponent c) {
		return new OnlyTabbedPaneUI();
	}

	@Override
	protected void installDefaults() {
		tabRunOverlay = 2;
		textIconGap = 5;
		tabInsets = new Insets(1, 9, 1, 9);
		selectedTabPadInsets = new Insets(2, 2, 2, 1);
		contentBorderInsets = new Insets(0, 0, 0, 0);
		tabAreaInsets = new Insets(0, 0, 0, 0);
	}

	@Override
	protected void uninstallDefaults() {
	}

	@Override
	public void paint(Graphics g, JComponent c) {
		paintTabBarBackground(g);
		super.paint(g, c);
	}

	@Override
	protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {
	}

	@Override
	protected void paintFocusIndicator(Graphics g, int tabPlacement, Rectangle[] rects, int tabIndex, Rectangle iconRect, Rectangle textRect, boolean isSelected) {
	}

	@Override
	protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
		Image image = null;
		boolean tabEnabled = tabPane.isEnabledAt(tabIndex) && tabPane.isEnabled();

		if (isSelected) {
			image = UIBox.getImage(UIBox.key_image_tabbed_pane_pressed);
		} else if (getRolloverTab() == tabIndex && tabEnabled) {
			image = UIBox.getImage(UIBox.key_image_tabbed_pane_rollover);
		} else {
			image = UIBox.getImage(UIBox.key_image_tabbed_pane_normal);
		}

		if (image != null) {
			Graphics2D g2d = (Graphics2D) g;
			

			Composite oldComposite = g2d.getComposite();

			if (!tabEnabled) {
				g2d.setComposite(AlphaComposite.SrcOver.derive(0.5f));
			}
			
			OnlyUIUtil.paintImage(g, image, new Insets(1, 1, 1, 1), new Rectangle(x, y, w, h), tabPane);
			g2d.setComposite(oldComposite);
		}

		if (!tabPane.isEnabledAt(tabIndex) && tabPane.isEnabled() && !isSelected) {
			g.setColor(new Color(255, 255, 255, 127));
			g.fillRect(x, y, w, h);
		}
	}

	protected void paintTabBarBackground(Graphics g) {
		Rectangle rect = null;
		Insets insets = tabPane.getInsets();
		int tabPlacement = tabPane.getTabPlacement();
		int width = tabPane.getWidth();
		int height = tabPane.getHeight();
		int tabBarWidth = calculateTabAreaWidth(tabPlacement, runCount, maxTabWidth);
		int tabBarHeight = calculateTabAreaHeight(tabPlacement, runCount, maxTabHeight);

		switch (tabPlacement) {
		case JTabbedPane.BOTTOM: {
			rect = new Rectangle(insets.left, height - insets.bottom - tabBarHeight, width - insets.left - insets.right, tabBarHeight);
			break;
		}
		case JTabbedPane.LEFT: {
			rect = new Rectangle(insets.left, insets.top, tabBarWidth, height - insets.top - insets.bottom);
			break;
		}
		case JTabbedPane.RIGHT: {
			rect = new Rectangle(width - insets.right - tabBarWidth, insets.top, tabBarWidth, height - insets.top - insets.bottom);
			break;
		}
		case JTabbedPane.TOP:
		default: {
			rect = new Rectangle(insets.left, insets.top, width - insets.left - insets.right, tabBarHeight);
		}
		}

		Graphics2D g2d = (Graphics2D) g;
		Composite oldComposite = g2d.getComposite();

		if (!tabPane.isEnabled()) {
			g2d.setComposite(AlphaComposite.SrcOver.derive(0.5f));
		}
		if (tabPane instanceof OnlyTabbedPane) {
			float alpha = ((OnlyTabbedPane) tabPane).getAlpha();
			if (alpha < 1.0F) {
				g2d.setComposite(AlphaComposite.SrcOver.derive(alpha));
			}
		}
		OnlyUIUtil.paintImage(g, UIBox.getImage(UIBox.key_image_tabbed_pane_bacdground), new Insets(1, 1, 1, 1), rect, tabPane);
		g2d.setComposite(oldComposite);
	}

	@Override
	protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
		int rolloverTab = getRolloverTab();
		boolean tabEnabled = tabPane.isEnabledAt(tabIndex) && tabPane.isEnabled();

		if ((rolloverTab == tabIndex && tabEnabled) || isSelected) {
			return;
		}

//		Image image;
		Graphics2D g2d = (Graphics2D) g;
		Composite oldComposite = g2d.getComposite();

		if (!tabEnabled) {
			g2d.setComposite(AlphaComposite.SrcOver.derive(0.5f));
		}

//		if (tabPlacement == JTabbedPane.LEFT || tabPlacement == JTabbedPane.RIGHT) {
//			image = UIBox.getImage(UIBox.key_image_tabbed_pane_split_h);
//			OnlyUIUtil.paintImage(g, image, new Insets(2, 1, 2, 1), new Rectangle(x, y + (h - 1), w, 2), tabPane);
//		} else {
//			image = UIBox.getImage(UIBox.key_image_tabbed_pane_split_v);
//			OnlyUIUtil.paintImage(g, image, new Insets(2, 1, 2, 1), new Rectangle(x + (w - 2), y, 2, h), tabPane);
//		}

		g2d.setComposite(oldComposite);
	}

	@Override
	protected void paintText(Graphics g, int tabPlacement, Font font, FontMetrics metrics, int tabIndex, String title, Rectangle textRect, boolean isSelected) {
		g.setFont(font);
		View view = getTextViewForTab(tabIndex);
		if (view != null) {
			view.paint(g, textRect);
		} else {
			int mnemIndex = tabPane.getDisplayedMnemonicIndexAt(tabIndex);
			if (tabPane instanceof OnlyTabbedPane) {
				boolean tabEnabled = tabPane.isEnabledAt(tabIndex) && tabPane.isEnabled();
				if (isSelected) {
					g.setColor(((OnlyTabbedPane) tabPane).getRolloverTextColor());
				} else if (getRolloverTab() == tabIndex && tabEnabled) {
					g.setColor(((OnlyTabbedPane) tabPane).getForeground());
				} else {
					g.setColor(((OnlyTabbedPane) tabPane).getForeground());
				}
				//g.setColor(((OnlyTabbedPane) tabPane).getDisabledForegroundAt(tabIndex));
				SwingUtilities2.drawStringUnderlineCharAt(tabPane, g, title, mnemIndex, textRect.x, textRect.y + metrics.getAscent());
			} else {
				super.paintText(g, tabPlacement, font, metrics, tabIndex, title, textRect, isSelected);
			}
//			if (tabPane.isEnabled() && tabPane.isEnabledAt(tabIndex)) {
//				g.setColor(tabPane.getForegroundAt(tabIndex));
//				SwingUtilities2.drawStringUnderlineCharAt(tabPane, g, title, mnemIndex, textRect.x, textRect.y + metrics.getAscent());
//			} else {
//				if (tabPane instanceof OnlyTabbedPane) {
//					boolean tabEnabled = tabPane.isEnabledAt(tabIndex) && tabPane.isEnabled();
//					if (isSelected) {
//						g.setColor(((OnlyTabbedPane) tabPane).getRolloverTextColor());
//					} else if (getRolloverTab() == tabIndex && tabEnabled) {
//						g.setColor(((OnlyTabbedPane) tabPane).getRolloverTextColor());
//					} else {
//						g.setColor(((OnlyTabbedPane) tabPane).getDisabledForegroundAt(tabIndex));
//					}
//					//g.setColor(((OnlyTabbedPane) tabPane).getDisabledForegroundAt(tabIndex));
//					SwingUtilities2.drawStringUnderlineCharAt(tabPane, g, title, mnemIndex, textRect.x, textRect.y + metrics.getAscent());
//				} else {
//					super.paintText(g, tabPlacement, font, metrics, tabIndex, title, textRect, isSelected);
//				}
//			}
		}
	}

	@Override
	protected void paintIcon(Graphics g, int tabPlacement, int tabIndex, Icon icon, Rectangle iconRect, boolean isSelected) {
		if (icon != null) {
			Graphics2D g2d = (Graphics2D) g;
			Composite oldComposite = g2d.getComposite();

			if (!tabPane.isEnabled() || !tabPane.isEnabledAt(tabIndex)) {
				g2d.setComposite(AlphaComposite.SrcOver.derive(0.5f));
			}

			icon.paintIcon(tabPane, g, iconRect.x, iconRect.y);
			g2d.setComposite(oldComposite);
		}
	}

	@Override
	protected Icon getIconForTab(int tabIndex) {
		return tabPane.getIconAt(tabIndex);
	}

	@Override
	protected int getTabLabelShiftX(int tabPlacement, int tabIndex, boolean isSelected) {
		return 0;
	}

	@Override
	protected int getTabLabelShiftY(int tabPlacement, int tabIndex, boolean isSelected) {
		return 0;
	}

	@Override
	protected int calculateTabHeight(int tabPlacement, int tabIndex, int fontHeight) {
		if (tabPane instanceof OnlyTabbedPane) {
			int tabHeight = ((OnlyTabbedPane) tabPane).getTabHeight();

			if (tabHeight >= 0) {
				return tabHeight;
			}
		}

		return super.calculateTabHeight(tabPlacement, tabIndex, fontHeight);
	}

	@Override
	protected void setRolloverTab(int index) {
		int oldRolloverTab = getRolloverTab();
		Rectangle rect1 = null;
		Rectangle rect2 = null;
		super.setRolloverTab(index);

		if (oldRolloverTab >= 0 && oldRolloverTab < tabPane.getTabCount()) {
			rect1 = getTabBounds(tabPane, oldRolloverTab);
		}

		if (index >= 0) {
			rect2 = getTabBounds(tabPane, index);
		}

		if (rect1 != null) {
			if (rect2 != null) {
				tabPane.repaint(rect1.union(rect2));
			} else {
				tabPane.repaint(rect1);
			}
		} else if (rect2 != null) {
			tabPane.repaint(rect2);
		}
	}

	@Override
	protected LayoutManager createLayoutManager() {
		if (tabPane.getTabLayoutPolicy() == JTabbedPane.SCROLL_TAB_LAYOUT) {
			return super.createLayoutManager();
		}

		return new TabbedPaneLayout();
	}

	@Override
	protected JButton createScrollButton(int direction) {
		if (direction != SOUTH && direction != NORTH && direction != EAST && direction != WEST) {
			throw new IllegalArgumentException("Direction must be one of: " + "SOUTH, NORTH, EAST or WEST");
		}

		return new ScrollableTabButton(direction);
	}

	public class TabbedPaneLayout extends BasicTabbedPaneUI.TabbedPaneLayout {

		public TabbedPaneLayout() {
			OnlyTabbedPaneUI.this.super();
		}

		@Override
		protected void normalizeTabRuns(int tabPlacement, int tabCount, int start, int max) {
			if (tabPlacement == TOP || tabPlacement == BOTTOM) {
				super.normalizeTabRuns(tabPlacement, tabCount, start, max);
			}
		}

		@Override
		protected void rotateTabRuns(int tabPlacement, int selectedRun) {
		}

		@Override
		protected void padSelectedTab(int tabPlacement, int selectedIndex) {
		}
	}

	private class ScrollableTabButton extends OnlyButton implements UIResource, SwingConstants {

		private static final long serialVersionUID = 8901211504611624538L;

		public ScrollableTabButton(int direction) {
			super();
			setRequestFocusEnabled(false);

			if (direction == WEST) {
				setNormalImage(UIBox.getImage(UIBox.key_image_tabbed_pane_previous_normal));
				setDisabledImage(UIBox.getImage(UIBox.key_image_tabbed_pane_previous_disabled));
				setRolloverImage(UIBox.getImage(UIBox.key_image_tabbed_pane_previous_rollover));
				setPressedImage(UIBox.getImage(UIBox.key_image_tabbed_pane_previous_pressed));

			} else if (direction == EAST) {
				setNormalImage(UIBox.getImage(UIBox.key_image_tabbed_pane_next_normal));
				setDisabledImage(UIBox.getImage(UIBox.key_image_tabbed_pane_next_disabled));
				setRolloverImage(UIBox.getImage(UIBox.key_image_tabbed_pane_next_rollover));
				setPressedImage(UIBox.getImage(UIBox.key_image_tabbed_pane_next_pressed));
			}
		}

		@Override
		public boolean isFocusTraversable() {
			return false;
		}

		@Override
		public Dimension getPreferredSize() {
			Image image = this.getNormalImage();
			return image != null ? new Dimension(image.getWidth(null), image.getHeight(null)) : super.getPreferredSize();
		}

		@Override
		public Dimension getMinimumSize() {
			return new Dimension(5, 5);
		}

		@Override
		public Dimension getMaximumSize() {
			return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
		}
	}
}