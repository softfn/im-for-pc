package com.only;

import java.awt.Color;
import java.awt.Image;
import java.awt.Insets;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.border.EmptyBorder;

import com.only.box.OnlyImageBox;
import com.only.laf.OnlyButtonUI;
import com.only.util.OnlyUIUtil;

public class OnlyBorderButton extends OnlyButton {

	private static final long serialVersionUID = 1L;

	private Image normalImage;
	private Image disabledImage;
	private Image pressedImage;
	private Image rolloverImage;
	private Image focusImage;
	private float alpha;
	private Insets normalImageInsets;
	private Insets focusImageInsets;
	private Color disabledTextColor;
	private boolean imageOnly;
	private boolean paintPressDown;

	public OnlyBorderButton() {
		this(null, null);
	}

	public OnlyBorderButton(Icon icon) {
		this(null, icon);
	}

	public OnlyBorderButton(String text) {
		this(text, null);
	}

	public OnlyBorderButton(Action action) {
		this(null, null);
		this.setAction(action);
	}

	public OnlyBorderButton(String text, Icon icon) {
		super(text, icon);
		initOnlyButton();
	}

	private void initOnlyButton() {
		this.alpha = 1.0f;
		this.imageOnly = true;
		this.paintPressDown = true;
		this.normalImageInsets = new Insets(2, 2, 2, 2);
		this.focusImageInsets = new Insets(4, 4, 4, 4);
		this.disabledTextColor = new Color(103, 117, 127);
		this.setRolloverTextColor(new Color(0, 28, 48));
		this.setForeground(new Color(0, 28, 48));
		this.setBackground(Color.GRAY);
		this.setBorder(new EmptyBorder(0, 0, 0, 0));
		this.setContentAreaFilled(false);
		this.setFont(OnlyUIUtil.getDefaultFont());
		this.setRolloverEnabled(true);
		this.setIconTextGap(5);
		this.setMargin(new Insets(0, 0, 0, 0));

		normalImage = OnlyImageBox.getClassPathImageByName("button/button_border_normal.png");
		rolloverImage = OnlyImageBox.getClassPathImageByName("button/button_border_rollover.png");
		pressedImage = OnlyImageBox.getClassPathImageByName("button/button_border_pressed.png");
		focusImage = OnlyImageBox.getClassPathImageByName("button/button_border_foucs.png");
		disabledImage = OnlyImageBox.getClassPathImageByName("button/button_border_disabled.png");

		// this.normalImage = UIBox.getImage(UIBox.key_image_button_normal);
		// this.disabledImage = UIBox.getImage(UIBox.key_image_button_disabled);
		// this.pressedImage = UIBox.getImage(UIBox.key_image_button_pressed);
		// this.rolloverImage = UIBox.getImage(UIBox.key_image_button_rollover);
		// this.focusImage = UIBox.getImage(UIBox.key_image_button_foucs);
		this.setUI(new OnlyButtonUI());
	}

	public Image getNormalImage() {
		return normalImage;
	}

	public void setNormalImage(Image normalImage) {
		this.normalImage = normalImage;
		this.repaint();
	}

	public Image getDisabledImage() {
		return disabledImage;
	}

	public void setDisabledImage(Image disabledImage) {
		this.disabledImage = disabledImage;
		this.repaint();
	}

	public Image getPressedImage() {
		return pressedImage;
	}

	public void setPressedImage(Image pressedImage) {
		this.pressedImage = pressedImage;
		this.repaint();
	}

	public Image getRolloverImage() {
		return rolloverImage;
	}

	public void setRolloverImage(Image rolloverImage) {
		this.rolloverImage = rolloverImage;
		this.repaint();
	}

	public Image getFocusImage() {
		return focusImage;
	}

	public void setFocusImage(Image focusImage) {
		this.focusImage = focusImage;
		this.repaint();
	}

	public float getAlpha() {
		return alpha;
	}

	public void setAlpha(float alpha) {
		if (alpha >= 0.0f && alpha <= 1.0f) {
			this.alpha = alpha;
			this.repaint();
		} else {
			throw new IllegalArgumentException("Invalid alpha:" + alpha);
		}
	}

	public Insets getNormalImageInsets() {
		return normalImageInsets;
	}

	public void setNormalImageInsets(int top, int left, int bottom, int right) {
		this.normalImageInsets.set(top, left, bottom, right);
		this.repaint();
	}

	public Insets getFocusImageInsets() {
		return focusImageInsets;
	}

	public void setFocusImageInsets(int top, int left, int bottom, int right) {
		this.focusImageInsets.set(top, left, bottom, right);
		this.repaint();
	}

	public boolean isPaintPressDown() {
		return paintPressDown;
	}

	public void setPaintPressDown(boolean paintPressDown) {
		this.paintPressDown = paintPressDown;
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

	public boolean isImageOnly() {
		return imageOnly;
	}

	public void setImageOnly(boolean imageOnly) {
		this.imageOnly = imageOnly;
		this.repaint();
	}

	@Deprecated
	public void updateUI() {
		this.setUI(new OnlyButtonUI());
	}

	@Deprecated
	public void setOpaque(boolean opaque) {

	}
}