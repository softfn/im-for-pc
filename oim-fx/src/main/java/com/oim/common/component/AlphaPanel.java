package com.oim.common.component;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.RenderingHints;

import com.only.OnlyPanel;

/**
 * 这个面板组件可以调节颜色的透明度
 * 
 * @author XiaHui
 * @date 2016年2月10日 下午11:49:41
 * @version 0.0.1
 */
public class AlphaPanel extends OnlyPanel {

	private static final long serialVersionUID = 1L;
	private Color backgroundColor = new Color(255, 255, 255, 200);

	public AlphaPanel() {
		super();

	}

	public AlphaPanel(boolean isDoubleBuffered) {
		super(isDoubleBuffered);

	}

	public AlphaPanel(LayoutManager layout) {
		super(layout);

	}

	public AlphaPanel(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (null != backgroundColor && backgroundColor.getAlpha() > 0) {
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
		this.repaint();
	}

	/**
	 * 设置背景颜色的透明度，0-255，越小越透明
	 * 
	 * @param alpha
	 */
	public void setBackgroundColorAlpha(int alpha) {
		this.backgroundColor = new Color(backgroundColor.getRed(), backgroundColor.getGreen(), backgroundColor.getBlue(), alpha);
		this.repaint();
	}
}
