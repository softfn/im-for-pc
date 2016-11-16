package com.oim.common.component.table;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import com.oim.common.component.calendar.CalendarComboBox;




/**
 *
 * @author XiaHui
 */
@SuppressWarnings("serial")
public class OurTableDateCellEditor extends OurTableCellEditor {

    JTextField text;

    public OurTableDateCellEditor() {
        this(new CalendarComboBox());
    }
    @SuppressWarnings("rawtypes")
    public OurTableDateCellEditor(final JComboBox comboBox) {
        editorComponent = comboBox;
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
                if (comboBox.isEditable()) {
                    // Commit edited value.
                    comboBox.actionPerformed(new ActionEvent(OurTableDateCellEditor.this, 0, ""));
                }
                return super.stopCellEditing();
            }
        };
        comboBox.addActionListener(delegate);
        initComponents();
    }


    @Override
    public boolean stopCellEditing() {
        if (!isAllowNull()) {
            Object o = delegate.getCellEditorValue();
            if (null == o || "".equals(o)) {
                JComponent component = ((JComponent) getComponent());
                component.setBorder(new LineBorder(Color.red));
                if (component.isShowing()) {// && component instanceof
                    // JTextField
                    if (component instanceof CalendarComboBox) {
                        CalendarComboBox calendarComboBox = (CalendarComboBox) component;
                        if (null != calendarComboBox.getErrorMessage()) {
                            label.setText(calendarComboBox.getErrorMessage());
                        } else {
                            label.setText("不能为空！");
                        }
                    }
                    popupMenu.show(component, 0, 0 - popupMenu.getHeight());
                    // Popup popup =
                    // PopupFactory.getSharedInstance().getPopup(component,
                    // label, 0, component.getHeight());
                    // popup.show();
                    //  component.requestFocus();

                }
                if (!this.allowMove) {
                    return false;
                }
                // return false;
            }
        }
        return delegate.stopCellEditing();
    }
}
