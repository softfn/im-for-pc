package com.only.laf;

import com.only.OnlyList;
import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicListUI;

import com.only.util.OnlyUIUtil;

public class OnlyListUI extends BasicListUI {

    public static ComponentUI createUI(JComponent list) {
        return new OnlyListUI();
    }

    @Override
    public void update(Graphics g, JComponent c) {
        paintBackground(g, c);
        super.update(g, c);
    }

    private void paintBackground(Graphics g, JComponent c) {
        if (c instanceof OnlyList) {
            OnlyList<?> onlyList = (OnlyList<?>) c;
            OnlyUIUtil.paintBackground(g, c, onlyList.getBackground(), onlyList.getBackground(), onlyList.getImage(), onlyList.isImageOnly(), onlyList.getAlpha(), onlyList.getVisibleInsets());
        }
    }

    @Override
    protected void installDefaults() {
        list.setLayout(null);
    }

    @Override
    protected void uninstallDefaults() {
        if (list.getTransferHandler() instanceof UIResource) {
            list.setTransferHandler(null);
        }
    }
}