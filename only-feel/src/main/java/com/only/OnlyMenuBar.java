package com.only;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JMenuBar;

@SuppressWarnings("serial")
public class OnlyMenuBar extends JMenuBar {
	
	private Color backgroundColor = new Color(255, 255, 255, 180);

	@Override
	protected void paintComponent(Graphics g) {

		if (null == backgroundColor || isOpaque() || backgroundColor.getAlpha() <= 0) {
			super.paintComponent(g);
		} else {
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setPaint(backgroundColor);
			g2.fillRect(0, 0, this.getWidth(), this.getHeight());
		}
	}

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
	
	
}
