package com.only;

import com.only.laf.OnlyLabelUI;
import java.awt.Color;

import javax.swing.Icon;
import javax.swing.JLabel;

import com.only.util.OnlyUIUtil;

/**
 * 
 * @description:
 * @author XiaHui
 * @date 2014年6月23日 下午3:42:09
 * @version 1.0.0
 */
public class OnlyLabel extends JLabel {

	private static final long serialVersionUID = 1L;

	private double angle;
	private float iconAlpha;
	private float textAlpha;
	private float backgroundAlpha;
	private int deltaX, deltaY;
	private Color disabledForeground;

	public OnlyLabel() {
		this("", null, LEADING);
	}

	public OnlyLabel(String text) {
		this(text, null, LEADING);
	}

	public OnlyLabel(Icon image) {
		this(null, image, CENTER);
	}

	public OnlyLabel(String text, int horizontalAlignment) {
		this(text, null, horizontalAlignment);
	}

	public OnlyLabel(Icon image, int horizontalAlignment) {
		this(null, image, horizontalAlignment);
	}

	public OnlyLabel(String text, Icon icon, int horizontalAlignment) {
		super(text, icon, horizontalAlignment);
		initialize();
	}

	private void initialize() {
		angle = 0.0;
		textAlpha = iconAlpha = 1.0f;
		backgroundAlpha = 0.0f;
		disabledForeground = new Color(103, 117, 127);
		setUI(new OnlyLabelUI());
		setBackground(Color.GRAY);
		setForeground(new Color(0, 28, 48));
		setFont(OnlyUIUtil.getDefaultFont());
		super.setOpaque(false);
	}

	public double getAngle() {
		return angle;
	}

	public void setAngle(double angle) {
		this.angle = angle;
		this.repaint();
	}

	public float getIconAlpha() {
		return iconAlpha;
	}

	public void setIconAlpha(float iconAlpha) {
		if (iconAlpha >= 0.0f && iconAlpha <= 1.0f) {
			this.iconAlpha = iconAlpha;
			this.repaint();
		} else {
			throw new IllegalArgumentException("Invalid iconAlpha:" + iconAlpha);
		}
	}

	public float getTextAlpha() {
		return textAlpha;
	}

	public void setTextAlpha(float textAlpha) {
		if (textAlpha >= 0.0f && textAlpha <= 1.0f) {
			this.textAlpha = textAlpha;
			this.repaint();
		} else {
			throw new IllegalArgumentException("Invalid textAlpha:" + textAlpha);
		}
	}

	public float getBackgroundAlpha() {
		return backgroundAlpha;
	}

	public void setBackgroundAlpha(float backgroundAlpha) {
		if (backgroundAlpha >= 0.0f && backgroundAlpha <= 1.0f) {
			this.backgroundAlpha = backgroundAlpha;
			this.repaint();
		} else {
			throw new IllegalArgumentException("Invalid backgroundAlpha:" + backgroundAlpha);
		}
	}

	public Color getDisabledForeground() {
		return disabledForeground;
	}

	public void setDisabledForeground(Color disabledForeground) {
		this.disabledForeground = disabledForeground;

		if (!this.isEnabled()) {
			this.repaint();
		}
	}

	public int getDeltaX() {
		return deltaX;
	}

	public void setDeltaX(int deltaX) {
		this.deltaX = deltaX;
		this.repaint();
	}

	public int getDeltaY() {
		return deltaY;
	}

	public void setDeltaY(int deltaY) {
		this.deltaY = deltaY;
		this.repaint();
	}

	@Deprecated
	public void updateUI() {
		setUI(new OnlyLabelUI());
	}

	@Deprecated
	public void setOpaque(boolean isOpaque) {
		if (isOpaque) {
			setTextAlpha(1.0f);
		} else {
			setTextAlpha(0.3f);
		}
	}
}