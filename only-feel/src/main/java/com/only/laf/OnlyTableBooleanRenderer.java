package com.only.laf;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.table.TableCellRenderer;

import sun.awt.AppContext;

import com.only.OnlyCheckBox;
import com.only.OnlyTable;
import com.only.box.UIBox;
import com.only.component.GridBorder;
import com.only.util.OnlyUIUtil;

public class OnlyTableBooleanRenderer extends OnlyCheckBox implements TableCellRenderer, UIResource {
	private static final long serialVersionUID = 7496257651303408385L;

	private static final GridBorder GRID_BORDER = new GridBorder(Color.GRAY, 0, 0, 0, 0);

	private boolean selected;

	private int column;

	private JTable table;

	public OnlyTableBooleanRenderer() {
		super();
		setUI(new RendererUI());
		setHorizontalAlignment(OnlyCheckBox.CENTER);
		setBorderPainted(true);
		setFocusPainted(false);
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		this.selected = isSelected;
		this.column = column;
		this.table = table;
		Color fg = table.getForeground();

		if (table instanceof OnlyTable && !table.isEnabled()) {
			fg = ((OnlyTable) table).getDisabledForeground();
		}

		if (table instanceof OnlyTable) {
			OnlyTable cTable = (OnlyTable) table;
			Color color1 = cTable.getRendererBackground1();
			Color color2 = cTable.getRendererBackground2();
			setOpaque(isSelected || (cTable.isRendererOpaque() && color1 != null && color2 != null));
			setBackground(row % 2 == 0 ? color1 : color2);
		} else {
			setOpaque(isSelected);
		}

		setEnabled(table.isEnabled());
		setFont(table.getFont());
		setForeground(isSelected ? table.getSelectionForeground() : fg);
		setSelected((value != null && ((Boolean) value).booleanValue()));
		showGridLine();
		return this;
	}

	private void showGridLine() {
		if (table instanceof OnlyTable) {
			OnlyTable cTable = (OnlyTable) table;
			boolean showColumnLines = cTable.isShowColumnLines();
			boolean showRowLines = cTable.isShowRowLines();
			int right = (showColumnLines && column < table.getColumnCount() - 1) ? 1 : 0;
			int bottom = (showRowLines) ? 1 : 0;
			GRID_BORDER.setColor(table.getGridColor());
			GRID_BORDER.setInsets(0, 0, bottom, right);
			setBorder(GRID_BORDER);
		}
	}

	private static class RendererUI extends OnlyCheckBoxUI {
		protected static RendererUI rendererUI = new RendererUI();

		private static final Object RENDERER_UI_KEY = new Object();

		private static final Image BG_IMAGE = UIBox.getImage(UIBox.key_image_table_selected_item_normal_background);
		private static final Image BG_IMAGE_DISABLED = UIBox.getImage(UIBox.key_image_table_selected_item_disabled_background);

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
			OnlyTableBooleanRenderer renderer = (OnlyTableBooleanRenderer) c;

			if (renderer.selected) {
				Rectangle paintRect = new Rectangle(0, 0, c.getWidth(), c.getHeight());
				int columnCount = renderer.table.getColumnCount();
				Image image;

				if (renderer.table != null && !renderer.table.isEnabled()) {
					image = BG_IMAGE_DISABLED;
				} else {
					image = BG_IMAGE;
				}

				if (columnCount > 1) {
					int width = image.getWidth(null);
					int height = image.getHeight(null);

					if (renderer.column == 0) {
						image = OnlyUIUtil.cutImage(image, new Rectangle(0, 0, width - 1, height), c);
					} else if (renderer.column == columnCount - 1) {
						image = OnlyUIUtil.cutImage(image, new Rectangle(1, 0, width - 1, height), c);
					} else {
						image = OnlyUIUtil.cutImage(image, new Rectangle(1, 0, width - 2, height), c);
					}
				}

				OnlyUIUtil.paintImage(g, image, new Insets(1, 1, 1, 1), paintRect, c);
			} else {
				g.setColor(c.getBackground());
				g.fillRect(0, 0, c.getWidth(), c.getHeight());
			}
		}
	}

	public static class UIResource extends OnlyTableBooleanRenderer implements javax.swing.plaf.UIResource {
		private static final long serialVersionUID = 7539959665271865113L;
	}
}