/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oim.ui.component;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * date 2012-9-14 7:41:47
 * 
 * @author XiaHui
 */
public class StatusLabel extends JLabel {

	private static final long serialVersionUID = 1L;
	private boolean isMouseEntered = false;
	private int rx = 5;
	private int ry = 5;
	private Color borderColor = new Color(110, 110, 110);
	private Color backgroundColor = new Color(255, 255, 255, 80);
	private boolean drawBorder = false;


	public StatusLabel() {
		initEvent();
	}

	public StatusLabel(ImageIcon icon) {
		super(icon);
		initEvent();

	}

	public StatusLabel(String text) {
		super(text);
		initEvent();
	}

	private void initEvent() {

		this.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				isMouseEntered = true;
				drawBorder = true;
				repaint();
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				isMouseEntered = false;
				drawBorder = false;
				repaint();
			}

			@Override
			public void mousePressed(MouseEvent e) {

			}

			@Override
			public void mouseReleased(MouseEvent e) {

			}
		});
	}

	public void setRoundedCorner(int rx, int ry) {
		if (this.rx != rx || this.ry != ry) {
			this.rx = rx;
			this.ry = ry;
			repaint();
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setStroke(new BasicStroke(1));
		if (drawBorder) {// 边框
			g2.setColor(borderColor);
			g2.drawRoundRect(0, 0, this.getWidth() - 1, this.getHeight() - 1, rx, ry);
		}
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setStroke(new BasicStroke(1));
		if (isMouseEntered) {// 鼠标进入颜色
			g2.setPaint(backgroundColor);
			g2.fillRoundRect(1, 1, this.getWidth() - 2, this.getHeight() - 2, rx, ry);
		}

	}

	public Color getBorderColor() {
		return borderColor;
	}

	public void setBorderColor(Color borderColor) {
		this.borderColor = borderColor;
		repaint();
	}

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
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
