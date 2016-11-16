package com.oim.ui.component.list;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JButton;

/**
 * 描述：
 * 
 * @author XiaHui
 * @date 2015年3月1日 上午8:20:58
 * @version 0.0.1
 */
public class IconButton extends JButton {

	private static final long serialVersionUID = 1L;

	private int rx = 5;
	private int ry = 5;
	private Color borderColor = new Color(169, 238, 236);
	private boolean drawBorder = false;

	public IconButton() {

		initComponents();
		initEvent();
	}

	private void initComponents() {
		this.setBorder(null);
		this.setContentAreaFilled(false);
		this.setFocusPainted(false);
		this.setDoubleBuffered(true);

		this.setOpaque(false);
		this.setFocusable(false);
		this.setActionCommand(null);
		this.setSelected(false);
	}

	private void initEvent() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setStroke(new BasicStroke(1));
		if (drawBorder) {// 边框
			g2.setColor(borderColor);
			g2.drawRoundRect(0, 0, this.getWidth() - 1, this.getHeight() - 1, rx, ry);
		}
		super.paintComponent(g);
	}

	public void setRoundedCorner(int rx, int ry) {
		if (this.rx != rx || this.ry != ry) {
			this.rx = rx;
			this.ry = ry;
			repaint();
		}
	}

	public Color getBorderColor() {
		return borderColor;
	}

	public void setBorderColor(Color borderColor) {
		this.borderColor = borderColor;
		repaint();
	}

	public boolean isDrawBorder() {
		return drawBorder;
	}

	public void setDrawBorder(boolean drawBorder) {
		if (this.drawBorder != drawBorder) {
			this.drawBorder = drawBorder;
			repaint();
		}
	}
}
