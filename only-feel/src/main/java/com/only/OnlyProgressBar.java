package com.only;

import java.awt.Color;

import javax.swing.BoundedRangeModel;
import javax.swing.JProgressBar;
import javax.swing.border.EmptyBorder;

import com.only.box.UIBox;
import com.only.laf.OnlyProgressBarUI;
import com.only.util.OnlyUIUtil;

/**
 * @author XiaHui
 * @date 2015年1月28日 上午10:28:39
 */
public class OnlyProgressBar extends JProgressBar {

	private static final long serialVersionUID = 1L;
	private Color fontColor;
	private Color fontCoverClor;

	public OnlyProgressBar() {
		this(HORIZONTAL);
	}

	public OnlyProgressBar(int orient) {
		this(orient, 0, 100);
	}

	public OnlyProgressBar(int min, int max) {
		this(HORIZONTAL, min, max);
	}

	public OnlyProgressBar(int orient, int min, int max) {
		super(orient, min, max);
		init();
	}

	public OnlyProgressBar(BoundedRangeModel newModel) {
		super(newModel);
		init();
	}

	private void init() {
		this.fontColor = Color.ORANGE;
		this.fontCoverClor = UIBox.getWhiteColor();
		setUI(new OnlyProgressBarUI());
		setFont(OnlyUIUtil.getDefaultFont());
		setForeground(Color.BLUE);
		setBackground(Color.GRAY);
		setBorder(new EmptyBorder(0, 0, 0, 0));
		setBorderPainted(false);
		setOpaque(false);
	}

	@Deprecated
	public void updateUI() {
	}

	public Color getFontColor() {
		return this.fontColor;
	}

	public void setFontColor(Color fontColor) {
		this.fontColor = fontColor;
		this.repaint();
	}

	public Color getFontCoverClor() {
		return this.fontCoverClor;
	}

	public void setFontCoverClor(Color fontCoverClor) {
		this.fontCoverClor = fontCoverClor;
		this.repaint();
	}
}
