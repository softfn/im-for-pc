/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oim.common.component;

import java.awt.LayoutManager;

import com.only.OnlyPanel;
import java.awt.FlowLayout;

/**
 * 2013-8-29 15:24:16
 *
 * @author XiaHui
 */
@SuppressWarnings("serial")
public class OurPanel extends OnlyPanel {

    public OurPanel() {
        this(true);
    }

    public OurPanel(LayoutManager layout) {
        this(layout, true);
    }

    public OurPanel(boolean isDoubleBuffered) {
        this(new FlowLayout(), isDoubleBuffered);
    }

    public OurPanel(LayoutManager layout, boolean isDoubleBuffered) {
        super(layout, isDoubleBuffered);
        initEvent();
    }

    private void initEvent() {
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized();
            }
        });
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        formComponentResized();
    }

    public void formComponentResized() {
    }

    public void execute() {
    }
}
