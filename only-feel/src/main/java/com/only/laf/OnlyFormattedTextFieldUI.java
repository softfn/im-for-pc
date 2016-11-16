package com.only.laf;

import java.awt.Color;
import java.awt.Graphics;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.swing.JComponent;
import javax.swing.event.MouseInputAdapter;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicFormattedTextFieldUI;
import javax.swing.plaf.basic.BasicTextUI;
import javax.swing.text.JTextComponent;

import com.only.OnlyFormattedTextField;
import com.only.box.UIBox;
import com.only.util.OnlyUIUtil;

public class OnlyFormattedTextFieldUI extends BasicFormattedTextFieldUI {
	
	private static final Color NON_EDITABLE_BG = UIBox.getColor(UIBox.key_color_formatted_text_not_editable_background);
	private static final Color DISABLED_BG =UIBox.getColor(UIBox.key_color_formatted_text_disabled_background);
	

	public static ComponentUI createUI(JComponent c) {
		return new OnlyFormattedTextFieldUI();
	}

	protected void paintSafely(Graphics g) {
		paintBackground(g);
		super.paintSafely(g);
	}

	protected void paintBackground(Graphics g) {
		JTextComponent editor = getComponent();

		if (editor instanceof OnlyFormattedTextField) {
			OnlyFormattedTextField field = (OnlyFormattedTextField) editor;
			Color bg = getBackground(field);
			OnlyUIUtil.paintBackground(g, field, bg, bg, field.getImage(), field.isImageOnly(), field.getAlpha(), field.getVisibleInsets());
		} else {
			super.paintBackground(g);
		}
	}

	private Color getBackground(OnlyFormattedTextField field) {
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