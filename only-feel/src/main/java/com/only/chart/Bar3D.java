package com.only.chart;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

public class Bar3D extends Chart3DBean {

    private static final long serialVersionUID = 6928396989040217938L;
    private int offsetX;
    private int offsetY;
    private float topAlpha;
    private Number data;
    private Rectangle2D rect;

    public Bar3D(String name) {
        this.name = name;
        this.textColor = Color.BLACK;
        this.rect = new Rectangle2D.Double();
        this.legendBounds = new Rectangle();
        this.textPosition = new Point();
        this.setArea(new Area());
    }

    public int getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(int offsetX) {
        this.offsetX = offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(int offsetY) {
        this.offsetY = offsetY;
    }

    public float getTopAlpha() {
        return topAlpha;
    }

    public void setTopAlpha(float topAlpha) {
        this.topAlpha = topAlpha;
    }

    public Color getBarColor() {
        return super.getColor();
    }

    public void setBarColor(Color barColor) {
        super.setColor(barColor);
    }

    public Number getData() {
        return data;
    }

    public void setData(Number data) {
        this.data = data;
    }

    public Rectangle2D getRect() {
        return rect;
    }

    public void setRect(double x, double y, double width, double height) {
        rect.setRect(x, y, width, height);
    }
}