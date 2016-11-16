package com.only.component;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;
import javax.swing.undo.UndoManager;

import com.only.OnlyMenuItem;
import com.only.OnlyPopupMenu;

public class TextExtender implements KeyListener, MouseListener, ActionListener {

	private static final String CMD_UNDO = "Undo";
	private static final String CMD_REDO = "Redo";
	private static final String CMD_CUT = "Cut";
	private static final String CMD_COPY = "Copy";
	private static final String CMD_PASTE = "Paste";
	private static final String CMD_DEL = "Del";
	private static final String CMD_SELECT_ALL = "SelectAll";
	private JTextComponent text;
	private UndoManager undo;
	private OnlyPopupMenu popupMenu;
	private OnlyMenuItem miUndo, miRedo, miCut, miCopy, miPaste, miDel, miSelectAll;
	private boolean popupMenuEnabled;

	public TextExtender(JTextComponent text) {
		this.text = text;
		this.undo = new UndoManager();
		text.getDocument().addUndoableEditListener(undo);
		text.addKeyListener(this);
		setPopupMenuEnabled(true);
	}

	private void showPopupMenu(int x, int y) {
		String selectedText = text.getSelectedText();
		boolean editable = text.isEditable();
		boolean hasSelected = selectedText != null && !selectedText.isEmpty();
		Transferable trans = text.getToolkit().getSystemClipboard().getContents(null);

		if (popupMenu == null) {
			popupMenu = new OnlyPopupMenu();
			popupMenu.add(miUndo = createMenuItem("撤销(U)", CMD_UNDO, 'U', KeyEvent.VK_Z, KeyEvent.CTRL_MASK));
			popupMenu.add(miRedo = createMenuItem("恢复(R)", CMD_REDO, 'R', KeyEvent.VK_Y, KeyEvent.CTRL_MASK));
			popupMenu.addSeparator();
			popupMenu.add(miCut = createMenuItem("剪切(T)", CMD_CUT, 'T', KeyEvent.VK_X, KeyEvent.CTRL_MASK));
			popupMenu.add(miCopy = createMenuItem("复制(C)", CMD_COPY, 'C', KeyEvent.VK_C, KeyEvent.CTRL_MASK));
			popupMenu.add(miPaste = createMenuItem("粘贴(P)", CMD_PASTE, 'P', KeyEvent.VK_V, KeyEvent.CTRL_MASK));
			popupMenu.add(miDel = createMenuItem("删除(D)", CMD_DEL, 'D', KeyEvent.VK_DELETE, 0));
			popupMenu.addSeparator();
			popupMenu.add(miSelectAll = createMenuItem("全部选择(A)", CMD_SELECT_ALL, 'A', KeyEvent.VK_A, KeyEvent.CTRL_MASK));
		}

		miUndo.setEnabled(editable && undo.canUndo());
		miRedo.setEnabled(editable && undo.canRedo());
		miCut.setEnabled(editable && hasSelected);
		miCopy.setEnabled(hasSelected);
		miDel.setEnabled(editable && hasSelected);
		miPaste.setEnabled(editable && trans != null && trans.isDataFlavorSupported(DataFlavor.stringFlavor));
		miSelectAll.setEnabled(true);
		popupMenu.show(text, x, y);
	}

	//
	private OnlyMenuItem createMenuItem(String text, String actionCommand, char mnemonic, int keyCode, int modifiers) {
		OnlyMenuItem item = new OnlyMenuItem(text, mnemonic);
		item.setActionCommand(actionCommand);
		item.setAccelerator(KeyStroke.getKeyStroke(keyCode, modifiers));
		item.addActionListener(this);
		return item;
	}

	public boolean isPopupMenuEnabled() {
		return popupMenuEnabled;
	}

	public void setPopupMenuEnabled(boolean popupMenuEnabled) {
		if (this.popupMenuEnabled != popupMenuEnabled) {
			if (popupMenuEnabled) {
				text.addMouseListener(this);
			} else {
				text.removeMouseListener(this);
			}

			this.popupMenuEnabled = popupMenuEnabled;
		}
	}

	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();

		if (cmd != null) {
			if (cmd.equals(CMD_UNDO)) {
				undo.undo();
			} else if (cmd.equals(CMD_REDO)) {
				undo.redo();
			} else if (cmd.equals(CMD_CUT)) {
				text.cut();
			} else if (cmd.equals(CMD_COPY)) {
				text.copy();
			} else if (cmd.equals(CMD_PASTE)) {
				text.paste();
			} else if (cmd.equals(CMD_DEL)) {
				text.replaceSelection(null);
			} else if (cmd.equals(CMD_SELECT_ALL)) {
				text.selectAll();
			}
		}
	}

	public void mouseReleased(MouseEvent e) {
		if (SwingUtilities.isRightMouseButton(e) && text.isEnabled()) {
			showPopupMenu(e.getX(), e.getY());
		}
	}

	public void keyPressed(KeyEvent e) {
		if (text.isEnabled() && text.isEditable() && e.getModifiers() == KeyEvent.CTRL_MASK) {
			int keyCode = e.getKeyCode();

			if (keyCode == KeyEvent.VK_Z && undo.canUndo()) {
				undo.undo();
			} else if (keyCode == KeyEvent.VK_Y && undo.canRedo()) {
				undo.redo();
			}
		}
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void keyTyped(KeyEvent e) {
	}

	public void keyReleased(KeyEvent e) {
	}
}