/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.oim.common.component;

import java.awt.Component;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;

import com.only.OnlyTabbedPane;

/**
 * 2013-8-29 17:43:23
 * @author XiaHui
 */
@SuppressWarnings("serial")
public class OurTabbedPane extends OnlyTabbedPane{

    private Map<Object, Boolean> map = new HashMap<Object, Boolean>();

    @Override
    public void addTab(String title, Component component) {
        this.addTab(title, component, null, false);
    }

    public void addTab(String title, Component component, Icon icon) {
        this.addTab(title, component, icon, false);
    }

    public void addTab(String title, Component component, boolean close) {
        this.addTab(title, component, null, close);
    }

    public void addTab(String title, Component component, Icon icon, boolean close) {
        this.addTab(title, component, icon, null, close);
    }

    public void addTab(String title, Component component, Icon icon, String tip, boolean close) {
        map.put(component, close);
        super.addTab(title, icon, component, tip);
    }

    @Override
    public void insertTab(String title, Icon icon, Component component, String tip, int index) {
        super.insertTab(title, icon, component, tip, index);
        Boolean close = map.get(component);
        if (null != close) {
            int i = indexOfComponent(component);
            if (i != -1) {
                OurTabComponent ButtonTabComponent = new OurTabComponent(this, close);
                if (null != icon) {
                    ButtonTabComponent.setIcon(icon);
                }
                setTabComponentAt(i, ButtonTabComponent);
            }
        }
    }

    @Override
    public void removeTabAt(int index) {
        Component component = getComponentAt(index);
        map.remove(component);
        super.removeTabAt(index);
    }
}
