package com.over.component.tree;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.lang.reflect.Constructor;

import javax.swing.DefaultCellEditor;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;

import com.only.component.ImageBorder;
import com.only.util.OnlyUIUtil;
import com.over.OverTreeTable;

public class OverTreeColumnCellEditor extends DefaultCellEditor {
	private static final long serialVersionUID = 2475808674866003675L;

	private static final Border ERROR_BORDER = new LineBorder(Color.RED);

	private static final Border GENERIC_BORDER = new LineBorder(Color.BLACK);

	private JTextField field;

	private Border insideBorder;

	private Class<?>[] argTypes;

	private Constructor<?> constructor;

	private Object value;

	public OverTreeColumnCellEditor() {
		super(new JTextField());
		getComponent().setName("TreeColumn.editor");
		argTypes = new Class[] { String.class };
		field = (JTextField) getComponent();
	}

	public boolean stopCellEditing() {
		String value = (String) super.getCellEditorValue();

		if ("".equals(value)) {
			if (constructor.getDeclaringClass() == String.class) {
				this.value = value;
			}

			super.stopCellEditing();
		}

		try {
			this.value = constructor.newInstance(new Object[] { value });
		} catch (Exception e) {
			field.setBorder(new CompoundBorder(ERROR_BORDER, insideBorder));
			return false;
		}

		return super.stopCellEditing();
	}

	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		this.value = null;
		table.clearSelection();

		if (table instanceof OverTreeTable) {
			Insets insets = GENERIC_BORDER.getBorderInsets(field);
			Rectangle rect = table.getCellRect(row, column, false);
			rect.x += insets.left;
			rect.y += insets.top;
			rect.width -= insets.left + insets.right;
			rect.height -= insets.top + insets.bottom;
			BufferedImage image = OnlyUIUtil.getGraphicsConfiguration(editorComponent).createCompatibleImage(rect.width, rect.height, Transparency.TRANSLUCENT);
			Graphics2D g2d = image.createGraphics();
			g2d.translate(-rect.x, -rect.y);
			table.paint(g2d);
			g2d.dispose();
			OverTreeTable treeTable = (OverTreeTable) table;
			JTree tree = treeTable.getTree();
			JLabel renderer = (JLabel) tree.getCellRenderer();
			int textStart = treeTable.getTreeRendererTextStartAt(row);

			if (textStart < 0) {
				Icon icon = renderer.getIcon();
				textStart = renderer.getInsets().left + (icon == null ? 0 : icon.getIconWidth() + renderer.getIconTextGap());
			}

			insideBorder = new ImageBorder(image, 0, tree.getRowBounds(row).x + textStart - insets.left, 0, 0);
		}

		field.setBorder(new CompoundBorder(GENERIC_BORDER, insideBorder));

		try {
			Class<?> type = table.getColumnClass(column);

			if (type == Object.class) {
				type = String.class;
			}

			constructor = type.getConstructor(argTypes);
		} catch (Exception e) {
			return null;
		}

		return super.getTableCellEditorComponent(table, value, isSelected, row, column);
	}

	public Object getCellEditorValue() {
		return value;
	}
}