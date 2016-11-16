package com.only.laf;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicHTML;

import sun.awt.AppContext;

import com.only.OnlyCheckBox;
import com.only.OnlyList;
import com.only.box.UIBox;
import com.only.util.OnlyUIUtil;

@SuppressWarnings("rawtypes")
public class OnlyCheckBoxListCellRenderer extends OnlyCheckBox implements ListCellRenderer, Serializable {

	private static final long serialVersionUID = -5977306258474788249L;

	private boolean selected;
	private JList list;
	private List<?> selectedList;

	public OnlyCheckBoxListCellRenderer(List<?> selectedList) {
		super();
		setUI(new RendererUI());
		setBorder(new EmptyBorder(0, 1, 0, 0));
		setName("List.cellRenderer");
		this.selectedList = selectedList;
	}

	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		this.list = list;
		this.selected = isSelected;
		Color fg = list.getForeground();

		if (list instanceof OnlyList && !list.isEnabled()) {
			fg = ((OnlyList) list).getDisabledForeground();
		}

		if (list instanceof OnlyList) {
			OnlyList cList = (OnlyList) list;
			Color color1 = cList.getRendererBackground1();
			Color color2 = cList.getRendererBackground2();
			setOpaque(isSelected || (cList.isRendererOpaque() && color1 != null && color2 != null));
			setBackground(index % 2 == 0 ? color1 : color2);
		} else {
			setOpaque(isSelected);
		}

		setText(value == null ? "" : value.toString());
		setFont(list.getFont());
		setForeground(isSelected ? list.getSelectionForeground() : fg);
		setSelected(index >= selectedList.size() ? false : selectedList.get(index) == value);
		return this;
	}

	public boolean isOpaque() {
		Color back = getBackground();
		Component p = getParent();

		if (p != null) {
			p = p.getParent();
		}

		boolean colorMatch = (back != null) && (p != null) && back.equals(p.getBackground()) && p.isOpaque();
		return !colorMatch && super.isOpaque();
	}

	public void validate() {
	}

	public void invalidate() {
	}

	public void repaint() {
	}

	public void revalidate() {
	}

	public void repaint(long tm, int x, int y, int width, int height) {
	}

	public void repaint(Rectangle r) {
	}

	protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
		if (propertyName.equals("text") || ((propertyName.equals("font") || propertyName.equals("foreground")) && oldValue != newValue && getClientProperty(BasicHTML.propertyKey) != null)) {

			super.firePropertyChange(propertyName, oldValue, newValue);
		}
	}

	public void firePropertyChange(String propertyName, byte oldValue, byte newValue) {
	}

	public void firePropertyChange(String propertyName, char oldValue, char newValue) {
	}

	public void firePropertyChange(String propertyName, short oldValue, short newValue) {
	}

	public void firePropertyChange(String propertyName, int oldValue, int newValue) {
	}

	public void firePropertyChange(String propertyName, long oldValue, long newValue) {
	}

	public void firePropertyChange(String propertyName, float oldValue, float newValue) {
	}

	public void firePropertyChange(String propertyName, double oldValue, double newValue) {
	}

	public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
	}

	private static class RendererUI extends OnlyCheckBoxUI {

		private static final Object RENDERER_UI_KEY = new Object();

		private static final Image BG_IMAGE = UIBox.getImage(UIBox.key_image_check_box_list_selected_item_normal_background);
		private static final Image BG_IMAGE_DISABLED = UIBox.getImage(UIBox.key_image_check_box_list_selected_item_disabled_background);

		public static ComponentUI createUI(JComponent b) {
			AppContext appContext = AppContext.getAppContext();
			OnlyCheckBoxUI checkboxUI = (OnlyCheckBoxUI) appContext.get(RENDERER_UI_KEY);

			if (checkboxUI == null) {
				checkboxUI = new OnlyCheckBoxUI();
				appContext.put(RENDERER_UI_KEY, checkboxUI);
			}

			return checkboxUI;
		}

		public void update(Graphics g, JComponent c) {
			if (c.isOpaque()) {
				paintBackground(g, c);
			}

			paint(g, c);
		}

		private void paintBackground(Graphics g, JComponent c) {
			if (((OnlyCheckBoxListCellRenderer) c).selected) {
				Rectangle paintRect = new Rectangle(0, 0, c.getWidth(), c.getHeight());
				Image image;
				JList list = ((OnlyCheckBoxListCellRenderer) c).list;

				if (list != null && !list.isEnabled()) {
					image = BG_IMAGE_DISABLED;
				} else {
					image = BG_IMAGE;
				}

				OnlyUIUtil.paintImage(g, image, new Insets(1, 1, 1, 1), paintRect, c);
			} else {
				g.setColor(c.getBackground());
				g.fillRect(0, 0, c.getWidth(), c.getHeight());
			}
		}
	}

	public static class UIResource extends OnlyCheckBoxListCellRenderer implements javax.swing.plaf.UIResource {
		private static final long serialVersionUID = 3395332241183382595L;

		public UIResource(List<Object> selectedList) {
			super(selectedList);
		}
	}
}