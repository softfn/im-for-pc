/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oim.common.component.table;

import java.util.Vector;

import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author XiaHui
 */
@SuppressWarnings({"serial", "rawtypes"})
public class OurTableModel extends DefaultTableModel {
	 //protected Vector dataVector;
    //protected Vector columnIdentifiers;
    private Vector<Vector<Boolean>> canEdit;
    //private boolean[][] canEditArray;

    public OurTableModel() {
        this(0, 0);
    }

    private static Vector newVector(int size) {
        Vector v = new Vector(size);
        v.setSize(size);
        return v;
    }

    public OurTableModel(int rowCount, int columnCount) {
        this(newVector(columnCount), rowCount);
    }

    public OurTableModel(Vector columnNames, int rowCount) {
        setDataVector(newVector(rowCount), columnNames);
    }

    public OurTableModel(Object[] columnNames, int rowCount) {
        this(convertToVector(columnNames), rowCount);
    }

    public OurTableModel(Vector data, Vector columnNames) {
        setDataVector(data, columnNames);
    }

    public OurTableModel(Object[][] data, Object[] columnNames) {
        setDataVector(data, columnNames);
    }

    @Override
    public Vector getDataVector() {
        return dataVector;
    }

    private static Vector nonNullVector(Vector v) {
        return (v != null) ? v : new Vector();
    }

    @Override
    public void setDataVector(Vector dataVector, Vector columnIdentifiers) {
        this.dataVector = nonNullVector(dataVector);
        this.columnIdentifiers = nonNullVector(columnIdentifiers);
        justifyRows(0, getRowCount());
        fireTableStructureChanged();
    }

    public void setDataVector(Vector dataVector) {
        this.dataVector = nonNullVector(dataVector);
        justifyRows(0, getRowCount());
        fireTableStructureChanged();
    }

    public void setDataVector(Object[][] dataVector) {
        setDataVector(convertToVector(dataVector));
    }

    @Override
    public void setDataVector(Object[][] dataVector, Object[] columnIdentifiers) {
        setDataVector(convertToVector(dataVector), convertToVector(columnIdentifiers));
    }

    @Override
    public void newDataAvailable(TableModelEvent event) {
        fireTableChanged(event);
    }

    @SuppressWarnings("unchecked")
    private void justifyRows(int from, int to) {

        dataVector.setSize(getRowCount());

        for (int i = from; i < to; i++) {
            if (dataVector.elementAt(i) == null) {
                dataVector.setElementAt(new Vector(), i);
            }
            ((Vector) dataVector.elementAt(i)).setSize(getColumnCount());
        }
    }

    @Override
    public void newRowsAdded(TableModelEvent e) {
        justifyRows(e.getFirstRow(), e.getLastRow() + 1);
        fireTableChanged(e);
    }

    @Override
    public void rowsRemoved(TableModelEvent event) {
        fireTableChanged(event);
    }

    @Override
    public void setNumRows(int rowCount) {
        int old = getRowCount();
        if (old == rowCount) {
            return;
        }
        dataVector.setSize(rowCount);
        if (rowCount <= old) {
            fireTableRowsDeleted(rowCount, old - 1);
        } else {
            justifyRows(old, rowCount);
            fireTableRowsInserted(old, rowCount - 1);
        }
    }

    @Override
    public void setRowCount(int rowCount) {
        setNumRows(rowCount);
    }

    @Override
    public void addRow(Vector rowData) {
        insertRow(getRowCount(), rowData);
        if (null != this.canEdit && !this.canEdit.isEmpty()) {
            Vector<Boolean> edit = new Vector<Boolean>();
            for (Boolean b : canEdit.get(0)) {
                edit.add(b);
            }
            this.addOneCanEdit(edit);
        }
    }

    @Override
    public void addRow(Object[] rowData) {
        addRow(convertToVector(rowData));
    }

    @SuppressWarnings("unchecked")
    @Override
    public void insertRow(int row, Vector rowData) {
        dataVector.insertElementAt(rowData, row);
        justifyRows(row, row + 1);
        fireTableRowsInserted(row, row);
    }

    @Override
    public void insertRow(int row, Object[] rowData) {
        insertRow(row, convertToVector(rowData));
    }

    private static int gcd(int i, int j) {
        return (j == 0) ? i : gcd(j, i % j);
    }

    @SuppressWarnings("unchecked")
    private static void rotate(Vector v, int a, int b, int shift) {
        int size = b - a;
        int r = size - shift;
        int g = gcd(size, r);
        for (int i = 0; i < g; i++) {
            int to = i;
            Object tmp = v.elementAt(a + to);
            for (int from = (to + r) % size; from != i; from = (to + r) % size) {
                v.setElementAt(v.elementAt(a + from), a + to);
                to = from;
            }
            v.setElementAt(tmp, a + to);
        }
    }

    @Override
    public void moveRow(int start, int end, int to) {
        int shift = to - start;
        int first, last;
        if (shift < 0) {
            first = to;
            last = end;
        } else {
            first = start;
            last = to + end - start;
        }
        rotate(dataVector, first, last + 1, shift);

        fireTableRowsUpdated(first, last);
    }

    @Override
    public void removeRow(int row) {
        dataVector.removeElementAt(row);
        fireTableRowsDeleted(row, row);
    }

    @Override
    public void setColumnIdentifiers(Vector columnIdentifiers) {
        setDataVector(dataVector, columnIdentifiers);
    }

    @Override
    public void setColumnIdentifiers(Object[] newIdentifiers) {
        setColumnIdentifiers(convertToVector(newIdentifiers));
    }

    @Override
    public void setColumnCount(int columnCount) {
        columnIdentifiers.setSize(columnCount);
        justifyRows(0, getRowCount());
        fireTableStructureChanged();
    }

    @Override
    public void addColumn(Object columnName) {
        addColumn(columnName, (Vector) null);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void addColumn(Object columnName, Vector columnData) {
        columnIdentifiers.addElement(columnName);
        if (columnData != null) {
            int columnSize = columnData.size();
            if (columnSize > getRowCount()) {
                dataVector.setSize(columnSize);
            }
            justifyRows(0, getRowCount());
            int newColumn = getColumnCount() - 1;
            for (int i = 0; i < columnSize; i++) {
                Vector row = (Vector) dataVector.elementAt(i);
                row.setElementAt(columnData.elementAt(i), newColumn);
            }
        } else {
            justifyRows(0, getRowCount());
        }

        fireTableStructureChanged();
    }

    @Override
    public void addColumn(Object columnName, Object[] columnData) {
        addColumn(columnName, convertToVector(columnData));
    }

    @Override
    public int getRowCount() {
        return dataVector.size();
    }

    @Override
    public int getColumnCount() {
        return columnIdentifiers.size();
    }

    @Override
    public String getColumnName(int column) {
        Object id = null;
        if (column < columnIdentifiers.size() && (column >= 0)) {
            id = columnIdentifiers.elementAt(column);
        }
        return (id == null) ? super.getColumnName(column) : id.toString();
    }

    @Override
    public boolean isCellEditable(int row, int column) {

        if (null != canEdit && !canEdit.isEmpty()) {
            if (canEdit.size() > row) {
                Vector rowVector = (Vector) canEdit.elementAt(row);
                if (rowVector.size() > column) {
                    return (Boolean) rowVector.elementAt(column);
                } else {
                    return true;
                }
            } else {
                Vector rowVector = (Vector) canEdit.elementAt(0);
                if (rowVector.size() > column) {
                    return (Boolean) rowVector.elementAt(column);
                } else {
                    return true;
                }
            }
        }
        return true;
    }

    @Override
    public Object getValueAt(int row, int column) {
        if (dataVector.size() > row && row > -1) {
            Vector rowVector = (Vector) dataVector.elementAt(row);
            if (rowVector.size() > column) {
                return rowVector.elementAt(column);
            } else {
                return null;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setValueAt(Object aValue, int row, int column) {
        if (dataVector.size() > row && row >= 0) {
            Vector rowVector = (Vector) dataVector.elementAt(row);
            if (null != rowVector && rowVector.size() > column && column >= 0) {
                rowVector.setElementAt(aValue, column);
                fireTableCellUpdated(row, column);
            }
        }
    }

    protected static Vector convertToVector(Object[][] anArray) {
        if (anArray == null) {
            return null;
        }
        Vector<Vector> v = new Vector<Vector>(anArray.length);
        for (Object[] o : anArray) {
            v.addElement(convertToVector(o));
        }
        return v;
    }

    protected static Vector convertToVector(Object[] anArray) {
        if (anArray == null) {
            return null;
        }
        Vector<Object> v = new Vector<Object>(anArray.length);
        for (Object o : anArray) {
            v.addElement(o);
        }
        return v;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class getColumnClass(int c) {
        if (null == getValueAt(0, c)) {
            return super.getColumnClass(c);
        }
        return getValueAt(0, c).getClass();
    }

    protected static Vector convertToVector(boolean[][] anArray) {
        if (anArray == null) {
            return null;
        }
        Vector<Vector> v = new Vector<Vector>(anArray.length);
        for (boolean[] o : anArray) {
            v.addElement(convertToVector(o));
        }
        return v;
    }

    protected static Vector convertToVector(boolean[] anArray) {
        if (anArray == null) {
            return null;
        }
        Vector<Object> v = new Vector<Object>(anArray.length);
        for (Object o : anArray) {
            v.addElement(o);
        }
        return v;
    }

    @SuppressWarnings("unchecked")
    public void setCanEdit(boolean[][] canEdit) {
        //this.canEditArray = canEdit;
        setCanEdit(convertToVector(canEdit));
    }

    @SuppressWarnings("unchecked")
    public void setCanEdit(Vector<Vector<Boolean>> canEdit) {
        this.canEdit = nonNullVector(canEdit);
        // justifyCanEditRows(0, canEdit.size());
    }

    @SuppressWarnings("unchecked")
    public void setCanEditAt(boolean value, int row, int column) {
        Vector rowVector = (Vector) canEdit.elementAt(row);
        rowVector.setElementAt(value, column);
        fireTableCellUpdated(row, column);
    }

    @SuppressWarnings("unchecked")
    public void addCanEdit(boolean[][] canEdit) {
        addCanEdit(convertToVector(canEdit));
    }

    public void addCanEdit(Vector<Vector<Boolean>> canEdit) {
        insertCanEdit(canEdit.size(), canEdit);
    }

    @SuppressWarnings("unchecked")
    public void addCanEdit(boolean[] canEdit) {
        addCanEdit(convertToVector(canEdit));
    }

    public void addOneCanEdit(Vector<Boolean> canEdit) {
        insertCanEdit((null != this.canEdit) ? this.canEdit.size() : 0, canEdit);
    }

    @SuppressWarnings("unchecked")
    public void insertCanEdit(int row, Vector rowData) {
        canEdit.insertElementAt(rowData, row);
       // justifyRows(row, row + 1);
        fireTableRowsInserted(row, row);
    }

    public void insertCanEdit(int row, Object[] rowData) {
        insertCanEdit(row, convertToVector(rowData));
    }

    // private void justifyCanEditRows(int from, int to) {
    // canEdit.setSize(canEdit.size());
    // for (int i = from; i < to; i++) {
    // if (canEdit.elementAt(i) == null) {
    // canEdit.setElementAt(new Vector(), i);
    // }
    // ((Vector) canEdit.elementAt(i)).setSize(getColumnCount());
    // }
    // }
    public Vector<Vector<Boolean>> getCanEdit() {
        return canEdit;
    }
}
