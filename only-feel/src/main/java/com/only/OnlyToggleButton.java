package com.only;

import java.awt.Color;
import java.awt.Image;
import java.awt.Insets;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JToggleButton;
import javax.swing.border.EmptyBorder;

import com.only.box.UIBox;
import com.only.laf.OnlyToggleButtonUI;
import com.only.util.OnlyUIUtil;

public class OnlyToggleButton extends JToggleButton {

	private static final long serialVersionUID = -393947872129394154L;
	private Image normalImage;
	private Image disabledImage;
	private Image disabledSelectedImage;
	private Image pressedImage;
	private Image selectedImage;
	private Image rolloverImage;
	private Image rolloverSelectedImage;
	private float alpha;
	private Insets imageInsets;
	private Color disabledTextColor;
	private boolean imageOnly;
	private boolean paintPressDown;

	public OnlyToggleButton() {
		this(null, null, false);
	}

	public OnlyToggleButton(Icon icon) {
		this(null, icon, false);
	}

	public OnlyToggleButton(Icon icon, boolean selected) {
		this(null, icon, selected);
	}

	public OnlyToggleButton(String text) {
		this(text, null, false);
	}

	public OnlyToggleButton(String text, boolean selected) {
		this(text, null, selected);
	}

	public OnlyToggleButton(Action a) {
		this();
		setAction(a);
	}

	public OnlyToggleButton(String text, Icon icon) {
		this(text, icon, false);
	}

	public OnlyToggleButton(String text, Icon icon, boolean selected) {
		super(text, icon, selected);
		setUI(new OnlyToggleButtonUI());
		alpha = 1.0f;
		imageOnly = true;
		paintPressDown = true;
		imageInsets = new Insets(3, 3, 3, 3);
		disabledTextColor = new Color(103, 117, 127);
		setForeground(new Color(0, 28, 48));
		setBackground(Color.GRAY);
		setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentAreaFilled(false);
		setFont(OnlyUIUtil.getDefaultFont());
		setFocusable(false);
		setRolloverEnabled(true);
		setIconTextGap(5);
		setMargin(new Insets(0, 0, 0, 0));
		super.setOpaque(false);
		normalImage = UIBox.getImage(UIBox.key_image_toggle_button_normal);
		rolloverImage = UIBox.getImage(UIBox.key_image_toggle_button_rollover);
		disabledImage = UIBox.getImage(UIBox.key_image_toggle_button_disabled);
		selectedImage = UIBox.getImage(UIBox.key_image_toggle_button_selected);
		disabledSelectedImage = UIBox.getImage(UIBox.key_image_toggle_button_selected_disabled);
	}

	public Image getNormalImage() {
		return normalImage;
	}

	public void setNormalImage(Image image) {
		this.normalImage = image;
		this.repaint();
	}

	public Image getDisabledImage() {
		return disabledImage;
	}

	public void setDisabledImage(Image disabledImage) {
		this.disabledImage = disabledImage;
		this.repaint();
	}

	public Image getDisabledSelectedImage() {
		return disabledSelectedImage;
	}

	public void setDisabledSelectedImage(Image disabledSelectedImage) {
		this.disabledSelectedImage = disabledSelectedImage;
		this.repaint();
	}

	public Image getPressedImage() {
		return pressedImage;
	}

	public void setPressedImage(Image pressedImage) {
		this.pressedImage = pressedImage;
		this.repaint();
	}

	public Image getSelectedImage() {
		return selectedImage;
	}

	public void setSelectedImage(Image selectedImage) {
		this.selectedImage = selectedImage;
		this.repaint();
	}

	public Image getRolloverImage() {
		return rolloverImage;
	}

	public void setRolloverImage(Image rolloverImage) {
		this.rolloverImage = rolloverImage;
		this.repaint();
	}

	public Image getRolloverSelectedImage() {
		return rolloverSelectedImage;
	}

	public void setRolloverSelectedImage(Image rolloverSelectedImage) {
		this.rolloverSelectedImage = rolloverSelectedImage;
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

	public Insets getImageInsets() {
		return imageInsets;
	}

	public void setImageInsets(int top, int left, int bottom, int right) {
		this.imageInsets.set(top, left, bottom, right);
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
	}

	@Deprecated
	public void setOpaque(boolean opaque) {
	}
}