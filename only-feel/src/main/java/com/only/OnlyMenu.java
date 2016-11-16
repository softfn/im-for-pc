package com.only;

import com.only.laf.OnlyMenuUI;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.beans.PropertyChangeListener;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.MenuElement;
import javax.swing.border.EmptyBorder;

import com.only.util.OnlyUIUtil;

public class OnlyMenu extends JMenu {
	private static final long serialVersionUID = -4247570496135633786L;
	private JPopupMenu popupMenu;
	private Point customMenuLocation;
	private Icon disabledIcon;
	private Color disabledTextColor;
	private Color selectedForeground;
	private int preferredHeight;

	private boolean showWhenRollover;

	public OnlyMenu() {
		this("");
	}

	public OnlyMenu(Action a) {
		this();
		setAction(a);
	}

	public OnlyMenu(String s, boolean b) {
		this(s);
	}

	public OnlyMenu(String s) {
		super(s);
		setUI(new OnlyMenuUI());
		setOpaque(false);
		setFont(OnlyUIUtil.getDefaultFont());
		setForeground(new Color(0, 20, 35));
		setBackground(Color.GRAY);
		setIconTextGap(0);
		setRolloverEnabled(true);
		setBorderPainted(false);
		setBorder(new EmptyBorder(0, 0, 0, 0));
		setFocusPainted(false);
		setDelay(200);
		setMargin(new Insets(0, 0, 0, 0));
		selectedForeground = new Color(253, 253, 253);
		disabledTextColor = new Color(127, 137, 144);
		preferredHeight = 22;
		showWhenRollover = true;
	}

	@Deprecated
	public void updateUI() {
		if (popupMenu != null) {
			popupMenu.updateUI();
		}
	}

	public boolean isPopupMenuVisible() {
		ensurePopupMenuCreated();
		return popupMenu.isVisible();
	}

	public void setPopupMenuVisible(boolean b) {
		// System.out.println("jm11111"+b);
		if (b != isPopupMenuVisible() && (isEnabled() || !b) && getMenuComponentCount() > 0) {
			// System.out.println("jm2222"+b);
			ensurePopupMenuCreated();
			if (b && isShowing()) {
				Point p = customMenuLocation;
				if (p == null) {
					p = getPopupMenuOrigin();
				}
				getPopupMenu().show(this, p.x, p.y);
			} else {
				getPopupMenu().setVisible(false);
			}
		}
	}

	protected Point getPopupMenuOrigin() {
		int x = 0;
		int y = 0;
		JPopupMenu popup = getPopupMenu();
		Dimension size = getSize();
		Dimension popupSize = popup.getSize();
		popupSize = popupSize.width == 0 ? popup.getPreferredSize() : popupSize;
		Point position = getLocationOnScreen();
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		GraphicsConfiguration gc = getGraphicsConfiguration();
		Rectangle screenBounds = new Rectangle(toolkit.getScreenSize());
		GraphicsDevice[] gds = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
		Container parent = getParent();

		for (GraphicsDevice gd : gds) {
			if (gd.getType() == GraphicsDevice.TYPE_RASTER_SCREEN) {
				GraphicsConfiguration dgc = gd.getDefaultConfiguration();

				if (dgc.getBounds().contains(position)) {
					gc = dgc;
					break;
				}
			}
		}

		if (gc != null) {
			screenBounds = gc.getBounds();
			Insets screenInsets = toolkit.getScreenInsets(gc);
			screenBounds.width -= Math.abs(screenInsets.left + screenInsets.right);
			screenBounds.height -= Math.abs(screenInsets.top + screenInsets.bottom);
			position.x -= Math.abs(screenInsets.left);
			position.y -= Math.abs(screenInsets.top);
		}

		if (parent instanceof JPopupMenu) {
			int xOffset = 0;
			int yOffset = -popup.getInsets().top;
			x = size.width + xOffset;
			y = yOffset;

			if (position.x + x + popupSize.width >= screenBounds.width + screenBounds.x && screenBounds.width - size.width < 2 * (position.x - screenBounds.x)) {
				x = -(xOffset + popupSize.width);
			}

			if (position.y + y + popupSize.height >= screenBounds.height + screenBounds.y && screenBounds.height - size.height < 2 * (position.y - screenBounds.y)) {
				y = size.height - yOffset - popupSize.height;
			}
		} else {
			int xOffset = 0;
			int yOffset = 0;
			x = xOffset;
			y = size.height + yOffset;

			if (position.x + x + popupSize.width >= screenBounds.width + screenBounds.x && screenBounds.width - size.width < 2 * (position.x - screenBounds.x)) {
				x = size.width - xOffset - popupSize.width;
			}

			if (position.y + y + popupSize.height >= screenBounds.height && screenBounds.height - size.height < 2 * (position.y - screenBounds.y)) {
				y = -(yOffset + popupSize.height);
			}
		}

		return new Point(x, y);
	}

	private void ensurePopupMenuCreated() {
		if (popupMenu == null) {
			this.popupMenu = new OnlyPopupMenu();
			popupMenu.setInvoker(this);
			popupListener = createWinListener(popupMenu);
		}
	}

	public void setMenuLocation(int x, int y) {
		customMenuLocation = new Point(x, y);

		if (popupMenu != null) {
			popupMenu.setLocation(x, y);
		}
	}

	public JMenuItem add(JMenuItem menuItem) {
		ensurePopupMenuCreated();
		return popupMenu.add(menuItem);
	}

	public Component add(Component c) {
		ensurePopupMenuCreated();
		popupMenu.add(c);
		return c;
	}

	public Component add(Component c, int index) {
		ensurePopupMenuCreated();
		popupMenu.add(c, index);
		return c;
	}

	public JMenuItem add(String s) {
		return add(new OnlyMenuItem(s));
	}

	protected JMenuItem createActionComponent(Action a) {
		OnlyMenuItem mi = new OnlyMenuItem() {
			private static final long serialVersionUID = -5940148878356449223L;

			protected PropertyChangeListener createActionPropertyChangeListener(Action a) {
				PropertyChangeListener pcl = createActionChangeListener(this);
				pcl = pcl == null ? super.createActionPropertyChangeListener(a) : pcl;
				return pcl;
			}
		};

		return mi;
	}

	public void addSeparator() {
		ensurePopupMenuCreated();
		popupMenu.addSeparator();
	}

	public void insert(String s, int pos) {
		if (pos < 0) {
			throw new IllegalArgumentException("index less than zero.");
		}

		ensurePopupMenuCreated();
		popupMenu.insert(new OnlyMenuItem(s), pos);
	}

	public JMenuItem insert(JMenuItem mi, int pos) {
		if (pos < 0) {
			throw new IllegalArgumentException("index less than zero.");
		}

		ensurePopupMenuCreated();
		popupMenu.insert(mi, pos);
		return mi;
	}

	public JMenuItem insert(Action a, int pos) {
		if (pos < 0) {
			throw new IllegalArgumentException("index less than zero.");
		}

		ensurePopupMenuCreated();
		OnlyMenuItem mi = new OnlyMenuItem(a);
		popupMenu.insert(mi, pos);
		return mi;
	}

	public void insertSeparator(int index) {
		if (index < 0) {
			throw new IllegalArgumentException("index less than zero.");
		}

		ensurePopupMenuCreated();
		popupMenu.insert(new OnlyPopupMenu.Separator(), index);
	}

	public void remove(JMenuItem item) {
		if (popupMenu != null) {
			popupMenu.remove(item);
		}
	}

	public void remove(int pos) {
		if (pos < 0) {
			throw new IllegalArgumentException("index less than zero.");
		}

		if (pos > getItemCount()) {
			throw new IllegalArgumentException("index greater than the number of items.");
		}

		if (popupMenu != null) {
			popupMenu.remove(pos);
		}
	}

	public void remove(Component c) {
		if (popupMenu != null) {
			popupMenu.remove(c);
		}
	}

	public void removeAll() {
		if (popupMenu != null) {
			popupMenu.removeAll();
		}
	}

	public int getMenuComponentCount() {
		return popupMenu != null ? popupMenu.getComponentCount() : 0;
	}

	public Component getMenuComponent(int n) {
		return popupMenu != null ? popupMenu.getComponent(n) : null;
	}

	public Component[] getMenuComponents() {
		return popupMenu != null ? popupMenu.getComponents() : new Component[0];
	}

	public JPopupMenu getPopupMenu() {
		ensurePopupMenuCreated();
		return popupMenu;
	}

	public MenuElement[] getSubElements() {
		return popupMenu == null ? new MenuElement[0] : new MenuElement[] { popupMenu };
	}

	public void applyComponentOrientation(ComponentOrientation o) {
		super.applyComponentOrientation(o);

		if (popupMenu != null) {
			int ncomponents = getMenuComponentCount();

			for (int i = 0; i < ncomponents; ++i) {
				getMenuComponent(i).applyComponentOrientation(o);
			}

			popupMenu.setComponentOrientation(o);
		}
	}

	public void setComponentOrientation(ComponentOrientation o) {
		super.setComponentOrientation(o);

		if (popupMenu != null) {
			popupMenu.setComponentOrientation(o);
		}
	}

	public Color getDisabledTextColor() {
		return disabledTextColor;
	}

	public void setDisabledTextColor(Color disabledTextColor) {
		this.disabledTextColor = disabledTextColor;

		if (!this.isEnabled()) {
			this.repaint();
		}
	}

	public Color getSelectedForeground() {
		return selectedForeground;
	}

	public void setSelectedForeground(Color selectedForeground) {
		this.selectedForeground = selectedForeground;
		this.repaint();
	}

	public int getPreferredHeight() {
		return preferredHeight;
	}

	public void setPreferredHeight(int preferredHeight) {
		this.preferredHeight = preferredHeight;
		this.revalidate();
	}

	public Dimension getPreferredSize() {
		Dimension size = super.getPreferredSize();

		if (preferredHeight > 0) {
			size.height = preferredHeight;
		}

		return size;
	}

	public Icon getDisabledIcon() {
		return disabledIcon;
	}

	public void setDisabledIcon(Icon disabledIcon) {
		super.setDisabledIcon(disabledIcon);
		this.disabledIcon = disabledIcon;
	}

	public boolean isShowWhenRollover() {
		return showWhenRollover;
	}

	public void setShowWhenRollover(boolean showWhenRollover) {
		this.showWhenRollover = showWhenRollover;
	}
}