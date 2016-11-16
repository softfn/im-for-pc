package com.only.laf;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTableHeaderUI;

import com.only.box.UIBox;
import com.only.util.OnlyUIUtil;

public class OnlyTableHeaderUI extends BasicTableHeaderUI {

    private static final Image BG_IMAGE = UIBox.getImage(UIBox.key_image_table_header_default);
    private static final Image DISABLED_BG_IMAGE = OnlyUIUtil.toBufferedImage(BG_IMAGE, 0.5f, null);

    public static ComponentUI createUI(JComponent header) {
        return new OnlyTableHeaderUI();
    }

    @Override
    protected void rolloverColumnUpdated(int oldColumn, int newColumn) {
        header.repaint(header.getHeaderRect(oldColumn));
        header.repaint(header.getHeaderRect(newColumn));
    }

    @Override
    protected MouseInputListener createMouseInputListener() {
        return new OnlyMouseInputHandler();
    }

    @Override
    public void update(Graphics g, JComponent c) {
        Image image = c.isEnabled() ? BG_IMAGE : DISABLED_BG_IMAGE;
        OnlyUIUtil.paintImage(g, image, new Insets(1, 1, 1, 1), new Rectangle(0, 0, c.getWidth(), c.getHeight() - 1), c);
        paint(g, c);
    }

    @Override
    public int getRolloverColumn() {
        return super.getRolloverColumn();
    }

    @Override
    protected void installDefaults() {
    }

    public class OnlyMouseInputHandler extends MouseInputHandler {

        @Override
        public void mousePressed(MouseEvent e) {
            if (SwingUtilities.isLeftMouseButton(e)) {
                super.mousePressed(e);
            }
        }
    }
}