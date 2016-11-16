package com.oim.common.component.table;

import java.awt.Component;

import javax.swing.JTable;

import com.oim.common.component.common.OurValue;
import com.only.laf.OnlyTableCellRenderer;

@SuppressWarnings("serial")
public class OurTableCellRenderer extends OnlyTableCellRenderer {

    public OurTableCellRenderer() {
        super();
        this.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        if (value instanceof OurValue) {
            super.getTableCellRendererComponent(table, null, isSelected, hasFocus, row, column);
            OurValue tableValue = (OurValue) value;
            setText((tableValue.toStringValue() == null) ? "" : tableValue.toStringValue());
            return this;
        } else if (value instanceof Object[]) {
            super.getTableCellRendererComponent(table, null, isSelected, hasFocus, row, column);
            Object[] array = (Object[]) value;
            setText((array.length > 1 && array[0] == null) ? "" : array[0].toString());
            return this;
        } else {
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
    }
}
