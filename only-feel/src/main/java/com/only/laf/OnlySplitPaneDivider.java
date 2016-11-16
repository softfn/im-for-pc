/*
 * Copyright (c) 1998, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

package com.only.laf;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JSplitPane;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import com.only.OnlyButton;
import com.only.OnlySplitPane;
import com.only.box.UIBox;

/**
 * 
 * @author XiaHui
 * @date 2015年1月8日 下午4:48:30
 */
public class OnlySplitPaneDivider extends BasicSplitPaneDivider {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4749207407605321059L;

	/**
	 * Creates a new Windows SplitPaneDivider
	 */
	public OnlySplitPaneDivider(BasicSplitPaneUI ui) {
		super(ui);
	}

	/**
	 * Paints the divider.
	 */
	public void paint(Graphics g) {
		if (this.splitPane instanceof OnlySplitPane) {
			Dimension size = getSize();
			OnlySplitPane sp = (OnlySplitPane) this.splitPane;
			boolean drawDividerLine = sp.isDrawDividerLine();
			Color dividerLineColor = sp.getDividerLineColor();
			int dividerLineSize = sp.getDividerLineSize();
			if (drawDividerLine) {
				g.setColor(dividerLineColor);
				if (OnlySplitPane.VERTICAL_SPLIT == sp.getOrientation()) {
					g.fillRect(0, (size.height/2), size.width, dividerLineSize);
				} else {
					g.fillRect((size.width/2), 0, dividerLineSize, size.height);
				}
			}
		}
		// Color bgColor = (splitPane.hasFocus()) ?
		// UIManager.getColor("SplitPane.shadow") : getBackground();
		// Dimension size = getSize();
		//
		// if (bgColor != null) {
		// g.setColor(Color.red);
		// g.fillRect(0, 0, 10, 2);
		// }
		
		Graphics g2 = g.create();

		Rectangle bounds = getBounds();

		bounds.x = bounds.y = 0;

		// super.paint(g2);
		for (int counter = 0; counter < getComponentCount(); counter++) {
			Component child = getComponent(counter);
			Rectangle childBounds = child.getBounds();
			Graphics childG = g.create(childBounds.x, childBounds.y, childBounds.width, childBounds.height);

			child.paint(childG);
			childG.dispose();
		}

		g2.dispose();
		// super.paint(g);
	}

	protected JButton createLeftOneTouchButton() {
		// key_icon_split_pane_touch_left
		// key_icon_split_pane_touch_up
		// key_icon_split_pane_touch_right
		// key_icon_split_pane_touch_down

		Icon leftIcon = UIBox.getIcon(UIBox.key_icon_split_pane_touch_left);
		Icon upIcon = UIBox.getIcon(UIBox.key_icon_split_pane_touch_up);

		Dimension dimension = splitPane.getOrientation() == JSplitPane.HORIZONTAL_SPLIT ? new Dimension(6, 7) : new Dimension(7, 6);

		OnlyButton iconWebButton = new OnlyButton((splitPane.getOrientation() == JSplitPane.HORIZONTAL_SPLIT) ? leftIcon : upIcon);
		iconWebButton.setNormalImage(null);
		iconWebButton.setRolloverImage(null);
		iconWebButton.setPressedImage(null);
		iconWebButton.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		iconWebButton.setCursor(Cursor.getDefaultCursor());
		iconWebButton.setPreferredSize(dimension);
		iconWebButton.setBorder(null);
		// iconWebButton.setAlpha(0.3f);

		return iconWebButton;
	}

	//
	protected JButton createRightOneTouchButton() {
		Icon rightIcon = UIBox.getIcon(UIBox.key_icon_split_pane_touch_right);
		Icon downIcon = UIBox.getIcon(UIBox.key_icon_split_pane_touch_down);

		OnlyButton iconWebButton = new OnlyButton((orientation == JSplitPane.HORIZONTAL_SPLIT) ? rightIcon : downIcon);

		Dimension dimension = orientation == JSplitPane.HORIZONTAL_SPLIT ? new Dimension(6, 7) : new Dimension(7, 6);

		iconWebButton.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		iconWebButton.setCursor(Cursor.getDefaultCursor());
		iconWebButton.setPreferredSize(dimension);
		iconWebButton.setBorder(null);
		// iconWebButton.setAlpha(0.3f);;
		iconWebButton.setNormalImage(null);
		iconWebButton.setRolloverImage(null);
		iconWebButton.setPressedImage(null);
		return iconWebButton;
	}
}
