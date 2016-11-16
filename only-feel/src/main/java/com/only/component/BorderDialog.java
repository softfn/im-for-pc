/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.only.component;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.Window;

import javax.swing.FocusManager;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JRootPane;
import javax.swing.LayoutFocusTraversalPolicy;

import com.only.OnlyPanel;
import com.only.common.ImageDisplayMode;
import com.only.util.OnlyFeelUtil;
import com.only.util.OnlyUIUtil;

/**
 * 
 * @author XiaHui
 */
@SuppressWarnings("serial")
public class BorderDialog extends JDialog {

	OnlyPanel onlyPanel = new OnlyPanel();
	boolean opaque = true;

	public BorderDialog() {
		this(new JFrame());
	}

	public BorderDialog(Frame owner) {
		this(owner, false);
	}

	public BorderDialog(Frame owner, boolean modal) {
		this(owner, null, modal);
	}

	public BorderDialog(Frame owner, String title) {
		this(owner, title, false);
	}

	public BorderDialog(Frame owner, String title, boolean modal) {
		super(owner, title, modal);
		initOnlyDialog();
	}

	public BorderDialog(Frame owner, String title, boolean modal, GraphicsConfiguration gc) {
		super(owner, title, modal, gc);
		initOnlyDialog();
	}

	public BorderDialog(Dialog owner) {
		this(owner, false);
	}

	public BorderDialog(Dialog owner, boolean modal) {
		this(owner, null, modal);
	}

	public BorderDialog(Dialog owner, String title) {
		this(owner, title, false);
	}

	public BorderDialog(Dialog owner, String title, boolean modal) {
		super(owner, title, modal);
		initOnlyDialog();
	}

	public BorderDialog(Dialog owner, String title, boolean modal, GraphicsConfiguration gc) {
		super(owner, title, modal, gc);
		initOnlyDialog();
	}

	public BorderDialog(Window owner) {
		this(owner, Dialog.ModalityType.MODELESS);
	}

	public BorderDialog(Window owner, Dialog.ModalityType modalityType) {
		this(owner, null, modalityType);
	}

	public BorderDialog(Window owner, String title) {
		this(owner, title, Dialog.ModalityType.MODELESS);
	}

	public BorderDialog(Window owner, String title, Dialog.ModalityType modalityType) {
		super(owner, title, modalityType);
		initOnlyDialog();
	}

	public BorderDialog(Window owner, String title, Dialog.ModalityType modalityType, GraphicsConfiguration gc) {
		super(owner, title, modalityType, gc);
		initOnlyDialog();
	}

	private void initOnlyDialog() {
		this.setUndecorated(true);
		this.getRootPane().setContentPane(onlyPanel);
		this.setOpaque(false);
		onlyPanel.setBorderInsets(new Insets(10, 10, 10, 10));
		onlyPanel.setBorderX(7);
		onlyPanel.setBorderY(7);
		onlyPanel.setBorderPainted(true);
	}

	@Override
	protected JRootPane createRootPane() {
		JRootPane rp = new JRootPane() {
			private static final long serialVersionUID = 6817397706458749155L;

			@Deprecated
			public void updateUI() {
				// updateUI空实现在JDK6中正常，但是JDK7中若JDialog无JFrame父窗体，则其中的组件焦点无法用Tab键切换
				// 跟踪到UIManager.getUI(target)时发现在JDK7中多了一行maybeInitializeFocusPolicy(target)，其中对JRootPane做了特殊处理
				// 虽然JFrame尚未发现该问题，但是为了避免其他问题产生，也将maybeInitializeFocusPolicy中的有效代码借来一用
				if (FocusManager.isFocusManagerEnabled()) {
					KeyboardFocusManager.getCurrentKeyboardFocusManager().setDefaultFocusTraversalPolicy(new LayoutFocusTraversalPolicy());
				}
			}
		};

		rp.setOpaque(true);
		return rp;
	}

	public void setOpaque(boolean opaque) {
		this.opaque = opaque;
		com.sun.awt.AWTUtilities.setWindowOpaque(this, opaque);
	}

	public Image getBackgroundImage() {
		return onlyPanel.getBackgroundImage();
	}

	public void setBackgroundImage(Image backgroundImage) {
		onlyPanel.setBackgroundImage(backgroundImage);
	}

	public Insets getBackgroundInsets() {
		return onlyPanel.getBackgroundInsets();
	}

	public void setBackgroundInsets(Insets backgroundInsets) {
		onlyPanel.setBackgroundInsets(backgroundInsets);
	}

	public boolean isFullBackgroundImage() {
		return onlyPanel.isFullBackgroundImage();
	}

	public void setFullBackgroundImage(boolean fullBackgroundImage) {
		onlyPanel.setFullBackgroundImage(fullBackgroundImage);
	}

	public float getImageAlpha() {
		return this.onlyPanel.getImageAlpha();
	}

	/**
	 * 设置透明度
	 * 
	 * @param imageAlpha
	 */
	public void setImageAlpha(float imageAlpha) {
		if (null != onlyPanel) {
			onlyPanel.setImageAlpha(imageAlpha);
		}
	}

	/**
	 * 设置图形显示模式
	 * 
	 * @param imageDisplayMode
	 */
	public void setImageDisplayMode(ImageDisplayMode imageDisplayMode) {
		if (null != onlyPanel) {
			onlyPanel.setImageDisplayMode(imageDisplayMode);
		}
	}

	public ImageDisplayMode getImageDisplayMode() {
		return onlyPanel.getImageDisplayMode();
	}

	public int getRoundBorder() {
		return onlyPanel.getRoundBorder();
	}

	public void setRoundBorder(int roundBorder) {
		onlyPanel.setRoundBorder(roundBorder);
	}

	/**
	 * 是否绘制边框
	 * 
	 * @param borderPainted
	 */
	public void setBorderPainted(boolean borderPainted) {
		onlyPanel.setBorderPainted(borderPainted);
	}

	public boolean isBorderPainted() {
		return this.onlyPanel.isBorderPainted();
	}

	public Color getBorderColor() {
		return onlyPanel.getBorderColor();
	}

	public void setBorderColor(Color borderColor) {
		onlyPanel.setBorderColor(borderColor);
	}

	public Image getBorderImage() {
		return onlyPanel.getBorderImage();
	}

	public void setBorderImage(Image borderImage) {
		onlyPanel.setBorderImage(borderImage);
	}

	public int getBorderSize() {
		return onlyPanel.getBorderSize();
	}

	public void setBorderSize(int borderSize) {
		onlyPanel.setBorderSize(borderSize);
	}

	public int getBorderX() {
		return onlyPanel.getBorderX();
	}

	public void setBorderX(int borderX) {
		onlyPanel.setBorderX(borderX);
	}

	public int getBorderY() {
		return onlyPanel.getBorderY();
	}

	public void setBorderY(int borderY) {
		onlyPanel.setBorderY(borderY);
	}
	
	@Override
	public void setVisible(boolean b) {
		if(OnlyFeelUtil.isWindows()){
			super.setVisible(b);
		}
	}
}
