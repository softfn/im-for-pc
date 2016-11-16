package com.over;

import java.awt.Color;

import javax.swing.BoundedRangeModel;
import javax.swing.JProgressBar;
import javax.swing.border.EmptyBorder;

import com.only.box.UIBox;
import com.only.util.OnlyUIUtil;
import com.over.ui.OverRound3DProgressBarUI;

public class OverRound3DProgressBar extends JProgressBar {

    private static final long serialVersionUID = 1632391629083296112L;
    private Color fontColor;
    private Color fontCoverClor;

    public OverRound3DProgressBar() {
        this(HORIZONTAL);
    }

    public OverRound3DProgressBar(int orient) {
        this(orient, 0, 100);
    }

    public OverRound3DProgressBar(int min, int max) {
        this(HORIZONTAL, min, max);
    }

    public OverRound3DProgressBar(int orient, int min, int max) {
        super(orient, min, max);
        init();
    }

    public OverRound3DProgressBar(BoundedRangeModel newModel) {
        super(newModel);
        init();
    }

    private void init() {
        this.fontColor = Color.ORANGE;
        this.fontCoverClor = UIBox.getWhiteColor();
        setUI(new OverRound3DProgressBarUI());
        setFont(OnlyUIUtil.getDefaultFont());
        setForeground(Color.BLUE);
        setBackground(Color.GRAY);
        setBorder(new EmptyBorder(0, 0, 0, 0));
        setBorderPainted(false);
        setOpaque(false);
    }

    @Deprecated
    public void updateUI() {
    }

    public Color getFontColor() {
        return this.fontColor;
    }

    public void setFontColor(Color fontColor) {
        this.fontColor = fontColor;
        this.repaint();
    }

    public Color getFontCoverClor() {
        return this.fontCoverClor;
    }

    public void setFontCoverClor(Color fontCoverClor) {
        this.fontCoverClor = fontCoverClor;
        this.repaint();
    }
}