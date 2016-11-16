package com.only.component.tree;

import java.io.Serializable;

import javax.swing.JTree;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

public abstract class AbstractTreeTableModel extends DefaultTreeModel implements TableModel, Serializable {
	private static final long serialVersionUID = -1810683165492112232L;

	protected JTree tree;

	public AbstractTreeTableModel(TreeNode root) {
		super(root);
	}

	public AbstractTreeTableModel(TreeNode root, boolean asksAllowsChildren) {
		super(root, asksAllowsChildren);
	}

	public String getColumnName(int column) {
		String result = "";

		for (; column >= 0; column = column / 26 - 1) {
			result = (char) ((char) (column % 26) + 'A') + result;
		}

		return result;
	}

	public int findColumn(String columnName) {
		for (int i = 0; i < getColumnCount(); i++) {
			if (columnName.equals(getColumnName(i))) {
				return i;
			}
		}

		return -1;
	}

	public Class<?> getColumnClass(int columnIndex) {
		return Object.class;
	}

	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
	}

	public void addTableModelListener(TableModelListener l) {
		listenerList.add(TableModelListener.class, l);
	}

	public void removeTableModelListener(TableModelListener l) {
		listenerList.remove(TableModelListener.class, l);
	}

	public TableModelListener[] getTableModelListeners() {
		return (TableModelListener[]) listenerList.getListeners(TableModelListener.class);
	}

	public void fireTableDataChanged() {
		fireTableChanged(new TableModelEvent(this));
	}

	public void fireTableStructureChanged() {
		fireTableChanged(new TableModelEvent(this, TableModelEvent.HEADER_ROW));
	}

	public void fireTableRowsInserted(int firstRow, int lastRow) {
		fireTableChanged(new TableModelEvent(this, firstRow, lastRow, TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT));
	}

	public void fireTableRowsUpdated(int firstRow, int lastRow) {
		fireTableChanged(new TableModelEvent(this, firstRow, lastRow, TableModelEvent.ALL_COLUMNS, TableModelEvent.UPDATE));
	}

	public void fireTableRowsDeleted(int firstRow, int lastRow) {
		fireTableChanged(new TableModelEvent(this, firstRow, lastRow, TableModelEvent.ALL_COLUMNS, TableModelEvent.DELETE));
	}

	public void fireTableCellUpdated(int row, int column) {
		fireTableChanged(new TableModelEvent(this, row, row, column));
	}

	public void fireTableChanged(TableModelEvent e) {
		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == TableModelListener.class) {
				((TableModelListener) listeners[i + 1]).tableChanged(e);
			}
		}
	}

	public JTree getTree() {
		return tree;
	}

	public void setTree(JTree tree) {
		this.tree = tree;
	}
}