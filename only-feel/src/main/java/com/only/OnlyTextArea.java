package com.only;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.text.Document;

import com.only.box.UIBox;
import com.only.component.TextExtender;
import com.only.laf.OnlyTextAreaUI;
import com.only.util.OnlyUIUtil;

public class OnlyTextArea extends JTextArea {
	
	private static final long serialVersionUID = -7145841694314317852L;
	private Image image;
	private float alpha;
	private boolean imageOnly;
	private Border normalBorder;
	private Border rolloverBorder;
	private Border notEditableBorder;
	private Border notEditableRolloverBorder;
	private Border disabledBorder;
	private Insets visibleInsets;
	private MouseListener listener;
	private boolean borderChange;
	private boolean leadingTextVisible;
	private TextExtender extender;

	public OnlyTextArea() {
		this(null, null, 0, 0);
	}

	public OnlyTextArea(String text) {
		this(null, text, 0, 0);
	}

	public OnlyTextArea(int rows, int columns) {
		this(null, null, rows, columns);
	}

	public OnlyTextArea(String text, int rows, int columns) {
		this(null, text, rows, columns);
	}

	public OnlyTextArea(Document doc) {
		this(doc, null, 0, 0);
	}

	public OnlyTextArea(Document doc, String text, int rows, int columns) {
		super(doc, text, rows, columns);
		setUI(new OnlyTextAreaUI());
		super.setOpaque(false);
		setFont(OnlyUIUtil.getDefaultFont());
		setBackground(UIBox.getWhiteColor());
		setForeground(Color.BLACK);
		setCaretColor(Color.BLACK);
		setSelectionColor(new Color(49, 106, 197));
		setSelectedTextColor(UIBox.getWhiteColor());
		setDisabledTextColor(new Color(123, 123, 122));
		setCursor(new Cursor(Cursor.TEXT_CURSOR));
		setMargin(new Insets(0, 0, 0, 0));

		extender = new TextExtender(this);

		normalBorder = UIBox.getBorder(UIBox.key_border_text_area_normal);
		rolloverBorder = UIBox.getBorder(UIBox.key_border_text_area_rollover);
		disabledBorder = UIBox.getBorder(UIBox.key_border_text_area_disabled);
		notEditableBorder = UIBox.getBorder(UIBox.key_border_text_area_not_editable);
		notEditableRolloverBorder = UIBox.getBorder(UIBox.key_border_text_area_not_editable_rollover);

		super.setBorder(normalBorder);

		alpha = 1.0f;
		visibleInsets = new Insets(1, 1, 1, 1);
		borderChange = true;
		leadingTextVisible = true;
		listener = new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				mouseIn();
			}

			public void mouseExited(MouseEvent e) {
				mouseOut();
			}
		};

		addMouseListener(listener);
	}

	public void setText(String text) {
		super.setText(text);

		if (leadingTextVisible) {
			setSelectionStart(0);
			setSelectionEnd(0);
		}
	}

	public float getAlpha() {
		return alpha;
	}

	public void setAlpha(float alpha) {
		if (alpha >= 0.0f && alpha <= 1.0f) {
			this.alpha = alpha;
			this.repaint();
		} else {
			throw new IllegalArgumentException("Invalid alpha:" + alpha);
		}
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
		this.repaint();
	}

	@Deprecated
	public void updateUI() {
	}

	@Deprecated
	public void setOpaque(boolean isOpaque) {
	}

	public Insets getVisibleInsets() {
		return visibleInsets;
	}

	public void setVisibleInsets(int top, int left, int bottom, int right) {
		this.visibleInsets.set(top, left, bottom, right);
		this.repaint();
	}

	public boolean isImageOnly() {
		return imageOnly;
	}

	public void setImageOnly(boolean imageOnly) {
		this.imageOnly = imageOnly;
		this.repaint();
	}

	public void setBorder(Border border) {
		this.normalBorder = border;

		if (border == null && visibleInsets != null) {
			visibleInsets.set(0, 0, 0, 0);
		}

		super.setBorder(border);
	}

	public void setEditable(boolean editable) {
		super.setEditable(editable);

		if (borderChange) {
			mouseOut();
		}
	}

	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);

		if (borderChange) {
			if (enabled) {
				mouseOut();
			} else if (normalBorder != null) {
				super.setBorder(disabledBorder);
			}
		}
	}

	public boolean isLeadingTextVisible() {
		return leadingTextVisible;
	}

	public void setLeadingTextVisible(boolean leadingTextVisible) {
		this.leadingTextVisible = leadingTextVisible;
	}

	public boolean isPopupMenuEnabled() {
		return extender.isPopupMenuEnabled();
	}

	public void setPopupMenuEnabled(boolean popupMenuEnabled) {
		extender.setPopupMenuEnabled(popupMenuEnabled);
	}

	private void mouseIn() {
		if (normalBorder != null && isEnabled()) {
			super.setBorder(isEditable() ? rolloverBorder : notEditableRolloverBorder);
		}
	}

	private void mouseOut() {
		if (normalBorder != null && isEnabled()) {
			super.setBorder(isEditable() ? normalBorder : notEditableBorder);
		}
	}

	public void clearBorderListener() {
		this.removeMouseListener(listener);
		borderChange = false;
	}
}