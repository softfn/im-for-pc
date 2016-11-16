package com.only;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.RowSorter;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.table.TableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import com.only.component.tree.OnlyTreeColumnCellEditor;
import com.only.component.tree.OnlyTreeTableCellRenderer;
import com.only.component.tree.CommonTreeTableModel;

public class OnlyTreeTable extends OnlyTable {

    private static final long serialVersionUID = -8142407744125469821L;
    private OnlyTreeTableCellRenderer tree;
    private CommonTreeTableModel model;
    private Map<Integer, Integer> treeRendererTextStartMap;

    public OnlyTreeTable(CommonTreeTableModel model) {
        super();
        this.model = model;
        init();
    }

    public OnlyTreeTable(DefaultMutableTreeNode root, String[] columnsName, Class<?>[] columnsClass,
            String[] getMethodsName, String[] setMethodsName) {
        this(root, columnsName, columnsClass, getMethodsName, setMethodsName, false);
    }

    public OnlyTreeTable(DefaultMutableTreeNode root, String[] columnsName, Class<?>[] columnsClass,
            String[] getMethodsName, String[] setMethodsName, boolean asksAllowsChildren) {
        super();
        this.model = new CommonTreeTableModel(this, root, columnsName, columnsClass, getMethodsName, setMethodsName, asksAllowsChildren);
        init();
    }

    private void init() {
        treeRendererTextStartMap = new HashMap<Integer, Integer>();
        tree = new OnlyTreeTableCellRenderer(this, model);
        setModel(model);
        getColumn(model.getColumnName(0)).setCellRenderer(tree);
        getColumn(model.getColumnName(0)).setCellEditor(new OnlyTreeColumnCellEditor());
        this.addMouseListener(new MouseHandler());
    }

    public void setTreeRendererTextStartAt(int row, int textStart) {
        treeRendererTextStartMap.put(row, textStart);
    }

    public int getTreeRendererTextStartAt(int row) {
        Integer start = treeRendererTextStartMap.get(row);
        return start == null ? -1 : start;
    }

    public boolean isPaintTreeLines() {
        return tree == null ? false : tree.isPaintLines();
    }

    public void setPaintTreeLines(boolean paintTreeLines) {
        if (tree != null) {
            tree.setPaintLines(paintTreeLines);
        }
    }

    public void setRowHeight(int rowHeight) {
        super.setRowHeight(rowHeight);

        if (tree != null) {
            tree.setRowHeight(rowHeight);
        }
    }

    public void setRowMargin(int rowMargin) {
        super.setRowMargin(Math.max(0, rowMargin));
    }

    @Deprecated
    public void setRowHeight(int row, int rowHeight) {
        setRowHeight(rowHeight);
    }

    @Deprecated
    public void setRowSorter(RowSorter<? extends TableModel> sorter) {
        super.setRowSorter(null);
    }

    public JTree getTree() {
        return tree;
    }

    private class MouseHandler extends MouseAdapter {

        final JTable table = OnlyTreeTable.this;

        public void mousePressed(MouseEvent e) {
            if (!SwingUtilities.isLeftMouseButton(e)) {
                return;
            }

            Point point = e.getPoint();
            int clickCount = e.getClickCount();
            int selRow = table.getSelectedRow();
            int row = table.rowAtPoint(point);
            int colV = table.columnAtPoint(point);
            int col = table.convertColumnIndexToModel(colV);
            TreePath path = tree.getPathForRow(row);

            if (path == null) {
                return;
            }

            Object node = path.getLastPathComponent();

            if (col != 0 || path == null || model.isLeaf(node)) {
                return;
            }

            if (clickCount % 2 == 0 && !tree.getShowsRootHandles() && node == model.getRoot()) {
                toggleExpandState(tree, path, selRow);
            } else if (clickCount % 2 == 1) {
                Rectangle rect = table.getCellRect(row, colV, false);

                if (rect.contains(point)) {
                    BasicTreeUI ui = (BasicTreeUI) tree.getUI();

                    try {
                        Method method = BasicTreeUI.class.getDeclaredMethod("isLocationInExpandControl", TreePath.class, int.class, int.class);
                        method.setAccessible(true);
                        boolean toggle = (Boolean) method.invoke(ui, path, point.x - rect.x, point.y - rect.y);

                        if (toggle) {
                            toggleExpandState(tree, path, selRow);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }

        private void toggleExpandState(JTree tree, TreePath path, int selRow) {
            if (!tree.isExpanded(path)) {
                tree.expandPath(path);
            } else {
                tree.collapsePath(path);
            }

            model.fireTableDataChanged();
            table.getSelectionModel().setSelectionInterval(selRow, selRow);
        }
    }
}