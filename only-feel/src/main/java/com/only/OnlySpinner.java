package com.only;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicFormattedTextFieldUI;

import com.only.box.UIBox;
import com.only.laf.OnlySpinnerUI;
import com.only.util.OnlyUIUtil;

public class OnlySpinner extends JSpinner {
	private static final long serialVersionUID = -7216220112988325312L;

	private static final Border EDITOR_BORDER = UIBox.getBorder(UIBox.key_border_spinner_editor_normal);
	private static final Border EDITOR_DISABLED_BORDER = UIBox.getBorder(UIBox.key_border_spinner_editor_disabled);
	private Image image;
	private float alpha;
	private boolean imageOnly;
	private Border normalBorder;
	private Border rolloverBorder;
	private Border disabledBorder;
	private Insets visibleInsets;
	private Color disabledTextColor;
	private MouseListener listener;
	private boolean borderChange;

	public OnlySpinner() {
		this(new SpinnerNumberModel());
	}

	public OnlySpinner(SpinnerModel model) {
		super(model);
		alpha = 1.0f;
		visibleInsets = new Insets(1, 1, 1, 1);
		borderChange = true;
		disabledTextColor = new Color(126, 126, 125);

		normalBorder = UIBox.getBorder(UIBox.key_border_compound_text_normal);
		rolloverBorder = UIBox.getBorder(UIBox.key_border_compound_text_rollover);
		disabledBorder = UIBox.getBorder(UIBox.key_border_compound_text_disabled);
		listener = new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				mouseIn();
			}

			public void mouseExited(MouseEvent e) {
				mouseOut();
			}
		};

		setUI(new OnlySpinnerUI());
		super.setBorder(normalBorder);
		super.setOpaque(false);
		super.setForeground(Color.BLACK);
		setFont(OnlyUIUtil.getDefaultFont());
		setBackground(UIBox.getWhiteColor());
		initEditor(getEditor());
		addMouseListener(listener);

		for (Component c : this.getComponents()) {
			if (c instanceof JButton) {
				c.addMouseListener(listener);
			}
		}
	}

	protected JComponent createEditor(SpinnerModel model) {
		JComponent editor = super.createEditor(model);
		changeEditorField(editor);
		return editor;
	}

	protected void changeEditorField(JComponent c) {
		if (c != null && c instanceof DefaultEditor) {
			DefaultEditor editor = (DefaultEditor) c;
			JFormattedTextField oldField = editor.getTextField();
			JFormattedTextField newField = new SpinnerTextField();
			newField.setName(oldField.getName());
			newField.setValue(oldField.getValue());
			newField.setEditable(oldField.isEditable());
			newField.setInheritsPopupMenu(oldField.getInheritsPopupMenu());
			newField.setToolTipText(oldField.getToolTipText());
			newField.setActionMap(oldField.getActionMap());
			newField.setFormatterFactory(oldField.getFormatterFactory());
			newField.setHorizontalAlignment(oldField.getHorizontalAlignment());
			newField.setColumns(oldField.getColumns());
			newField.setBorder(null);

			for (PropertyChangeListener pcl : oldField.getPropertyChangeListeners()) {
				newField.addPropertyChangeListener(pcl);
			}

			editor.remove(oldField);
			editor.add(newField);
			editor.setBorder(null);
		}
	}

	private void initEditor(JComponent editor) {
		setForeground(getForeground());
		setDisabledTextColor(disabledTextColor);
		editor.setOpaque(false);
		editor.setBorder(isEnabled() ? EDITOR_BORDER : EDITOR_DISABLED_BORDER);
		editor.addMouseListener(listener);

		for (Component c : editor.getComponents()) {
			((JComponent) c).setOpaque(false);
			c.addMouseListener(listener);
		}
	}

	public void setEditor(JComponent editor) {
		JComponent oldEditor = getEditor();

		if (oldEditor != null) {
			oldEditor.removeMouseListener(listener);

			for (Component c : oldEditor.getComponents()) {
				c.removeMouseListener(listener);
			}
		}

		super.setEditor(editor);
		initEditor(editor);
	}

	public void setHorizontalAlignment(int alignment) {
		JComponent editor = this.getEditor();

		if (editor instanceof DefaultEditor) {
			((DefaultEditor) editor).getTextField().setHorizontalAlignment(alignment);
		}
	}

	public int getHorizontalAlignment() {
		JComponent editor = this.getEditor();

		if (editor instanceof DefaultEditor) {
			return ((DefaultEditor) editor).getTextField().getHorizontalAlignment();
		} else {
			return -1;
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

	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		getEditor().setBorder(isEnabled() ? EDITOR_BORDER : EDITOR_DISABLED_BORDER);

		if (borderChange) {
			if (enabled) {
				mouseOut();
			} else if (normalBorder != null) {
				super.setBorder(disabledBorder);
			}
		}
	}

	public void setFont(Font font) {
		super.setFont(font);
		JComponent editor = getEditor();

		if (editor instanceof DefaultEditor) {
			((DefaultEditor) editor).getTextField().setFont(font);
		}
	}

	public Color getDisabledTextColor() {
		return disabledTextColor;
	}

	public void setDisabledTextColor(Color disabledTextColor) {
		this.disabledTextColor = disabledTextColor;
		JComponent editor = getEditor();

		if (editor instanceof DefaultEditor) {
			JFormattedTextField field = ((DefaultEditor) editor).getTextField();
			field.setDisabledTextColor(disabledTextColor);
		}
	}

	public void setForeground(Color foreground) {
		super.setForeground(foreground);

		JComponent editor = getEditor();

		if (editor instanceof DefaultEditor) {
			JFormattedTextField field = ((DefaultEditor) editor).getTextField();
			field.setForeground(foreground);
		}
	}

	private void mouseIn() {
		if (isEnabled() && normalBorder != null) {
			super.setBorder(rolloverBorder);
		}
	}

	private void mouseOut() {
		if (isEnabled() && normalBorder != null) {
			super.setBorder(normalBorder);
		}
	}

	private class SpinnerTextField extends JFormattedTextField {
		private static final long serialVersionUID = -4082862799280638287L;

		public SpinnerTextField() {
			super();
			setUI(new BasicFormattedTextFieldUI());
			setSelectionColor(UIBox.getColor(UIBox.key_color_spinner_text_selection_background));
			setSelectedTextColor(UIBox.getColor(UIBox.key_color_spinner_text_selection_foreground));
			setMargin(new Insets(0, 0, 0, 0));
			setCursor(new Cursor(Cursor.TEXT_CURSOR));
			setFont(OnlyUIUtil.getDefaultFont());
			setBackground(UIBox.getWhiteColor());
			setCaretColor(Color.BLACK);
			setBorder(new EmptyBorder(0, 0, 0, 0));
		}

		@Deprecated
		public void updateUI() {
		}
	}
}