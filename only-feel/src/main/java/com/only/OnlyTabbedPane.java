package com.only;

import com.only.laf.OnlyTabbedPaneUI;
import java.awt.Color;
import java.awt.Component;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;

import com.only.util.OnlyUIUtil;

public class OnlyTabbedPane extends JTabbedPane {

	private static final long serialVersionUID = 7830093625100770074L;
	private int tabHeight;
	private Color disabledForeground;
	private Map<Component, Color> disabledForegroundMap;
	private float alpha;
	private Color rolloverTextColor;
	private Color selectedTextColor;
	
	public OnlyTabbedPane() {
		this(TOP, WRAP_TAB_LAYOUT);
	}

	public OnlyTabbedPane(int tabPlacement) {
		this(tabPlacement, WRAP_TAB_LAYOUT);
	}

	public OnlyTabbedPane(int tabPlacement, int tabLayoutPolicy) {
		super(tabPlacement, tabLayoutPolicy);
		setUI(new OnlyTabbedPaneUI());
		setFont(OnlyUIUtil.getDefaultFont());
		setForeground(new Color(255, 255, 255));
		setBackground(Color.GRAY);
		setBorder(new EmptyBorder(0, 0, 0, 0));
		setOpaque(false);
		setTabLayoutPolicy(SCROLL_TAB_LAYOUT);
		tabHeight = 24;
		disabledForeground = new Color(123, 123, 122);
		disabledForegroundMap = new HashMap<Component, Color>();
		rolloverTextColor=new Color(0, 0, 0);
		selectedTextColor=new Color(0, 0, 0);
		this.alpha = 1.0F;
	}

	public int getTabHeight() {
		return tabHeight;
	}

	public void setTabHeight(int tabHeight) {
		this.tabHeight = tabHeight;
		this.revalidate();
	}

	public Color getDisabledForeground() {
		return disabledForeground;
	}

	public void setDisabledForeground(Color disabledForeground) {
		this.disabledForeground = disabledForeground;
		this.repaint();
	}

	public Color getDisabledForegroundAt(int tabIndex) {
		Component c = getComponentAt(tabIndex);
		Color color = c == null ? null : disabledForegroundMap.get(c);
		return color == null ? disabledForeground : color;
	}

	public void setDisabledForegroundAt(int tabIndex, Color disabledForeground) {
		Component c = getComponentAt(tabIndex);
		if (c != null) {
			disabledForegroundMap.put(c, disabledForeground);
			if (!isEnabledAt(tabIndex) || !this.isEnabled()) {
				this.repaint();
			}
		}
	}

	@Override
	public void removeTabAt(int index) {
		Component c = getComponentAt(index);
		if (c != null) {
			disabledForegroundMap.remove(c);
		}
		super.removeTabAt(index);
	}

	@Override
	public void setComponentAt(int index, Component component) {
		Component c = getComponentAt(index);
		Color color = null;
		if (c != null) {
			color = disabledForegroundMap.remove(c);
		}
		super.setComponentAt(index, component);
		if (color != null) {
			disabledForegroundMap.put(component, color);
		}
	}

	public float getAlpha() {
		return alpha;
	}

	public void setAlpha(float alpha) {
		if ((alpha >= 0.0F) && (alpha <= 1.0F)) {
			this.alpha = alpha;
			repaint();
		} else {
			this.alpha = 1.0F;
		}
	}

	@Deprecated
	@Override
	public void updateUI() {
	}

	public Color getRolloverTextColor() {
		return rolloverTextColor;
	}

	public void setRolloverTextColor(Color rolloverTextColor) {
		this.rolloverTextColor = rolloverTextColor;
		this.repaint();
	}

	public Color getSelectedTextColor() {
		return selectedTextColor;
	}

	public void setSelectedTextColor(Color selectedTextColor) {
		this.selectedTextColor = selectedTextColor;
		this.repaint();
	}
	
	
}