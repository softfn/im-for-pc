package com.only;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.Window;
import java.io.File;

import javax.accessibility.AccessibleContext;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.UIManager;
import javax.swing.filechooser.FileSystemView;
import javax.swing.plaf.FileChooserUI;

import com.only.laf.OnlyFileChooserUI;
import com.only.util.OnlyImageUtil;
import com.only.util.OnlyUIUtil;

/**
 * @author XiaHui
 * @date 2015年1月29日 上午10:36:16
 */
public class OnlyFileChooser extends JFileChooser {

	private static final long serialVersionUID = 1L;

	public OnlyFileChooser() {
		this((File) null, (FileSystemView) null);
	}

	public OnlyFileChooser(String currentDirectoryPath) {
		this(currentDirectoryPath, (FileSystemView) null);
	}

	public OnlyFileChooser(File currentDirectory) {
		this(currentDirectory, (FileSystemView) null);
	}

	public OnlyFileChooser(FileSystemView fsv) {
		this((File) null, fsv);
	}

	public OnlyFileChooser(File currentDirectory, FileSystemView fsv) {
		super(currentDirectory, fsv);
		init();
	}

	public OnlyFileChooser(String currentDirectoryPath, FileSystemView fsv) {
		super(currentDirectoryPath, fsv);
		init();
	}

	private void init() {
		this.setUI(new OnlyFileChooserUI(this));
	}

	public void updateUI() {
		this.setUI(new OnlyFileChooserUI(this));
	}

	protected JDialog createDialog(Component parent) throws HeadlessException {
		FileChooserUI ui = getUI();
		String title = ui.getDialogTitle(this);
		putClientProperty(AccessibleContext.ACCESSIBLE_DESCRIPTION_PROPERTY, title);

		JDialog dialog;
		Window window = OnlyUIUtil.getWindowForComponent(parent);
		if (window instanceof Frame) {
			dialog = new OnlyBorderDialog((Frame) window, title, true);
		} else {
			dialog = new OnlyBorderDialog((Dialog) window, title, true);
		}
		dialog.setComponentOrientation(this.getComponentOrientation());

		Container contentPane = dialog.getContentPane();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		
		
	
		
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setLayout(new BorderLayout());
		panel.add(this, BorderLayout.CENTER);
		
//		contentPane.setLayout(new BorderLayout());
//		contentPane.add(this, BorderLayout.CENTER);
		Icon icon=OnlyImageUtil.getEmptyIcon(20, 35);
		contentPane.add(new JLabel(icon));
		contentPane.add(panel);
		
		if (JDialog.isDefaultLookAndFeelDecorated()) {
			boolean supportsWindowDecorations = UIManager.getLookAndFeel().getSupportsWindowDecorations();
			if (supportsWindowDecorations) {
				dialog.getRootPane().setWindowDecorationStyle(JRootPane.FILE_CHOOSER_DIALOG);
			}
		}
		dialog.getRootPane().setDefaultButton(ui.getDefaultButton(this));
		dialog.pack();
		dialog.setLocationRelativeTo(parent);

		return dialog;
	}
}
