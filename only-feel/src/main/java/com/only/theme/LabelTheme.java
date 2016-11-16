package com.only.theme;

import java.awt.Color;
import java.awt.Font;

import javax.swing.border.Border;

/**
 * @Author: XiaHui
 * @Date: 2015年12月29日
 * @ModifyUser: XiaHui
 * @ModifyDate: 2015年12月29日
 */
public class LabelTheme {

	private Font font = new Font("微软雅黑", 0, 12);
	private Color foreground;
	private double angle;
	private float iconAlpha;
	private float textAlpha;
	private float backgroundAlpha;
	private int deltaX;
	private int deltaY;
	private Color disabledForeground;

	private boolean paintPressDown;
	private Color background;
	private Border border;

	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		this.font = font;
	}

	public Color getForeground() {
		return foreground;
	}

	public void setForeground(Color foreground) {
		this.foreground = foreground;
	}

	public double getAngle() {
		return angle;
	}

	public void setAngle(double angle) {
		this.angle = angle;
	}

	public float getIconAlpha() {
		return iconAlpha;
	}

	public void setIconAlpha(float iconAlpha) {
		this.iconAlpha = iconAlpha;
	}

	public float getTextAlpha() {
		return textAlpha;
	}

	public void setTextAlpha(float textAlpha) {
		this.textAlpha = textAlpha;
	}

	public float getBackgroundAlpha() {
		return backgroundAlpha;
	}

	public void setBackgroundAlpha(float backgroundAlpha) {
		this.backgroundAlpha = backgroundAlpha;
	}

	public int getDeltaX() {
		return deltaX;
	}

	public void setDeltaX(int deltaX) {
		this.deltaX = deltaX;
	}

	public int getDeltaY() {
		return deltaY;
	}

	public void setDeltaY(int deltaY) {
		this.deltaY = deltaY;
	}

	public Color getDisabledForeground() {
		return disabledForeground;
	}

	public void setDisabledForeground(Color disabledForeground) {
		this.disabledForeground = disabledForeground;
	}

	public boolean isPaintPressDown() {
		return paintPressDown;
	}

	public void setPaintPressDown(boolean paintPressDown) {
		this.paintPressDown = paintPressDown;
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

}
