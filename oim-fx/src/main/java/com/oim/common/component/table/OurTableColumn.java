/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oim.common.component.table;

import javax.swing.JLabel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**
 * 2013-9-5 10:02:06
 *
 * @author XiaHui
 */
@SuppressWarnings("serial")
public class OurTableColumn extends TableColumn {

    protected OurTableEnterAction ourTableEnterAction;

    public OurTableColumn() {
        this(0);
    }

    public OurTableColumn(int modelIndex) {
        super(modelIndex);
    }

    public OurTableColumn(int modelIndex, int width) {
        super(modelIndex, width, null, null);
    }

    public OurTableColumn(int modelIndex, int width, TableCellRenderer cellRenderer, TableCellEditor cellEditor) {
        super(modelIndex, width, cellRenderer, cellEditor);
    }

    @Override
    protected TableCellRenderer createDefaultHeaderRenderer() {
        DefaultTableCellRenderer label = new OurTableCellRenderer();
        label.setHorizontalAlignment(JLabel.CENTER);
        return label;
    }

    @Override
    public void setCellEditor(TableCellEditor cellEditor) {
        super.setCellEditor(cellEditor);
        if (cellEditor instanceof OurTableCellEditor) {
            ((OurTableCellEditor) cellEditor).setOurTableEnterAction(ourTableEnterAction);
        }
    }

    public OurTableEnterAction getOurTableEnterAction() {
        return ourTableEnterAction;
    }

    public void setOurTableEnterAction(OurTableEnterAction ourTableEnterAction) {
        this.ourTableEnterAction = ourTableEnterAction;
    }
}
