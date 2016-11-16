package com.only.laf;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicLabelUI;
import javax.swing.table.DefaultTableCellRenderer;

import sun.awt.AppContext;

import com.only.OnlyTable;
import com.only.box.UIBox;
import com.only.component.GridBorder;
import com.only.util.OnlyUIUtil;

public class OnlyTableCellRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = 6677889202548351952L;

	private static final GridBorder GRID_BORDER = new GridBorder(Color.GRAY, 0, 0, 0, 0);
	private boolean selected;
	private int column;
	private JTable table;
	private Border cellBorder;
	private Border cellInsideBorder;
	private boolean useDefaultBackgroundColor = true;

	public OnlyTableCellRenderer() {
		super();
		setUI(new RendererUI());
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		this.selected = isSelected;
		this.column = column;
		this.table = table;
		Color fg = table.getForeground();

		if (table instanceof OnlyTable && !table.isEnabled()) {
			fg = ((OnlyTable) table).getDisabledForeground();
		}

		if (table instanceof OnlyTable) {
			if (useDefaultBackgroundColor) {
				OnlyTable onlyTable = (OnlyTable) table;
				Color color1 = onlyTable.getRendererBackground1();
				Color color2 = onlyTable.getRendererBackground2();
				setOpaque(isSelected || (onlyTable.isRendererOpaque() && color1 != null && color2 != null));
				Color color = onlyTable.getCellRendererBackgroundAt(row, column);
				if (null == color) {
					setBackground(row % 2 == 0 ? color1 : color2);
				} else {
					setBackground(color);
				}
			}
		} else {
			setOpaque(isSelected);
		}

		setFont(table.getFont());
		setForeground(isSelected ? table.getSelectionForeground() : fg);
		setText((value == null) ? "" : value.toString());
		showGridLine();
		return this;
	}

	private void showGridLine() {
		if (table instanceof OnlyTable) {
			OnlyTable cTable = (OnlyTable) table;
			boolean showColumnLines = cTable.isShowColumnLines();
			boolean showRowLines = cTable.isShowRowLines();
			int right = (showColumnLines && column < table.getColumnCount() - 1) ? 1 : 0;
			int bottom = showRowLines ? 1 : 0;
			GRID_BORDER.setColor(table.getGridColor());
			GRID_BORDER.setInsets(0, 0, bottom, right);

			if (cellBorder == null || cellInsideBorder != cTable.getCellInsideBorder()) {
				setBorder(cellBorder = new CompoundBorder(GRID_BORDER, cellInsideBorder = cTable.getCellInsideBorder()));
			} else {
				setBorder(cellBorder);
			}
		}
	}

	public boolean isUseDefaultBackgroundColor() {
		return useDefaultBackgroundColor;
	}

	public void setUseDefaultBackgroundColor(boolean useDefaultBackgroundColor) {
		if (this.useDefaultBackgroundColor != useDefaultBackgroundColor) {
			this.useDefaultBackgroundColor = useDefaultBackgroundColor;
			this.repaint();
		}
	}

	@Deprecated
	public void updateUI() {
	}

	private static class RendererUI extends BasicLabelUI {

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
			OnlyTableCellRenderer renderer = (OnlyTableCellRenderer) c;

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

		protected void installDefaults(JLabel c) {
		}
	}

	public static class UIResource extends OnlyTableCellRenderer implements javax.swing.plaf.UIResource {
		private static final long serialVersionUID = 416233352847216000L;
	}
}