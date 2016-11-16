package com.only;

import com.only.laf.OnlyCheckBoxMenuItemUI;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.border.EmptyBorder;

import com.only.util.OnlyUIUtil;

public class OnlyCheckBoxMenuItem extends JCheckBoxMenuItem {
	private static final long serialVersionUID = 4910658177623130650L;

	private Color disabledTextColor;
	private Color selectedForeground;
	private int preferredHeight;

	public OnlyCheckBoxMenuItem() {
		this(null, null, false);
	}

	public OnlyCheckBoxMenuItem(Icon icon) {
		this(null, icon, false);
	}

	public OnlyCheckBoxMenuItem(String text) {
		this(text, null, false);
	}

	public OnlyCheckBoxMenuItem(Action a) {
		this();
		setAction(a);
	}

	public OnlyCheckBoxMenuItem(String text, Icon icon) {
		this(text, icon, false);
	}

	public OnlyCheckBoxMenuItem(String text, boolean selected) {
		this(text, null, selected);
	}

	public OnlyCheckBoxMenuItem(String text, Icon icon, boolean selected) {
		super(text, icon, selected);
		setUI(new OnlyCheckBoxMenuItemUI());
		setSelectedIcon(icon);
		setOpaque(false);
		setFont(OnlyUIUtil.getDefaultFont());
		setForeground(new Color(0, 20, 35));
		setBackground(Color.GRAY);
		setIconTextGap(0);
		setBorderPainted(false);
		setBorder(new EmptyBorder(0, 0, 0, 0));
		setFocusPainted(false);
		setRolloverEnabled(true);
		setMargin(new Insets(0, 0, 0, 0));
		selectedForeground = new Color(253, 253, 253);
		disabledTextColor = new Color(127, 137, 144);
		preferredHeight = 22;
	}

	public Color getDisabledTextColor() {
		return disabledTextColor;
	}

	public void setDisabledTextColor(Color disabledTextColor) {
		this.disabledTextColor = disabledTextColor;

		if (!this.isEnabled()) {
			this.repaint();
		}
	}

	public Color getSelectedForeground() {
		return selectedForeground;
	}

	public void setSelectedForeground(Color selectedForeground) {
		this.selectedForeground = selectedForeground;
		this.repaint();
	}

	public int getPreferredHeight() {
		return preferredHeight;
	}

	public void setPreferredHeight(int preferredHeight) {
		this.preferredHeight = preferredHeight;
		this.revalidate();
	}

	public Dimension getPreferredSize() {
		Dimension size = super.getPreferredSize();
		if (preferredHeight > 0) {
			size.height = preferredHeight;
		}
		return size;
	}

	@Deprecated
	public void updateUI() {
	}
}