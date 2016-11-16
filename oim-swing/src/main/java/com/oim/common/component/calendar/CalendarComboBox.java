/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oim.common.component.calendar;

import javax.swing.JTextField;
import javax.swing.plaf.basic.BasicComboBoxEditor;

import com.only.OnlyCalendarComboBox;

/**
 * 
 * @author XiaHui
 */
@SuppressWarnings("serial")
public class CalendarComboBox extends OnlyCalendarComboBox {

	CalendarTextField textField;
	String text;

	public CalendarComboBox() {
		super();
		initEvent();

	}

	private void initEvent() {
		this.setEditor(new BasicComboBoxEditor.UIResource() {
			@Override
			public JTextField createEditorComponent() {
				textField = new CalendarTextField();
				textField.setName("ComboBox.textField");
				textField.setBorder(null);
				return textField;
			}
		});
	}

	@Override
	public String getSelectedItem() {
		return (null != textField) ? textField.getText() : null;
	}

	@Override
	public void setSelectedItem(Object o) {
		if (null != textField) {
			textField.setText(null == o ? "" : o.toString());
		}
	}

	public String getErrorMessage() {
		return (null != textField) ? textField.getErrorMessage() : null;
	}
}
