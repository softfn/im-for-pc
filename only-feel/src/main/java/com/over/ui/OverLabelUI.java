package com.over.ui;

import java.awt.Color;
import java.awt.Graphics;
//import java.awt.Rectangle;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicLabelUI;

import sun.awt.AppContext;
import sun.swing.SwingUtilities2;

/**
 * 
 * @description:
 * @author XiaHui
 * @date 2014年6月23日 下午6:07:04
 * @version 1.0.0
 */
public class OverLabelUI extends BasicLabelUI {

	private static final Object ONLY_LABEL_UI_KEY = new Object();
//	private Rectangle iconR = new Rectangle();
//	private Rectangle textR = new Rectangle();
//	private Rectangle contentBounds = new Rectangle();
	
	public static ComponentUI createUI(JComponent c) {
		AppContext appContext = AppContext.getAppContext();
		OverLabelUI labelUI = (OverLabelUI) appContext.get(ONLY_LABEL_UI_KEY);
		if (labelUI == null) {
			labelUI = new OverLabelUI();
			appContext.put(ONLY_LABEL_UI_KEY, labelUI);
		}
		return labelUI;
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
		super.installDefaults(c);
	}

	@Override
	public void paint(Graphics g, JComponent c) {
		super.paint(g, c);
	}

	@Override
	public void update(Graphics g, JComponent c) {
		super.update(g, c);

	}

	@Override
	protected void paintDisabledText(JLabel label, Graphics g, String text, int textX, int textY) {
		defaultPaintDisabledText(label, g, text, textX, textY);
	}
}