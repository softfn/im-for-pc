/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oim.common.component;

import java.util.Vector;
import javax.swing.JList;
import javax.swing.ListModel;

/**
 * 2013-8-29 15:24:06
 * 
 * @author XiaHui
 * @param <E>
 */
@SuppressWarnings({ "serial", "rawtypes", "unchecked" })
public class OurList<E> extends JList {

	public OurList() {
	}

	public OurList(ListModel dataModel) {
		super(dataModel);
	}

	public OurList(Object[] listData) {
		super(listData);
	}

	public OurList(Vector listData) {
		super(listData);
	}

}
