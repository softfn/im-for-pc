/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oim.ui;

import java.awt.Color;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import com.oim.common.component.AlphaPanel;
import com.oim.ui.component.BaseDialog;
import com.only.OnlyButton;
import com.only.OnlyLabel;
import com.only.OnlyPasswordField;

/**
 * 
 * @author XiaHui
 */
@SuppressWarnings("serial")
public class UpdatePasswordDialog extends BaseDialog {

	OnlyButton saveButton = new OnlyButton();
	OnlyButton cancelButton = new OnlyButton();
	OnlyLabel oldPasswordLabel = new OnlyLabel();
	OnlyLabel newPasswordLabel = new OnlyLabel();
	OnlyLabel verifyPasswordLabel = new OnlyLabel();
	OnlyPasswordField oldPasswordField = new OnlyPasswordField();
	OnlyPasswordField newPasswordField = new OnlyPasswordField();
	OnlyPasswordField verifyPasswordField = new OnlyPasswordField();

	AlphaPanel panel = new AlphaPanel();

	public UpdatePasswordDialog(java.awt.Frame parent, boolean modal) {
		super(parent, modal);
		initComponents();
		initEvent();
	}

	public UpdatePasswordDialog(JFrame frame, String text, boolean b) {
		super(frame, text, b);
		initComponents();
		initEvent();
	}

	private void initComponents() {
		this.setLayout(null);
		this.setResizable(false);
		this.setSize(300, 220);
		this.setShowIconImage(false);
		this.setBackground(new Color(230, 245, 240));
		this.setTitle("修改密码");
		this.setBorderPainted(false);
		this.setLocationRelativeTo(null);
		this.getRootPane().setBackground(new Color(0, 128, 157));

		panel.setLayout(null);
		panel.setOpaque(false);
		panel.setBackgroundColorAlpha(100);
		panel.setBounds(0, 40, 300, 200);

		oldPasswordLabel.setText("原来密码：");
		newPasswordLabel.setText("新设密码：");
		verifyPasswordLabel.setText("确定密码：");

		oldPasswordLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		newPasswordLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		verifyPasswordLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

		saveButton.setText("确定");
		cancelButton.setText("取消");

		oldPasswordLabel.setBounds(25, 20, 60, 25);
		oldPasswordField.setBounds(85, 20, 180, 25);

		newPasswordLabel.setBounds(25, 55, 60, 25);
		newPasswordField.setBounds(85, 55, 180, 25);

		verifyPasswordLabel.setBounds(25, 90, 60, 25);
		verifyPasswordField.setBounds(85, 90, 180, 25);

		saveButton.setBounds(190, 135, 80, 25);
		cancelButton.setBounds(30, 135, 80, 25);

		panel.add(oldPasswordLabel);
		panel.add(newPasswordLabel);
		panel.add(verifyPasswordLabel);
		panel.add(oldPasswordField);
		panel.add(newPasswordField);
		panel.add(verifyPasswordField);
		panel.add(saveButton);
		panel.add(cancelButton);

		add(panel);
	}

	private void initEvent() {

		cancelButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				cancelButtonActionPerformed(evt);
			}
		});
	}

	private void initSetting() {
		this.oldPasswordField.setText("");
		this.verifyPasswordField.setText("");
		this.newPasswordField.setText("");
	}

	private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton2ActionPerformed
		this.setVisible(false);
	}

	public void addSaveAction(ActionListener l) {
		saveButton.addActionListener(l);
	}

	@Override
	public void setVisible(boolean visible) {
		initSetting();
		super.setVisible(visible);
	}

	public String getOldPassword() {
		return new String(oldPasswordField.getPassword());
	}

	public String getNewPassword() {
		return new String(newPasswordField.getPassword());
	}

	public String getVerifyPassword() {
		return new String(verifyPasswordField.getPassword());
	}

	public static void main(String args[]) {

		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(UpdatePasswordDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(UpdatePasswordDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(UpdatePasswordDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(UpdatePasswordDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		// </editor-fold>

		/*
		 * Create and display the dialog
		 */
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				UpdatePasswordDialog dialog = new UpdatePasswordDialog(new javax.swing.JFrame(), "登录设置", true);
				dialog.addWindowListener(new java.awt.event.WindowAdapter() {
					@Override
					public void windowClosing(java.awt.event.WindowEvent e) {
						System.exit(0);
					}
				});
				dialog.setVisible(true);
			}
		});
	}
}
