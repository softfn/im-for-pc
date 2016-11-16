package com.oim.ui.component;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.Window;

import com.oim.swing.UIBox;
import com.oim.swing.common.box.ImageBox;
import com.only.OnlyBorderDialog;
import com.only.common.ImageDisplayMode;

/**
 * @author XiaHui
 * @date 2015年3月11日 下午4:14:17
 */
public class BaseDialog extends OnlyBorderDialog {

	private static final long serialVersionUID = 1L;
	private PromptPopup promptPopup = new PromptPopup();
	private TextPromptPopup textPromptPopup = new TextPromptPopup();

	public BaseDialog() {
		this((Frame) null, false);
	}

	public BaseDialog(Frame owner) {
		this(owner, false);
	}

	public BaseDialog(Frame owner, boolean modal) {
		this(owner, null, modal);
	}

	public BaseDialog(Frame owner, String title) {
		this(owner, title, false);
	}

	public BaseDialog(Dialog owner) {
		this(owner, false);
	}

	public BaseDialog(Dialog owner, boolean modal) {
		this(owner, null, modal);
	}

	public BaseDialog(Dialog owner, String title) {
		this(owner, title, false);
	}

	public BaseDialog(Window owner) {
		this(owner, Dialog.ModalityType.MODELESS);
	}

	public BaseDialog(Window owner, Dialog.ModalityType modalityType) {
		this(owner, null, modalityType);
	}

	public BaseDialog(Window owner, String title) {
		this(owner, title, Dialog.ModalityType.MODELESS);
	}

	public BaseDialog(Dialog owner, String title, boolean modal, GraphicsConfiguration gc) {
		super(owner, title, modal, gc);
		initComponents();
	}

	public BaseDialog(Dialog owner, String title, boolean modal) {
		super(owner, title, modal);
		initComponents();
	}

	public BaseDialog(Frame owner, String title, boolean modal, GraphicsConfiguration gc) {
		super(owner, title, modal, gc);
		initComponents();
	}

	public BaseDialog(Frame owner, String title, boolean modal) {
		super(owner, title, modal);
		initComponents();
	}

	public BaseDialog(Window owner, String title, Dialog.ModalityType modalityType) {
		super(owner, title, modalityType);
		initComponents();
	}

	public BaseDialog(Window owner, String title, Dialog.ModalityType modalityType, GraphicsConfiguration gc) {
		super(owner, title, modalityType, gc);
		initComponents();
	}

	private void initComponents() {
		this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		this.setIconImage(ImageBox.getImage("Resources/Images/Logo/logo_64.png"));
		this.setBorderPainted(false);
		this.setImageDisplayMode(ImageDisplayMode.fill);
		UIBox.add(this);
	}

	public void showPromptMessage(String message) {
		int x = 0;
		int y = 0;
		int w = this.getWidth() - promptPopup.getWidth();
		int h = this.getHeight() - promptPopup.getHeight();
		// if (w > 0) {
		x = w / 2;
		// }
		// if (h > 0) {
		y = h / 2;
		// }
		showPromptMessage(this, x, y, "提示信息", message);
	}

	public void showPromptMessage(Component invoker, String message) {
		showPromptMessage(invoker, 0, invoker.getHeight(), "", message);
	}

	public void showPromptMessage(Component invoker, int x, int y, String message) {
		showPromptMessage(invoker, x, y, "", message);
	}

	public void showPromptMessage(Component invoker, int x, int y, String title, String message) {
		if(invoker.isShowing()){
			promptPopup.setTitle(title);
			promptPopup.setText(message);
			promptPopup.show(invoker, x, y);
		}
	}

	public void showMessage(Component invoker, int x, int y, String message) {
		if(invoker.isShowing()){
			textPromptPopup.setText(message);
			textPromptPopup.show(invoker, x, y);
		}
	}
}
