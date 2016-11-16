/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oim.common.component;

import javax.swing.Action;
import javax.swing.JMenu;

/**
 *
 * @author XiaHui
 */
@SuppressWarnings("serial")
public class OurMenu extends JMenu {

    private Object object;

    public OurMenu() {
    }

    public OurMenu(String s) {
        super(s);
    }

    public OurMenu(Action a) {
        super(a);
    }

    public OurMenu(String s, boolean b) {
        super(s, b);
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
