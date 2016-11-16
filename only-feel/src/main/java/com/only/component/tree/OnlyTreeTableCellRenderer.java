package com.only.component.tree;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;

import com.only.OnlyTree;
import com.only.OnlyTreeTable;
import com.only.box.UIBox;
import com.only.component.GridBorder;
import com.only.laf.OnlyTreeUI.CTreeCellRenderer;
import com.only.util.OnlyUIUtil;

public class OnlyTreeTableCellRenderer extends OnlyTree implements TableCellRenderer {
	
	private static final long serialVersionUID = 913551938920374007L;

	private static final Image BG_IMAGE = UIBox.getImage(UIBox.key_image_tree_table_cell_selected_item_normal_background);
	private static final Image BG_IMAGE_DISABLED = UIBox.getImage(UIBox.key_image_tree_table_cell_selected_item_disabled_background);
	
	private static final GridBorder GRID_BORDER = new GridBorder(Color.GRAY, 0, 0, 0, 0);
	private int visibleRow;
	private OnlyTreeTable treeTable;
	private boolean selected;
	private int column;

	private boolean paintBackground;

	public OnlyTreeTableCellRenderer(OnlyTreeTable treeTable, AbstractTreeTableModel model) {
		super(model);
		this.treeTable = treeTable;
		model.setTree(this);
		setEditable(false);
		setAlpha(0.0f);
		setCellRenderer(new TreeCellRenderer());
		((JComponent) getCellRenderer()).setOpaque(false);
		setPaintLines(false);
		setBorder(new EmptyBorder(0, 5, 0, 0));
	}

	public void setPaintLines(boolean paintLines) {
		super.setPaintLines(paintLines);
		treeTable.repaint();
	}

	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, 0, width, treeTable.getHeight());
	}

	public void paint(Graphics g) {
		int deltaY = -(visibleRow * getRowHeight() + 1);
		Rectangle paintRect = new Rectangle(0, 0, this.getWidth(), this.getRowHeight());

		if (paintBackground) {
			if (selected) {
				int columnCount = treeTable.getColumnCount();
				Image image = treeTable.isEnabled() ? BG_IMAGE : BG_IMAGE_DISABLED;

				if (columnCount > 1) {
					int width = image.getWidth(null);
					int height = image.getHeight(null);

					if (column == 0) {
						image = OnlyUIUtil.cutImage(image, new Rectangle(0, 0, width - 1, height), this);
					} else if (column == columnCount - 1) {
						image = OnlyUIUtil.cutImage(image, new Rectangle(1, 0, width - 1, height), this);
					} else {
						image = OnlyUIUtil.cutImage(image, new Rectangle(1, 0, width - 2, height), this);
					}
				}

				OnlyUIUtil.paintImage(g, image, new Insets(1, 1, 1, 1), paintRect, this);
			} else {
				g.setColor(this.getBackground());
				g.fillRect(paintRect.x, paintRect.y, paintRect.width, paintRect.height);
			}
		}

		g.translate(0, deltaY);
		super.paint(g);
		g.translate(0, -deltaY);
		GRID_BORDER.paintBorder(this, g, paintRect.x, paintRect.y, paintRect.width, paintRect.height);
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		this.selected = isSelected;
		this.column = column;
		this.visibleRow = row;
		Color fg = treeTable.isEnabled() ? treeTable.getForeground() : treeTable.getDisabledForeground();
		Color color1 = treeTable.getRendererBackground1();
		Color color2 = treeTable.getRendererBackground2();
		paintBackground = isSelected || (treeTable.isRendererOpaque() && color1 != null && color2 != null);
		setBackground(row % 2 == 0 ? color1 : color2);
		setFont(table.getFont());
		setForeground(isSelected ? table.getSelectionForeground() : fg);
		showGridLine();
		return this;
	}

	private void showGridLine() {
		boolean showColumnLines = treeTable.isShowColumnLines();
		boolean showRowLines = treeTable.isShowRowLines();
		int right = (showColumnLines && column < treeTable.getColumnCount() - 1) ? 1 : 0;
		int bottom = showRowLines ? 1 : 0;
		GRID_BORDER.setColor(treeTable.getGridColor());
		GRID_BORDER.setInsets(0, 0, bottom, right);
	}

	private class TreeCellRenderer extends CTreeCellRenderer {
		private static final long serialVersionUID = -7459869452121826190L;

		private int row;

		public TreeCellRenderer() {
			super();
		}

		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
			this.row = row;
			super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
			return this;
		}

		public void paint(Graphics g) {
			Icon icon = this.getIcon();
			treeTable.setTreeRendererTextStartAt(row, getInsets().left + (icon == null ? 0 : icon.getIconWidth() + getIconTextGap()));
			super.paint(g);
		}
	}
}