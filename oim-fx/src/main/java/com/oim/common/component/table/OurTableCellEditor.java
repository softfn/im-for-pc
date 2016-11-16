/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oim.common.component.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.border.LineBorder;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.TreeCellEditor;

import com.oim.common.component.combo.AutoCompletionField;
import com.only.OnlyLabel;

/**
 * 
 * @author XiaHui
 */
@SuppressWarnings("serial")
public class OurTableCellEditor extends AbstractCellEditor implements TableCellEditor, TreeCellEditor {

	protected JComponent editorComponent;
	protected EditorDelegate delegate;
	protected int clickCountToStart = 1;
	protected Object value;
	protected boolean defaultLastValue = true;// 是否取默认值
	protected boolean allowNull = true;//是否允许为空（true：可为空    false：不能为空）
	protected boolean allowMove = true;//为空时是否可移动（true：可移动    false：不可以移动）
	protected boolean allowLastValueReplaceCurrentValue = false;// 是否允许上一个值替换当前值
	protected OnlyLabel label = new OnlyLabel("不能为空！");
	protected JPopupMenu popupMenu = new JPopupMenu();
	protected int row = 0;
	private OurTableEnterAction ourTableEnterAction;
	protected boolean enterToNextCell = true;
	protected ActionEvent actionEvent = new ActionEvent(OurTableCellEditor.this, 0, "");
	protected EditorType editorType = EditorType.defaultType;

	public OurTableCellEditor() {
		this(new JTextField());
		initComponents();
	}

	public OurTableCellEditor(final JTextField textField) {
		editorComponent = textField;
		initComponents();

		this.clickCountToStart = 2;
		delegate = new EditorDelegate() {
			@Override
			public void setValue(Object value) {
				textField.setText((value != null) ? value.toString() : "");
			}

			@Override
			public Object getCellEditorValue() {
				return textField.getText();
			}
		};
		textField.addActionListener(delegate);
	}

	public OurTableCellEditor(final JCheckBox checkBox) {
		editorComponent = checkBox;
		initComponents();
		delegate = new EditorDelegate() {
			@Override
			public void setValue(Object value) {
				boolean selected = false;
				if (value instanceof Boolean) {
					selected = ((Boolean) value).booleanValue();
				} else if (value instanceof String) {
					selected = value.equals("true");
				}
				checkBox.setSelected(selected);
			}

			@Override
			public Object getCellEditorValue() {
				return Boolean.valueOf(checkBox.isSelected());
			}
		};
		checkBox.addActionListener(delegate);
		checkBox.setRequestFocusEnabled(false);
	}

	@SuppressWarnings("rawtypes")
	public OurTableCellEditor(final JComboBox comboBox) {
		editorComponent = comboBox;
		initComponents();
		comboBox.putClientProperty("JComboBox.isTableCellEditor", Boolean.TRUE);
		delegate = new OurTableCellEditor.EditorDelegate() {
			@Override
			public void setValue(Object value) {
				comboBox.setSelectedItem(value);
			}

			@Override
			public Object getCellEditorValue() {
				return comboBox.getSelectedItem();
			}

			@Override
			public boolean shouldSelectCell(EventObject anEvent) {
				if (anEvent instanceof MouseEvent) {
					MouseEvent e = (MouseEvent) anEvent;
					return e.getID() != MouseEvent.MOUSE_DRAGGED;
				}
				return true;
			}

			@Override
			public boolean stopCellEditing() {
				if (comboBox.isEditable()) {// Commit edited value.
					comboBox.actionPerformed(new ActionEvent(OurTableCellEditor.this, 0, ""));
				}
				return super.stopCellEditing();
			}
		};
		comboBox.addActionListener(delegate);
	}

	public OurTableCellEditor(final AutoCompletionField autoCompletionField) {
		editorComponent = autoCompletionField;
		initComponents();

		this.clickCountToStart = 2;
		delegate = new EditorDelegate() {
			@Override
			public void setValue(Object value) {
				autoCompletionField.setText((value != null) ? value.toString() : "");
			}

			@Override
			public Object getCellEditorValue() {
				return autoCompletionField.getText();
			}

			@Override
			public boolean stopCellEditing() {
				boolean b = !autoCompletionField.isShowPopup();
				if (b) {
					b = super.stopCellEditing();
				}
				return b;
			}
		};
		autoCompletionField.addActionListener(delegate);
	}

	protected void initComponents() {
		popupMenu.add(label);
	}

	/**
	 * Returns a reference to the editor component.
	 * 
	 * @return the editor <code>Component</code>
	 */
	public Component getComponent() {
		return editorComponent;
	}

	public void setClickCountToStart(int count) {
		clickCountToStart = count;
	}

	public int getClickCountToStart() {
		return clickCountToStart;
	}

	@Override
	public Object getCellEditorValue() {
		return delegate.getCellEditorValue();
	}

	@Override
	public boolean isCellEditable(EventObject anEvent) {
		return delegate.isCellEditable(anEvent);
	}

	@Override
	public boolean shouldSelectCell(EventObject anEvent) {
		return delegate.shouldSelectCell(anEvent);
	}

	@Override
	public boolean stopCellEditing() {
		if (!isAllowNull()) {
			Object o = delegate.getCellEditorValue();
			if (null == o || "".equals(o)) {
				JComponent component = ((JComponent) getComponent());
				component.setBorder(new LineBorder(Color.red));
				if (component.isShowing()) {
					popupMenu.show(component, 0, component.getHeight());
					component.requestFocus();
				}
				if (!this.allowMove) {
					return false;
				}
			} else {
				popupMenu.setVisible(false);
			}
		} else {
			popupMenu.setVisible(false);
		}
		return delegate.stopCellEditing();
	}

	@Override
	public void cancelCellEditing() {
		delegate.cancelCellEditing();
	}

	@Override
	public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row) {
		String stringValue = tree.convertValueToText(value, isSelected, expanded, leaf, row, false);
		delegate.setValue(stringValue);
		return editorComponent;
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		this.row = row;
		delegate.setValue(value);
		if (editorComponent instanceof JCheckBox) {
			TableCellRenderer renderer = table.getCellRenderer(row, column);
			Component c = renderer.getTableCellRendererComponent(table, value, isSelected, true, row, column);
			if (c != null) {
				editorComponent.setOpaque(true);
				editorComponent.setBackground(c.getBackground());
				if (c instanceof JComponent) {
					editorComponent.setBorder(((JComponent) c).getBorder());
				}
			} else {
				editorComponent.setOpaque(false);
			}
		}
		return editorComponent;
	}

	public boolean isDefaultLastValue() {
		return defaultLastValue;
	}

	/**
	 * 设置是否取上一个值
	 * 
	 * @param defaultLastValue
	 */
	public void setDefaultLastValue(boolean defaultLastValue) {
		this.defaultLastValue = defaultLastValue;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	/**
	 * 是否允许为空
	 * 
	 * @return
	 */
	public boolean isAllowNull() {
		return allowNull;
	}

	/**
	 * 设置值是否允许为空
	 * 
	 * @param allowNull
	 */
	public void setAllowNull(boolean allowNull) {
		this.allowNull = allowNull;
	}

	/**
	 * 设置是否允许为空,以及为空时是否允许移动
	 * 
	 * @param allowNull
	 * @param allowMove
	 */
	public void setAllowNull(boolean allowNull, boolean allowMove) {
		this.allowNull = allowNull;
		this.allowMove = allowMove;
	}

	public boolean isAllowLastValueReplaceCurrentValue() {
		return allowLastValueReplaceCurrentValue;
	}

	/**
	 * 设置是否允许当前有值的时候,允许上一个值覆盖当前的值
	 * 
	 * @param allowLastValueReplaceCurrentValue
	 */
	public void setAllowLastValueReplaceCurrentValue(boolean allowLastValueReplaceCurrentValue) {
		this.allowLastValueReplaceCurrentValue = allowLastValueReplaceCurrentValue;
	}

	public boolean isEnterToNextCell() {
		return enterToNextCell;
	}

	public void setEnterToNextCell(boolean enterToNextCell) {
		this.enterToNextCell = enterToNextCell;
	}

	public int getRow() {
		return row;
	}

	public OurTableEnterAction getOurTableEnterAction() {
		return ourTableEnterAction;
	}

	public void setOurTableEnterAction(OurTableEnterAction ourTableEnterAction) {
		this.ourTableEnterAction = ourTableEnterAction;
	}

	public EditorType getEditorType() {
		return editorType;
	}

	public void setEditorType(EditorType editorType) {
		this.editorType = editorType;
	}

	protected class EditorDelegate implements ActionListener, ItemListener, Serializable {

		protected Object value;

		public Object getCellEditorValue() {
			return value;
		}

		public void setValue(Object value) {
			this.value = value;
		}

		public boolean isCellEditable(EventObject anEvent) {
			if (anEvent instanceof MouseEvent) {
				return ((MouseEvent) anEvent).getClickCount() >= clickCountToStart;
			}
			return true;
		}

		public boolean shouldSelectCell(EventObject anEvent) {
			return true;
		}

		public boolean startCellEditing(EventObject anEvent) {
			return true;
		}

		public boolean stopCellEditing() {
			fireEditingStopped();
			return true;
		}

		public void cancelCellEditing() {
			fireEditingCanceled();
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			boolean b = OurTableCellEditor.this.stopCellEditing();
			if (b && enterToNextCell && null != ourTableEnterAction) {
				if (e.getSource() instanceof JComboBox) {
				} else if (e.getSource() instanceof JCheckBox) {
				} else {
					ourTableEnterAction.actionPerformed(e);
				}
			}
		}

		@Override
		public void itemStateChanged(ItemEvent e) {
			OurTableCellEditor.this.stopCellEditing();
		}
	}

	public enum EditorType {
		/**
		 * 编辑器为默认类型
		 */
		defaultType,
		/**
		 * 编辑器为序号类型
		 */
		indexType;
	}

}
