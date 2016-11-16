package com.only;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.border.Border;

import com.only.box.UIBox;

public class OnlyScrollText extends OnlyScrollPane {
	
	private static final long serialVersionUID = -8804672738482197370L;
	private static final Color NON_EDITABLE_BG = UIBox.getColor(UIBox.key_color_scroll_text_not_editable_background);
	private static final Color DISABLED_BG = UIBox.getColor(UIBox.key_color_scroll_text_disabled_background);
	private OnlyTextArea editor;
	private Border normalBorder;
	private Border rolloverBorder;
	private Border notEditableBorder;
	private Border notEditableRolloverBorder;
	private Border disabledBorder;
	private MouseListener listener;
	private Color background;

	public OnlyScrollText(OnlyTextArea editor) {
		super();
		setViewportView(this.editor = editor);
		setBackground(UIBox.getWhiteColor());
		setHeaderVisible(false);
		editor.clearBorderListener();
		editor.setBorder(UIBox.getBorder(UIBox.key_border_scroll_text));
		editor.setVisibleInsets(0, 0, 0, 0);
		editor.setAlpha(0.0f);
	    
        normalBorder = UIBox.getBorder(UIBox.key_border_scroll_text_field_normal);
		rolloverBorder = UIBox.getBorder(UIBox.key_border_scroll_text_field_rollover);
		disabledBorder = UIBox.getBorder(UIBox.key_border_scroll_text_field_disabled);
		notEditableBorder = UIBox.getBorder(UIBox.key_border_scroll_text_field_not_editable);
		notEditableRolloverBorder = UIBox.getBorder(UIBox.key_border_scroll_text_field_not_editable_rollover);

				installListener();
		setBorder(normalBorder);
	}

	public OnlyScrollText() {
		this(new OnlyTextArea());
	}

	private void installListener() {
		listener = new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				mouseIn();
			}

			public void mouseExited(MouseEvent e) {
				mouseOut();
			}
		};

		this.addMouseListener(listener);
		editor.addMouseListener(listener);
		viewport.addMouseListener(listener);
		horizontalScrollBar.addMouseListener(listener);
		verticalScrollBar.addMouseListener(listener);

		for (Component c : horizontalScrollBar.getComponents()) {
			c.addMouseListener(listener);
		}

		for (Component c : verticalScrollBar.getComponents()) {
			c.addMouseListener(listener);
		}
	}

	public OnlyTextArea getEditor() {
		return editor;
	}

	public void setEditable(boolean editable) {
		editor.setEditable(editable);
		mouseOut();

		if (isEnabled()) {
			super.setBackground(editable ? background : NON_EDITABLE_BG);
		}
	}

	public boolean isEditable() {
		return editor.isEditable();
	}

	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		super.setBackground(enabled ? (isEditable() ? background : NON_EDITABLE_BG) : DISABLED_BG);

		if (enabled) {
			mouseOut();
		} else if (normalBorder != null) {
			setBorder(disabledBorder);
		}
	}

	public void setBackground(Color bg) {
		background = bg;
		super.setBackground(bg);
	}

	private void mouseIn() {
		if (normalBorder != null && editor.isEnabled()) {
			setBorder(editor.isEditable() ? rolloverBorder : notEditableRolloverBorder);
		}
	}

	private void mouseOut() {
		if (normalBorder != null && editor.isEnabled()) {
			setBorder(editor.isEditable() ? normalBorder : notEditableBorder);
		}
	}
}