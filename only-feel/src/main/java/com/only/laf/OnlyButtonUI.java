package com.only.laf;

import com.only.OnlyButton;
import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicButtonUI;

import sun.awt.AppContext;
import sun.swing.SwingUtilities2;
import com.only.util.OnlyUIUtil;

public class OnlyButtonUI extends BasicButtonUI {

	private static final Object ONLY_BUTTON_UI_KEY = new Object();

	private int pressMoveX, pressMoveY;

	public static ComponentUI createUI(JComponent c) {
		AppContext appContext = AppContext.getAppContext();
		OnlyButtonUI buttonUI = (OnlyButtonUI) appContext.get(ONLY_BUTTON_UI_KEY);
		if (buttonUI == null) {
			buttonUI = new OnlyButtonUI();
			appContext.put(ONLY_BUTTON_UI_KEY, buttonUI);
		}
		return buttonUI;
	}

	protected void paintFocus(Graphics g, AbstractButton button, Rectangle viewRect, Rectangle textRect, Rectangle iconRect) {
		if (!(button instanceof OnlyButton)) {
			super.paintFocus(g, button, viewRect, textRect, iconRect);
		}
	}

	protected void paintText(Graphics g, JComponent c, Rectangle textRect, String text) {
		if (c instanceof OnlyButton) {
			OnlyButton button = (OnlyButton) c;
			ButtonModel model = button.getModel();
			FontMetrics fm = SwingUtilities2.getFontMetrics(c, g);
			int mnemIndex = button.getDisplayedMnemonicIndex();

			if (!model.isEnabled()) {
				g.setColor(button.getDisabledTextColor());
			} else if (button.isRolloverEnabled() && model.isRollover()) {
				g.setColor(button.getRolloverTextColor());
			} else {
				g.setColor(button.getForeground());
			}

			SwingUtilities2.drawStringUnderlineCharAt(c, g, text, mnemIndex, textRect.x + pressMoveX, textRect.y + pressMoveY + fm.getAscent());
		} else {
			super.paintText(g, c, textRect, text);
		}
	}

	protected void paintButtonPressed(Graphics g, AbstractButton b) {
		if (b instanceof OnlyButton && ((OnlyButton) b).isPaintPressDown()) {
			pressMoveX = pressMoveY = 1;
		}

		super.paintButtonPressed(g, b);
	}

	public void paint(Graphics g, JComponent c) {
		pressMoveX = pressMoveY = 0;
		super.paint(g, c);
	}

	public void update(Graphics g, JComponent c) {
		Graphics2D g2d = (Graphics2D) g;
		Composite oldComposite = g2d.getComposite();
		boolean opaque = c.isOpaque();

		if (c instanceof OnlyButton) {
			g2d.setComposite(AlphaComposite.SrcOver.derive(((OnlyButton) c).getAlpha()));
			opaque = opaque || !((OnlyButton) c).isImageOnly();
		}

		if (opaque) {
			g.setColor(c.getBackground());
			g.fillRect(0, 0, c.getWidth(), c.getHeight());
		}

		paintBackgroundImage(g, c);
		g2d.setComposite(oldComposite);
		paint(g, c);
	}

	protected void paintIcon(Graphics g, JComponent c, Rectangle iconRect) {
		Graphics2D g2d = (Graphics2D) g;
		Composite oldComposite = g2d.getComposite();

		if (c instanceof OnlyButton) {
			g2d.setComposite(AlphaComposite.SrcOver.derive(((OnlyButton) c).getAlpha()));
		}

		super.paintIcon(g, c, new Rectangle(iconRect.x + pressMoveX, iconRect.y + pressMoveY, iconRect.width, iconRect.height));
		g2d.setComposite(oldComposite);
	}

	protected void paintBackgroundImage(Graphics g, JComponent c) {
		if (c instanceof OnlyButton) {
			OnlyButton button = (OnlyButton) c;
			ButtonModel model = button.getModel();
			Image image = button.getNormalImage();
			Image tempImage = null;
			Insets insets = button.getNormalImageInsets();

			if (image == null) {
				return;
			}

			if (!model.isEnabled()) {
				tempImage = button.getDisabledImage();
			} else if (model.isPressed() && model.isArmed()) {
				tempImage = button.getPressedImage();
			} else if (button.isRolloverEnabled() && model.isRollover()) {
				tempImage = button.getRolloverImage();
			} else if (button.isFocusPainted() && button.isFocusable() && button.hasFocus()) {
				tempImage = button.getFocusImage();
				insets = button.getFocusImageInsets();
			}

			if (tempImage != null) {
				image = tempImage;
			}

			Rectangle paintRect = new Rectangle(0, 0, button.getWidth(), button.getHeight());
			OnlyUIUtil.paintImage(g, image, insets, paintRect, button);
		}
	}

	protected void installDefaults(AbstractButton b) {
	}

	protected void uninstallDefaults(AbstractButton b) {
	}
}