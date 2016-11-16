/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oim.common.component;

import javax.swing.Action;
import javax.swing.Icon;

import com.only.OnlyMenuItem;

/**
 * 
 * @author XiaHui
 */
@SuppressWarnings("serial")
public class OurMenuItem extends OnlyMenuItem {

	private Object object;

	public OurMenuItem() {
	}

	public OurMenuItem(Icon icon) {
		super(icon);
	}

	public OurMenuItem(String text) {
		super(text);
	}

	public OurMenuItem(Action a) {
		super(a);
	}

	public OurMenuItem(String text, Icon icon) {
		super(text, icon);
	}

	public OurMenuItem(String text, int mnemonic) {
		super(text, mnemonic);
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}
}
