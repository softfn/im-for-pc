package com.only.laf;

import java.awt.Color;
import java.awt.Component;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.swing.JComponent;
import javax.swing.event.MouseInputAdapter;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicPasswordFieldUI;
import javax.swing.plaf.basic.BasicTextUI;
import javax.swing.text.JTextComponent;

import sun.swing.SwingUtilities2;

import com.only.OnlyPasswordField;
import com.only.box.UIBox;
import com.only.util.OnlyUIUtil;

public class OnlyPasswordFieldUI extends BasicPasswordFieldUI {

	
	private static final Color NON_EDITABLE_BG = UIBox.getColor(UIBox.key_color_password_not_editable_background);
	private static final Color DISABLED_BG = UIBox.getColor(UIBox.key_color_password_disabled_background);
	private Color disabledForeground = new Color(103, 117, 127);

	private Rectangle textR = new Rectangle();
	public static ComponentUI createUI(JComponent c) {
		return new OnlyPasswordFieldUI();
	}

	protected void paintSafely(Graphics g) {
		paintBackground(g);
		super.paintSafely(g);
	}
	public void installUI(JComponent c) {
		super.installUI(c);
		if (c instanceof JTextComponent) {
			c.addFocusListener(new FocusListener() {

				@Override
				public void focusLost(FocusEvent e) {
					Component component = e.getComponent();
					if (null != component) {
						component.repaint();
					}

				}

				@Override
				public void focusGained(FocusEvent e) {
					Component component = e.getComponent();
					if (null != component) {
						component.repaint();
					}
				}
			});
		}
	}
	protected void paintBackground(Graphics g) {
		JTextComponent editor = getComponent();

		if (editor instanceof OnlyPasswordField) {
			OnlyPasswordField field = (OnlyPasswordField) editor;
			Color bg = getBackground(field);
			OnlyUIUtil.paintBackground(g, field, bg, bg, field.getImage(), field.isImageOnly(), field.getAlpha(), field.getVisibleInsets());
		
			String labelText = field.getLabelText();
			String text = new String(field.getPassword());
			boolean focus = field.hasFocus();
			boolean editable = field.isEditable();

			if ((!focus && null != labelText && (null == text || "".equals(text))) || (!editable && focus)) {

				FontMetrics fm = SwingUtilities2.getFontMetrics(field, g);
				//int textX = textR.x;
				int textY = textR.y + fm.getAscent() + ((field.getHeight() - 16) / 2);
				g.setColor(disabledForeground);
				SwingUtilities2.drawStringUnderlineCharAt(field, g, labelText, -1, 5, textY);
			}
		} else {
			super.paintBackground(g);
		}
	}

	private Color getBackground(OnlyPasswordField field) {
		Color color = field.getBackground();

		if (!field.isEnabled()) {
			color = DISABLED_BG;
		} else if (!field.isEditable()) {
			color = NON_EDITABLE_BG;
		}

		return color;
	}

	protected void installDefaults() {
		try {
			Method updateCursorMethod = BasicTextUI.class.getDeclaredMethod("updateCursor");
			updateCursorMethod.setAccessible(true);
			updateCursorMethod.invoke(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void uninstallDefaults() {
		JTextComponent editor = getComponent();

		try {
			Field dragListenerField = BasicTextUI.class.getDeclaredField("dragListener");
			dragListenerField.setAccessible(true);
			MouseInputAdapter dragListener = (MouseInputAdapter) dragListenerField.get(this);
			editor.removeMouseListener(dragListener);
			editor.removeMouseMotionListener(dragListener);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (editor.getCaret() instanceof UIResource) {
			editor.setCaret(null);
		}

		if (editor.getHighlighter() instanceof UIResource) {
			editor.setHighlighter(null);
		}

		if (editor.getTransferHandler() instanceof UIResource) {
			editor.setTransferHandler(null);
		}

		if (editor.getCursor() instanceof UIResource) {
			editor.setCursor(null);
		}
	}
}