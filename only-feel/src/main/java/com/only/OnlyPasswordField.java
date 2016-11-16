package com.only;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPasswordField;
import javax.swing.border.Border;
import javax.swing.text.Document;

import com.only.box.UIBox;
import com.only.laf.OnlyPasswordFieldUI;
import com.only.util.OnlyUIUtil;

public class OnlyPasswordField extends JPasswordField {

	private static final long serialVersionUID = -8430512245033603572L;
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
	private String labelText;

	public OnlyPasswordField() {
		this(null, null, 0);
	}

	public OnlyPasswordField(String text) {
		this(null, text, 0);
	}

	public OnlyPasswordField(int columns) {
		this(null, null, columns);
	}

	public OnlyPasswordField(String text, int columns) {
		this(null, text, columns);
	}

	public OnlyPasswordField(Document doc, String txt, int columns) {
		super(doc, txt, columns);
		setUI(new OnlyPasswordFieldUI());
		super.setOpaque(false);
		setFont(OnlyUIUtil.getDefaultFont());
		setBackground(UIBox.getWhiteColor());
		setForeground(Color.BLACK);
		setCaretColor(Color.BLACK);
		setSelectionColor(new Color(49, 106, 197));
		setSelectedTextColor(UIBox.getWhiteColor());
		setDisabledTextColor(new Color(123, 123, 122));
		setCursor(new Cursor(Cursor.TEXT_CURSOR));
		setEchoChar('●');
		setMargin(new Insets(0, 0, 0, 0));

		normalBorder = UIBox.getBorder(UIBox.key_border_password_field_normal);
		rolloverBorder = UIBox.getBorder(UIBox.key_border_password_field_rollover);
		disabledBorder = UIBox.getBorder(UIBox.key_border_password_field_disabled);
		notEditableBorder = UIBox.getBorder(UIBox.key_border_password_field_not_editable);
		notEditableRolloverBorder = UIBox.getBorder(UIBox.key_border_password_field_not_editable_rollover);

		super.setBorder(normalBorder);

		putClientProperty("JPasswordField.cutCopyAllowed", false);
		alpha = 1.0f;
		visibleInsets = new Insets(1, 1, 1, 1);
		borderChange = true;
		listener = new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				mouseIn();
			}

			@Override
			public void mouseExited(MouseEvent e) {
				mouseOut();
			}
		};

		addMouseListener(listener);
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
	@Override
	public void updateUI() {
	}

	@Deprecated
	@Override
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

	@Override
	public void setBorder(Border border) {
		this.normalBorder = border;

		if (border == null && visibleInsets != null) {
			visibleInsets.set(0, 0, 0, 0);
		}

		super.setBorder(border);
	}

	@Override
	public void setEditable(boolean editable) {
		super.setEditable(editable);

		if (borderChange) {
			mouseOut();
		}
	}

	@Override
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
	public String getLabelText() {
		return labelText;
	}

	public void setLabelText(String labelText) {
		if (null != labelText) {
			if (!labelText.equals(this.labelText)) {
				this.labelText = labelText;
				this.repaint();
			}
		} else {
			this.labelText = "";
			this.repaint();
		}
	}
}