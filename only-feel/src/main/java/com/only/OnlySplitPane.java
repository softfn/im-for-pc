/*
 * This file is part of WebLookAndFeel library.
 *
 * WebLookAndFeel library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * WebLookAndFeel library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with WebLookAndFeel library.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.only;

import com.only.laf.OnlySplitPaneUI;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ComponentListener;

/**
 * 
 * @author XiaHui
 * @date 2015年1月8日 下午3:24:59
 */
@SuppressWarnings("serial")
public class OnlySplitPane extends JSplitPane {
	// private Color color = new Color(158, 158, 158);
	// private Color transparent = new Color(0, 0, 0, 0);
	// private Color[] gradient = new Color[] { transparent, color, color,
	// transparent };

	private boolean drawDividerLine = false;
	private Color dividerLineColor = new Color(80, 80, 80);
	private int dividerLineSize = 1;

	public OnlySplitPane() {
		super();
		initOnlySplitPane();
	}

	public OnlySplitPane(int newOrientation) {
		super(newOrientation);
		initOnlySplitPane();
	}

	public OnlySplitPane(int newOrientation, boolean newContinuousLayout) {
		super(newOrientation, newContinuousLayout);
		initOnlySplitPane();
	}

	public OnlySplitPane(int newOrientation, Component newLeftComponent, Component newRightComponent) {
		super(newOrientation, newLeftComponent, newRightComponent);
		initOnlySplitPane();
	}

	public OnlySplitPane(int newOrientation, boolean newContinuousLayout, Component newLeftComponent, Component newRightComponent) {
		super(newOrientation, newContinuousLayout, newLeftComponent, newRightComponent);
		initOnlySplitPane();
	}

	public void addDividerListener(ComponentListener listener) {
		getWebUI().getDivider().addComponentListener(listener);
	}

	public void removeDividerListener(ComponentListener listener) {
		getWebUI().getDivider().removeComponentListener(listener);
	}

	public Color getDragDividerColor() {
		return getWebUI().getDragDividerColor();
	}

	public void setDragDividerColor(Color dragDividerColor) {
		getWebUI().setDragDividerColor(dragDividerColor);
	}

	public OnlySplitPaneUI getWebUI() {
		return (OnlySplitPaneUI) getUI();
	}

	private void initOnlySplitPane() {
		this.setUI(new OnlySplitPaneUI());
	}

	@Override
	public void updateUI() {
		setUI(getUI());
		revalidate();
	}

	public boolean isDrawDividerLine() {
		return drawDividerLine;
	}

	public void setDrawDividerLine(boolean drawDividerLine) {
		if(this.drawDividerLine!=drawDividerLine){
			this.drawDividerLine = drawDividerLine;
			this.repaint();
		}
	}

	public Color getDividerLineColor() {
		return dividerLineColor;
	}

	public void setDividerLineColor(Color dividerLineColor) {
		this.dividerLineColor = dividerLineColor;
		this.repaint();
	}

	public int getDividerLineSize() {
		return dividerLineSize;
	}

	public void setDividerLineSize(int dividerLineSize) {
		this.dividerLineSize = dividerLineSize;
		this.repaint();
	}

	// public void paint(Graphics g) {
	// super.paint(g);
	// Graphics2D g2d = (Graphics2D) g;
	//
	// if (getOrientation() == JSplitPane.HORIZONTAL_SPLIT) {
	// int startY = getHeight() / 2 - 35;
	// int endY = getHeight() / 2 + 35;
	// g2d.setPaint(new LinearGradientPaint(0, startY, 0, endY, new float[] {
	// 0f, 0.25f, 0.75f, 1f }, gradient));
	// for (int i = startY; i < endY; i += 5) {
	// g2d.fillRect(getWidth() / 2 - 1, i - 1, 2, 2);
	// }
	// } else {
	// int startX = getWidth() / 2 - 35;
	// int endX = getWidth() / 2 + 35;
	// g2d.setPaint(new LinearGradientPaint(startX, 0, endX, 0, new float[] {
	// 0f, 0.25f, 0.75f, 1f }, gradient));
	// for (int i = startX; i < endX; i += 5) {
	// g2d.fillRect(i - 1, getHeight() / 2 - 1, 2, 2);
	// }
	// }
	//
	// }
}
