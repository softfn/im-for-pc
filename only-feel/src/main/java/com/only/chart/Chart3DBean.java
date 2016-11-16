package com.only.chart;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.io.Serializable;

public abstract class Chart3DBean implements Serializable {

    private static final long serialVersionUID = 4363749894056800443L;
    protected String name;
    protected Rectangle legendBounds;
    protected Point textPosition;
    protected boolean selected;
    protected String text;
    protected String toolTipText;
    protected Area area;
    protected Color color;
    protected Color darkerColor;
    protected Color textColor;

    public Rectangle getLegendBounds() {
        return legendBounds;
    }

    public void setLegendLocation(int x, int y) {
        legendBounds.setLocation(x, y);
    }

    public void setLegendSize(int width, int height) {
        legendBounds.setSize(width, height);
    }

    public void setLegendBounds(int x, int y, int width, int height) {
        this.legendBounds.setBounds(x, y, width, height);
    }

    public Point getTextPosition() {
        return textPosition;
    }

    public void setTextPosition(int x, int y) {
        textPosition.setLocation(x, y);
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getToolTipText() {
        return toolTipText;
    }

    public void setToolTipText(String toolTipText) {
        this.toolTipText = toolTipText;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getDarkerColor() {
        return darkerColor;
    }

    public void setDarkerColor(Color darkerColor) {
        this.darkerColor = darkerColor;
    }

    public Color getTextColor() {
        return textColor;
    }

    public void setTextColor(Color textColor) {
        this.textColor = textColor;
    }

    public String getName() {
        return name;
    }
}