/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oim.common.component;

import java.awt.Component;

import javax.swing.JList;

import com.oim.common.component.common.OurValue;
import com.only.laf.OnlyListCellRenderer;

/**
 * 
 * @author XiaHui
 */
@SuppressWarnings("serial")
public class OurListCellRenderer extends OnlyListCellRenderer {

	@SuppressWarnings("rawtypes")
	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		if (value instanceof OurValue) {
			super.getListCellRendererComponent(list, null, index, isSelected, cellHasFocus);
			OurValue tableValue = (OurValue) value;
			setText((tableValue.toStringValue() == null) ? "" : tableValue.toStringValue());
			return this;
		} else if (value instanceof Object[]) {
			super.getListCellRendererComponent(list, null, index, isSelected, cellHasFocus);
			Object[] array = (Object[]) value;
			setText((array.length > 1 && array[0] == null) ? "" : array[0].toString());
			return this;
		} else {
			return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		}
	}
}
