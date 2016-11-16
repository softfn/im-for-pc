package com.only.laf;

import com.only.OnlyTable;
import java.awt.Graphics;
import java.lang.reflect.Field;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTableUI;

import com.only.util.OnlyUIUtil;

public class OnlyTableUI extends BasicTableUI {

    public static ComponentUI createUI(JComponent table) {
        return new OnlyTableUI();
    }

    @Override
    public void update(Graphics g, JComponent c) {
        paintBackground(g, c);
        super.update(g, c);
    }

    private void paintBackground(Graphics g, JComponent c) {
        if (c instanceof OnlyTable) {
            OnlyTable onlyTable = (OnlyTable) c;
            OnlyUIUtil.paintBackground(g, c, onlyTable.getBackground(), onlyTable.getBackground(), onlyTable.getImage(), onlyTable.isImageOnly(), onlyTable.getAlpha(), onlyTable.getVisibleInsets());
        }
    }

    @Override
    protected void installDefaults() {
        try {
            Field isFileListField = BasicTableUI.class.getDeclaredField("isFileList");
            isFileListField.setAccessible(true);
            isFileListField.set(this, Boolean.TRUE.equals(table.getClientProperty("Table.isFileList")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}