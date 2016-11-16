package com.only.laf;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.io.Serializable;

import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicLabelUI;

import sun.awt.AppContext;

import com.only.box.UIBox;
import com.only.util.OnlyUIUtil;

@SuppressWarnings("rawtypes")
public class OnlyComboBoxRenderer extends JLabel implements ListCellRenderer, Serializable {

	private static final long serialVersionUID = 3622492300888739118L;
	private static final Border NORMAL_BORDER = UIBox.getBorder(UIBox.key_border_combo_box_renderer_normal);
	public static final Border SELECTED_BORDER = UIBox.getBorder(UIBox.key_border_combo_box_renderer_selected);
	private JComboBox combo;

	public OnlyComboBoxRenderer(JComboBox combo) {
		super();
		setUI(new RendererUI());
		this.combo = combo;

	}

	@Override
	public Dimension getPreferredSize() {
		Dimension size;

		if ((this.getText() == null) || (this.getText().isEmpty())) {
			setText(" ");
			size = super.getPreferredSize();
			setText("");
		} else {
			size = super.getPreferredSize();
		}

		if (size.height < 20) {
			size.height = 20;
		}

		return size;
	}

	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		if (value instanceof Icon) {
			setIcon((Icon) value);
		} else {
			setText((value == null) ? "" : value.toString());
		}

		setBorder(NORMAL_BORDER);
		setOpaque(isSelected);
		setFont(combo.getFont());
		setForeground(isSelected ? UIBox.getColor(UIBox.key_color_combo_box_text_selection_foreground) : combo.getForeground());
		return this;
	}

	@Deprecated
	@Override
	public void updateUI() {
	}

	private static class RendererUI extends BasicLabelUI {

		protected static RendererUI rendererUI = new RendererUI();
		private static final Object RENDERER_UI_KEY = new Object();
		private static final Image BG_IMAGE = UIBox.getImage(UIBox.key_image_combo_box_selected_item_background);

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

		@Override
		public void update(Graphics g, JComponent c) {
			if (c.isOpaque()) {
				paintBackground(g, c);
			}

			paint(g, c);
		}

		private void paintBackground(Graphics g, JComponent c) {
			Rectangle paintRect = new Rectangle(0, 0, c.getWidth(), c.getHeight());
			OnlyUIUtil.paintImage(g, BG_IMAGE, new Insets(1, 1, 1, 1), paintRect, c);
		}

		@Override
		protected void installDefaults(JLabel c) {
		}

		@Override
		protected void uninstallDefaults(JLabel c) {
		}
	}
}