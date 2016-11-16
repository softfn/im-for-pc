/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oim.ui;

import java.awt.Color;

import javax.swing.JFrame;

import com.oim.common.component.AlphaPanel;
import com.oim.core.common.config.ConfigManage;
import com.oim.core.common.config.data.ConnectConfigData;
import com.oim.ui.component.BaseDialog;
import com.only.OnlyButton;
import com.only.OnlyIPV4AddressField;
import com.only.OnlyLabel;
import com.only.OnlyNumberField;

/**
 * 
 * @author XiaHui
 */
@SuppressWarnings("serial")
public class LoginSettingDialog extends BaseDialog {

	OnlyButton saveButton = new OnlyButton();
	OnlyButton cancelButton = new OnlyButton();
	OnlyLabel ipLabel = new OnlyLabel();
	OnlyLabel tcpPortLabel = new OnlyLabel();
	OnlyLabel httpPortLabel = new OnlyLabel();
	OnlyIPV4AddressField ipField = new OnlyIPV4AddressField();
	OnlyNumberField tcpPortField = new OnlyNumberField();
	OnlyNumberField httpPortField = new OnlyNumberField();

	AlphaPanel panel = new AlphaPanel();

	public LoginSettingDialog(java.awt.Frame parent, boolean modal) {
		super(parent, modal);
		initComponents();
		initEvent();
	}

	public LoginSettingDialog(JFrame frame, String text, boolean b) {
		super(frame, text, b);
		initComponents();
		initEvent();
	}

	private void initComponents() {
		this.setLayout(null);
		this.setResizable(false);
		this.setSize(300, 220);
		this.setShowIconImage(false);
		this.setTitle("登录设置");
		this.getRootPane().setBackground(new Color(0, 128, 157));
		this.setLocationRelativeTo(null);
		
		//panel.setOpaque(false);
		panel.setLayout(null);
		//panel.setImageAlpha(0.4f);
		//panel.setBackground(new Color(69, 159, 134));
		//panel.setBackgroundColor(new Color(69, 159, 134));
		//panel.setBackgroundColor(new Color(255, 255, 255));
		panel.setOpaque(false);
		panel.setBackgroundColorAlpha(100);
		panel.setBounds(0, 40, 300, 200);

		ipLabel.setText("IP:");
		tcpPortLabel.setText("TCP端口:");
		httpPortLabel.setText("Http端口:");

		//Color white = new Color(255, 255, 255);

		//ipLabel.setForeground(white);
		//tcpPortLabel.setForeground(white);
		//httpPortLabel.setForeground(white);

		ipLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		tcpPortLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		httpPortLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

		saveButton.setText("确定");
		cancelButton.setText("取消");

		ipLabel.setBounds(25, 20, 60, 25);
		ipField.setBounds(85, 20, 150, 25);

		tcpPortLabel.setBounds(25, 55, 60, 25);
		tcpPortField.setBounds(85, 55, 150, 25);

		httpPortLabel.setBounds(25, 90, 60, 25);
		httpPortField.setBounds(85, 90, 150, 25);

		cancelButton.setBounds(30, 135, 60, 25);
		saveButton.setBounds(210, 135, 60, 25);

		panel.add(ipLabel);
		panel.add(tcpPortLabel);
		panel.add(httpPortLabel);
		panel.add(ipField);
		panel.add(tcpPortField);
		panel.add(httpPortField);
		panel.add(saveButton);
		panel.add(cancelButton);

		add(panel);
	}

	private void initEvent() {
		saveButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				saveActionPerformed(evt);
			}
		});

		cancelButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				cancelButtonActionPerformed(evt);
			}
		});
	}

	private void initSetting() {
		ConnectConfigData ccd = (ConnectConfigData) ConfigManage.get(ConnectConfigData.path, ConnectConfigData.class);
		ipField.setText(ccd.getBusinessAddress());
		tcpPortField.setText(ccd.getBusinessPort() + "");
		httpPortField.setText(ccd.getHttpPort() + "");
	}

	private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton2ActionPerformed
		this.setVisible(false);
	}

	private void saveActionPerformed(java.awt.event.ActionEvent evt) {
		String ip = ipField.getText();
		int tcpPort = tcpPortField.getNumber().intValue();
		int httpPort = httpPortField.getNumber().intValue();
		if (ip == null || "".endsWith(ip)) {
			this.showMessage(ipField, ipField.getWidth(), 0, "IP不能为空！");
			return;
		}
		if (tcpPort <= 0) {
			this.showMessage(tcpPortField, tcpPortField.getWidth(), 0, "端口请输入大于0的数字！");
			return;
		}
		if (httpPort <= 0) {
			this.showMessage(httpPortField, httpPortField.getWidth(), 0, "端口请输入大于0的数字！");
			return;
		}
		ConnectConfigData ccd = new ConnectConfigData();
		ccd.setBusinessAddress(ip);
		ccd.setBusinessPort(tcpPort);
		ccd.setHttpPort(httpPort);
		ConfigManage.addOrUpdate(ConnectConfigData.path, ccd);
		this.setVisible(false);
	}

	@Override
	public void setVisible(boolean visible) {
		initSetting();
		super.setVisible(visible);
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
			java.util.logging.Logger.getLogger(LoginSettingDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(LoginSettingDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(LoginSettingDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(LoginSettingDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		// </editor-fold>

		/*
		 * Create and display the dialog
		 */
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				LoginSettingDialog dialog = new LoginSettingDialog(new javax.swing.JFrame(), "登录设置", true);
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
