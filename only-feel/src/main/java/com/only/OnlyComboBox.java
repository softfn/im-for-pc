package com.only;

import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

import javax.swing.ComboBoxEditor;
import javax.swing.ComboBoxModel;
import javax.swing.InputMap;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.plaf.ComboBoxUI;
import javax.swing.plaf.basic.ComboPopup;
import javax.swing.text.JTextComponent;

import com.only.box.UIBox;
import com.only.component.ImageBorder;
import com.only.laf.OnlyComboBoxRenderer;
import com.only.laf.OnlyComboBoxUI;
import com.only.util.OnlyFeelUtil;
import com.only.util.OnlyUIUtil;


@SuppressWarnings("rawtypes")
public class OnlyComboBox<E> extends JComboBox {

	private static final long serialVersionUID = 2902489693121071103L;
	private OnlyComboBoxUI ui;
	private Image image;
	private float alpha;
	private boolean imageOnly;
	private boolean editableAll;
	private Border normalBorder;
	private Border rolloverBorder;
	private Border disabledBorder;
	private Insets visibleInsets;
	private Color disabledTextColor;
	private MouseListener listener;
	private boolean borderChange;

	
	@SuppressWarnings("unchecked")
	public OnlyComboBox(ComboBoxModel<E> model) {
		super(model);
		init();
	}

	@SuppressWarnings("unchecked")
	public OnlyComboBox(final Object items[]) {
		super(items);
		init();
	}

	@SuppressWarnings("unchecked")
	public OnlyComboBox(Vector<?> items) {
		super(items);
		init();
	}

	public OnlyComboBox() {
		super();
		init();
	}

	@SuppressWarnings("unchecked")
	private void init() {
		alpha = 1.0f;
		visibleInsets = new Insets(1, 1, 1, 1);
		borderChange = true;
		editableAll = true;
		disabledTextColor = new Color(126, 126, 125);

		normalBorder = new ImageBorder(UIBox.getImage(UIBox.key_image_combobox_border_normal), 2, 2, 2, 2);
		rolloverBorder = new ImageBorder(UIBox.getImage(UIBox.key_image_combobox_border_rollover), 2, 2, 2, 2);
		disabledBorder = new ImageBorder(UIBox.getImage(UIBox.key_image_combobox_border_disabled), 2, 2, 2, 2);
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

		setUI(new OnlyComboBoxUI());
		super.setBorder(normalBorder);
		super.setOpaque(false);
		setFont(OnlyUIUtil.getDefaultFont());
		setBackground(UIBox.getWhiteColor());
		setForeground(Color.BLACK);
		setRenderer(new OnlyComboBoxRenderer(this));
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
		ComboPopup popup;
		if (ui != null && (popup = ui.getPopup()) != null && popup instanceof Component) {
			SwingUtilities.updateComponentTreeUI((Component) popup);
		}
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
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		ui.getArrowButton().setEnabled(enabled && isEditableAll());

		if (borderChange) {
			if (enabled) {
				mouseOut();
			} else if (normalBorder != null) {
				super.setBorder(disabledBorder);
			}
		}
	}

	@Override
	public void setEditable(boolean editable) {
		super.setEditable(editable && isEditableAll());
	}

	public boolean isEditableAll() {
		return editableAll;
	}

	public void setEditableAll(boolean editableAll) {
		this.editableAll = editableAll;
		ui.getArrowButton().setEnabled(editableAll && isEnabled());
		this.repaint();
	}

	protected void resetShortcutKeys() {
		InputMap inputMap = getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		Object value;

		for (KeyStroke ks : inputMap.allKeys()) {
			value = inputMap.get(ks);

			if (value.equals("selectNext")) {
				inputMap.put(ks, "selectNext2");
			} else if (value.equals("selectPrevious")) {
				inputMap.put(ks, "selectPrevious2");
			}
		}
	}

	@Override
	public void setUI(ComboBoxUI ui) {
		removeMouseListener(listener);
		ComboBoxEditor boxEditor = this.getEditor();

		if (boxEditor != null) {
			boxEditor.getEditorComponent().removeMouseListener(listener);
		}

		if (renderer != null && renderer instanceof Component) {
			((Component) renderer).removeMouseListener(listener);
		}

		for (Component c : this.getComponents()) {
			c.removeMouseListener(listener);
		}

		if (ui instanceof OnlyComboBoxUI) {
			this.ui = (OnlyComboBoxUI) ui;
		}

		super.setUI(ui);
		putClientProperty("JComboBox.isTableCellEditor", false);
		resetShortcutKeys();
		addMouseListener(listener);
		this.getEditor().getEditorComponent().addMouseListener(listener);

		if (renderer instanceof Component) {
			((Component) renderer).addMouseListener(listener);
		}

		for (Component c : this.getComponents()) {
			c.addMouseListener(listener);
		}
	}

	public Color getDisabledTextColor() {
		return disabledTextColor;
	}

	public void setDisabledTextColor(Color disabledTextColor) {
		this.disabledTextColor = disabledTextColor;
		ComboBoxEditor editor = this.getEditor();
		Component field;

		if (editor != null && (field = editor.getEditorComponent()) instanceof JTextComponent) {
			((JTextComponent) field).setDisabledTextColor(disabledTextColor);
		}

		if (!this.isEnabled()) {
			this.repaint();
		}
	}

	@Override
	public void setForeground(Color color) {
		super.setForeground(color);

		if (this.getEditor() != null) {
			this.getEditor().getEditorComponent().setForeground(color);
		}
	}

	protected void mouseIn() {
		if (isEnabled()) {
			if (normalBorder != null) {
				super.setBorder(rolloverBorder);
			}

			if (ui != null) {
				ui.changeButtonBorder(true);
			}
		}
	}

	protected void mouseOut() {
		if (isEnabled() && !isPopupVisible()) {
			if (normalBorder != null) {
				super.setBorder(normalBorder);
			}

			if (ui != null) {
				ui.changeButtonBorder(false);
			}
		}
	}

	public void resetBorder() {
		if (!this.isShowing() || !new Rectangle(this.getLocationOnScreen(), this.getSize()).contains(OnlyFeelUtil.getMouseLocation())) {
			super.setBorder(normalBorder);

			if (ui != null) {
				ui.changeButtonBorder(false);
			}
		}
	}
}