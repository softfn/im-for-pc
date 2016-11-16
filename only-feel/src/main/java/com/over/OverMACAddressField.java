package com.over;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;

import com.only.util.OnlyFeelUtil;

public class OverMACAddressField extends JTextField implements Serializable {
	private static final long serialVersionUID = -4711699807610552544L;

	private static final String MAC_BLOCK_REGEX = "[0-9A-Fa-f]{2}";

	private static final String MAC_REGEX = "(" + MAC_BLOCK_REGEX + ":){5}" + MAC_BLOCK_REGEX;

	private static final String CHAR_AREA = "0123456789abcdefABCDEF";

	private static final int BLOCK_LENGTH = 6;

	private static final String LEFT_PRESS = "Left";

	private static final String RIGHT_PRESS = "Right";

	private static final String BACK_SPACE_PRESS = "BackSpace";

	private static final String DELETE_PRESS = "Delete";

	private static final Border EMPTY_BORDER = BorderFactory.createEmptyBorder();

	private JMACField[] macFields;

	private Colon[] colons;

	private KeyAdapter keyListener = new KeyAdapter() {
		public void keyPressed(KeyEvent e) {
			if (!isEditable() || !isEnabled()) {
				return;
			}

			JTextComponent field = (JTextComponent) e.getComponent();
			int keyCode = e.getKeyCode();
			char keyChar = e.getKeyChar();
			String text = field.getText();
			String selText = field.getSelectedText();
			int caretPos = field.getCaretPosition();
			int textLength = text.length();

			if (keyCode == KeyEvent.VK_LEFT && caretPos == 0 && selText == null) {
				field.firePropertyChange(LEFT_PRESS, 0, 1);
			} else if ((keyCode == KeyEvent.VK_RIGHT && caretPos == textLength && selText == null) || (keyChar == ':' && !text.isEmpty() && selText == null)) {
				field.firePropertyChange(RIGHT_PRESS, 0, 1);
			} else if (keyCode == KeyEvent.VK_BACK_SPACE && caretPos == 0 && selText == null) {
				field.firePropertyChange(BACK_SPACE_PRESS, 0, 1);
			} else if (keyCode == KeyEvent.VK_DELETE && caretPos == textLength && selText == null) {
				field.firePropertyChange(DELETE_PRESS, 0, 1);
			} else if (keyCode == KeyEvent.VK_HOME) {
				macFields[0].unSelectAllWhenFocusGained();
				macFields[0].requestFocus();
				macFields[0].setCaretPosition(0);
			} else if (keyCode == KeyEvent.VK_END) {
				int last = macFields.length - 1;
				textLength = macFields[last].getText().length();
				macFields[last].unSelectAllWhenFocusGained();
				macFields[last].requestFocus();
				macFields[last].setCaretPosition(textLength);
			} else if ((CHAR_AREA.indexOf(keyChar) >= 0) && ((selText == null && caretPos == 1) || (selText != null && field.getSelectionStart() == 1 && field.getSelectionEnd() == 2))) {
				field.firePropertyChange(RIGHT_PRESS, 0, 1);
			}
		}
	};

	public OverMACAddressField() {
		this(null);
	}

	public OverMACAddressField(String macAddress) {
		this.setLayout(new GridLayout(1, BLOCK_LENGTH, 0, 0));
		this.setFocusable(false);
		createMacFields();
		setMacAddress(macAddress);
	}

	public void updateUI() {
		Component[] children = this.getComponents();

		for (Component child : children) {
			this.remove(child);
		}

		super.updateUI();

		for (Component child : children) {
			this.add(child);
		}

		if (this.getCaret() != null) {
			this.getCaret().deinstall(this);
		}
	}

	private void createMacFields() {
		macFields = new JMACField[BLOCK_LENGTH];
		colons = new Colon[BLOCK_LENGTH - 1];
		JPanel[] fieldPanes = new JPanel[BLOCK_LENGTH];

		for (int i = 0; i < BLOCK_LENGTH; i++) {
			macFields[i] = new JMACField();
			fieldPanes[i] = new JPanel();

			macFields[i].addKeyListener(keyListener);
			fieldPanes[i].setOpaque(false);
			fieldPanes[i].setLayout(new BorderLayout());
			fieldPanes[i].add(macFields[i], BorderLayout.CENTER);

			if (i != BLOCK_LENGTH - 1) {
				fieldPanes[i].add(colons[i] = new Colon(), BorderLayout.EAST);
			}

			this.add(fieldPanes[i]);
		}

		for (int i = 0; i < BLOCK_LENGTH; i++) {
			if (i == 0) {
				macFields[i].addPropertyChangeListener(new KeyPressListener(null, macFields[i + 1]));
			} else if (i == BLOCK_LENGTH - 1) {
				macFields[i].addPropertyChangeListener(new KeyPressListener(macFields[i - 1], null));
			} else {
				macFields[i].addPropertyChangeListener(new KeyPressListener(macFields[i - 1], macFields[i + 1]));
			}
		}
	}

	public void setMacAddress(final String macAddress) {
		if (macAddress != null && !macAddress.isEmpty()) {
			if (!Pattern.matches(MAC_REGEX, macAddress)) {
				throw new IllegalArgumentException("Invalid Mac Address:" + macAddress);
			}

			String macBit[] = macAddress.split("\\:");
			int index = 0;

			for (JMACField macField : macFields) {
				macField.setText(macBit[index++]);
			}
		} else {
			for (JMACField macField : macFields) {
				macField.setText("");
			}
		}
	}

	public void setText(String text) {
		setMacAddress(text);
	}

	public void setFont(Font font) {
		super.setFont(font);

		if (macFields != null) {
			for (JMACField field : macFields) {
				field.setFont(font);
			}
		}

		if (colons != null) {
			for (Colon colon : colons) {
				colon.setFont(font);
			}
		}
	}

	public void setSelectionColor(Color color) {
		super.setSelectionColor(color);

		if (macFields != null) {
			for (JMACField field : macFields) {
				field.setSelectionColor(color);
			}
		}
	}

	public void setSelectedTextColor(Color color) {
		super.setSelectedTextColor(color);

		if (macFields != null) {
			for (JMACField field : macFields) {
				field.setSelectedTextColor(color);
			}
		}
	}

	public void setDisabledTextColor(Color color) {
		super.setDisabledTextColor(color);

		if (macFields != null) {
			for (JMACField field : macFields) {
				field.setDisabledTextColor(color);

				if (!field.isEnabled()) {
					field.repaint();
				}
			}
		}

		if (colons != null && !this.isEnabled()) {
			for (Colon colon : colons) {
				colon.setForeground(color);
			}
		}
	}

	public void setCaretColor(Color color) {
		super.setCaretColor(color);

		if (macFields != null) {
			for (JMACField field : macFields) {
				field.setCaretColor(color);
			}
		}
	}

	public void setForeground(Color color) {
		super.setForeground(color);

		if (macFields != null) {
			for (JMACField field : macFields) {
				field.setForeground(color);
			}
		}

		if (colons != null && this.isEnabled()) {
			for (Colon colon : colons) {
				colon.setForeground(color);
			}
		}
	}

	public void setEnabled(boolean isEnable) {
		super.setEnabled(isEnable);

		if (macFields != null) {
			for (JMACField field : macFields) {
				field.setEnabled(isEnable);
			}
		}

		if (colons != null) {
			for (Colon colon : colons) {
				colon.setForeground(isEnable ? this.getForeground() : this.getDisabledTextColor());
			}
		}
	}

	public void setEditable(boolean isEditable) {
		super.setEditable(isEditable);

		if (macFields != null) {
			for (JMACField field : macFields) {
				field.setEditable(isEditable);
			}
		}
	}

	public String getMacAddress() {
		StringBuilder mac = new StringBuilder();
		int emptyCount = 0;

		if (macFields != null) {
			String str;

			for (JMACField field : macFields) {
				str = field.getText();

				if (str.isEmpty()) {
					emptyCount++;
					str = "00";
				}

				str = str.length() == 1 ? 0 + str : str;
				mac.append(':');
				mac.append(str);
			}

			mac.deleteCharAt(0);
		}

		return emptyCount == BLOCK_LENGTH ? "" : mac.toString();
	}

	public String getText() {
		return getMacAddress();
	}

	public JTextField[] getFieldComponents() {
		return macFields;
	}

	public void addFocusListener(FocusListener listener) {
		if (macFields != null) {
			for (JMACField field : macFields) {
				field.addFocusListener(listener);
			}
		}
	}

	public void removeFocusListener(FocusListener listener) {
		if (macFields != null) {
			for (JMACField field : macFields) {
				field.removeFocusListener(listener);
			}
		}
	}

	private class KeyPressListener implements PropertyChangeListener {
		private JMACField leftField, rightField;

		public KeyPressListener(JMACField leftField, JMACField rightField) {
			this.leftField = leftField;
			this.rightField = rightField;
		}

		public void propertyChange(PropertyChangeEvent evt) {
			String name = evt.getPropertyName();

			if (name == LEFT_PRESS && leftField != null) {
				leftField.requestFocus();
			} else if (name == RIGHT_PRESS && rightField != null) {
				rightField.requestFocus();
			} else if (name == BACK_SPACE_PRESS && leftField != null) {
				leftField.unSelectAllWhenFocusGained();
				leftField.requestFocus();
				leftField.setCaretPosition(leftField.getText().length());
			} else if (name == DELETE_PRESS && rightField != null) {
				rightField.unSelectAllWhenFocusGained();
				rightField.requestFocus();
				rightField.setCaretPosition(0);
			}
		}
	}

	private class JMACField extends JTextField implements ActionListener, FocusListener, Serializable {
		private static final long serialVersionUID = -5440880126904364761L;

		private boolean selectAll;

		public JMACField() {
			super();
			selectAll = true;
			setHorizontalAlignment(SwingConstants.CENTER);
			setBorder(EMPTY_BORDER);
			setOpaque(false);
			setMargin(new Insets(0, 0, 0, 0));
			setDocument(new MacBlockDocument());
			setFont(OverMACAddressField.this.getFont());
			setSelectionColor(OverMACAddressField.this.getSelectionColor());
			setSelectedTextColor(OverMACAddressField.this.getSelectedTextColor());
			setEditable(OverMACAddressField.this.isEditable());
			setDisabledTextColor(OverMACAddressField.this.getDisabledTextColor());
			setCaretColor(OverMACAddressField.this.getCaretColor());
			setForeground(OverMACAddressField.this.getForeground());
			setEnabled(OverMACAddressField.this.isEnabled());
			addActionListener(this);
			addFocusListener(this);
		}

		public void unSelectAllWhenFocusGained() {
			if (!this.isFocusOwner()) {
				selectAll = false;
			}
		}

		public void paste() {
			String clipboardText, text, selectedText;

			if (isEditable() && isEnabled() && (clipboardText = OnlyFeelUtil.getSystemClipboardText()) != null && !clipboardText.isEmpty()) {
				if (Pattern.matches(MAC_REGEX, clipboardText)) {
					setMacAddress(clipboardText);
				} else if ((text = this.getText()) != null && !text.isEmpty() && (selectedText = this.getSelectedText()) != null && !selectedText.isEmpty()) {
					int selStart = this.getSelectionStart();
					int selEnd = this.getSelectionEnd();
					String newText = text.substring(0, selStart) + clipboardText + text.substring(selEnd);
					String fullText = newText.length() == 1 ? 0 + newText : newText;

					if (Pattern.matches(MAC_BLOCK_REGEX, fullText)) {
						this.setText(newText);
					}
				} else {
					super.paste();
				}
			}
		}

		public void actionPerformed(ActionEvent e) {
			transferFocus();
		}

		public void focusGained(FocusEvent e) {
			if (selectAll) {
				selectAll();
			} else {
				selectAll = true;
			}
		}

		public void focusLost(FocusEvent e) {
		}

		private class MacBlockDocument extends PlainDocument {
			private static final long serialVersionUID = -2667829308055179550L;

			private static final String REGEX = "[0-9A-Fa-f]{0,2}";

			private final Pattern PATTERN = Pattern.compile(REGEX);

			private StringBuilder text = new StringBuilder();

			public void insertString(int offset, String input, AttributeSet a) throws BadLocationException {
				text.delete(0, text.length());
				text.append(getText(0, getLength()));
				text.insert(offset, input);

				if (!PATTERN.matcher(text).matches()) {
					return;
				} else {
					super.insertString(offset, input, a);
				}
			}
		}
	}

	private class Colon extends JLabel {
		private static final long serialVersionUID = 301136113913078182L;

		public Colon() {
			super(":");
			setOpaque(false);
			setBorder(EMPTY_BORDER);
			setForeground(OverMACAddressField.this.getForeground());
			setEnabled(OverMACAddressField.this.isEnabled());
			setHorizontalAlignment(SwingConstants.CENTER);
		}

		public void setEnabled(boolean enabled) {
			super.setEnabled(enabled);
			setForeground(enabled ? OverMACAddressField.this.getForeground() : OverMACAddressField.this.getDisabledTextColor());
		}

		public void updateUI() {
			super.updateUI();
			setFont(OverMACAddressField.this.getFont());
		}
	}
}