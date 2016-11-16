/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * NewJFrame.java
 *
 * Created on 2011-1-5, 9:00:27
 */
package com.oim.ui;

import java.io.File;
import java.io.FileWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.oim.ui.component.BaseDialog;
import com.oim.ui.component.GenericFileFilter;
import com.only.OnlyButton;
import com.only.OnlyScrollPane;
import com.only.OnlyTextArea;

/**
 * 
 * @author Administrator
 */
public class ShowAccountDialog extends BaseDialog {

	private static final long serialVersionUID = 1L;

	private OnlyButton saveButton = new OnlyButton();
	private OnlyButton closeButton = new OnlyButton();

	private OnlyScrollPane scrollPane = new OnlyScrollPane();
	private OnlyTextArea textArea = new OnlyTextArea();

	private JFileChooser fileChooser = new JFileChooser();
	private String[] imageTypes = new String[] { "txt" };
	private GenericFileFilter filter = new GenericFileFilter(imageTypes, "文件");

	public ShowAccountDialog() {
		initComponents();
		initEvent();
	}

	public ShowAccountDialog(JDialog frame, String text, boolean b) {
		super(frame, text, b);
		initComponents();
		initEvent();
	}

	private void initComponents() {
		this.setSize(320, 230);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setShowIconImage(false);
		this.setLayout(null);

		fileChooser.setFileFilter(filter);

		textArea.setEnabled(false);
		textArea.setColumns(20);
		textArea.setRows(5);
		scrollPane.setHeaderVisible(false);
		scrollPane.setViewportView(textArea);

		closeButton.setText("关闭");
		saveButton.setText("保存");

		scrollPane.setBounds(0, 40, 320, 150);
		closeButton.setBounds(50, 195, 100, 25);
		saveButton.setBounds(170, 195, 100, 25);

		add(scrollPane);
		add(closeButton);
		add(saveButton);
	}

	private void initEvent() {
		saveButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				saveActionPerformed(evt);
			}
		});
		closeButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				closeActionPerformed(evt);
			}
		});
	}

	private void saveActionPerformed(java.awt.event.ActionEvent evt) {

		File file = fileChooser.getSelectedFile();
		if (null != file && file.isDirectory()) {
			String path = file.getAbsolutePath();
			fileChooser.setSelectedFile(new File(path + "/账号信息.txt")); // 设置默认文件名
		}
		int result = fileChooser.showSaveDialog(null);
		if (result == JFileChooser.APPROVE_OPTION) {
			File icon = fileChooser.getSelectedFile();
			String path = icon.getAbsolutePath();
			if (null != path && !"".equals(path)) {
				writeFile(path);
			}
		}

	}

	private void closeActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton3ActionPerformed
		this.setVisible(false);
	}

	public void writeFile(String fileName) {
		try {
			File f = new File(fileName);
			FileWriter write = new FileWriter(f);
			String text = textArea.getText();
			write.write(text);
			write.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "保存失败", "失败", JOptionPane.OK_CANCEL_OPTION);
		}
	}

	public void setText(String text) {
		textArea.setText(text);
	}

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String args[]) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException ex) {
			Logger.getLogger(ShowAccountDialog.class.getName()).log(Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			Logger.getLogger(ShowAccountDialog.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			Logger.getLogger(ShowAccountDialog.class.getName()).log(Level.SEVERE, null, ex);
		} catch (UnsupportedLookAndFeelException ex) {
			Logger.getLogger(ShowAccountDialog.class.getName()).log(Level.SEVERE, null, ex);
		}
		java.awt.EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				new ShowAccountDialog().setVisible(true);
			}
		});
	}
	// Variables declaration - do not modify//GEN-BEGIN:variables

	// End of variables declaration//GEN-END:variables
}
