package com.only.component;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.JComponent;
import javax.swing.border.EmptyBorder;

import com.only.box.UIBox;
import com.only.util.OnlyUIUtil;

public class HeaderPane extends JComponent {

    private static final long serialVersionUID = 5223934257160048018L;
    private static final Image BG_IMAGE = UIBox.getImage(UIBox.key_image_common_header_default);
    private static final Image DISABLED_BG_IMAGE = OnlyUIUtil.toBufferedImage(BG_IMAGE, 0.5f, null);
    private int headerHeight;

    public HeaderPane() {
        this(null);
    }

    public HeaderPane(Component c) {
        setOpaque(false);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(0, 0, 0, 0));
        headerHeight = 21;

        if (c != null) {
            add(c, BorderLayout.CENTER);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        Image image = this.isEnabled() ? BG_IMAGE : DISABLED_BG_IMAGE;
        OnlyUIUtil.paintImage(g, image, new Insets(1, 1, 1, 1), new Rectangle(0, 0, this.getWidth(), this.getHeight() - 1), this);
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension size = super.getPreferredSize();
        size.height = headerHeight;
        return size;
    }

    public int getHeaderHeight() {
        return headerHeight;
    }

    public void setHeaderHeight(int headerHeight) {
        this.headerHeight = headerHeight;
        this.revalidate();
    }
}