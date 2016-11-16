package com.only.laf;

import java.awt.Color;
import java.awt.Graphics;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.swing.JComponent;
import javax.swing.event.MouseInputAdapter;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicTextAreaUI;
import javax.swing.plaf.basic.BasicTextUI;
import javax.swing.text.JTextComponent;

import com.only.OnlyTextArea;
import com.only.box.UIBox;
import com.only.util.OnlyUIUtil;

public class OnlyTextAreaUI extends BasicTextAreaUI {

	private static final Color NON_EDITABLE_BG = UIBox.getColor(UIBox.key_color_text_not_editable_background);
	private static final Color DISABLED_BG = UIBox.getColor(UIBox.key_color_text_disabled_background);

    public static ComponentUI createUI(JComponent c) {
        return new OnlyTextAreaUI();
    }

    @Override
    protected void paintSafely(Graphics g) {
        paintBackground(g);
        super.paintSafely(g);
    }

    @Override
    protected void paintBackground(Graphics g) {
        JTextComponent editor = getComponent();

        if (editor instanceof OnlyTextArea) {
            OnlyTextArea textArea = (OnlyTextArea) editor;
            Color bg = getBackground(textArea);
            OnlyUIUtil.paintBackground(g, textArea, bg, bg, textArea.getImage(), textArea.isImageOnly(),
                    textArea.getAlpha(), textArea.getVisibleInsets());
        } else {
            super.paintBackground(g);
        }
    }

    private Color getBackground(OnlyTextArea textArea) {
        Color color = textArea.getBackground();

        if (!textArea.isEnabled()) {
            color = DISABLED_BG;
        } else if (!textArea.isEditable()) {
            color = NON_EDITABLE_BG;
        }

        return color;
    }

    @Override
    protected void installDefaults() {
        try {
            Method updateCursorMethod = BasicTextUI.class.getDeclaredMethod("updateCursor");
            updateCursorMethod.setAccessible(true);
            updateCursorMethod.invoke(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
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