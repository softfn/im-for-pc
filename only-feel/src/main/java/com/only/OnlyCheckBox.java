package com.only;

import java.awt.Color;
import java.awt.Insets;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.border.EmptyBorder;

import com.only.box.UIBox;
import com.only.laf.OnlyCheckBoxUI;
import com.only.util.OnlyUIUtil;

public class OnlyCheckBox extends JCheckBox {

	private static final long serialVersionUID = 1L;

	private Color disabledTextColor;
	private Icon pressedSelectedIcon;

	public OnlyCheckBox() {
		this(null, null, false);
	}

	public OnlyCheckBox(Icon icon) {
		this(null, icon, false);
	}

	public OnlyCheckBox(Icon icon, boolean selected) {
		this(null, icon, selected);
	}

	public OnlyCheckBox(String text) {
		this(text, null, false);
	}

	public OnlyCheckBox(Action a) {
		this();
		setAction(a);
	}

	public OnlyCheckBox(String text, boolean selected) {
		this(text, null, selected);
	}

	public OnlyCheckBox(String text, Icon icon) {
		this(text, icon, false);
	}

	public OnlyCheckBox(String text, Icon icon, boolean selected) {
		super(text, icon, selected);
		setUI(new OnlyCheckBoxUI());
		setForeground(new Color(0, 28, 48));
		setBackground(Color.GRAY);
		setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentAreaFilled(false);
		setFont(OnlyUIUtil.getDefaultFont());
		setOpaque(false);
		setBorderPainted(false);
		setHorizontalAlignment(LEADING);
		setRolloverEnabled(true);
		setIconTextGap(5);
		setMargin(new Insets(0, 0, 0, 0));
		disabledTextColor = new Color(103, 117, 127);
		setIcon(UIBox.getIcon(UIBox.key_icon_checkbox_normal));
		setPressedIcon(UIBox.getIcon(UIBox.key_icon_checkbox_pressed));
		setRolloverIcon(UIBox.getIcon(UIBox.key_icon_checkbox_rollover));
		setDisabledIcon(UIBox.getIcon(UIBox.key_icon_checkbox_disabled));

		setSelectedIcon(UIBox.getIcon(UIBox.key_icon_checkbox_selected_normal));
		setRolloverSelectedIcon(UIBox.getIcon(UIBox.key_icon_checkbox_selected_rollover));
		setPressedSelectedIcon(UIBox.getIcon(UIBox.key_icon_checkbox_selected_pressed));
		setDisabledSelectedIcon(UIBox.getIcon(UIBox.key_icon_checkbox_selected_disabled));
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

	public Icon getPressedSelectedIcon() {
		return pressedSelectedIcon;
	}

	public void setPressedSelectedIcon(Icon pressedSelectedIcon) {
		this.pressedSelectedIcon = pressedSelectedIcon;
		this.repaint();
	}

	@Deprecated
	public void updateUI() {
	}
}
