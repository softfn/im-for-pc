/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oim.common.component;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import com.oim.common.component.common.OurValue;
import com.oim.common.component.table.OurTableCellRenderer;
import com.only.OnlyTable;

/**
 * 2013-8-29 15:25:41
 *
 * @author XiaHui
 */
@SuppressWarnings("serial")
public class OurTable extends OnlyTable {

    public boolean autoResizeTable = true;

    public OurTable() {
    }


    @SuppressWarnings("rawtypes")
    @Override
    public TableCellRenderer getDefaultRenderer(Class<?> columnClass) {
        if (columnClass == null) {
            return new OurTableCellRenderer();
        } else if (Integer.class.isAssignableFrom(columnClass)) {
            return new OurTableCellRenderer();
        } else if (OurValue.class.isAssignableFrom(columnClass) || String.class.isAssignableFrom(columnClass) || Object[].class.isAssignableFrom(columnClass)) {
            return new OurTableCellRenderer();
        } else if (Object.class.isAssignableFrom(columnClass) && !Boolean.class.isAssignableFrom(columnClass)) {
            return new OurTableCellRenderer();
        } else {
            Object renderer = defaultRenderersByColumnClass.get(columnClass);
            if (renderer != null) {
                return (TableCellRenderer) renderer;
            } else {
                Class c = columnClass.getSuperclass();
                if (c == null && columnClass != Object.class) {
                    c = Object.class;
                }
                return getDefaultRenderer(c);
            }
        }
    }

    public void setSuperComponentWidth(int thisWidth, int width) {
        if (thisWidth > width) {
            if (autoResizeTable) {
                setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                autoResizeTable = false;
            }
        } else {
            if (!autoResizeTable) {
                setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
                autoResizeTable = true;
            }
        }
    }
}
