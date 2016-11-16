/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.only;

import com.only.common.ImageDisplayMode;
import com.only.laf.OnlyPanelUI;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.LayoutManager;
import javax.swing.JPanel;

/**
 * 
 * @description:
 * @author XiaHui
 * @date 2014年6月26日 上午10:20:07
 * @version 1.0.0
 */
@SuppressWarnings("serial")
public class OnlyPanel extends JPanel {

	private Image backgroundImage;
	private Insets backgroundInsets = new Insets(1, 1, 1, 1);
	private Insets borderInsets = new Insets(1, 1, 1, 1);
	private boolean fullBackgroundImage = true;
	private float imageAlpha;
	private ImageDisplayMode imageDisplayMode;
	private int roundBorder = 0;
	private boolean borderPainted;
	private Color borderColor;
	private int borderSize;
	private int borderX = 0;
	private int borderY = 0;
	private Image borderImage;

	public OnlyPanel() {
		this(true);
	}

	public OnlyPanel(LayoutManager layout) {
		this(layout, true);
	}

	public OnlyPanel(boolean isDoubleBuffered) {
		this(new FlowLayout(), isDoubleBuffered);
	}

	public OnlyPanel(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
		initOnlyPanel();
	}

	private void initOnlyPanel() {
		this.imageAlpha = 1.0F;
		this.imageDisplayMode = ImageDisplayMode.scaled;
		this.borderPainted = false;
		this.borderColor = new Color(102, 102, 102);
		this.setUI(new OnlyPanelUI());
	}

	public Image getBackgroundImage() {
		return backgroundImage;
	}

	public void setBackgroundImage(Image backgroundImage) {
		if (null != backgroundImage && !backgroundImage.equals(this.backgroundImage)) {
			this.backgroundImage = backgroundImage;
			this.repaint();
		}
	}

	public Insets getBackgroundInsets() {
		return backgroundInsets;
	}

	public void setBackgroundInsets(Insets backgroundInsets) {
		this.backgroundInsets = backgroundInsets;
	}

	public boolean isFullBackgroundImage() {
		return fullBackgroundImage;
	}

	public void setFullBackgroundImage(boolean fullBackgroundImage) {
		this.fullBackgroundImage = fullBackgroundImage;
	}

	public float getImageAlpha() {
		return this.imageAlpha;
	}

	/**
	 * 设置透明度
	 * 
	 * @param imageAlpha
	 */
	public void setImageAlpha(float imageAlpha) {
		if ((imageAlpha >= 0.0F) && (imageAlpha <= 1.0F)) {
			this.imageAlpha = imageAlpha;
			repaint();
		} else {
			this.imageAlpha = 1.0F;
		}
	}

	/**
	 * 设置图形显示模式
	 * 
	 * @param imageDisplayMode
	 */
	public void setImageDisplayMode(ImageDisplayMode imageDisplayMode) {
		if (this.imageDisplayMode != imageDisplayMode) {
			this.imageDisplayMode = imageDisplayMode;
			repaint();
		}
	}

	public ImageDisplayMode getImageDisplayMode() {
		return imageDisplayMode;
	}

	public int getRoundBorder() {
		return roundBorder;
	}

	public void setRoundBorder(int roundBorder) {
		this.roundBorder = roundBorder;
		repaint();
	}

	/**
	 * 是否绘制边框
	 * 
	 * @param borderPainted
	 */
	public void setBorderPainted(boolean borderPainted) {
		if (this.borderPainted != borderPainted) {
			this.borderPainted = borderPainted;
			repaint();
		}
	}

	public boolean isBorderPainted() {
		return this.borderPainted;
	}

	public Color getBorderColor() {
		return borderColor;
	}

	public void setBorderColor(Color borderColor) {
		if (!this.borderColor.equals(borderColor)) {
			this.borderColor = borderColor;
			repaint();
		}
	}

	public Image getBorderImage() {
		return borderImage;
	}

	public void setBorderImage(Image borderImage) {
		if ((null == this.borderImage) || !this.borderImage.equals(borderImage)) {
			this.borderImage = borderImage;
			repaint();
		}
	}

	public int getBorderSize() {
		return borderSize;
	}

	public void setBorderSize(int borderSize) {
		if (this.borderSize != borderSize) {
			this.borderSize = borderSize;
			repaint();
		}
	}

	public int getBorderX() {
		return borderX;
	}

	public void setBorderX(int borderX) {
		if (this.borderX != borderX) {
			this.borderX = borderX;
			repaint();
		}
	}

	public int getBorderY() {
		return borderY;
	}

	public void setBorderY(int borderY) {
		if (this.borderY != borderY) {
			this.borderY = borderY;
			repaint();
		}
	}

	public Insets getBorderInsets() {
		return borderInsets;
	}

	public void setBorderInsets(Insets borderInsets) {
		if (this.borderInsets != borderInsets) {
			this.borderInsets = borderInsets;
			repaint();
		}
	}
}
