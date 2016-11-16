package com.only.laf;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.plaf.basic.BasicLabelUI;
import javax.swing.text.View;

import sun.awt.AppContext;
import sun.swing.SwingUtilities2;

import com.only.OnlyLabel;

/**
 * 
 * @description:
 * @author XiaHui
 * @date 2014年6月23日 下午6:07:04
 * @version 1.0.0
 */
public class OnlyLabelUI extends BasicLabelUI {

	private static final Object ONLY_LABEL_UI_KEY = new Object();
	private Rectangle iconR = new Rectangle();
	private Rectangle textR = new Rectangle();
	private Rectangle contentBounds = new Rectangle();

	public static ComponentUI createUI(JComponent c) {
		AppContext appContext = AppContext.getAppContext();
		OnlyLabelUI onlyLabelUI = (OnlyLabelUI) appContext.get(ONLY_LABEL_UI_KEY);
		if (onlyLabelUI == null) {
			onlyLabelUI = new OnlyLabelUI();
			appContext.put(ONLY_LABEL_UI_KEY, onlyLabelUI);
		}
		return onlyLabelUI;
	}

	private Dimension computeMove(JLabel label, int contentWidth, int contentHeight, double theta) {
		int ha = label.getHorizontalAlignment();
		int va = label.getVerticalAlignment();
		boolean leftToRight = label.getComponentOrientation().isLeftToRight();
		double moveX = 0;
		double moveY = 0;

		if (ha == JLabel.LEFT || (leftToRight && ha == JLabel.LEADING) || (!leftToRight && ha == JLabel.TRAILING)) {
			moveX = (contentWidth - (Math.abs(Math.cos(theta) * contentWidth) + Math.abs(Math.sin(theta) * contentHeight))) / 2.0;
		} else if (ha == JLabel.RIGHT || (!leftToRight && ha == JLabel.LEADING) || (leftToRight && ha == JLabel.TRAILING)) {
			moveX = -(contentWidth - (Math.abs(Math.cos(theta) * contentWidth) + Math.abs(Math.sin(theta) * contentHeight))) / 2.0;
		}

		if (va == JLabel.TOP) {
			moveY = (contentHeight - (Math.abs(Math.sin(theta) * contentWidth) + Math.abs(Math.cos(theta) * contentHeight))) / 2.0;
		} else if (va == JLabel.BOTTOM) {
			moveY = -(contentHeight - (Math.abs(Math.sin(theta) * contentWidth) + Math.abs(Math.cos(theta) * contentHeight))) / 2.0;
		}

		return new Dimension((int) Math.round(moveX), (int) Math.round(moveY));
	}

	private String computeContentBounds(JComponent c, Graphics g, FontMetrics metrics) {
		JLabel label = (JLabel) c;
		String text = label.getText();
		Icon icon = (label.isEnabled()) ? label.getIcon() : label.getDisabledIcon();
		Insets insets = label.getInsets(null);
		String clippedText = null;

		if (icon == null && text == null) {
			contentBounds.setBounds(0, 0, 0, 0);
		} else {
			Rectangle viewR = new Rectangle();
			iconR.x = iconR.y = iconR.width = iconR.height = 0;
			textR.x = textR.y = textR.width = textR.height = 0;
			viewR.x = insets.left;
			viewR.y = insets.top;
			viewR.width = c.getWidth() - (insets.left + insets.right);
			viewR.height = c.getHeight() - (insets.top + insets.bottom);
			clippedText = layoutCL(label, metrics, text, icon, viewR, iconR, textR);
			int x1 = Math.min(iconR.x, textR.x);
			int x2 = Math.max(iconR.x + iconR.width, textR.x + textR.width);
			int y1 = Math.min(iconR.y, textR.y);
			int y2 = Math.max(iconR.y + iconR.height, textR.y + textR.height);
			contentBounds.setBounds(x1, y1, x2 - x1, y2 - y1);
		}

		return clippedText;
	}

	@Override
	protected void paintEnabledText(JLabel label, Graphics g, String text, int textX, int textY) {
		int mnemIndex = label.getDisplayedMnemonicIndex();
		g.setColor(label.getForeground());
		SwingUtilities2.drawStringUnderlineCharAt(label, g, text, mnemIndex, textX, textY);
	}

	protected void defaultPaintDisabledText(JLabel label, Graphics g, String text, int textX, int textY) {
		int mnemonicIndex = label.getDisplayedMnemonicIndex();

		if (UIManager.getColor("Label.disabledForeground") instanceof Color && UIManager.getColor("Label.disabledShadow") instanceof Color) {
			g.setColor(UIManager.getColor("Label.disabledShadow"));
			SwingUtilities2.drawStringUnderlineCharAt(label, g, text, mnemonicIndex, textX + 1, textY + 1);
			g.setColor(UIManager.getColor("Label.disabledForeground"));
			SwingUtilities2.drawStringUnderlineCharAt(label, g, text, mnemonicIndex, textX, textY);
		} else {
			Color background = label.getBackground();
			g.setColor(background.brighter());
			SwingUtilities2.drawStringUnderlineCharAt(label, g, text, mnemonicIndex, textX + 1, textY + 1);
			g.setColor(background.darker());
			SwingUtilities2.drawStringUnderlineCharAt(label, g, text, mnemonicIndex, textX, textY);
		}
	}

	@Override
	protected void installDefaults(JLabel c) {
	}

	@Override
	public void paint(Graphics g, JComponent c) {
		if (c instanceof OnlyLabel) {
			onlyPaint(g, c);
		} else {
			super.paint(g, c);
		}
	}

	@Override
	public void update(Graphics g, JComponent c) {
		if (c instanceof OnlyLabel) {
			onlyUpdate(g, c);
		} else {
			super.update(g, c);
		}
	}

	@Override
	protected void paintDisabledText(JLabel label, Graphics g, String text, int textX, int textY) {
		if (label instanceof OnlyLabel) {
			onlyPaintDisabledText(label, g, text, textX, textY);
		} else {
			defaultPaintDisabledText(label, g, text, textX, textY);
		}
	}

	public void onlyUpdate(Graphics g, JComponent c) {
		OnlyLabel onlyLabel = (OnlyLabel) c;
		if (onlyLabel.getBackgroundAlpha() > 0.0f) {
			Graphics2D g2d = (Graphics2D) g;
			Composite oldComposite = g2d.getComposite();
			g2d.setComposite(AlphaComposite.SrcOver.derive(onlyLabel.getBackgroundAlpha()));
			g2d.setColor(c.getBackground());
			g2d.fillRect(0, 0, c.getWidth(), c.getHeight());
			g2d.setComposite(oldComposite);
		}
		paint(g, c);
	}

	protected void onlyPaintDisabledText(JLabel label, Graphics g, String text, int textX, int textY) {
		OnlyLabel onlyLabel = (OnlyLabel) label;
		int mnemIndex = onlyLabel.getDisplayedMnemonicIndex();
		g.setColor(onlyLabel.getDisabledForeground());
		SwingUtilities2.drawStringUnderlineCharAt(onlyLabel, g, text, mnemIndex, textX, textY);
	}

	private void onlyPaint(Graphics g, JComponent c) {
		OnlyLabel onlyLabel = (OnlyLabel) c;
		String text = onlyLabel.getText();
		Icon icon = (onlyLabel.isEnabled()) ? onlyLabel.getIcon() : onlyLabel.getDisabledIcon();

		if (icon == null && text == null) {
			return;
		}

		Graphics2D g2d = (Graphics2D) g;
		FontMetrics metrics = SwingUtilities2.getFontMetrics(onlyLabel, g2d);
		Composite oldComposite = g2d.getComposite();
		String clippedText = computeContentBounds(c, g2d, metrics);
		int contentWidth = contentBounds.width;
		int contentHeight = contentBounds.height;
		double theta = Math.toRadians(onlyLabel.getAngle() % 360);
		double rotateX = contentBounds.x + contentWidth / 2.0;
		double rotateY = contentBounds.y + contentHeight / 2.0;
		Dimension move = computeMove(onlyLabel, contentWidth, contentHeight, theta);
		AffineTransform trans = g2d.getTransform();
		trans.translate(-move.width, -move.height);
		trans.rotate(theta, rotateX, rotateY);
		g2d.setTransform(trans);

		if (icon != null && onlyLabel.getIconAlpha() > 0.0f) {
			g2d.setComposite(AlphaComposite.SrcOver.derive(onlyLabel.getIconAlpha()));
			icon.paintIcon(c, g2d, iconR.x, iconR.y);
		}

		if (text != null) {
			View view = (View) c.getClientProperty(BasicHTML.propertyKey);
			g2d.translate(onlyLabel.getDeltaX(), onlyLabel.getDeltaY());

			if (view != null) {
				view.paint(g2d, textR);
			} else if (onlyLabel.getTextAlpha() > 0.0f) {
				int textX = textR.x;
				int textY = textR.y + metrics.getAscent();
				g2d.setComposite(AlphaComposite.SrcOver.derive(onlyLabel.getTextAlpha()));
				if (onlyLabel.isEnabled()) {
					paintEnabledText(onlyLabel, g2d, clippedText, textX, textY);
				} else {
					paintDisabledText(onlyLabel, g2d, clippedText, textX, textY);
				}
			}
			g2d.translate(-onlyLabel.getDeltaX(), -onlyLabel.getDeltaY());
		}

		trans.rotate(-theta, rotateX, rotateY);
		trans.translate(move.width, move.height);
		g2d.setTransform(trans);
		g2d.setComposite(oldComposite);
	}
}