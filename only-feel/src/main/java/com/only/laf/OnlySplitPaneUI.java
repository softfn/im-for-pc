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
package com.only.laf;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.JSplitPane;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

/**
 * 
 * @author XiaHui
 * @date 2015年1月8日 下午3:24:48
 */
public class OnlySplitPaneUI extends BasicSplitPaneUI {

	public Color dragDividerColor = Color.LIGHT_GRAY;

	public static ComponentUI createUI(JComponent c) {
		return new OnlySplitPaneUI();
	}

	@Override
	public void installUI(JComponent c) {
		super.installUI(c);
		splitPane.setOpaque(false);
		splitPane.setBorder(null);
		splitPane.setDividerSize(8);
	}

	public Color getDragDividerColor() {
		return dragDividerColor;
	}

	public void setDragDividerColor(Color dragDividerColor) {
		this.dragDividerColor = dragDividerColor;
	}

	public BasicSplitPaneDivider createDefaultDivider() {
		OnlySplitPaneDivider d=new OnlySplitPaneDivider(this);
		return d;
	}

	@SuppressWarnings("serial")
	@Override
	protected Component createDefaultNonContinuousLayoutDivider() {
		return new Canvas() {
			@Override
			public void paint(Graphics g) {
				if (!isContinuousLayout() && getLastDragLocation() != -1) {
					Dimension size = splitPane.getSize();
					g.setColor(dragDividerColor);
					if (getOrientation() == JSplitPane.HORIZONTAL_SPLIT) {
						g.fillRect(0, 0, dividerSize - 1, size.height - 1);
					} else {
						g.fillRect(0, 0, size.width - 1, dividerSize - 1);
					}
				}
			}
		};
	}

	@Override
	public void finishedPaintingChildren(JSplitPane jc, Graphics g) {
		if (jc == splitPane && getLastDragLocation() != -1 && !isContinuousLayout() && !draggingHW) {
			Dimension size = splitPane.getSize();
			g.setColor(dragDividerColor);
			if (getOrientation() == JSplitPane.HORIZONTAL_SPLIT) {
				g.fillRect(getLastDragLocation(), 0, dividerSize - 1, size.height - 1);
			} else {
				g.fillRect(0, getLastDragLocation(), size.width - 1, dividerSize - 1);
			}
		}
	}

	@Override
	public void paint(Graphics g, JComponent c) {
		super.paint(g, c);
	}
}
