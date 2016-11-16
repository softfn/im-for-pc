package com.only;

import java.awt.Color;
import java.awt.Image;

import javax.swing.BoundedRangeModel;
import javax.swing.JSlider;
import javax.swing.border.EmptyBorder;

import com.only.laf.OnlySliderUI;
import com.only.util.OnlyUIUtil;

public class OnlySlider extends JSlider {
	private static final long serialVersionUID = -1988588632695957051L;

	private OnlySliderUI ui;
	private Color majorTickColor;
	private Color minorTickColor;
	private boolean miniMode;

	public OnlySlider() {
		this(HORIZONTAL, 0, 100, 50);
	}

	public OnlySlider(int orientation) {
		this(orientation, 0, 100, 50);
	}

	public OnlySlider(int min, int max) {
		this(HORIZONTAL, min, max, (min + max) / 2);
	}

	public OnlySlider(int min, int max, int value) {
		this(HORIZONTAL, min, max, value);
	}

	public OnlySlider(int orientation, int min, int max, int value) {
		super(orientation, min, max, value);
		init();
	}

	public OnlySlider(BoundedRangeModel brm) {
		super(brm);
		init();
	}

	private void init() {
		majorTickColor = new Color(76, 181, 237);
		minorTickColor = new Color(78, 160, 209);
		setUI(ui = new OnlySliderUI());
		setFont(OnlyUIUtil.getDefaultFont());
		setBackground(Color.GRAY);
		setForeground(new Color(0, 28, 48));
		setOpaque(false);
		setBorder(new EmptyBorder(0, 0, 0, 0));
	}

	public Color getMajorTickColor() {
		return majorTickColor;
	}

	public void setMajorTickColor(Color majorTickColor) {
		this.majorTickColor = majorTickColor;
		this.repaint();
	}

	public Color getMinorTickColor() {
		return minorTickColor;
	}

	public void setMinorTickColor(Color minorTickColor) {
		this.minorTickColor = minorTickColor;
		this.repaint();
	}

	public boolean isMiniMode() {
		return miniMode;
	}

	public void setMiniMode(boolean miniMode) {
		this.miniMode = miniMode;
		ui.calculateGeometry();
		this.repaint();
	}

	/**
	 * JDK7中JSlider重写了imageUpdate方法，但是没有对labelTable做非空判断，此处回避该问题
	 * JDK6中可以不用在此重写imageUpdate
	 */
	public boolean imageUpdate(Image img, int infoflags, int x, int y, int w, int h) {
		if (getLabelTable() == null) {
			return isShowing();
		} else {
			return super.imageUpdate(img, infoflags, x, y, w, h);
		}
	}

	@Deprecated
	public void updateUI() {
	}
}