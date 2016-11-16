package com.only.laf;

import com.only.OnlyScrollPane;
import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.ScrollPaneUI;
import javax.swing.plaf.basic.BasicScrollPaneUI;

import com.only.util.OnlyUIUtil;

public class OnlyScrollPaneUI extends BasicScrollPaneUI {

    private static ScrollPaneUI viewportUI;

    public static ComponentUI createUI(JComponent c) {
        if (viewportUI == null) {
            viewportUI = new OnlyScrollPaneUI();
        }

        return viewportUI;
    }

    @Override
    public void update(Graphics g, JComponent c) {
        if (c instanceof OnlyScrollPane) {
            paintBackground(g, c);
        }

        super.update(g, c);
    }

    private void paintBackground(Graphics g, JComponent c) {
        OnlyScrollPane scrollPane = (OnlyScrollPane) c;
        OnlyUIUtil.paintBackground(g, scrollPane, scrollPane.getBackground(), scrollPane.getBackground(), scrollPane.getImage(), scrollPane.isImageOnly(), scrollPane.getAlpha(),
                scrollPane.getVisibleInsets());
    }

    protected void installDefaults(JScrollPane scrollpane) {
    }

    protected void uninstallDefaults(JScrollPane scrollpane) {
    }
}