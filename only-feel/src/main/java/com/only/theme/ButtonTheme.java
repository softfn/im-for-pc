package com.only.theme;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;

import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import com.only.box.UIBox;
import com.only.util.OnlyUIUtil;

/**
 * @Author: XiaHui
 * @Date: 2015年12月29日
 * @ModifyUser: XiaHui
 * @ModifyDate: 2015年12月29日
 */
public class ButtonTheme {

	private Image normalImage;// 正常的按钮
	private Image disabledImage;//
	private Image pressedImage;//
	private Image rolloverImage;//
	private Image focusImage;//
	private float alpha;
	private Insets normalImageInsets;
	private Insets focusImageInsets;
	private Color disabledTextColor;
	private boolean imageOnly;
	private boolean paintPressDown;
	private Color foreground;
	private Color background;
	private Border border;
	private Font font;
	Insets margin;
	int iconTextGap;
	boolean rolloverEnabled;
	boolean contentAreaFilled;

	public ButtonTheme() {
		initTheme();
	}

	private void initTheme() {
		this.alpha = 1.0f;
		this.imageOnly = true;
		this.paintPressDown = true;
		this.normalImageInsets = new Insets(0, 0, 0, 0);
		this.focusImageInsets = new Insets(4, 4, 4, 4);
		this.disabledTextColor = new Color(235, 235, 235);
		this.setForeground(new Color(255, 255, 255));
		this.setBackground(Color.GRAY);
		this.setBorder(new EmptyBorder(0, 0, 0, 0));
		this.setContentAreaFilled(false);
		this.setFont(OnlyUIUtil.getDefaultFont());
		this.setRolloverEnabled(true);
		this.setIconTextGap(5);
		this.setMargin(new Insets(0, 0, 0, 0));

		this.normalImage = UIBox.getImage(UIBox.key_image_button_normal);
		this.disabledImage = UIBox.getImage(UIBox.key_image_button_disabled);
		this.pressedImage = UIBox.getImage(UIBox.key_image_button_pressed);
		this.rolloverImage = UIBox.getImage(UIBox.key_image_button_rollover);
		this.focusImage = UIBox.getImage(UIBox.key_image_button_foucs);
	}

	public Image getNormalImage() {
		return normalImage;
	}

	public void setNormalImage(Image normalImage) {
		this.normalImage = normalImage;
	}

	public Image getDisabledImage() {
		return disabledImage;
	}

	public void setDisabledImage(Image disabledImage) {
		this.disabledImage = disabledImage;
	}

	public Image getPressedImage() {
		return pressedImage;
	}

	public void setPressedImage(Image pressedImage) {
		this.pressedImage = pressedImage;
	}

	public Image getRolloverImage() {
		return rolloverImage;
	}

	public void setRolloverImage(Image rolloverImage) {
		this.rolloverImage = rolloverImage;
	}

	public Image getFocusImage() {
		return focusImage;
	}

	public void setFocusImage(Image focusImage) {
		this.focusImage = focusImage;
	}

	public float getAlpha() {
		return alpha;
	}

	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}

	public Insets getNormalImageInsets() {
		return normalImageInsets;
	}

	public void setNormalImageInsets(Insets normalImageInsets) {
		this.normalImageInsets = normalImageInsets;
	}

	public Insets getFocusImageInsets() {
		return focusImageInsets;
	}

	public void setFocusImageInsets(Insets focusImageInsets) {
		this.focusImageInsets = focusImageInsets;
	}

	public Color getDisabledTextColor() {
		return disabledTextColor;
	}

	public void setDisabledTextColor(Color disabledTextColor) {
		this.disabledTextColor = disabledTextColor;
	}

	public boolean isImageOnly() {
		return imageOnly;
	}

	public void setImageOnly(boolean imageOnly) {
		this.imageOnly = imageOnly;
	}

	public boolean isPaintPressDown() {
		return paintPressDown;
	}

	public void setPaintPressDown(boolean paintPressDown) {
		this.paintPressDown = paintPressDown;
	}

	public Color getForeground() {
		return foreground;
	}

	public void setForeground(Color foreground) {
		this.foreground = foreground;
	}

	public Color getBackground() {
		return background;
	}

	public void setBackground(Color background) {
		this.background = background;
	}

	public Border getBorder() {
		return border;
	}

	public void setBorder(Border border) {
		this.border = border;
	}

	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		this.font = font;
	}

	public Insets getMargin() {
		return margin;
	}

	public void setMargin(Insets margin) {
		this.margin = margin;
	}

	public int getIconTextGap() {
		return iconTextGap;
	}

	public void setIconTextGap(int iconTextGap) {
		this.iconTextGap = iconTextGap;
	}

	public boolean isRolloverEnabled() {
		return rolloverEnabled;
	}

	public void setRolloverEnabled(boolean rolloverEnabled) {
		this.rolloverEnabled = rolloverEnabled;
	}

	public boolean isContentAreaFilled() {
		return contentAreaFilled;
	}

	public void setContentAreaFilled(boolean contentAreaFilled) {
		this.contentAreaFilled = contentAreaFilled;
	}

}
