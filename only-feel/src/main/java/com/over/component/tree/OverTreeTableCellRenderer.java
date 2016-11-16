package com.over.component.tree;

import com.only.component.GridBorder;
import com.only.component.tree.AbstractTreeTableModel;
import com.over.OverTreeTable;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.lang.reflect.Field;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.DefaultTreeCellRenderer;

import sun.swing.DefaultLookup;

public class OverTreeTableCellRenderer extends JTree implements TableCellRenderer {
	private static final long serialVersionUID = 913551938920374007L;

	private static final Border SAFE_NO_FOCUS_BORDER = new EmptyBorder(1, 1, 1, 1);

	private static final Border DEFAULT_NO_FOCUS_BORDER = new EmptyBorder(1, 1, 1, 1);

	private static final GridBorder GRID_BORDER = new GridBorder(Color.WHITE, 0, 0, 0, 0);

	protected Border noFocusBorder = DEFAULT_NO_FOCUS_BORDER;

	private Border treeBorder;

	private OverTreeTable treeTable;

	private int visibleRow;

	private boolean hasFocus, isSelected;

	public OverTreeTableCellRenderer(OverTreeTable treeTable, AbstractTreeTableModel model) {
		super(model);
		this.treeTable = treeTable;
		model.setTree(this);
		setEditable(false);
		setShowsRootHandles(true);
		setCellRenderer(new TreeCellRenderer());
		((JComponent) getCellRenderer()).setOpaque(true);
		setPaintLines(false);
	}

	public void setPaintLines(boolean paintLines) {
		try {
			Field paintLinesField = BasicTreeUI.class.getDeclaredField("paintLines");
			paintLinesField.setAccessible(true);
			paintLinesField.set((BasicTreeUI) getUI(), paintLines);
			treeTable.repaint();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, 0, width, treeTable.getHeight());
	}

	public void paint(Graphics g) {
		int rowMargin = treeTable.getRowMargin();
		int columnMargin = treeTable.getColumnModel().getColumnMargin();
		int deltaY = -(visibleRow * getRowHeight() + rowMargin / 2 - (rowMargin + 1) % 2 + (treeBorder == null ? 0 : treeBorder.getBorderInsets(this).top));
		Rectangle paintRect = new Rectangle(0, 0, this.getWidth(), this.getRowHeight() - rowMargin);
		g.translate(0, deltaY);
		super.paint(g);
		g.translate(0, -deltaY);

		if (treeBorder != null) {
			treeBorder.paintBorder(this, g, paintRect.x, paintRect.y, paintRect.width, paintRect.height);
		}

		if (!isSelected && !hasFocus && (columnMargin <= 0 || rowMargin <= 0)) {
			paintRect.width -= (columnMargin > 0 ? 0 : Math.round(-columnMargin / 2.0f));
			GRID_BORDER.setColor(treeTable.getGridColor());
			GRID_BORDER.setInsets(0, 0, rowMargin > 0 ? 0 : 1, columnMargin > 0 ? 0 : 1);
			GRID_BORDER.paintBorder(this, g, paintRect.x, paintRect.y, paintRect.width, paintRect.height);
		}
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
		setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
		setOpaque(table.isOpaque());
		setFont(table.getFont());
		this.visibleRow = row;
		this.isSelected = isSelected;
		this.hasFocus = hasFocus;

		if (hasFocus) {
			treeBorder = null;

			if (isSelected) {
				treeBorder = DefaultLookup.getBorder(this, ui, "Table.focusSelectedCellHighlightBorder");
			}

			if (treeBorder == null) {
				treeBorder = DefaultLookup.getBorder(this, ui, "Table.focusCellHighlightBorder");
			}
		} else {
			treeBorder = getNoFocusBorder();
		}

		return this;
	}

	private Border getNoFocusBorder() {
		Border border = DefaultLookup.getBorder(this, ui, "Table.cellNoFocusBorder");

		if (System.getSecurityManager() != null) {
			if (border != null) {
				return border;
			}

			return SAFE_NO_FOCUS_BORDER;
		} else if (border != null && (noFocusBorder == null || noFocusBorder == DEFAULT_NO_FOCUS_BORDER)) {
			return border;
		}

		return noFocusBorder;
	}

	private class TreeCellRenderer extends DefaultTreeCellRenderer {
		private static final long serialVersionUID = -7459869452121826190L;

		private int row;

		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
			this.row = row;
			super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
			setForeground(tree.getForeground());
			return this;
		}

		public void paint(Graphics g) {
			Icon icon = this.getIcon();
			treeTable.setTreeRendererTextStartAt(row, getInsets().left + (icon == null ? 0 : icon.getIconWidth() + getIconTextGap()));
			super.paint(g);
		}
	}
}