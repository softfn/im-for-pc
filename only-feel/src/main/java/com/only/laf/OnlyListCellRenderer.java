package com.only.laf;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.io.Serializable;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicLabelUI;

import sun.awt.AppContext;

import com.only.OnlyList;
import com.only.box.UIBox;
import com.only.util.OnlyUIUtil;

@SuppressWarnings("rawtypes")
public class OnlyListCellRenderer extends JLabel implements ListCellRenderer, Serializable {
	
	private static final long serialVersionUID = 3622492300888739118L;

	private boolean selected;
	private JList list;

	public OnlyListCellRenderer() {
		super();
		setUI(new RendererUI());
		setBorder(UIBox.getBorder(UIBox.key_border_list_renderer));
	}

	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		if (value instanceof Icon) {
			setIcon((Icon) value);
		} else {
			setText((value == null) ? "" : value.toString());
		}

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

		setFont(list.getFont());
		setForeground(isSelected ? list.getSelectionForeground() : fg);
		return this;
	}

	@Deprecated
	public void updateUI() {
	}

	private static class RendererUI extends BasicLabelUI {
		
		protected static RendererUI rendererUI = new RendererUI();
		private static final Object RENDERER_UI_KEY = new Object();
		private static final Image BG_IMAGE = UIBox.getImage(UIBox.key_image_list_selected_item_normal_background);
		private static final Image BG_IMAGE_DISABLED = UIBox.getImage(UIBox.key_image_list_selected_item_disabled_background);

		public static ComponentUI createUI(JComponent c) {
			if (System.getSecurityManager() != null) {
				AppContext appContext = AppContext.getAppContext();
				RendererUI safeRendererUI = (RendererUI) appContext.get(RENDERER_UI_KEY);

				if (safeRendererUI == null) {
					safeRendererUI = new RendererUI();
					appContext.put(RENDERER_UI_KEY, safeRendererUI);
				}

				return safeRendererUI;
			}

			return rendererUI;
		}

		public void update(Graphics g, JComponent c) {
			if (c.isOpaque()) {
				paintBackground(g, c);
			}

			paint(g, c);
		}

		private void paintBackground(Graphics g, JComponent c) {
			if (((OnlyListCellRenderer) c).selected) {
				Rectangle paintRect = new Rectangle(0, 0, c.getWidth(), c.getHeight());
				Image image;
				JList list = ((OnlyListCellRenderer) c).list;

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

		protected void installDefaults(JLabel c) {
		}
	}
}