/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oim.common.component.combo;

/**
 *
 * @author XiaHui
 */
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.event.CaretEvent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

@SuppressWarnings("serial")
public class AutoCompletionField extends JTextField implements DocumentListener, MouseListener, ListSelectionListener, ActionListener, KeyListener {

	private static int DEFAULT_PREFERRED_HEIGHT = 150;
	private AutoCompletionListPopup popup;
	private int preferredHeight = DEFAULT_PREFERRED_HEIGHT;
	private AutoCompletionFilter filter;
	private AutoCompletionRenderer textRenderer;
	private AutoCompletionAction autoCompletionAction;
	private String textTemp = "";
	private Object value;

	public void setFilter(AutoCompletionFilter f) {
		filter = f;
	}

	public AutoCompletionField() {
		popup = new AutoCompletionListPopup();
		init();
	}

	private void init() {
		// getDocument().addDocumentListener(this);
		addMouseListener(this);
		popup.addListSelectionListener(this);
		addActionListener(this);
		addKeyListener(this);
		addCaretListener(new javax.swing.event.CaretListener() {
			public void caretUpdate(javax.swing.event.CaretEvent evt) {
				textFieldCaretUpdate(evt);
			}
		});
	}

	protected void textFieldCaretUpdate(CaretEvent evt) {
		textChanged();
	}

	public void setPopupPreferredHeight(int h) {
		preferredHeight = h;
	}

	@SuppressWarnings("rawtypes")
	private boolean isListChange(List array) {
		if (array.size() != popup.getItemCount()) {
			return true;
		}
		for (int i = 0; i < array.size(); i++) {
			if (!array.get(i).equals(popup.getItem(i))) {
				return true;
			}
		}
		return false;
	}

	private void textChanged() {
		// if (!this.isFocusOwner()) {
		// requestFocus();
		// }
		if (null != this.getText() && !"".equals(this.getText())) {
			if (filter != null) {
				String text = getText();
				if (null != text && !"".equals(text) && !textTemp.equals(text)) {
					filter.filter(getText());
				}
			}
		}
	}

	public void showPopup() {
		// if (this.isShowing()) {
		// popup.setPopupSize(getWidth(), preferredHeight);
		popup.show(this, 0, getHeight() - 1);
		// }
	}

	@Override
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		popup.setPopupSize(getWidth(), preferredHeight);
	}

	public boolean isShowPopup() {
		return popup.isVisible();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void changeList(List array) {
		EventQueue.invokeLater(new ChangeList(array));
	}

	class ChangeList implements Runnable {
		List<Object> array;

		public ChangeList(List<Object> array) {
			this.array = array;
		}

		@Override
		public void run() {
			if (null == array) {
				array = new ArrayList<Object>();
			}
//			if (!popup.isVisible()) {
//				showPopup();
//				requestFocus();
//			}
			if (array.isEmpty()) {
				if (popup.isVisible()) {
					popup.setVisible(false);
				}
			} else {
				if (!popup.isVisible()) {
					showPopup();
					requestFocus();
				}
			}
			if (isListChange(array) && !array.isEmpty()) {
				popup.setList(array);
			}
			if (isListChange(array)) {
				popup.setList(array);
			}
		}

	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		textChanged();
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		textChanged();
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		textChanged();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() > 1 && !popup.isVisible()) {
			textChanged();
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void valueChanged(ListSelectionEvent e) {
		JList list = (JList) e.getSource();
		value = list.getSelectedValue();
		String text = "";
		if (value != null) {
			if (null != textRenderer) {
				text = textRenderer.getValue(value);
			} else {
				text = value.toString();
			}
		} else {
		}
		textTemp = text;
		setText(text);
		if (null != autoCompletionAction) {
			autoCompletionAction.action(value);
		}
		popup.setVisible(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (popup.isVisible()) {
			value = popup.getSelectedValue();
			String text = "";
			if (value != null) {
				if (null != textRenderer) {
					text = textRenderer.getValue(value);
				} else {
					text = value.toString();
				}
			}
			textTemp = text;
			setText(text);
			popup.setVisible(false);
			if (null != autoCompletionAction) {
				autoCompletionAction.action(value);
			}
		}
		this.selectAll();
		this.requestFocus();
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			if (popup.isVisible()) {
				if (!popup.isSelected()) {
					popup.setSelectedIndex(0);
				} else {
					popup.setSelectedIndex(popup.getSelectedIndex() + 1);
				}
			} else {

				if (!popup.isSelected()) {
					popup.setSelectedIndex(0);
				} else {
					popup.setSelectedIndex(popup.getSelectedIndex() + 1);
				}

				showPopup();
				requestFocus();
			}
		} else if (e.getKeyCode() == KeyEvent.VK_UP) {
			if (popup.isVisible()) {
				if (!popup.isSelected()) {
					popup.setLastOneSelected();
				} else {
					popup.setSelectedIndex(popup.getSelectedIndex() - 1);
				}
			} else {

				if (!popup.isSelected()) {
					popup.setSelectedIndex(0);
				} else {
					popup.setSelectedIndex(popup.getSelectedIndex() - 1);
				}
				showPopup();
				requestFocus();
			}
		} else if (e.getKeyCode() == KeyEvent.VK_PAGE_DOWN) {
			if (popup.isVisible()) {
				if (!popup.isSelected()) {
					popup.setSelectedIndex(0);
				} else {
					popup.setSelectedIndex(popup.getSelectedIndex() + 5);
				}
			} else {

				if (!popup.isSelected()) {
					popup.setSelectedIndex(0);
				} else {
					popup.setSelectedIndex(popup.getSelectedIndex() + 5);
				}
				showPopup();
				requestFocus();
			}
		} else if (e.getKeyCode() == KeyEvent.VK_PAGE_UP) {
			if (popup.isVisible()) {
				if (!popup.isSelected()) {
					popup.setLastOneSelected();
				} else {
					popup.setSelectedIndex(popup.getSelectedIndex() - 5);
				}
			} else {

				if (!popup.isSelected()) {
					popup.setSelectedIndex(0);
				} else {
					popup.setSelectedIndex(popup.getSelectedIndex() - 5);
				}
				showPopup();
				requestFocus();
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	public void showData(List<Object> objectList) {
		changeList(objectList);
	}

	@SuppressWarnings("rawtypes")
	public void setListCellRenderer(ListCellRenderer listCellRenderer) {
		popup.setListCellRenderer(listCellRenderer);
	}

	public AutoCompletionRenderer getTextRenderer() {
		return textRenderer;
	}

	public void setTextRenderer(AutoCompletionRenderer textRenderer) {
		this.textRenderer = textRenderer;
	}

	public AutoCompletionAction getAutoCompletionAction() {
		return autoCompletionAction;
	}

	public void setAutoCompletionAction(AutoCompletionAction autoCompletionAction) {
		this.autoCompletionAction = autoCompletionAction;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
}
