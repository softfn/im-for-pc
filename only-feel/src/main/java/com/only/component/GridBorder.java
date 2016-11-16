package com.only.component;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.border.AbstractBorder;

public class GridBorder extends AbstractBorder {
	private static final long serialVersionUID = -3127922461735948603L;
	private Color color;
	private int top, left, bottom, right;

	public GridBorder(Color color) {
		this(color, 1);
	}

	public GridBorder(Color color, int thickness) {
		this(color, thickness, thickness, thickness, thickness);
	}

	public GridBorder(Color color, int top, int left, int bottom, int right) {
		this.color = color;
		this.top = top;
		this.left = left;
		this.bottom = bottom;
		this.right = right;
	}

	public Insets getBorderInsets(Component c) {
		return getBorderInsets();
	}

	public Insets getBorderInsets(Component c, Insets insets) {
		insets.left = left;
		insets.top = top;
		insets.right = right;
		insets.bottom = bottom;
		return insets;
	}

	public Insets getBorderInsets() {
		return new Insets(top, left, bottom, right);
	}

	public boolean isBorderOpaque() {
		return false;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public void setInsets(int top, int left, int bottom, int right) {
		this.top = top;
		this.left = left;
		this.bottom = bottom;
		this.right = right;
	}

	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
		g.translate(x, y);
		g.setColor(color);

		if (top > 0) {
			g.fillRect(0, 0, width, top);
		}

		if (left > 0) {
			g.fillRect(0, 0, left, height);
		}

		if (bottom > 0) {
			g.fillRect(0, height - bottom, width, bottom);
		}

		if (right > 0) {
			g.fillRect(width - right, 0, right, height);
		}

		g.translate(-x, -y);
	}
}