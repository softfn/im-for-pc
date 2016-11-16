package com.only.component;

import java.awt.Rectangle;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import com.only.util.OnlyFeelUtil;

public class CommonTableModel<E> extends AbstractTableModel {
	private static final long serialVersionUID = -3141048810337880013L;

	/**
	 * 该TableModel绑定的JTable
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
	 * 表格数据集合
	 */
	private List<E> dataList;

	/**
	 * 控制单元格是否可编辑的实现
	 */
	private TableCellEditableController editableController;

	/**
	 * 构造方法
	 * 
	 * @param table
	 *            该TableModel绑定的JTable
	 * @param columnsName
	 *            列名数组
	 * @param columnsClass
	 *            列的数据类型数组
	 * @param getMethodsName
	 *            getValueAt时从对象获取数据的方法名数组
	 * @param setMethodsName
	 *            setValueAt时更新对象数据的方法名数组
	 * @param dataList
	 *            表格数据集合
	 */
	public CommonTableModel(JTable table, String[] columnsName, Class<?>[] columnsClass, String[] getMethodsName, String[] setMethodsName, List<E> dataList) {
		super();
		this.table = table;
		this.columnsName = columnsName;
		this.columnsClass = columnsClass;
		this.unPrimitiveClass = OnlyFeelUtil.createUnprimitiveClasses(columnsClass);
		this.getMethodsName = getMethodsName;
		this.setMethodsName = setMethodsName;
		this.dataList = dataList;
	}

	/**
	 * 设置表格模型中指定单元格的值
	 * 
	 * @param 值
	 * @param row
	 *            行
	 * @param column
	 *            列
	 */
	public void setValueAt(Object value, int row, int column) {
		if (row >= 0 && row < getRowCount() && column >= 0 && column < getColumnCount()) {
			E rowData = getRowDataAt(row);
			Class<?> rowClass = rowData.getClass();

			try {
				Method method = rowClass.getMethod(setMethodsName[column], new Class[] { columnsClass[column] });
				method.invoke(rowData, value);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
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
		row = table.convertRowIndexToModel(row);
		column = table.convertColumnIndexToModel(column);
		setValueAt(value, row, column);
	}

	/**
	 * 获得表格模型中指定单元格的值
	 * 
	 * @param row
	 *            行
	 * @param column
	 *            列
	 */
	public Object getValueAt(int row, int column) {
		Object value = null;

		if (!dataList.isEmpty() && row < getRowCount() && column < getColumnCount()) {
			E rowData = getRowDataAt(row);
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
		row = table.convertRowIndexToModel(row);
		column = table.convertColumnIndexToModel(column);
		return getValueAt(row, column);
	}

	/**
	 * 获得表格的总列数
	 */
	public int getColumnCount() {
		return columnsName.length;
	}

	/**
	 * 获得表格的总行数
	 */
	public int getRowCount() {
		return dataList.size();
	}

	/**
	 * 获得表格模型中指定列的列名
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
	 *            指定的列，表格界面中所显示的列号
	 */
	public String getColumnNameOnView(int column) {
		return getColumnName(table.convertColumnIndexToModel(column));
	}

	/**
	 * 获得表格模型中指定的列对象
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
	 * 表格模型中指定单元格是否可编辑
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
		row = table.convertRowIndexToModel(row);
		column = table.convertColumnIndexToModel(column);
		return isCellEditable(row, column);
	}

	/**
	 * 在表格末尾插入一条新数据
	 * 
	 * @param rowData
	 *            需要插入的数据对象
	 */
	public void insertRowDataOnView(E rowData) {
		insertRowDataOnViewAt(getRowCount(), rowData);
	}

	/**
	 * 在表格模型末尾插入一条新数据
	 * 
	 * @param rowData
	 *            需要插入的数据对象
	 */
	public void insertRowData(E rowData) {
		insertRowDataAt(getRowCount(), rowData);
	}

	/**
	 * 在表格指定位置插入一条新数据
	 * 
	 * @param rowData
	 *            需要插入的数据对象
	 * @param row
	 *            行号
	 */
	public void insertRowDataOnViewAt(int row, E rowData) {
		int rowCount = getRowCount();

		if (row >= 0 && row <= rowCount && rowData != null) {
			if (row == rowCount) {
				insertRowDataAt(table.convertRowIndexToModel(row - 1) + 1, rowData);
			} else {
				insertRowDataAt(table.convertRowIndexToModel(row), rowData);
			}
		}
	}

	/**
	 * 在表格模型指定位置插入一条新数据
	 * 
	 * @param rowData
	 *            需要插入的数据对象
	 * @param row
	 *            行号
	 */
	public void insertRowDataAt(int row, E rowData) {
		if (row >= 0 && row <= getRowCount() && rowData != null) {
			dataList.add(row, rowData);
			fireTableRowsInserted(row, row);
			scrollToRowAt(row);
		}
	}

	/**
	 * 在表格指定位置插入多条新数据
	 * 
	 * @param rowDatas
	 *            需要插入的数据列表
	 * @param startRow
	 *            开始行号
	 */
	public void insertRowDatasOnView(int startRow, List<E> rowDatas) {
		int rowCount = getRowCount();

		if (startRow >= 0 && startRow <= rowCount && rowDatas != null && !rowDatas.isEmpty()) {
			if (startRow == rowCount) {
				insertRowDatas(table.convertRowIndexToModel(rowCount - 1) + 1, rowDatas);
			} else {
				insertRowDatas(table.convertRowIndexToModel(startRow), rowDatas);
			}
		}
	}

	/**
	 * 在表格模型指定位置插入多条新数据
	 * 
	 * @param rowDatas
	 *            需要插入的数据列表
	 * @param startRow
	 *            开始行号
	 */
	public void insertRowDatas(int startRow, List<E> rowDatas) {
		if (startRow >= 0 && startRow <= getRowCount() && rowDatas != null && !rowDatas.isEmpty()) {
			int endRow = startRow + rowDatas.size() - 1;
			dataList.addAll(startRow, rowDatas);
			fireTableRowsInserted(startRow, endRow);
			scrollToRowAt(endRow);
		}
	}

	/**
	 * 在表格末尾插入多条新数据
	 * 
	 * @param rowDatas
	 *            需要插入的数据列表
	 */
	public void insertRowDatas(List<E> rowDatas) {
		insertRowDatas(getRowCount(), rowDatas);
	}

	/**
	 * 删除指定的行
	 * 
	 * @param firstRow
	 *            开始行
	 * @param lastRow
	 *            结束行
	 */
	public void delRowsAreaOnView(int firstRow, int lastRow) {
		int[] rows = new int[lastRow - firstRow + 1];

		for (int i = 0; i < rows.length; i++) {
			rows[i] = firstRow + i;
		}

		delRowsOnView(rows);
	}

	/**
	 * 删除表格模型中指定的行
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
	 * 删除指定的行
	 * 
	 * @param rows
	 *            行号数组
	 */
	public void delRowsOnView(int... rows) {
		delRows(convertRowIndexesToModel(rows));
	}

	/**
	 * 删除表格模型中指定行的行
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
				dataList.remove(row);
				fireTableRowsDeleted(row, row);
			}
		}
	}

	/**
	 * 删除当前选择的多行数据
	 */
	public void delSelectedRows() {
		delRowsOnView(table.getSelectedRows());
	}

	/**
	 * 删除当前选择的一行数据
	 */
	public void delSelectedRow() {
		delRowOnViewAt(table.getSelectedRow());
	}

	/**
	 * 删除界面上指定的行
	 * 
	 * @param row
	 *            行号
	 */
	public void delRowOnViewAt(int row) {
		if (row >= 0 && row < getRowCount()) {
			delRowAt(table.convertRowIndexToModel(row));
		}
	}

	/**
	 * 删除表格模型中指定的行
	 * 
	 * @param row
	 *            表格模型中的行号
	 */
	public void delRowAt(int row) {
		if (row >= 0 && row < getRowCount()) {
			dataList.remove(row);
			fireTableRowsDeleted(row, row);
		}
	}

	/**
	 * 删除一条数据
	 * 
	 * @param obj
	 *            需要删除的数据
	 */
	public void delRowData(E obj) {
		int pos;

		if (obj != null && (pos = dataList.indexOf(obj)) >= 0) {
			delRowAt(pos);
		}
	}

	/**
	 * 删除多条数据并更新表格
	 * 
	 * @param subObjs
	 *            需要删除的数据集合
	 */
	public void delSubRowDatas(List<E> subObjs) {
		if (subObjs != null && !subObjs.isEmpty()) {
			for (E obj : subObjs) {
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
			dataList.clear();
			fireTableRowsDeleted(0, rowCount - 1);
		}
	}

	/**
	 * 设置表格某一行数据
	 * 
	 * @param row
	 *            行号
	 * @param obj
	 *            数据对象
	 */
	public void setRowDataOnViewAt(int row, E obj) {
		if (row >= 0 && row < getRowCount() && obj != null) {
			setRowDataAt(table.convertRowIndexToModel(row), obj);
		}
	}

	/**
	 * 设置表格模型中的某一行数据
	 * 
	 * @param row
	 *            表格模型中的行号
	 * @param obj
	 *            数据对象
	 */
	public void setRowDataAt(int row, E obj) {
		if (row >= 0 && row < getRowCount() && obj != null) {
			dataList.set(row, obj);
			fireTableRowsUpdated(row, row);
		}
	}

	/**
	 * 重新设置表格的数据
	 * 
	 * @param dataList
	 *            表格数据
	 */
	public void setDataList(List<E> dataList) {
		if (dataList != null) {
			this.dataList = dataList;
			refreshUI();
		}
	}

	/**
	 * 刷新表格的某一行
	 * 
	 * @param row
	 *            行号
	 */
	public void refreshRowOnView(int row) {
		if (row >= 0 && row < getRowCount()) {
			refreshRow(table.convertRowIndexToModel(row));
		}
	}

	/**
	 * 刷新表格模型中的某一行
	 * 
	 * @param row
	 *            表格模型中的行号
	 */
	public void refreshRow(int row) {
		if (row >= 0 && row < getRowCount()) {
			fireTableRowsUpdated(row, row);
		}
	}

	/**
	 * 刷新整个表格
	 */
	public void refreshUI() {
		fireTableDataChanged();
	}

	/**
	 * 获取表格的当前数据集合（按界面中显示的顺序）
	 * 
	 * @return 表格的当前数据集合（按界面中显示的顺序）
	 */
	public List<E> getAllRowDatasOnView() {
		return getAreaRowDatasOnView(0, getRowCount() - 1);
	}

	/**
	 * 获取表格的当前数据集合（按数据模型中的顺序）
	 * 
	 * @return 表格的当前数据集合（按数据模型中的顺序）
	 */
	public List<E> getAllRowDatas() {
		return getAreaRowDatas(0, getRowCount() - 1);
	}

	/**
	 * 获取界面中的部分数据集合
	 * 
	 * @param firstRow
	 *            开始行
	 * @param lastRow
	 *            结束行
	 * @return 界面中的部分数据集合
	 */
	public List<E> getAreaRowDatasOnView(int firstRow, int lastRow) {
		int[] rows = new int[lastRow - firstRow + 1];

		for (int i = 0; i < rows.length; i++) {
			rows[i] = firstRow + i;
		}

		return getSubRowDatasOnView(rows);
	}

	/**
	 * 获取表格模型中的部分数据集合
	 * 
	 * @param firstRow
	 *            开始行
	 * @param lastRow
	 *            结束行
	 * @return 表格模型中的部分数据集合
	 */
	public List<E> getAreaRowDatas(int firstRow, int lastRow) {
		int[] rows = new int[lastRow - firstRow + 1];

		for (int i = 0; i < rows.length; i++) {
			rows[i] = firstRow + i;
		}

		return getSubRowDatas(rows);
	}

	/**
	 * 获取界面中指定行号的数据集合
	 * 
	 * @param rows
	 *            界面中的行号数组
	 * @return 界面中指定行号的数据集合
	 */
	public List<E> getSubRowDatasOnView(int... rows) {
		return getSubRowDatas(convertRowIndexesToModel(rows));
	}

	/**
	 * 获取表格模型中指定行号的数据集合
	 * 
	 * @param rows
	 *            行号数组
	 * @return 表格模型中指定行号的数据集合
	 */
	public List<E> getSubRowDatas(int... rows) {
		List<E> subList = new ArrayList<E>();
		int rowCount = getRowCount();

		for (int row : rows) {
			if (row < rowCount && row >= 0) {
				subList.add(getRowDataAt(row));
			}
		}

		return subList.isEmpty() ? null : subList;
	}

	/**
	 * 获取界面中指定行的数据
	 * 
	 * @param row
	 *            界面中的行号
	 * @return 界面中指定行的数据
	 */
	public E getRowDataOnViewAt(int row) {
		return getRowDataAt(table.convertRowIndexToModel(row));
	}

	/**
	 * 获取表格模型中指定行的数据
	 * 
	 * @param row
	 *            表格模型中的行号
	 * @return 表格模型中指定行的数据
	 */
	public E getRowDataAt(int row) {
		if (row >= 0 && row < getRowCount()) {
			return dataList.get(row);
		} else {
			return null;
		}
	}

	/**
	 * 获取当前选择的单行的数据
	 * 
	 * @return 当前选择的行数据
	 */
	public E getSelectedRowData() {
		return getRowDataOnViewAt(table.getSelectedRow());
	}

	/**
	 * 获取当前选择的多行的数据
	 * 
	 * @return 当前选择的行数据集合
	 */
	public List<E> getSelectedRowDatas() {
		return getSubRowDatasOnView(table.getSelectedRows());
	}

	/**
	 * 将界面中的行号转换为数据模型中的行号
	 * 
	 * @param rowIndexInView
	 *            界面中的行号数组
	 * @return 数据模型中的行号数组
	 */
	public int[] convertRowIndexesToModel(int... rowIndexInView) {
		int[] mIndexes = new int[rowIndexInView.length];
		int rowCount = getRowCount();
		int arrayIndex = 0;

		for (int vIndex : rowIndexInView) {
			if (vIndex >= 0 && vIndex < rowCount) {
				mIndexes[arrayIndex++] = table.convertRowIndexToModel(vIndex);
			} else {
				mIndexes[arrayIndex++] = vIndex;
			}
		}

		return mIndexes;
	}

	/**
	 * 将数据模型中的行号转换为界面中的行号
	 * 
	 * @param rowIndexesInModel
	 *            数据模型中的行号数组
	 * @return 界面中的行号数组
	 */
	public int[] convertRowIndexesToView(int... rowIndexesInModel) {
		int[] vIndexes = new int[rowIndexesInModel.length];
		int rowCount = getRowCount();
		int arrayIndex = 0;

		for (int mIndex : rowIndexesInModel) {
			if (mIndex >= 0 && mIndex < rowCount) {
				vIndexes[arrayIndex++] = table.convertRowIndexToView(mIndex);
			} else {
				vIndexes[arrayIndex++] = mIndex;
			}
		}

		return vIndexes;
	}

	/**
	 * 滚动到表格模型中的指定行
	 * 
	 * @param row
	 *            行号
	 */
	public void scrollToRowAt(int row) {
		scrollToRowOnViewAt(table.convertRowIndexToView(row));
	}

	/**
	 * 滚动到指定行
	 * 
	 * @param row
	 *            行号
	 */
	public void scrollToRowOnViewAt(int row) {
		Rectangle rect = table.getCellRect(row, 0, true);
		table.scrollRectToVisible(rect);
	}

	/**
	 * 滚动到最后一行
	 */
	public void scrollLastRow() {
		scrollToRowOnViewAt(table.getRowCount() - 1);
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
	public JTable getTable() {
		return table;
	}
}