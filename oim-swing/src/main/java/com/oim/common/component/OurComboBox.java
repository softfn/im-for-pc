/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.oim.common.component;

import com.only.OnlyComboBox;

/**
 * 2013-9-8 15:02:32
 * 
 * @author XiaHui
 * @param <E>
 */
public class OurComboBox<E> extends OnlyComboBox<E> {

	private static final long serialVersionUID = 1L;

	public OurComboBox() {
		super();
		initComponents();
	}

	@SuppressWarnings("unchecked")
	private void initComponents() {
		this.setRenderer(new OurListCellRenderer());
	}
}
