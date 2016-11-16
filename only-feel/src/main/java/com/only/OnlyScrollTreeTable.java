package com.only;

import javax.swing.tree.DefaultMutableTreeNode;

import com.only.component.tree.CommonTreeTableModel;

public class OnlyScrollTreeTable extends OnlyScrollTable {

	private static final long serialVersionUID = 6786205172777947771L;
	private OnlyTreeTable treeTable;

	public OnlyScrollTreeTable(OnlyTreeTable treeTable) {
		super(treeTable);
		this.treeTable = treeTable;
	}

	public OnlyScrollTreeTable(CommonTreeTableModel model) {
		this(new OnlyTreeTable(model));
	}

	public OnlyScrollTreeTable(DefaultMutableTreeNode root, String[] columnsName, Class<?>[] columnsClass, String[] getMethodsName, String[] setMethodsName) {
		this(new OnlyTreeTable(root, columnsName, columnsClass, getMethodsName, setMethodsName));
	}

	public OnlyScrollTreeTable(DefaultMutableTreeNode root, String[] columnsName, Class<?>[] columnsClass, String[] getMethodsName, String[] setMethodsName, boolean asksAllowsChildren) {
		this(new OnlyTreeTable(root, columnsName, columnsClass, getMethodsName, setMethodsName, asksAllowsChildren));
	}

	public OnlyTreeTable getTreeTable() {
		return treeTable;
	}
}
