/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oim.common.component.combo;

import java.awt.Color;
import java.awt.event.KeyListener;
import javax.swing.JComboBox;
import javax.swing.plaf.basic.BasicComboPopup;

/**
 *
 * @author XiaHui
 */
@SuppressWarnings("serial")
public class AutoComboPopup extends BasicComboPopup {

    @Override
    protected void configureList() {
        super.configureList();
        list.setSelectionForeground(Color.red);
        list.setSelectionBackground(Color.blue);
    }

    @SuppressWarnings("rawtypes")
	public AutoComboPopup(JComboBox cBox) {
        super(cBox);
    }

    @Override
    protected KeyListener createKeyListener() {
        return new InvocationKeyHandler();
    }

    protected class InvocationKeyHandler extends BasicComboPopup.InvocationKeyHandler {

        protected InvocationKeyHandler() {
            AutoComboPopup.this.super();
        }
    }
}
