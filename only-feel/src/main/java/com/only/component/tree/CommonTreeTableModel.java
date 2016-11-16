package com.only.component.tree;

import java.awt.Rectangle;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JTable;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;

import com.only.component.TableCellEditableController;
import com.only.util.OnlyFeelUtil;

public class CommonTreeTableModel extends AbstractTreeTableModel {
	private static final long serialVersionUID = 766211503325659293L;

	/**
	 * 该TreeTableModel绑定的JTable
	 */
	private JTable table;

	/**
	 * 列名数组
	 */
	private String[] columnsName;

	/**
	 * 列的数据类型数组
	 */
	private Class<?>[] columnsClass;

	/**
	 * 列的数据类型数组，与columnsClass不同之处在于该数组中不允许存在基本类型
	 */
	private Class<?>[] unPrimitiveClass;

	/**
	 * getValueAt时从对象获取数据的方法名数组
	 */
	private String[] getMethodsName;

	/**
	 * setValueAt时更新对象数据的方法名数组
	 */
	private String[] setMethodsName;

	/**
	 * 控制单元格是否可编辑的实现
	 */
	private TableCellEditableController editableController;

	/**
	 * 构造方法
	 * 
	 * @param root
	 *            根节点
	 * @param asksAllowsChildren
	 *            如果任何节点都可以有子节点，则为false，如果询问每个节点看是否有子节点，则为true
	 */
	private CommonTreeTableModel(DefaultMutableTreeNode root, boolean asksAllowsChildren) {
		super(root, asksAllowsChildren);
	}

	/**
	 * 构造方法
	 * 
	 * @param table
	 *            该TreeTableModel绑定的JTable
	 * @param root
	 *            根节点，可以为null，然后必要时通过DefaultTreeModel.setRoot加载
	 * @param columnsName
	 *            列名数组
	 * @param columnsClass
	 *            列的数据类型数组
	 * @param getMethodsName
	 *            getValueAt时从对象获取数据的方法名数组
	 * @param setMethodsName
	 *            setValueAt时更新对象数据的方法名数组
	 * @param asksAllowsChildren
	 *            如果任何节点都可以有子节点，则为false，如果询问每个节点看是否有子节点，则为true
	 */
	public CommonTreeTableModel(JTable table, DefaultMutableTreeNode root, String[] columnsName, Class<?>[] columnsClass, String[] getMethodsName, String[] setMethodsName, boolean asksAllowsChildren) {
		this(root, asksAllowsChildren);
		this.table = table;
		this.columnsName = columnsName;
		this.columnsClass = columnsClass;
		this.unPrimitiveClass = OnlyFeelUtil.createUnprimitiveClasses(columnsClass);
		this.getMethodsName = getMethodsName;
		this.setMethodsName = setMethodsName;
	}

	/**
	 * 设置树表模型中指定单元格的值
	 * 
	 * @param 值
	 * @param row
	 *            行
	 * @param column
	 *            列
	 */
	public void setValueAt(Object value, int row, int column) {
		if (row >= 0 && row < getRowCount() && column >= 0 && column < getColumnCount()) {
			Object rowData = getRowDataAt(row);
			Class<?> rowClass = rowData.getClass();

			try {
				Method method = rowClass.getMethod(setMethodsName[column], new Class[] { columnsClass[column] });
				method.invoke(rowData, value);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				nodeChanged((DefaultMutableTreeNode) tree.getPathForRow(row).getLastPathComponent());
				fireTableCellUpdated(row, column);
			}
		}
	}

	/**
	 * 设置界面上指定单元格的值
	 * 
	 * @param 需要设置的值
	 * @param row
	 *            界面上的行号
	 * @param column
	 *            界面上的列号
	 */
	public void setValueOnViewAt(Object value, int row, int column) {
		column = table.convertColumnIndexToModel(column);
		setValueAt(value, row, column);
	}

	/**
	 * 获得树表模型中指定单元格的值
	 * 
	 * @param row
	 *            行
	 * @param column
	 *            列
	 */
	public Object getValueAt(int row, int column) {
		Object value = null;

		if (tree.getRowCount() > 0 && row < getRowCount() && column < getColumnCount()) {
			Object rowData = getRowDataAt(row);
			Class<?> rowClass = rowData.getClass();

			try {
				Method method = rowClass.getMethod(getMethodsName[column], new Class[] {});
				value = method.invoke(rowData, new Object[] {});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return value;
	}

	/**
	 * 获取界面上指定单元格的值
	 * 
	 * @param row
	 *            界面上的行号
	 * @param column
	 *            界面上的列号
	 */
	public Object getValueOnViewAt(int row, int column) {
		column = table.convertColumnIndexToModel(column);
		return getValueAt(row, column);
	}

	/**
	 * 获得树表的总列数
	 */
	public int getColumnCount() {
		return columnsName.length;
	}

	/**
	 * 获得树表的总行数
	 */
	public int getRowCount() {
		return tree.getRowCount();
	}

	/**
	 * 获得树表模型中指定列的列名
	 * 
	 * @param column
	 *            指定的列
	 */
	public String getColumnName(int column) {
		return columnsName[column];
	}

	/**
	 * 获得界面中指定列的列名
	 * 
	 * @param column
	 *            指定的列，树表界面中所显示的列号
	 */
	public String getColumnNameOnView(int column) {
		return getColumnName(table.convertColumnIndexToModel(column));
	}

	/**
	 * 获得树表模型中指定的列对象
	 * 
	 * @param column
	 *            指定的列对象
	 */
	public Class<?> getColumnClass(int column) {
		return unPrimitiveClass[column];
	}

	/**
	 * 获得界面上指定的列对象
	 * 
	 * @param column
	 *            界面上的列号
	 */
	public Class<?> getColumnClassOnView(int column) {
		return getColumnClass(table.convertColumnIndexToModel(column));
	}

	/**
	 * 树表模型中指定单元格是否可编辑
	 * 
	 * @param row
	 *            行
	 * @param column
	 *            列
	 */
	public boolean isCellEditable(int row, int column) {
		if (editableController != null) {
			return editableController.isCellEditable(row, column);
		} else {
			return false;
		}
	}

	/**
	 * 指定单元格是否可编辑
	 * 
	 * @param row
	 *            界面中的行号
	 * @param column
	 *            界面中的列号
	 */
	public boolean isCellEditableOnView(int row, int column) {
		column = table.convertColumnIndexToModel(column);
		return isCellEditable(row, column);
	}

	/**
	 * 在父节点的指定位置添加子节点
	 * 
	 * @param newChild
	 *            待添加的节点
	 * @param parent
	 *            父节点
	 * @param index
	 *            添加到父节点中的位置
	 */
	public void insertNodeInto(MutableTreeNode newChild, MutableTreeNode parent, int index) {
		super.insertNodeInto(newChild, parent, index);
		fireTableDataChanged();
		TreePath path = new TreePath(getPathToRoot(parent));

		if (tree.isCollapsed(path)) {
			tree.expandPath(path);
		}

		scrollToRowAt(tree.getRowForPath(path) + index + 1);
	}

	/**
	 * 在父节点开始处添加子节点
	 * 
	 * @param newChild
	 *            子节点
	 * @param parent
	 *            父节点
	 */
	public void insertNodeAtFirst(MutableTreeNode newChild, MutableTreeNode parent) {
		insertNodeInto(newChild, parent, 0);
	}

	/**
	 * 在父节点末尾添加子节点
	 * 
	 * @param newChild
	 *            子节点
	 * @param parent
	 *            父节点
	 */
	public void insertNodeAtLast(MutableTreeNode newChild, MutableTreeNode parent) {
		insertNodeInto(newChild, parent, parent.getChildCount());
	}

	/**
	 * 删除树表中指定的行
	 * 
	 * @param firstRow
	 *            开始行
	 * @param lastRow
	 *            结束行
	 */
	public void delRowsArea(int firstRow, int lastRow) {
		int[] rows = new int[lastRow - firstRow + 1];

		for (int i = 0; i < rows.length; i++) {
			rows[i] = firstRow + i;
		}

		delRows(rows);
	}

	/**
	 * 删除树表中指定行的行
	 * 
	 * @param rows
	 *            行号数组
	 */
	public void delRows(int... rows) {
		int rowCount = getRowCount();
		int subRowCount = rows.length;
		int row;
		Arrays.sort(rows);

		for (int i = subRowCount - 1; i >= 0; i--) {
			row = rows[i];

			if (row >= 0 && row < rowCount) {
				delRowAt(row);
			}
		}
	}

	/**
	 * 删除当前选择的多行数据
	 */
	public void delSelectedRows() {
		delRows(table.getSelectedRows());
	}

	/**
	 * 删除当前选择的一行数据
	 */
	public void delSelectedRow() {
		delRowAt(table.getSelectedRow());
	}

	/**
	 * 删除树表中指定的行
	 * 
	 * @param row
	 *            树表中的行号
	 */
	public void delRowAt(int row) {
		if (row >= 0 && row < getRowCount()) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getPathForRow(row).getLastPathComponent();

			if (node == getRoot()) {
				delAllRow();
			} else {
				removeNodeFromParent(node);
				fireTableRowsDeleted(row, row);
			}
		}
	}

	/**
	 * 删除一条数据
	 * 
	 * @param obj
	 *            需要删除的数据
	 */
	public void delRowData(Object obj) {
		int pos = -1;

		for (int i = 0; i < getRowCount() && obj != null; i++) {
			if (obj == getRowDataAt(i)) {
				pos = i;
				break;
			}
		}

		if (pos >= 0) {
			delRowAt(pos);
		}
	}

	/**
	 * 删除多条数据并更新树表
	 * 
	 * @param subObjs
	 *            需要删除的数据集合
	 */
	public void delSubRowDatas(List<Object> subObjs) {
		if (subObjs != null && !subObjs.isEmpty()) {
			for (Object obj : subObjs) {
				delRowData(obj);
			}
		}
	}

	/**
	 * 删除所有行
	 */
	public void delAllRow() {
		int rowCount = getRowCount();

		if (rowCount > 0) {
			setRoot(null);
			fireTableRowsDeleted(0, rowCount - 1);
		}
	}

	/**
	 * 设置树表中的某一行数据
	 * 
	 * @param row
	 *            树表中的行号
	 * @param obj
	 *            数据对象
	 */
	public void setRowDataAt(int row, Object obj) {
		if (row >= 0 && row < getRowCount() && obj != null) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getPathForRow(row).getLastPathComponent();
			node.setUserObject(obj);
			nodeChanged(node);
			fireTableRowsUpdated(row, row);
		}
	}

	/**
	 * 刷新树表中的某一行
	 * 
	 * @param row
	 *            树表中的行号
	 */
	public void refreshRow(int row) {
		if (row >= 0 && row < getRowCount()) {
			nodeChanged((DefaultMutableTreeNode) tree.getPathForRow(row).getLastPathComponent());
			fireTableRowsUpdated(row, row);
		}
	}

	/**
	 * 刷新整个树表
	 */
	public void refreshUI() {
		for (int i = 0; i < getRowCount(); i++) {
			nodeChanged((DefaultMutableTreeNode) tree.getPathForRow(i).getLastPathComponent());
		}

		fireTableDataChanged();
	}

	/**
	 * 获取树表的当前数据集合
	 * 
	 * @return 树表的当前数据集合
	 */
	public List<Object> getAllRowDatas() {
		return getAreaRowDatas(0, getRowCount() - 1);
	}

	/**
	 * 获取树表中指定行的数据
	 * 
	 * @param row
	 *            树表中的行号
	 * @return 树表中指定行的数据
	 */
	public Object getRowDataAt(int row) {
		if (row >= 0 && row < getRowCount()) {
			return ((DefaultMutableTreeNode) tree.getPathForRow(row).getLastPathComponent()).getUserObject();
		} else {
			return null;
		}
	}

	/**
	 * 获取树表中指定行号的数据集合
	 * 
	 * @param rows
	 *            行号数组
	 * @return 树表中指定行号的数据集合
	 */
	public List<Object> getSubRowDatas(int... rows) {
		List<Object> subList = new ArrayList<Object>();
		int rowCount = getRowCount();

		for (int row : rows) {
			if (row < rowCount && row >= 0) {
				subList.add(getRowDataAt(row));
			}
		}

		return subList.isEmpty() ? null : subList;
	}

	/**
	 * 获取树表中的部分数据集合
	 * 
	 * @param firstRow
	 *            开始行
	 * @param lastRow
	 *            结束行
	 * @return 树表中的部分数据集合
	 */
	public List<Object> getAreaRowDatas(int firstRow, int lastRow) {
		int[] rows = new int[lastRow - firstRow + 1];

		for (int i = 0; i < rows.length; i++) {
			rows[i] = firstRow + i;
		}

		return getSubRowDatas(rows);
	}

	/**
	 * 获取当前选择的单行的数据
	 * 
	 * @return 当前选择的行数据
	 */
	public Object getSelectedRowData() {
		return getRowDataAt(table.getSelectedRow());
	}

	/**
	 * 获取当前选择的多行的数据
	 * 
	 * @return 当前选择的行数据集合
	 */
	public List<Object> getSelectedRowDatas() {
		return getSubRowDatas(table.getSelectedRows());
	}

	/**
	 * 滚动到指定行
	 * 
	 * @param row
	 *            行号
	 */
	public void scrollToRowAt(int row) {
		Rectangle rect = table.getCellRect(row, 0, true);
		table.scrollRectToVisible(rect);
	}

	/**
	 * 滚动到最后一行
	 */
	public void scrollLastRow() {
		scrollToRowAt(table.getRowCount() - 1);
	}

	/**
	 * 设置单元格是否可编辑的控制器
	 * 
	 * @param editableController
	 */
	public void setEditableController(TableCellEditableController editableController) {
		this.editableController = editableController;
	}

	/**
	 * 获取与该Model绑定的JTable
	 * 
	 * @return 与该Model绑定的JTable
	 */
	public JTable getTreeTable() {
		return table;
	}
}