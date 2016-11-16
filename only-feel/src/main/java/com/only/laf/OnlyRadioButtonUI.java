package com.only.laf;

import com.only.OnlyRadioButton;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.plaf.basic.BasicRadioButtonUI;
import javax.swing.text.View;

import sun.awt.AppContext;
import sun.swing.SwingUtilities2;

public class OnlyRadioButtonUI extends BasicRadioButtonUI {

	private static final Object ONLY_RADIO_BUTTON_UI_KEY = new Object();
	private Dimension size = new Dimension();
	private Rectangle viewRect = new Rectangle();
	private Rectangle iconRect = new Rectangle();
	private Rectangle textRect = new Rectangle();

	public static ComponentUI createUI(JComponent c) {
		AppContext appContext = AppContext.getAppContext();
		OnlyRadioButtonUI onlyRadioButtonUI = (OnlyRadioButtonUI) appContext.get(ONLY_RADIO_BUTTON_UI_KEY);

		if (onlyRadioButtonUI == null) {
			onlyRadioButtonUI = new OnlyRadioButtonUI();
			appContext.put(ONLY_RADIO_BUTTON_UI_KEY, onlyRadioButtonUI);
		}

		return onlyRadioButtonUI;
	}

	public synchronized void paint(Graphics g, JComponent c) {
		AbstractButton button = (AbstractButton) c;
		ButtonModel model = button.getModel();
		Font font = c.getFont();
		g.setFont(font);
		FontMetrics fm = SwingUtilities2.getFontMetrics(c, g, font);
		Insets insets = c.getInsets();
		size = button.getSize(size);
		viewRect.x = insets.left;
		viewRect.y = insets.top;
		viewRect.width = size.width - (insets.right + viewRect.x);
		viewRect.height = size.height - (insets.bottom + viewRect.y);
		iconRect.x = iconRect.y = iconRect.width = iconRect.height = 0;
		textRect.x = textRect.y = textRect.width = textRect.height = 0;
		Icon altIcon = button.getIcon();
		int iconTextGap = button.getText() == null ? 0 : button.getIconTextGap();
		String text = SwingUtilities.layoutCompoundLabel(c, fm, button.getText(), altIcon != null ? altIcon : getDefaultIcon(), button.getVerticalAlignment(), button.getHorizontalAlignment(), button.getVerticalTextPosition(), button.getHorizontalTextPosition(), viewRect,
				iconRect, textRect, iconTextGap);

		if (altIcon != null) {
			if (!model.isEnabled()) {
				if (model.isSelected()) {
					altIcon = button.getDisabledSelectedIcon();
				} else {
					altIcon = button.getDisabledIcon();
				}
			} else if (model.isPressed() && model.isArmed()) {
				altIcon = button.getPressedIcon();

				if (button instanceof OnlyRadioButton && model.isSelected()) {
					altIcon = ((OnlyRadioButton) button).getPressedSelectedIcon();
				}

				if (altIcon == null) {
					altIcon = button.getSelectedIcon();
				}
			} else if (model.isSelected()) {
				if (button.isRolloverEnabled() && model.isRollover()) {
					altIcon = (Icon) button.getRolloverSelectedIcon();

					if (altIcon == null) {
						altIcon = (Icon) button.getSelectedIcon();
					}
				} else {
					altIcon = (Icon) button.getSelectedIcon();
				}
			} else if (button.isRolloverEnabled() && model.isRollover()) {
				altIcon = (Icon) button.getRolloverIcon();
			}

			if (altIcon == null) {
				altIcon = button.getIcon();
			}

			altIcon.paintIcon(c, g, iconRect.x, iconRect.y);
		} else {
			getDefaultIcon().paintIcon(c, g, iconRect.x, iconRect.y);
		}

		if (text != null) {
			View view = (View) c.getClientProperty(BasicHTML.propertyKey);

			if (view != null) {
				view.paint(g, textRect);
			} else {
				paintText(g, button, textRect, text);
			}

			if (button.hasFocus() && button.isFocusPainted() && textRect.width > 0 && textRect.height > 0) {
				paintFocus(g, textRect, size);
			}
		}
	}

	protected void paintText(Graphics g, JComponent c, Rectangle textRect, String text) {
		if (c instanceof OnlyRadioButton) {
			OnlyRadioButton radioButton = (OnlyRadioButton) c;
			ButtonModel model = radioButton.getModel();
			FontMetrics fm = SwingUtilities2.getFontMetrics(c, g);
			int mnemIndex = radioButton.getDisplayedMnemonicIndex();

			if (model.isEnabled()) {
				g.setColor(radioButton.getForeground());
			} else {
				g.setColor(radioButton.getDisabledTextColor());
			}

			SwingUtilities2.drawStringUnderlineCharAt(c, g, text, mnemIndex, textRect.x, textRect.y + fm.getAscent());
		} else {
			super.paintText(g, c, textRect, text);
		}
	}

	public void installDefaults(AbstractButton b) {
	}

	protected void uninstallDefaults(AbstractButton b) {
	}
}