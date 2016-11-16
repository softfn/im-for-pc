package com.only.component.tree;

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
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicTextFieldUI;
import javax.swing.table.TableCellRenderer;

import com.only.OnlyTable;
import com.only.OnlyTreeTable;
import com.only.box.UIBox;
import com.only.component.ImageBorder;
import com.only.util.OnlyUIUtil;

public class OnlyTreeColumnCellEditor extends DefaultCellEditor {

    private static final long serialVersionUID = 2475808674866003675L;
    private static final Border GENERIC_BORDER = UIBox.getBorder(UIBox.key_border_tree_column_editor);
    private static final Border ERROR_BORDER = new CompoundBorder(new LineBorder(Color.RED, 2), new EmptyBorder(0, 0, 0, 1));
    private static final Color SELECTION_COLOR = UIBox.getColor(UIBox.key_color_tree_column_text_selection);
    private JTextField field;
    private Border insideBorder;
    private Class<?>[] argTypes;
    private Constructor<?> constructor;
    private Object value;

    public OnlyTreeColumnCellEditor() {
        super(new JTextField() {
            private static final long serialVersionUID = -5666529738511463777L;

            @Deprecated
            @Override
            public void updateUI() {
            }
        });
        getComponent().setName("TreeColumn.editor");
        argTypes = new Class[]{String.class};
        field = (JTextField) getComponent();
        field.setUI(new BasicTextFieldUI());
        field.setMargin(new Insets(0, 0, 0, 0));
        field.setCaretColor(Color.BLACK);
        field.setSelectionColor(SELECTION_COLOR);
        field.setSelectedTextColor(UIBox.getColor(UIBox.key_color_tree_column_text_selection_foreground));
    }
    
//key_text_selection_foreground
    @Override
    public boolean stopCellEditing() {
        String value = (String) super.getCellEditorValue();

        if ("".equals(value)) {
            if (constructor.getDeclaringClass() == String.class) {
                this.value = value;
            }

            super.stopCellEditing();
        }

        try {
            this.value = constructor.newInstance(new Object[]{value});
        } catch (Exception e) {
            field.setBorder(new CompoundBorder(ERROR_BORDER, insideBorder));
            return false;
        }

        return super.stopCellEditing();
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.value = null;
        table.clearSelection();

        if (table instanceof OnlyTreeTable) {
            Insets insets = GENERIC_BORDER.getBorderInsets(field);
            Rectangle rect = table.getCellRect(row, column, false);
            rect.x += insets.left;
            rect.y += insets.top;
            rect.width -= insets.left + insets.right;
            rect.height -= insets.top + insets.bottom;
            BufferedImage image = OnlyUIUtil.getGraphicsConfiguration(editorComponent).createCompatibleImage(rect.width, rect.height,
                    Transparency.TRANSLUCENT);
            Graphics2D g2d = image.createGraphics();
            g2d.translate(-rect.x, -rect.y);
            table.paint(g2d);
            g2d.dispose();
            OnlyTreeTable treeTable = (OnlyTreeTable) table;
            JTree tree = treeTable.getTree();
            JLabel treeRenderer = (JLabel) tree.getCellRenderer();
            int textStart = treeTable.getTreeRendererTextStartAt(row);

            if (textStart < 0) {
                Icon icon = treeRenderer.getIcon();
                textStart = treeRenderer.getInsets().left + (icon == null ? 0 : icon.getIconWidth() + treeRenderer.getIconTextGap());
            }

            insideBorder = new ImageBorder(image, 0, tree.getRowBounds(row).x + textStart - insets.left, 0, 0);
        }

        TableCellRenderer renderer = table.getCellRenderer(row, column);
        Component c = renderer.getTableCellRendererComponent(table, value, isSelected, true, row, column);
        field.setFont(c.getFont());
        field.setBackground(OnlyTable.createEditorBackground(table, c));
        field.setForeground(table.getForeground());
        field.setBorder(new CompoundBorder(GENERIC_BORDER, insideBorder));
        field.setOpaque(field.getBackground() != null);

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