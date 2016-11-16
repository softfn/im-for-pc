/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oim.common.component;

import com.only.OnlyCalendarComboBox;

/**
 *
 * @author XiaHui
 */
@SuppressWarnings("serial")
public class OurCalendarComboBox extends OnlyCalendarComboBox {

    public OurCalendarComboBox() {
        super();
        initEvent();
    }

    private void initEvent() {
        addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                comboBoxFocusGained();
            }
        });
    }

    private void comboBoxFocusGained() {
        this.showPopup();
    }
}
